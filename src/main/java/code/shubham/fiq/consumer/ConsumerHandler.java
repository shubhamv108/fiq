package code.shubham.fiq.consumer;

import code.shubham.commons.utils.FileUtils;
import code.shubham.fiq.services.QueueFactory;
import code.shubham.fiq.services.QueueFactory.SingletonHolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class ConsumerHandler {

    public static int getOffset(final String consumerName) {
        File lockFile = null;
        try {
            lockFile = FileUtils.lock(Path.of("tmp/offsets/" + consumerName));
            FileUtils.createFileIfNotExists("tmp/offsets/" + consumerName);
            try {
                return Files.readAllLines(Path.of("tmp/offsets/" + consumerName))
                        .stream()
                        .findFirst()
                        .map(Integer::parseInt)
                        .orElse(0);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } finally {
            if (lockFile != null)
                FileUtils.unLock(lockFile);
        }
    }

    public static void commit(final String consumerName, final int offset) {
        File lockFile = null;
        try {
            lockFile = FileUtils.lock(Path.of("tmp/offsets/" + consumerName));
            FileUtils.createFileIfNotExists("tmp/offsets/" + consumerName);
            try {
                Files.write(Path.of("tmp/offsets/" + consumerName), String.valueOf(offset).getBytes(), StandardOpenOption.WRITE);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } finally {
            if (lockFile != null)
                FileUtils.unLock(lockFile);
        }
    }

}
