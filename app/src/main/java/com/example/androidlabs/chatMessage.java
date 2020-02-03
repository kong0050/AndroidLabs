package com.example.androidlabs;

public class chatMessage {
    private String message;

    private boolean isSend;
    public String getMessage() {
        return message;
    }
    public void setMessage(String chatMessage) {
        this.message = chatMessage;
    }
    public boolean getIsSend() {
        return isSend;
    }
    public void setIsSend(boolean isSend) {
        this.isSend = isSend;
    }

}
