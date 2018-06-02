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

public class BottleAdapter extends RecyclerView.Adapter<BottleAdapter.ViewHolder> {

    private List<Bottle> mBottleList;

    private Context mContext;

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

    public BottleAdapter(List<Bottle> bottleList) {
        mBottleList = bottleList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.bottle_item, parent, false);
        final BottleAdapter.ViewHolder holder = new BottleAdapter.ViewHolder(view);
        final PopOptionUtil mPop;
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Bottle bottle = mBottleList.get(position);
                Intent intent = new Intent(mContext,BottlefrontActivity.class);
                intent.putExtra(BottlefrontActivity.BOTTLE_NAME, bottle.getBottleName());
                intent.putExtra(BottlefrontActivity.BOTTLE_IMAGE_ID, bottle.getImageId());
                mContext.startActivity(intent);
            }
        });

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
                        deleteBottle(position);
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
        Bottle bottle = mBottleList.get(position);
        holder.bottleName.setText(bottle.getBottleName());

        Glide.with(mContext).load(bottle.getImageId()).into(holder.bottleIcon);
    }
    public void addBottle(int position, Bottle bottle){
        mBottleList.add(position, bottle);
        notifyItemInserted(position);
    }
    public void deleteBottle(int position){
        mBottleList.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return mBottleList.size();
    }
}


