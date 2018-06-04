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
import com.example.dell.db.DatabaseHelper;
import com.example.dell.db.Diary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by dell on 2018/5/7.
 */

public class DiaryCardAdapter extends RecyclerView.Adapter<DiaryCardAdapter.ViewHolder> {
//    private List<DiaryCard> mDiaryCardList;
    private List<Diary>mDiaryCardList;
    private Context mContext;
    public List<String> weekList = new ArrayList<>(Arrays.asList("周日","周一","周二","周三"," 周四","周五","周六"));

    static class ViewHolder extends RecyclerView.ViewHolder {
        //CardView cardView;
        View timeLineView;
        //ImageView diaryIcon;
        TextView diaryDay;
        TextView diaryYearMonth;
        TextView diaryContent;
        TextView diaryWeekDay;

        public ViewHolder(View view) {
            super(view);
            //cardView = (CardView) view;
            timeLineView = view;
            //diaryIcon = (ImageView) view.findViewById(R.id.diary_icon);
            diaryDay = (TextView)view.findViewById(R.id.diary_day);
            diaryYearMonth = (TextView)view.findViewById(R.id.diary_year_month);
            diaryContent = (TextView)view.findViewById(R.id.diary_text);
            diaryWeekDay = (TextView)view.findViewById(R.id.diary_weekday);
        }
    }

    public DiaryCardAdapter(List<Diary> diaryCardList) {
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
                Diary diaryCard = mDiaryCardList.get(position);
                Intent intent = new Intent(mContext,DiaryActivity.class);
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
                        DatabaseHelper helper = new DatabaseHelper(mContext);
                        int position = holder.getAdapterPosition();
                        Diary diaryCard = mDiaryCardList.get(position);
                        mDiaryCardList.remove(diaryCard);
                        diaryCard.delete(helper);
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
                Diary diaryCard = mDiaryCardList.get(position);
                mPop.show(view);
                return true;
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
         Diary diaryCard = mDiaryCardList.get(position);
         holder.diaryContent.setText(diaryCard.getText());
         holder.diaryWeekDay.setText(weekList.get(diaryCard.getDate().getDay()));
         String yearMonth = (diaryCard.getDate().getYear())+"."+ (diaryCard.getDate().getMonth());
         holder.diaryYearMonth.setText(yearMonth);
         holder.diaryDay.setText(String.valueOf(diaryCard.getDate().getDate()));
        //Glide.with(mContext).load(diaryCard.getEmotionImageId()).into(holder.diaryIcon);
    }

    @Override
    public int getItemCount() {
        return mDiaryCardList.size();
    }
}
