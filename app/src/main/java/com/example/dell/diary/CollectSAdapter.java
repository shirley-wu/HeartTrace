package com.example.dell.diary;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dell.db.DatabaseHelper;
import com.example.dell.db.Diary;
import com.example.dell.db.Sentence;

import java.util.List;

public class CollectSAdapter extends RecyclerView.Adapter<CollectSAdapter.ViewHolder> {
    private List<Sentence> mfavoriteSList;
    private Context mContext;
    private DatabaseHelper databaseHelper = null;


    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView sentence_write;
        TextView sentenceDate;
        CardView cardView;


        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            sentence_write =  view.findViewById(R.id.sentence_content_like);
            sentenceDate = view.findViewById(R.id.sentence_date_like);

        }
    }

    public CollectSAdapter(List<Sentence> mfavoriteList){
        mfavoriteSList = mfavoriteList;

    }




    @Override
    public CollectSAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }

        View view = LayoutInflater.from(mContext).inflate(R.layout.collects_item, parent, false);
        final CollectSAdapter.ViewHolder holder = new CollectSAdapter.ViewHolder(view);


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Sentence sentence = mfavoriteSList.get(position);
                Intent intent = new Intent(mContext, TicketEditActivity.class);
                intent.putExtra(TicketEditActivity.POSITION, position);
                intent.putExtra(TicketEditActivity.SENTENCE_THIS, sentence);
                intent.putExtra(TicketEditActivity.NOTE_EDITABLE, "false");
                intent.putExtra(TicketEditActivity.NOTE_NEW, "false");
                Activity activity = (Activity) mContext;
                activity.startActivityForResult(intent,2);
            }
        });
        //常按操作
        final PopOptionUtil mPop;
        mPop = new PopOptionUtil(mContext);
        mPop.setOnPopClickEvent(new PopClickEvent() {
            @Override
            public void onPreClick() {
                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                dialog.setTitle("提示");
                dialog.setMessage("确认删除？");
                dialog.setCancelable(true);

                dialog.setPositiveButton("确认",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which){
                        int position = holder.getAdapterPosition();
                        deleteSentenceLike(position);
                    }
                });
                dialog.setNegativeButton("取消",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which){

                    }
                });
                dialog.show();
                //Toast.makeText(mContext,"删除:"+ position,Toast.LENGTH_SHORT).show();
            }

            //           @Override
            //          public void onNextClick() {
            //              Toast.makeText(mContext,"导出",Toast.LENGTH_SHORT).show();
            //           }
        });
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View view){
                mPop.show(view);
                return true;
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(CollectSAdapter.ViewHolder holder, int position) {
        Sentence sentence = mfavoriteSList.get(position);
        holder.sentenceDate.setText(sentence.getDate().toString());
        holder.sentence_write.setText(sentence.getText());
    }



    public void deleteSentenceLike(int position){
        DatabaseHelper helper = new DatabaseHelper(mContext);
        Sentence sentence = mfavoriteSList.get(position);
       /* sentence.setLike(false);*/
        sentence.update(helper);
        /*mfavoriteSList=Diary.getAllLike(helper, false);*/
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mfavoriteSList.size();
    }
}
