package com.example.demofirestore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demofirestore.R;
import com.example.demofirestore.model.ChatMessage;

import java.util.List;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.MessageViewHolder> {

    private List<ChatMessage> messages;
    private Context context;
    private String currentUser;
    private final int MY_MESSAGE = 0, THEIR_MESSAGE = 1;

    public RecycleViewAdapter(Context context, List<ChatMessage> messages, String currentUser){
        this.context = context;
        this.messages = messages;
        this.currentUser = currentUser;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        if(viewType == MY_MESSAGE) {
            view = inflater.inflate(R.layout.my_message, parent, false);
        }else{
            view = inflater.inflate(R.layout.their_message, parent, false);
        }
        return new MessageViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage message = messages.get(position);
        if (message.getMessageUser().equalsIgnoreCase(currentUser)) {
            return MY_MESSAGE;
        } else {
            return THEIR_MESSAGE;
        }
    }

    @Override
    public int getItemCount() {
        if(messages != null){
            return messages.size();
        }else{
            return 0;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        ChatMessage message = messages.get(position);
        if(holder.getItemViewType() == THEIR_MESSAGE) {
            holder.name.setText(message.getMessageUser());
        }
//        holder.times.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",message.getMessageTime()));
        holder.message.setText(message.getMessageText());
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        TextView message, name;

        public MessageViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            //times = (TextView) itemView.findViewById(R.id.textDateTime);
            message = (TextView) itemView.findViewById(R.id.message_body);
        }


    }

}
