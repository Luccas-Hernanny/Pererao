package br.com.pererao.model;

public class Chat {

    private String message;
    private String receiver;
    private String sender;
    private boolean isseen;
    private long messageTime;

    public Chat() {
    }

    public Chat(String message, String receiver, String sender, long messageTime, boolean isseen) {
        this.message = message;
        this.receiver = receiver;
        this.sender = sender;
        this.messageTime = messageTime;
        this.isseen = isseen;
    }

    public Chat(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }

    public boolean isIsseen() {
        return isseen;
    }

    public void setIsseen(boolean isseen) {
        this.isseen = isseen;
    }
}