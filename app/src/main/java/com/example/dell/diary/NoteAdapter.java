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

import java.util.List;

/**
 * Created by Helen_L on 2018/5/15.
 */

public class NoteAdapter extends RecyclerView.Adapter <NoteAdapter.ViewHolder> {
    private List<Note> mNoteList;

    private Context mContext;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView noteIcon;
        TextView noteDate;
        TextView noteContent;
        TextView noteWeekDay;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            noteIcon = (ImageView) view.findViewById(R.id.note_icon);
            noteDate = (TextView)view.findViewById(R.id.note_date);
            noteContent = (TextView)view.findViewById(R.id.note_text);
            noteWeekDay = (TextView)view.findViewById(R.id.note_weekday);
        }
    }

    public NoteAdapter(List<Note> noteList) {
        mNoteList = noteList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.note_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        final PopOptionUtil mPop;
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Note note = mNoteList.get(position);
                Intent intent = new Intent(mContext, NoteActivity.class);
                intent.putExtra(NoteActivity.NOTE_DATE, note.getDate());
                intent.putExtra(NoteActivity.NOTE_WEAKDAY, note.getWeekDay());
                intent.putExtra(NoteActivity.NOTE_CONTENT, note.getContent());
                mContext.startActivity(intent);
            }
        });

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
                        int position = holder.getAdapterPosition();
                        Note note = mNoteList.get(position);
                        mNoteList.remove(note);
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
    public void onBindViewHolder(ViewHolder holder, int position) {
        Note note = mNoteList.get(position);
        holder.noteDate.setText(note.getDate());
        holder.noteWeekDay.setText(note.getWeekDay());
        holder.noteContent.setText(note.getContent());
    }

    @Override
    public int getItemCount() {
        return mNoteList.size();
    }
}
