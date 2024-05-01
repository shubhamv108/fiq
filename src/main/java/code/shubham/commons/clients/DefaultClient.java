package code.shubham.commons.clients;

import code.shubham.commons.interations.commands.CommandFactory;

import javax.naming.OperationNotSupportedException;

public class DefaultClient extends Client {
    public DefaultClient(CommandFactory commandFactory) {
        super(null, commandFactory);
    }

    @Override
    public void handleInput() throws OperationNotSupportedException {
        throw new OperationNotSupportedException();
    }
}
