package code.shubham.fiq.consumer;

public class ConsumerMain {

    public static void main(final String[] args) {
        final int offset = ConsumerHandler.getOffset(args[2] + "-" + args[0]);
        final String consumerName = args[2];
        final String queueName = args[0];
        final int concurrencyFactor = Integer.parseInt(args[1]);
        new Consumer(
                consumerName,
                queueName,
                concurrencyFactor,
                offset).consume();
    }

}