package com.example.demofirestore.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.demofirestore.R;
import com.example.demofirestore.adapter.RecycleViewAdapter;
import com.example.demofirestore.adapter.RoomListAdapter;
import com.example.demofirestore.contract.RoomListContract;
import com.example.demofirestore.databinding.ActivityMainBinding;
import com.example.demofirestore.databinding.ActivityRoomListBinding;
import com.example.demofirestore.model.ChatRoom;
import com.example.demofirestore.presenter.MainAppPresenter;
import com.example.demofirestore.presenter.RoomListPresenter;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class RoomListActivity extends AppCompatActivity implements RoomListContract.View {

    private ActivityRoomListBinding binding;
    private RoomListContract.Presenter presenter;
    private RoomListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRoomListBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        presenter = new RoomListPresenter(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getBaseContext());
        binding.viewRooms.setLayoutManager(layoutManager);

        presenter.loadRooms();
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
                            Toast.makeText(RoomListActivity.this,
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
    public void setRooms(List<ChatRoom> rooms) {
        adapter = new RoomListAdapter(RoomListActivity.this, rooms);
        binding.viewRooms.setAdapter(adapter);
    }
}