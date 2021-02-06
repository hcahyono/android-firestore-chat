package com.example.demofirestore.contract;

import com.example.demofirestore.model.ChatMessage;

import java.util.List;

public class MessageListContract {

    public interface View{
        void setMessages(List<ChatMessage> messages);
        void setCurrentRoom(String roomId);
    }

    public interface Presenter{
        void loadChatMessages(String roomId);
        void initRoom();
    }
}
