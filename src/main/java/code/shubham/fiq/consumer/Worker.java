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

    public Worker(final int workerId, final String consumerName, final Queue queue) {
        this.workerId = workerId;
        this.consumerName = consumerName;
        this.queue = queue;
    }

    @Override
    public void run() {
        while (true) {
            File lockFile = null;
            try {
                lockFile = FileUtils.lock(Path.of("tmp/queues/" + queue.getName() + "-" + consumerName));
                int offset = ConsumerHandler.getOffset(this.consumerName);
                System.out.println("WorkerId: "+workerId+"; Polling message at offset: " + offset);
                final Message message = queue.poll(this.consumerName, offset);
                this.processMessage(message);
                ConsumerHandler.commit(this.consumerName, offset + 1);
            } finally {
                if (lockFile != null)
                    FileUtils.unLock(lockFile);
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
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
