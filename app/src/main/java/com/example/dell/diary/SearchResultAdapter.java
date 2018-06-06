package com.example.dell.diary;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.db.DatabaseHelper;
import com.example.dell.db.Diary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by dell on 2018/6/4.
 */

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder>{
    private List<Diary> mDiaryList;
    private Context mContext;
    public List<String> weekList = new ArrayList<>(Arrays.asList("周日","周一","周二","周三"," 周四","周五","周六"));

    static class ViewHolder extends RecyclerView.ViewHolder {

        View searchResultView;
        TextView diaryContent;
        TextView diaryDate;

        public ViewHolder(View view) {
            super(view);
            searchResultView = view;;
            diaryContent = (TextView)view.findViewById(R.id.diary_search_text);
            diaryDate = (TextView)view.findViewById(R.id.diary_search_date);
        }
    }

    public SearchResultAdapter(List<Diary> diaryList) {
        mDiaryList = diaryList;
    }

    @Override
    public SearchResultAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        final View view = LayoutInflater.from(mContext).inflate(R.layout.search_result_item, parent, false);
        final SearchResultAdapter.ViewHolder holder = new SearchResultAdapter.ViewHolder(view);
        holder.searchResultView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int position = holder.getAdapterPosition();
                Diary diary = mDiaryList.get(position);
                Intent intent = new Intent(mContext,DiaryActivity.class);
               // intent.putExtra("diary_list",diary);
                mContext.startActivity(intent);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(SearchResultAdapter.ViewHolder holder, int position) {
        Diary diary = mDiaryList.get(position);
        holder.diaryContent.setText(diary.getText());
        String date = (diary.getDate().getYear())+"年"+ (diary.getDate().getMonth()) + "月" + diary.getDate().getDate() + "日 " + weekList.get(diary.getDate().getDay());
        holder.diaryDate.setText(date);
        //Glide.with(mContext).load(diaryCard.getEmotionImageId()).into(holder.diaryIcon);
    }

    @Override
    public int getItemCount() {
        return mDiaryList.size();
    }

}
