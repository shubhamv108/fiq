package code.shubham.fiq.consumer;

public class ConsumerMain {

    public static void main(final String[] args) {
        final String consumptionId = args[2] + "-" + args[0];
        final int offset = ConsumerHandler.getOffset(consumptionId);
        final String consumerName = args[2];
        final String queueName = args[0];
        final int concurrencyFactor = Integer.parseInt(args[1]);

        new Consumer(
                consumerName,
                queueName,
                concurrencyFactor,
                offset).run();
    }

}