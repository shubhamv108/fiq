package code.shubham.fiq.services;

import code.shubham.commons.utils.FileUtils;
import code.shubham.commons.utils.ShutdownUtils;
import code.shubham.fiq.consumer.ConsumerHandler;
import code.shubham.fiq.models.Message;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class FileQueue implements Queue {

    private final Path path;
    private BufferedReader reader;
    private int currentReaderLine = 0;
    private String name;

    public FileQueue(final String name, final String filePath) {
        this.name = name;
        this.path = FileUtils.createFileIfNotExists(filePath);
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
    public void offer(final Message message) throws Exception {
        File lockFile = null;
        try {
            lockFile = FileUtils.lock(this.path);
            Files.write(this.path, (message.toString() + "\n").getBytes(), StandardOpenOption.APPEND);
        } finally {
            if (lockFile != null)
                FileUtils.unLock(lockFile);
        }
    }

    @Override
    public synchronized Message poll(final String pollerId, int offset) {
        File lockFile = null;
        try {
            lockFile = FileUtils.lock(Path.of("tmp/queues/" + this.name + "-" + pollerId));
            try {
                while (true) {
                    String line = reader.readLine();
                    if (line != null) {
                        this.currentReaderLine++;
                        if (this.currentReaderLine <= offset)
                            continue;

                        ConsumerHandler.commit(pollerId + "-" + this.name, currentReaderLine);
                        return Message.of(line);
                    }

                }
            } finally {
                if (lockFile != null)
                    FileUtils.unLock(lockFile);
            }
        } catch (final IOException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public void close() {
        try {
            this.reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



}
