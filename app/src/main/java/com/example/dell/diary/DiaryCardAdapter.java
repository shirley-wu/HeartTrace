package com.example.dell.diary;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by dell on 2018/5/7.
 */

public class DiaryCardAdapter extends RecyclerView.Adapter<DiaryCardAdapter.ViewHolder> {
    private List<DiaryCard> mDiaryCardList;
    private Context mContext;

    static class ViewHolder extends RecyclerView.ViewHolder {
        //CardView cardView;
        View timeLineView;
        ImageView diaryIcon;
        TextView diaryDay;
        TextView diaryYearMonth;
        TextView diaryContent;
        TextView diaryWeekDay;

        public ViewHolder(View view) {
            super(view);
            //cardView = (CardView) view;
            timeLineView = view;
            diaryIcon = (ImageView) view.findViewById(R.id.diary_icon);
            diaryDay = (TextView)view.findViewById(R.id.diary_day);
            diaryYearMonth = (TextView)view.findViewById(R.id.diary_year_month);
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
        final PopOptionUtil mPop;
        final View view = LayoutInflater.from(mContext).inflate(R.layout.timeline_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.timeLineView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int position = holder.getAdapterPosition();
                DiaryCard diaryCard = mDiaryCardList.get(position);
                Intent intent = new Intent(mContext,DiaryActivity.class);
                //intent.putExtra("diary_year",diaryCard.getYear());
                //intent.putExtra("diary_month",diaryCard.getMonth());
                //intent.putExtra("diary_day",diaryCard.getDay());
                //intent.putExtra("diary_weekday",diaryCard.getWeekDay());
                //intent.putExtra("diary_icon",diaryCard.getEmotionImageId());
                //intent.putExtra("diary_text",diaryCard.getContent());
                intent.putExtra("diary_list",diaryCard);
                mContext.startActivity(intent);
            }
        });
        mPop = new PopOptionUtil(mContext);
        mPop.setOnPopClickEvent(new PopClickEvent() {
            @Override
            public void onPreClick() {
                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                dialog.setTitle("提示");
                dialog.setMessage("你确定要删除你的日记吗？");
                dialog.setCancelable(true);

                dialog.setPositiveButton("确认",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which){
                        int position = holder.getAdapterPosition();
                        DiaryCard diaryCard = mDiaryCardList.get(position);
                        mDiaryCardList.remove(diaryCard);
                        notifyItemRemoved(position);
                    }
                });
                dialog.setNegativeButton("取消",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which){

                    }
                });
                dialog.show();
                //Toast.makeText(mContext,"删除:"+ position,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNextClick() {
                Toast.makeText(mContext,"导出",Toast.LENGTH_SHORT).show();
            }
        });
        holder.timeLineView.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v){
                int position = holder.getAdapterPosition();
                DiaryCard diaryCard = mDiaryCardList.get(position);
                mPop.show(view);
                return true;
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DiaryCard diaryCard = mDiaryCardList.get(position);
        holder.diaryContent.setText(diaryCard.getContent());
        holder.diaryWeekDay.setText(diaryCard.getWeekDay());
        String yearMonth = diaryCard.getYear()+"."+diaryCard.getMonth();
        holder.diaryYearMonth.setText(yearMonth);
        holder.diaryDay.setText(String.valueOf(diaryCard.getDay()));
        Glide.with(mContext).load(diaryCard.getEmotionImageId()).into(holder.diaryIcon);
    }

    @Override
    public int getItemCount() {
        return mDiaryCardList.size();
    }
}
