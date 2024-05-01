package code.shubham.commons.utils;

public class ShutdownUtils {

    public static void defer(final Runnable runnable) {
        Runtime.getRuntime().addShutdownHook(new Thread(runnable));
    }

}
