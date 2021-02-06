package com.example.demofirestore.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demofirestore.R;
import com.example.demofirestore.model.ChatMessage;
import com.example.demofirestore.model.ChatRoom;
import com.example.demofirestore.view.ChatRoomActivity;

import java.util.List;

public class RoomListAdapter extends RecyclerView.Adapter<RoomListAdapter.RoomViewHolder>{

    private List<ChatRoom> rooms;
    private Context context;

    public RoomListAdapter(Context context, List<ChatRoom> rooms){
        this.context = context;
        this.rooms = rooms;
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.room_item, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        ChatRoom room = rooms.get(position);
        holder.name.setText(room.getRoomName());
        holder.description.setText(room.getLastMessageString().equalsIgnoreCase("") ?
                "Ada customer ingin bertanya.." : room.getLastMessageString());
        holder.roomRow.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Log.d("ROOM_ID", room.getRoomId());
                Intent intent = new Intent(context, ChatRoomActivity.class);
                intent.putExtra("ROOM_ID", room.getRoomId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(rooms != null){
            return rooms.size();
        }else{
            return 0;
        }
    }

    public class RoomViewHolder extends RecyclerView.ViewHolder {

        TextView name, description;
        LinearLayout roomRow;

        public RoomViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.roomName);
            description = (TextView) itemView.findViewById(R.id.roomDescription);
            roomRow = (LinearLayout) itemView.findViewById(R.id.roomRow);
        }


    }
}


