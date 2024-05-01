package code.shubham.commons.interations.commands;

public interface Command {

    String helpText();

    void execute(String[] params);

}