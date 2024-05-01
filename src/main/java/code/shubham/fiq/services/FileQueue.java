package code.shubham.fiq.services;

import code.shubham.commons.utils.FileUtils;
import code.shubham.commons.utils.ProcessLock;
import code.shubham.commons.utils.ShutdownUtils;
import code.shubham.fiq.consumer.ConsumerHandler;
import code.shubham.fiq.models.Message;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class FileQueue implements Queue {

    private final String name;
    private final Path path;
    private BufferedReader reader;
    private int currentReaderLine = 0;
    private final ProcessLock processLock;

    public FileQueue(final String name, final String filePath) {
        this.name = name;
        this.path = FileUtils.createFileIfNotExists(filePath);
        this.processLock = new ProcessLock("Q" + this.name);
        try {
            this.reader = new BufferedReader(new FileReader(this.path.toFile()));
        } catch (final IOException exception) {
            exception.printStackTrace();
        }

        ShutdownUtils.defer(this::close);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void offer(final Message message) {
        this.processLock.run(() -> {
            try {
                Files.write(this.path, (message.toString() + "\n").getBytes(), StandardOpenOption.APPEND);
            } catch (final IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public synchronized Message poll(final String pollerId, int offset) {
        synchronized (this.name + "-" + pollerId) {
            try {
                while (true) {
                    String line = this.reader.readLine();
                    if (line != null) {
                        this.currentReaderLine++;
                        if (this.currentReaderLine <= offset)
                            continue;

                        ConsumerHandler.commit(pollerId + "-" + this.name, this.currentReaderLine);
                        return Message.of(line);
                    }

                }
            } catch (final IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    public void close() {
        try {
            this.reader.close();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
}
