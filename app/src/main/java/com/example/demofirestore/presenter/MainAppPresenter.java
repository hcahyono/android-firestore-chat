package com.example.demofirestore.presenter;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.demofirestore.contract.MessageListContract;
import com.example.demofirestore.model.ChatMessage;
import com.example.demofirestore.model.ChatRoom;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class MainAppPresenter implements MessageListContract.Presenter {

    private MessageListContract.View view;
    private List<ChatMessage> messages;


    public MainAppPresenter(MessageListContract.View view){
        this.view = view;
    }

    @Override
    public void loadChatMessages(String roomId) {

        Log.d("LOAD_CHAT", "Room ID" + roomId);

        Query query = FirebaseFirestore.getInstance()
                .collection("chats")
                .whereEqualTo("messageRoomId", roomId)
                .orderBy("messageTime")
                .limit(20);

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    // Handle error
                    //...
                    return;
                }

                // Convert query snapshot to a list of chats
                List<ChatMessage> messages = snapshot.toObjects(ChatMessage.class);

                // Update UI
                view.setMessages(messages);
            }
        });
    }

    @Override
    public void initRoom() {
        ChatRoom room = new ChatRoom(
                FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),
                FirebaseAuth.getInstance().getCurrentUser().getUid(),
                null,
                true,
                false,
                ""
        );

        FirebaseFirestore.getInstance()
                .collection("rooms").add(room)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("SUCCESS", "DocumentSnapshot added with ID: " + documentReference.getId());
                        view.setCurrentRoom(documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("FAILED", "Error adding document", e);
                    }
                });

    }
}
