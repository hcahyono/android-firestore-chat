package com.example.demofirestore;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class MessageAdapter extends ArrayAdapter<ChatMessage> {

    private final Context context;
    private final List<ChatMessage> chats;

    public MessageAdapter(Context context, List<ChatMessage> chats){
        super(context, R.layout.message, chats);
        this.context = context;
        this.chats = chats;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.message, parent, false);
        TextView name = (TextView) rowView.findViewById(R.id.textName);
        TextView dateTime = (TextView) rowView.findViewById(R.id.textDateTime);
        TextView message = (TextView) rowView.findViewById(R.id.textMessage);

        ChatMessage chat = chats.get(position);
        name.setText(chat.getMessageUser());
        dateTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                chat.getMessageTime()));
        message.setText(chat.getMessageText());
        return rowView;
    }
}
