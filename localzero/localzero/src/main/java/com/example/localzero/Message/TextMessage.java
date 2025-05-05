package com.example.localzero.Message;

public class TextMessage {
    private String sender;
    private String receiver;
    private String text;

    public TextMessage() {

    }

    public TextMessage(String sender, String receiver, String text) {
        this.sender = sender;
        this.receiver = receiver;
        this.text = text;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getContent() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
