package com.example.androidlabs;

public class chatMessage {
    private String message;
    private boolean isSend;
    private long id;


    public chatMessage(String m, Boolean b, long i)
    {
        setMessage(m);
        setIsSend(b);
        setId(i);
    }

    public void update(String m, Boolean b)
    {
        setMessage(m);
        setIsSend(b);
    }

    /**Chaining constructor: */
    public chatMessage(String m, Boolean b) { this(m, b, 0);}

    public chatMessage(){
    }


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

    public long getId() {
        return id;
    }
    public void setId (long id) {
        this.id=id;
    }
}
