package code.shubham.fiq.consumer;

import code.shubham.commons.interations.commands.CommandFactory;
import code.shubham.commons.utils.FileUtils;
import code.shubham.fiq.models.Message;
import code.shubham.fiq.services.Queue;

import java.io.File;
import java.nio.file.Path;

public class Worker implements Runnable {

    private final int workerId;
    private final String consumerName;
    private final Queue queue;
    private final int offset;

    public Worker(final int workerId, final String consumerName, final Queue queue, final int offset) {
        this.workerId = workerId;
        this.consumerName = consumerName;
        this.queue = queue;
        this.offset = offset;
    }

    @Override
    public void run() {
        while (true) {
            System.out.println("WorkerId: "+workerId+"; Polling message at offset: " + offset);
            final Message message = queue.poll(this.consumerName, offset);
            this.processMessage(message);
        }
    }

    private void processMessage(final Message message) {
        System.out.println("WorkerId: "+workerId+"; Processing message: " + message);
        try {
            Thread.sleep(message.getProcessingTimeInSeconds() * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("WorkerId: "+workerId+"; Processed message: " + message);
    }
}
