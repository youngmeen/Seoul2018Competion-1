package com.example.kim.finalprojectowner1.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kim.finalprojectowner1.R;
import com.example.kim.finalprojectowner1.model.Chat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    String TAG = "tiger";
    List<Chat> mChat;
    String stEmail;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");



    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public TextView mTimestamp;


        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.mTextView);
            mTimestamp = (TextView) itemView.findViewById(R.id.messageItem_textview_timestamp);
        }
    }


    public MyAdapter(List<Chat> mChat) {
        this.mChat = mChat;
    }


    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;
        v = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.right_text_view, parent, false);


        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        String time = simpleDateFormat.format(date);



        holder.mTimestamp.setText(time);
        holder.mTextView.setText(mChat.get(position).getText());
    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }
}
