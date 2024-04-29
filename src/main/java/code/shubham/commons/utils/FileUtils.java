package code.shubham.commons.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtils {

    public static Path createFileIfNotExists(final String filePath) {
        final Path path = Path.of(filePath);
        if (Files.exists(path))
            return path;

        synchronized (filePath) {
            if (Files.exists(path))
                return path;

            try {
                if (path.getParent() != null) {
                    Files.createDirectories(path.getParent());
                }

                return Files.createFile(path);
            } catch (final FileAlreadyExistsException exception) {
            } catch (final IOException exception) {
                exception.printStackTrace();
            }
        }
        return path;
    }

    public static File lockFile(final String lockFileName) {
        final File lockFile = new File(lockFileName);
        try {
            while (!lockFile.createNewFile()) {
                Thread.sleep(1000); // Wait and retry
            }
        } catch (final IOException | InterruptedException exception) {
            exception.printStackTrace();
        }
        return lockFile;
    }

    public static void delete(final File file) {
        file.delete();
    }

    public static File lock(final Path filePath) {
        return FileUtils.lockFile(filePath.toFile().getAbsolutePath() + ".lock");
    }
    public static void unLock(final File lockFile) {
        FileUtils.delete(lockFile);
    }

}
