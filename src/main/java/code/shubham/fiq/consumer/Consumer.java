package code.shubham.fiq.consumer;

import code.shubham.fiq.services.Queue;
import code.shubham.fiq.services.QueueFactory;

public class Consumer {
    private final String name;
    private final String queueName;
    private final int concurrencyFactor;
    private final int offset;

    public Consumer(
            final String name,
            final String queueName,
            final int concurrencyFactor,
            final int offset) {
        this.name = name;
        this.queueName = queueName;
        this.concurrencyFactor = concurrencyFactor;
        this.offset = offset;
    }

    public void consume() {
        final Queue queue = QueueFactory.getFactory().getQueue(this.queueName);
        for (int i = 0; i < this.concurrencyFactor; ++i) {
            new Thread(new Worker(i, this.name, queue, this.offset)).start();
        }
    }
}
