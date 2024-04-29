package code.shubham.fiq.consumer;

import code.shubham.commons.utils.FileUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class ConsumerHandler {

    public static int getOffset(final String consumerAndQueueName) {
        FileUtils.createFileIfNotExists("tmp/offsets/" + consumerAndQueueName);
        try {
            return Files.readAllLines(Path.of("tmp/offsets/" + consumerAndQueueName))
                    .stream()
                    .findFirst()
                    .map(Integer::parseInt)
                    .orElse(0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void commit(final String consumerAndQueueName, final int offset) {
        FileUtils.createFileIfNotExists("tmp/offsets/" + consumerAndQueueName);
        try {
            Files.write(Path.of("tmp/offsets/" + consumerAndQueueName), String.valueOf(offset).getBytes(), StandardOpenOption.WRITE);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

}
