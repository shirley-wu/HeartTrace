package com.example.dell.diary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dell.db.Diary;
import com.example.dell.db.Sentence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by dell on 2018/7/10.
 */

public class SearchResultSAdapter extends RecyclerView.Adapter<SearchResultSAdapter.ViewHolder>{
    private ArrayList<Sentence> mSentenceList;
    private Context mContext;
    public List<String> weekList = new ArrayList<>(Arrays.asList("周日","周一","周二","周三"," 周四","周五","周六"));

    static class ViewHolder extends RecyclerView.ViewHolder {

        View searchResultView;
        TextView sentenceContent;
        TextView sentenceDate;

        public ViewHolder(View view) {
            super(view);
            searchResultView = view;;
            sentenceContent = (TextView)view.findViewById(R.id.diary_search_text);
            sentenceDate = (TextView)view.findViewById(R.id.diary_search_date);
        }
    }

    public SearchResultSAdapter(ArrayList<Sentence> sentenceList) {
        mSentenceList = sentenceList;
    }

    @Override
    public SearchResultSAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        final View view = LayoutInflater.from(mContext).inflate(R.layout.search_result_item, parent, false);
        final SearchResultSAdapter.ViewHolder holder = new SearchResultSAdapter.ViewHolder(view);
        holder.searchResultView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int position = holder.getAdapterPosition();
                Sentence sentence = mSentenceList.get(position);
                Intent intent = new Intent(mContext, TicketEditActivity.class);
                intent.putExtra(TicketEditActivity.POSITION, position);
                intent.putExtra(TicketEditActivity.SENTENCE_THIS, sentence);
                intent.putExtra(TicketEditActivity.NOTE_EDITABLE, "false");
                intent.putExtra(TicketEditActivity.NOTE_NEW, "false");
                intent.putExtra(TicketEditActivity.TYPE, "ordinary");
                Activity activity = (Activity) mContext;
                activity.startActivityForResult(intent,2);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(SearchResultSAdapter.ViewHolder holder, int position) {
        Sentence sentence = mSentenceList.get(position);
        holder.sentenceContent.setText(sentence.getText());
        String date = (sentence.getDate().getYear()+1900)+"年"+ (sentence.getDate().getMonth()+1) + "月" + sentence.getDate().getDate() + "日 " + weekList.get(sentence.getDate().getDay());
        holder.sentenceDate.setText(date);
        //Glide.with(mContext).load(diaryCard.getEmotionImageId()).into(holder.diaryIcon);
    }

    @Override
    public int getItemCount() {
        return mSentenceList.size();
    }

}

