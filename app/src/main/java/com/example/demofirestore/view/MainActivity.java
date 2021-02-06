package com.example.demofirestore.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.demofirestore.R;
import com.example.demofirestore.adapter.RecycleViewAdapter;
import com.example.demofirestore.contract.MessageListContract;
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

public class MainActivity extends AppCompatActivity implements MessageListContract.View {

    private ActivityMainBinding binding;
    private static final int SIGN_IN_REQUEST_CODE = 1;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private RecycleViewAdapter adapter;
    private MessageListContract.Presenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        presenter = new MainAppPresenter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getBaseContext());
        layoutManager.setStackFromEnd(true);
        binding.viewMessages.setLayoutManager(layoutManager);

        if(FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .build(),
                    SIGN_IN_REQUEST_CODE
            );
        } else {
            if(FirebaseAuth.getInstance().getCurrentUser().getEmail().contains("cs")){
                final Intent roomListActivity = new Intent(this,RoomListActivity.class);
                roomListActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(roomListActivity);
                finish();
            }
            SharedPreferences sharedPref = this.getPreferences(this.getApplicationContext().MODE_PRIVATE);
            presenter.loadChatMessages(sharedPref.getString("ROOM_ID",""));
        }

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref = MainActivity.this.getPreferences(MainActivity.this.getApplicationContext().MODE_PRIVATE);

                long currentItem = new Date().getTime();
                ChatMessage chat = new ChatMessage();
                chat.setMessageRoomId(sharedPref.getString("ROOM_ID",""));
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
                db.collection("rooms")
                        .document(sharedPref.getString("ROOM_ID",""))
                        .update(room);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SIGN_IN_REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                if(FirebaseAuth.getInstance().getCurrentUser().getEmail().contains("cs")){
                    final Intent roomListActivity = new Intent(this,RoomListActivity.class);
                    roomListActivity.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(roomListActivity);
                    finish();
                }else {
                    presenter.initRoom();
                }
            } else {
                Toast.makeText(this,
                        "We couldn't sign you in. Please try again later.",
                        Toast.LENGTH_LONG)
                        .show();
                // Close the app
                finish();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_sign_out) {
            AuthUI.getInstance().signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(MainActivity.this,
                                    "You have been signed out.",
                                    Toast.LENGTH_LONG)
                                    .show();

                            // Close activity
                            finish();
                        }
                    });
        }
        return true;
    }

    @Override
    public void setMessages(List<ChatMessage> messages) {
        adapter = new RecycleViewAdapter(MainActivity.this, messages, FirebaseAuth.getInstance().getCurrentUser().getUid());
        binding.viewMessages.setAdapter(adapter);
    }

    @Override
    public void setCurrentRoom(String roomId) {
        SharedPreferences sharedPref = this.getPreferences(this.getApplicationContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("ROOM_ID", roomId);
        editor.commit();
        presenter.loadChatMessages(roomId);
    }

}