package code.shubham.fiq.producer;

import code.shubham.fiq.models.Message;
import code.shubham.fiq.services.Queue;
import code.shubham.fiq.services.QueueFactory;

import java.util.List;

public record Producer(String queueName, List<Message> messages) implements Runnable {
    public Producer(
            final String queueName,
            final Message messages) {
        this(queueName, List.of(messages));
    }

    @Override
    public void run() {
        final Queue queue = QueueFactory.getFactory().getQueue(this.queueName);
        this.messages.forEach(message -> this.send(queue, message));
    }

    private void send(final Queue queue, final Message message) {
        try {
            queue.offer(message);
            System.out.printf("ACK: %s%n", message);
        } catch (final Exception exception) {
            System.err.printf("NACK: %s%n", message);
            System.err.println(exception);
        }
    }
}
