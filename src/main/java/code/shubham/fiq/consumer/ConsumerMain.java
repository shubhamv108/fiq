package code.shubham.fiq.consumer;

public class ConsumerMain {

    public static void main(final String[] args) {
        new Consumer(args[2], args[0], Integer.parseInt(args[1])).consume();
    }

}