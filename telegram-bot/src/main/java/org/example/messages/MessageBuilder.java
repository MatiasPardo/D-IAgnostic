package org.example.messages;

public class MessageBuilder {
    private final StringBuilder message;

    public MessageBuilder() {
        this.message = new StringBuilder();
    }

    public static MessageBuilder builder() {
        return new MessageBuilder();
    }

    public MessageBuilder withLine(String line) {
        this.message.append("\n").append(line);
        return this;
    }

    public String build() {
        return this.message.toString();
    }
}
