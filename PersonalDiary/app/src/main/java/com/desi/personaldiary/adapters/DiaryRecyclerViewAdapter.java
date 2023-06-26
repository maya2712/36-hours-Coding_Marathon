package com.desi.personaldiary.adapters;

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.desi.personaldiary.R;
import com.desi.personaldiary.models.Diary;
import com.desi.personaldiary.utils.DateUtil;

import java.util.ArrayList;

public class DiaryRecyclerViewAdapter extends RecyclerView.Adapter<DiaryRecyclerViewAdapter.ViewHolder> {
    private ArrayList<Diary> mDiary;
    private OnDiaryListener mOnDiaryListener;

    public DiaryRecyclerViewAdapter(ArrayList<Diary> diaryList, OnDiaryListener onDiaryListener) {
        this.mDiary = diaryList;
        this.mOnDiaryListener = onDiaryListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        TextView timestamp;
        OnDiaryListener onDiaryListener;

        public ViewHolder(@NonNull View itemView, OnDiaryListener onDiaryListener) {
            super(itemView);
            title = itemView.findViewById(R.id.diary_title);
            timestamp = itemView.findViewById(R.id.diary_timestamp);
            this.onDiaryListener = onDiaryListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnDiaryListener.onDiaryClick(getAdapterPosition());
        }
    }

    public interface OnDiaryListener {
        void onDiaryClick(int position);

        void onClick(View view);

        //void onClick(View view);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_item_diary, parent, false);
        return new ViewHolder(view, mOnDiaryListener);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            String month = mDiary.get(position).getTimestamp().substring(0,2);
            month = DateUtil.getMonthFromNumber(month);
            String year = mDiary.get(position).getTimestamp().substring(3);
            String timestamp = month + " " + year;
            holder.timestamp.setText(timestamp);
            holder.title.setText(mDiary.get(position).getTitle());
        }catch (NullPointerException e){
            Log.e(TAG, "onBindViewHolder: "+e.getMessage());
        }
//        Diary diary = mDiary.get(position);
//        holder.title.setText(diary.getTitle());
//        holder.timestamp.setText(diary.getTimestamp());
    }

    @Override
    public int getItemCount() {
        return mDiary.size();
    }
}
