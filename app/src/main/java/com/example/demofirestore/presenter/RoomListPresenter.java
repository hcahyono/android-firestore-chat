package com.example.demofirestore.presenter;

import android.util.Log;

import androidx.annotation.Nullable;

import com.example.demofirestore.contract.RoomListContract;
import com.example.demofirestore.model.ChatMessage;
import com.example.demofirestore.model.ChatRoom;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class RoomListPresenter implements RoomListContract.Presenter {

    private RoomListContract.View view;
    private List<ChatRoom> rooms;

    public RoomListPresenter(RoomListContract.View view){
        this.view = view;
    }

    @Override
    public void loadRooms() {

        Query query = FirebaseFirestore.getInstance()
                .collection("rooms")
                .whereEqualTo("csUserId", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .whereEqualTo("active", true);

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    // Handle error
                    //...
                    return;
                }


                List<ChatRoom> rooms = new ArrayList<>();
                for(DocumentSnapshot ds: snapshot){
                    ChatRoom room = new ChatRoom();
                    room.setRoomId(ds.getId());
                    room.setRoomName(ds.getString("roomName"));
                    room.setCustomerUserId(ds.getString("customerUserId"));
                    room.setCsUserId(ds.getString("csUserId"));
                    room.setLastMessageTime(ds.getLong("lastMessageTime"));
                    room.setLastMessageString(ds.getString("lastMessageString"));
                    room.setActive(ds.getBoolean("active"));
                    room.setServed(ds.getBoolean("served"));
                    rooms.add(room);
                    Log.i("ROOM_ID", ds.getId());
                }

                // Update UI
                view.setRooms(rooms);
            }
        });

    }
}
