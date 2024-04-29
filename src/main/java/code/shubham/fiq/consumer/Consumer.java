package code.shubham.fiq.consumer;

import code.shubham.fiq.models.Message;
import code.shubham.fiq.services.FileQueue;
import code.shubham.fiq.services.Queue;
import code.shubham.fiq.services.QueueFactory;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Consumer {
    private final String name;
    private final String queueName;
    private final int concurrencyFactor;

    public Consumer(
            final String name,
            final String queueName,
            final int concurrencyFactor) {
        this.name = name;
        this.queueName = queueName;
        this.concurrencyFactor = concurrencyFactor;
    }

    public void consume() {
        final Queue queue = QueueFactory.getFactory().getQueue(this.queueName);
        for (int i = 0; i < this.concurrencyFactor; ++i) {
            new Thread(new Worker(i, this.name, queue)).start();
        }
    }
}
