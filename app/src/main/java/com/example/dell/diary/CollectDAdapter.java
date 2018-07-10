package com.example.dell.diary;

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
        import android.widget.EditText;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.example.dell.db.DatabaseHelper;
        import com.example.dell.db.Diary;
        import com.example.dell.db.Sentence;
        import com.example.dell.db.Sentencebook;

        import java.util.List;

public class CollectDAdapter extends RecyclerView.Adapter<CollectDAdapter.ViewHolder> {
    private List<Diary> mfavoriteDList;
    private Context mContext;
    private DatabaseHelper databaseHelper = null;


    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView diary_write;
        TextView diaryDate;
        CardView cardView;


        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            diary_write =  view.findViewById(R.id.diary_content_like);
            diaryDate = view.findViewById(R.id.diary_date_like);

        }
    }

    public CollectDAdapter(List<Diary> mfavoriteList){
        mfavoriteDList = mfavoriteList;

    }




    @Override
    public CollectDAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }

        View view = LayoutInflater.from(mContext).inflate(R.layout.collectd_item, parent, false);
        final CollectDAdapter.ViewHolder holder = new CollectDAdapter.ViewHolder(view);


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Diary diary = mfavoriteDList.get(position);
                Intent intent = new Intent(mContext, DiaryWriteActivity.class);
                intent.putExtra("diary_like", diary);
                intent.putExtra("diarylike_index", mfavoriteDList.size() - 1 -position);
                intent.putExtra("diary_origin", "like");
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
                dialog.setMessage("确认删除？");
                dialog.setCancelable(true);

                dialog.setPositiveButton("确认",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which){
                        int position = holder.getAdapterPosition();
                        deleteDiaryLike(position);
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
    public void onBindViewHolder(CollectDAdapter.ViewHolder holder, int position) {
        Diary diary = mfavoriteDList.get(position);
        holder.diaryDate.setText(diary.getDate().toString());
        holder.diary_write.setText(diary.getText());
    }


    public void update(List<Diary> mfavoriteList){
        this.mfavoriteDList = mfavoriteList;
        notifyDataSetChanged();
    }


    public void deleteDiaryLike(int position){
        DatabaseHelper helper = new DatabaseHelper(mContext);
        Diary diary = mfavoriteDList.get(position);
        diary.setLike(false);
        diary.update(helper);
        mfavoriteDList=Diary.getAllLike(helper, false);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mfavoriteDList.size();
    }
}
