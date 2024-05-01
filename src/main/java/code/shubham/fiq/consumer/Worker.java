package code.shubham.fiq.consumer;

import code.shubham.fiq.models.Message;
import code.shubham.fiq.services.Queue;

public record Worker(int workerId, String consumerName, Queue queue, int beginOffset) implements Runnable {

    @Override
    public void run() {
        while (true) {
            final Message message = this.queue.poll(this.consumerName, this.beginOffset);
            this.processMessage(message);
        }
    }

    private void processMessage(final Message message) {
        System.out.println("WorkerId: " + this.workerId + "; Processing message: " + message);
        try {
            Thread.sleep(message.processingTimeInSeconds() * 1000);
        } catch (final InterruptedException exception) {
            exception.printStackTrace();
        }
        System.out.println("WorkerId: " + this.workerId + "; Processed message: " + message);
    }
}
