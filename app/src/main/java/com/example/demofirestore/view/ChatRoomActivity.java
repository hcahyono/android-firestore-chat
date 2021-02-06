package com.example.demofirestore.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.demofirestore.R;
import com.example.demofirestore.adapter.RecycleViewAdapter;
import com.example.demofirestore.contract.MessageListContract;
import com.example.demofirestore.databinding.ActivityChatRoomBinding;
import com.example.demofirestore.databinding.ActivityMainBinding;
import com.example.demofirestore.model.ChatMessage;
import com.example.demofirestore.presenter.MainAppPresenter;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatRoomActivity extends AppCompatActivity implements MessageListContract.View{

    private ActivityChatRoomBinding binding;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private RecycleViewAdapter adapter;
    private MessageListContract.Presenter presenter;
    private String roomId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Intent intent = getIntent();
        roomId = intent.getStringExtra("ROOM_ID");

        presenter = new MainAppPresenter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getBaseContext());
        layoutManager.setStackFromEnd(true);
        binding.viewMessages.setLayoutManager(layoutManager);

        presenter.loadChatMessages(roomId);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SharedPreferences sharedPref = MainActivity.this.getPreferences(MainActivity.this.getApplicationContext().MODE_PRIVATE);

                long currentItem = new Date().getTime();
                ChatMessage chat = new ChatMessage();
                chat.setMessageRoomId(roomId);
                chat.setMessageTime(currentItem);
                chat.setMessageText(binding.input.getText().toString());
                chat.setMessageUser(
                        FirebaseAuth.getInstance().getCurrentUser().getDisplayName()!=null ?
                                FirebaseAuth.getInstance().getCurrentUser().getDisplayName() : "Customer Service");
                chat.setMessageEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                chat.setMessageUserId(FirebaseAuth.getInstance().getCurrentUser().getUid());

                db.collection("chats")
                        .add(chat)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d("SUCCESS", "DocumentSnapshot added with ID: " + documentReference.getId());
                                binding.input.setText("");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("FAILED", "Error adding document", e);
                            }
                        });

                //update room
                Map<String, Object> room = new HashMap<>();
                room.put("lastMessageString", binding.input.getText().toString());
                room.put("lastMessageTime", currentItem);
                room.put("served", true);
                db.collection("rooms")
                        .document(roomId)
                        .update(room);
            }
        });
    }

    @Override
    public void setMessages(List<ChatMessage> messages) {
        adapter = new RecycleViewAdapter(ChatRoomActivity.this, messages, FirebaseAuth.getInstance().getCurrentUser().getUid());
        binding.viewMessages.setAdapter(adapter);
    }

    @Override
    public void setCurrentRoom(String roomId) {
        this.roomId = roomId;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.close_chat_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.close_chat_menu) {
            //update room
            Map<String, Object> room = new HashMap<>();
            room.put("active", false);
            db.collection("rooms")
                    .document(roomId)
                    .update(room);
            Toast.makeText(this.getApplicationContext(),"Sesi chat ini sudah selesai",Toast.LENGTH_LONG).show();
            finish();
        }
        return true;
    }
}