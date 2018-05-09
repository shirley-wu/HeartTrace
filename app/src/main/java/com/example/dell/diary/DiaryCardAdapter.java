package com.example.dell.diary;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by dell on 2018/5/7.
 */

public class DiaryCardAdapter extends RecyclerView.Adapter<DiaryCardAdapter.ViewHolder> {
    private List<DiaryCard> mDiaryCardList;
    private Context mContext;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView diaryIcon;
        TextView diaryDate;
        TextView diaryContent;
        TextView diaryWeekDay;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            diaryIcon = (ImageView) view.findViewById(R.id.diary_icon);
            diaryDate = (TextView)view.findViewById(R.id.diary_date);
            diaryContent = (TextView)view.findViewById(R.id.diary_text);
            diaryWeekDay = (TextView)view.findViewById(R.id.diary_weekday);
        }
    }

    public DiaryCardAdapter(List<DiaryCard> diaryCardList) {
        mDiaryCardList = diaryCardList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.diarycard_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int position = holder.getAdapterPosition();
                DiaryCard diaryCard = mDiaryCardList.get(position);
                Intent intent = new Intent(mContext,DiaryActivity.class);
                intent.putExtra("diary_year",diaryCard.getYear());
                intent.putExtra("diary_month",diaryCard.getMonth());
                intent.putExtra("diary_day",diaryCard.getDay());
                intent.putExtra("diary_weekday",diaryCard.getWeekDay());
                intent.putExtra("diary_icon",diaryCard.getEmotionImageId());
                intent.putExtra("diary_text",diaryCard.getContent());
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DiaryCard diaryCard = mDiaryCardList.get(position);
        holder.diaryContent.setText(diaryCard.getContent());
        holder.diaryWeekDay.setText(diaryCard.getWeekDay());
        String date = diaryCard.getYear()+"."+diaryCard.getMonth()+"."+diaryCard.getDay();
        holder.diaryDate.setText(date);
        Glide.with(mContext).load(diaryCard.getEmotionImageId()).into(holder.diaryIcon);
    }

    @Override
    public int getItemCount() {
        return mDiaryCardList.size();
    }
}
