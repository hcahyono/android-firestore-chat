package com.example.demofirestore.model;

import java.util.Date;

public class ChatMessage {

    private String messageUserId;
    private String messageEmail;
    private String messageText;
    private String messageUser;
    private long messageTime;
    private String messageRoomId;


    public ChatMessage(String messageText, String messageUser, String messageUserId, String messageEmail, String messageRoomId) {
        this.messageText = messageText;
        this.messageUser = messageUser;
        this.messageUserId = messageUserId;
        this.messageEmail = messageEmail;
        this.setMessageRoomId(messageRoomId);

        // Initialize to current time
        messageTime = new Date().getTime();
    }

    public ChatMessage(){

    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }

    public String getMessageEmail() {
        return messageEmail;
    }

    public void setMessageEmail(String messageEmail) {
        this.messageEmail = messageEmail;
    }

    public String getMessageUserId(){
        return messageUserId;
    }

    public void setMessageUserId(String messageUserId) {
        this.messageUserId = messageUserId;
    }


    public String getMessageRoomId() {
        return messageRoomId;
    }

    public void setMessageRoomId(String messageRoomId) {
        this.messageRoomId = messageRoomId;
    }
}
