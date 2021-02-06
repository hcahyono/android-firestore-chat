package com.example.demofirestore.model;

import java.util.Date;

public class ChatRoom {

    private String roomId;
    private String roomName;
    private String customerUserId;
    private String csUserId;
    private long lastMessageTime;
    private String lastMessageString;
    private boolean active;
    private boolean served;

    public ChatRoom(String roomName, String customerUserId, String csUserId, boolean active,
                    boolean served, String lastMessageString){
        this.setRoomName(roomName);
        this.customerUserId = customerUserId;
        this.csUserId = csUserId;
        this.active = active;
        this.served = served;
        this.setLastMessageString(lastMessageString);
        this.setLastMessageTime(new Date().getTime());
    }

    public ChatRoom(){}


    public String getCustomerUserId() {
        return customerUserId;
    }

    public void setCustomerUserId(String customerUserId) {
        this.customerUserId = customerUserId;
    }

    public String getCsUserId() {
        return csUserId;
    }

    public void setCsUserId(String csUserId) {
        this.csUserId = csUserId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isServed() {
        return served;
    }

    public void setServed(boolean served) {
        this.served = served;
    }


    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public long getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(long lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    public String getLastMessageString() {
        return lastMessageString;
    }

    public void setLastMessageString(String lastMessageString) {
        this.lastMessageString = lastMessageString;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
}
