package com.example.codetrack;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.service.autofill.TextValueSanitizer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ContestAdapter extends RecyclerView.Adapter<ContestAdapter.MyViewHolder> {
    private List<Contest> mList;
    private Context mContext;
    private Contest mModel;

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textName, textStartTime, textDuration, textEndTime;
        RelativeLayout card;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.text_name);
            textStartTime = itemView.findViewById(R.id.text_start_time);
            textDuration = itemView.findViewById(R.id.text_duration);
            textEndTime = itemView.findViewById(R.id.text_end_time);
            card = itemView.findViewById(R.id.card);


        }
    }

    ContestAdapter(List<Contest> list, Context context) {
        this.mList = list;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ContestAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cards, parent, false);

        return new MyViewHolder(itemView);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull ContestAdapter.MyViewHolder holder, final int position) {
        mModel = mList.get(position);
        holder.textName.setText(mModel.getName());
        if (mModel.getStartTime().equals(""))
            holder.textStartTime.setVisibility(View.GONE);
        else
            holder.textStartTime.setText(mModel.getStartTime());
        if (mModel.getDuration().equals(""))
            holder.textDuration.setVisibility(View.GONE);
        else
            holder.textDuration.setText(mModel.getDuration());
        holder.textEndTime.setText(mModel.getEndTime());


    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }
}
