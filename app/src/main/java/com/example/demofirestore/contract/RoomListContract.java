package com.example.demofirestore.contract;

import com.example.demofirestore.model.ChatMessage;
import com.example.demofirestore.model.ChatRoom;

import java.util.List;

public class RoomListContract {

    public interface View{
        void setRooms(List<ChatRoom> rooms);
    }

    public interface Presenter{
        void loadRooms();
    }
}
