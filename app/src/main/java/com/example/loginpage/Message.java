package com.example.loginpage;

public class Message {
    public static final int TYPE_USER = 0;
    public static final int TYPE_BOT = 1;

    private final String content;
    private final int type;

    public Message(String content, int type) {
        this.content = content;
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public int getType() {
        return type;
    }
}
