package com.example.demofirestore.presenter;

import androidx.annotation.Nullable;

import com.example.demofirestore.contract.MessageListContract;
import com.example.demofirestore.model.ChatMessage;
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
    public void loadChatMessages() {
        Query query = FirebaseFirestore.getInstance()
                .collection("chats")
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
}
