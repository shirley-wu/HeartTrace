package com.example.dell.diary;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.example.dell.db.Sentence;

import java.util.List;

/**
 * Created by Helen_L on 2018/5/15.
 */

public class NoteAdapter extends RecyclerView.Adapter <NoteAdapter.ViewHolder> {
    private List<Sentence> mSentenceList;
    private Context mContext;
    private String sentencebookName;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        //ImageView noteIcon;
        TextView sentenceDate;
        TextView sentenceContent;
        //TextView noteWeekDay;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
           /* noteIcon = (ImageView) view.findViewById(R.id.note_icon);*/
            sentenceDate = (TextView)view.findViewById(R.id.note_date);
            sentenceContent = (TextView)view.findViewById(R.id.note_text);
          /*  noteWeekDay = (TextView)view.findViewById(R.id.note_weekday);*/
        }
    }

    public NoteAdapter(List<Sentence> sentenceList, String sentencebookName) {
        this.sentencebookName = sentencebookName;
        mSentenceList = sentenceList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (mContext == null) {
            mContext = parent.getContext();
        }

        //点击查看纸条的详情界面
        View view = LayoutInflater.from(mContext).inflate(R.layout.note_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Sentence sentence = mSentenceList.get(position);
                Intent intent = new Intent(mContext, TicketEditActivity.class);
                intent.putExtra(TicketEditActivity.SENTENCE_THIS, sentence);
                intent.putExtra(TicketEditActivity.NOTE_EDITABLE, "false");
                intent.putExtra(TicketEditActivity.NOTE_NEW, "false");
                mContext.startActivity(intent);
            }
        });

        //长按纸条
        final PopOptionUtil mPop;
        mPop = new PopOptionUtil(mContext);
        mPop.setOnPopClickEvent(new PopClickEvent() {
            @Override
            public void onPreClick() {
                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                dialog.setTitle("提示");
                dialog.setMessage("你确定要删除你的纸条吗？");
                dialog.setCancelable(true);

                dialog.setPositiveButton("确认",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which){
                        DatabaseHelper helper = new DatabaseHelper(mContext);
                        int position = holder.getAdapterPosition();
                       Sentence sentence = mSentenceList.get(position);
                       sentence.delete(helper);
                       mSentenceList = Sentence.getAll(helper,false);
                       notifyDataSetChanged();
                       /* mSentenceList.remove(note);
                        notifyItemRemoved(position);*/
                    }
                });
                dialog.setNegativeButton("取消",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which){
                        dialog.cancel();
                    }
                });
                dialog.show();
                //Toast.makeText(mContext,"删除:"+ position,Toast.LENGTH_SHORT).show();
            }

//            @Override
//            public void onNextClick() {
//                Toast.makeText(mContext,"导出",Toast.LENGTH_SHORT).show();
//            }
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

    public void update(List<Sentence> sentencebookList){
        mSentenceList = sentencebookList;
        notifyDataSetChanged();
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Sentence sentence = mSentenceList.get(position);
        holder.sentenceDate.setText(sentence.getDate().toString());
     //   holder.noteWeekDay.setText(note.getWeekDay());
        holder.sentenceContent.setText(sentence.getText());
    }

    @Override
    public int getItemCount() {
        return mSentenceList.size();
    }
}
