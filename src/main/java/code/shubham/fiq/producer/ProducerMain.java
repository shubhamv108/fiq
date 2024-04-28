package code.shubham.fiq.producer;

import code.shubham.commons.clients.Client;
import code.shubham.commons.clients.ClientFactory;
import code.shubham.commons.interations.commands.CommandFactory;
import code.shubham.fiq.models.Message;
import code.shubham.fiq.producer.handlers.ProducerCommandHandler;
import code.shubham.fiq.interactions.CommandFactoryProvider;

import javax.naming.OperationNotSupportedException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProducerMain {

    public static void main(String[] args) {
        final ProducerCommandHandler commandHandler = ProducerCommandHandler.getHandler();
        final CommandFactory commandFactory = CommandFactoryProvider
                .getProvider()
                .getProducerCommandFactory(commandHandler);

        try {
            initCLIClient(args, commandFactory);
//            initDefaultClientAndExecute();
        } catch (final Exception ex) {
            System.out.println("Something went wrong. Please try again!");
        }
    }

    public static void initCLIClient(
            final String[] args,
            final CommandFactory commandFactory)
            throws OperationNotSupportedException, IOException {
        final Client client = ClientFactory.buildClient(args, commandFactory);
        client.handleInput();
    }

    public static void initDefaultClientAndExecute() {
        final String queueName = "test";
        final String[] inputLines = getStaticInput();
        Arrays.stream(inputLines)
                .map(Message::of)
                .map(message -> new Producer(queueName, message))
                .map(Thread::new)
                .forEach(Thread::start);
    }

    private static String[] getStaticInput() {
        final List<String> inputs = new ArrayList<>();
        inputs.add("key1:message1:10");
        inputs.add("key2:message2:10");
        inputs.add("key3:message2:10");
        inputs.add("key4:message2:10");
        inputs.add("key5:message2:10");
        inputs.add("key6:message2:10");
        inputs.add("key7:message2:10");
        inputs.add("key8:message2:10");
        inputs.add("key9:message2:10");
        inputs.add("key0:message2:10");
        return inputs.toArray(String[]::new);
    }

}