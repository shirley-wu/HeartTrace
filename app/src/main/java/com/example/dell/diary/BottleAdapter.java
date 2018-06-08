package com.example.dell.diary;

import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.dell.db.DatabaseHelper;
import com.example.dell.db.Sentencebook;

import java.util.List;

import static com.example.dell.db.SentenceLabel.TAG;
import static java.security.AccessController.getContext;

/**
 * Created by Helen_L on 2018/5/15.
 */

public class BottleAdapter extends RecyclerView.Adapter<BottleAdapter.ViewHolder> {

    private List<Sentencebook> mSentencebookList;
    private Context mContext;
    private DatabaseHelper databaseHelper = null;


    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView bottleIcon;
        TextView bottleName;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            bottleIcon = (ImageView) view.findViewById(R.id.bottle_icon);
            bottleName = (TextView) view.findViewById(R.id.bottle_name);
        }
    }

    public BottleAdapter(List<Sentencebook> SentencebookList) {
       mSentencebookList = SentencebookList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.bottle_item, parent, false);
        final BottleAdapter.ViewHolder holder = new BottleAdapter.ViewHolder(view);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Sentencebook sentencebook = mSentencebookList.get(position);
                Intent intent = new Intent(mContext,BottlefrontActivity.class);
                intent.putExtra(BottlefrontActivity.BOTTLE_NAME, sentencebook.getSentencebookName());
                mContext.startActivity(intent);
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
                dialog.setMessage("你确定要删除你的瓶子吗？");
                dialog.setCancelable(true);

                dialog.setPositiveButton("确认",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which){
                        int position = holder.getAdapterPosition();
                       /* Sentencebook  sentencebook = mSentencebookList.get(position);
                        DatabaseHelper helper = new DatabaseHelper(mContext);
                        sentencebook.delete(helper);
                        mSentencebookList = Sentencebook.getAll(helper, false);*/
                        deleteSentencebook(position);
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
        Sentencebook sentencebook = mSentencebookList.get(position);
        holder.bottleName.setText(sentencebook.getSentencebookName());

       // Glide.with(mContext).load(sentencebook.getImageId()).into(holder.bottleIcon);*/
    }
    public void update(List<Sentencebook> sentencebookList){
        mSentencebookList = sentencebookList;
        notifyDataSetChanged();
    }



    public void addSentencebook(int position ,Sentencebook sentencebook){
       DatabaseHelper helper = new DatabaseHelper(mContext);
       sentencebook.insert(helper);
       mSentencebookList=Sentencebook.getAll(helper, false);
       mSentencebookList.add(position, sentencebook);
       notifyItemInserted(position);
    }


    public void deleteSentencebook(int position){
        DatabaseHelper helper = new DatabaseHelper(mContext);
        Sentencebook  sentencebook = mSentencebookList.get(position);
        sentencebook.delete(helper);
        mSentencebookList=Sentencebook.getAll(helper, false);
        //notifyItemRemoved(position);
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return mSentencebookList.size();
    }

}


