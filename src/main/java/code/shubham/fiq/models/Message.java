package code.shubham.fiq.models;

public record Message(String key, String message, long processingTimeInSeconds) {

    public static Message of(final String message) {
        final String[] part = message.split(":");
        return new Message(part[0], part[1], Long.parseLong(part[2]));
    }

    @Override
    public String toString() {
        return String.format("%s:%s:%s", this.key, this.message, this.processingTimeInSeconds);
    }
}
