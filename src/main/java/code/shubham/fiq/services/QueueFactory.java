package code.shubham.fiq.services;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class QueueFactory {

    private final Map<String, FileQueue> queues = new ConcurrentHashMap<>();

    private QueueFactory() {}

    public Queue getQueue(final String queueName) {
        final FileQueue queue = queues.get(queueName);
        if (queue != null) {
            return queue;
        }

        synchronized (queueName) {
            final FileQueue newQueue = new FileQueue(queueName, "tmp/queues/" + queueName);
            this.queues.put(queueName, newQueue);
            return newQueue;
        }
    }

    public static QueueFactory getFactory() {
        return SingletonHolder.INSTANCE;
    }

    public static final class SingletonHolder {
        private static final QueueFactory INSTANCE = new QueueFactory();
    }

}
