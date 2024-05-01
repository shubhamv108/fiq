package code.shubham.fiq.consumer;

import code.shubham.commons.utils.ProcessLock;
import code.shubham.fiq.services.Queue;
import code.shubham.fiq.services.QueueFactory;

public record Consumer(String name, String queueName, int concurrencyFactor, int beginOffset) {

    public void run() {
        new ProcessLock(this.name + "-" + this.queueName).run(this::consume);
    }

    public void consume() {
        final Queue queue = QueueFactory.getFactory().getQueue(this.queueName);
        for (int i = 0; i < this.concurrencyFactor; ++i) {
            new Thread(new Worker(i, this.name, queue, this.beginOffset)).start();
        }
    }
}
