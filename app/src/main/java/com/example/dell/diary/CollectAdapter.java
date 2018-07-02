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

import com.example.dell.db.DatabaseHelper;
import com.example.dell.db.Diary;
import com.example.dell.db.Sentencebook;

import java.util.List;

public class CollectAdapter extends RecyclerView.Adapter<CollectAdapter.ViewHolder> {
    private List<Diary> mfavoriteList;
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

    public CollectAdapter(List<Diary> mfavoriteListList) {
        mfavoriteList = mfavoriteList;
    }

    @Override
    public CollectAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.diarycard_item, parent, false);
        final CollectAdapter.ViewHolder holder = new CollectAdapter.ViewHolder(view);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* int position = holder.getAdapterPosition();
                Sentencebook sentencebook = mfavoriteList.get(position);
                Intent intent = new Intent(mContext,BottlefrontActivity.class);
                intent.putExtra(BottlefrontActivity.BOTTLE_NAME, sentencebook.getSentencebookName());
                mContext.startActivity(intent);*/
            }
        });
        //常按操作
    /*    final PopOptionUtil mPop;
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
        */
        return holder;
    }

    @Override
    public void onBindViewHolder(CollectAdapter.ViewHolder holder, int position) {
        Diary diary = mfavoriteList.get(position);
       /* holder.bottleName.setText(sentencebook.getSentencebookName());*/

        // Glide.with(mContext).load(sentencebook.getImageId()).into(holder.bottleIcon);*/
    }
   /* public void update(List<Sentencebook> sentencebookList){
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
*/

    @Override
    public int getItemCount() {
        return mfavoriteList.size();
    }
}
