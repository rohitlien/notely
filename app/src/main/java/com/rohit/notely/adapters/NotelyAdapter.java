package com.rohit.notely.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.rohit.notely.R;
import com.rohit.notely.activity.AddNoteActivity;
import com.rohit.notely.database.RealmHelper;
import com.rohit.notely.models.NoteData;
import com.rohit.notely.utils.NotelyTools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chau Thai on 4/8/16.
 */
public class NotelyAdapter extends RecyclerView.Adapter {
    private List<NoteData> mDataSet = new ArrayList<>();
    private LayoutInflater mInflater;
    private Context mContext;
    private final ViewBinderHelper binderHelper = new ViewBinderHelper();


    public NotelyAdapter(Context context, List<NoteData> dataSet) {
        mContext = context;
        mDataSet = dataSet;
        mInflater = LayoutInflater.from(context);
    }

    public void notifyChanges(ArrayList<NoteData> noteDataArrayList) {
        this.mDataSet = noteDataArrayList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder h, int position) {
        final ViewHolder holder = (ViewHolder) h;

        if (mDataSet != null && 0 <= position && position < mDataSet.size()) {
            NoteData data = mDataSet.get(position);
            binderHelper.bind(holder.swipeLayout, data.getId() + "");
            holder.bind(holder, data);
        }
    }

    @Override
    public int getItemCount() {
        if (mDataSet == null)
            return 0;
        return mDataSet.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        private SwipeRevealLayout swipeLayout;
        private View frontLayout;
        private View deleteLayout;
        private TextView row_heading, row_content, row_createdat;
        private ImageView row_starred, row_hearted;
        private LinearLayout main_row, viewForeground;
        private RelativeLayout view_background;

        public ViewHolder(View itemView) {
            super(itemView);
            swipeLayout = (SwipeRevealLayout) itemView.findViewById(R.id.swipe_layout);
            frontLayout = itemView.findViewById(R.id.front_layout);
            deleteLayout = itemView.findViewById(R.id.delete_layout);
            row_heading = itemView.findViewById(R.id.row_heading);
            row_content = itemView.findViewById(R.id.row_content);
            row_createdat = itemView.findViewById(R.id.row_createdat);
            row_starred = itemView.findViewById(R.id.row_starred);
            row_hearted = itemView.findViewById(R.id.row_hearted);
            main_row = itemView.findViewById(R.id.main_row);
            view_background = itemView.findViewById(R.id.view_background);
            viewForeground = itemView.findViewById(R.id.view_forground);
        }


        public void bind(ViewHolder holder, final NoteData data) {

            holder.row_heading.setText(data.getTitle());
            holder.row_content.setText(data.getContent());

            NotelyTools notelyTools = new NotelyTools();
            holder.row_createdat.setText(notelyTools.getDate(data.getTimeStamp()));

            if (data.isFavourite()) {
                holder.row_starred.setColorFilter(mContext.getResources().getColor(R.color.yellow_starred));
            } else {
                holder.row_starred.setColorFilter(mContext.getResources().getColor(R.color.light_grey));

            }
            if (data.isHearted()) {
                holder.row_hearted.setColorFilter(mContext.getResources().getColor(R.color.red_heart));
            } else {
                holder.row_hearted.setColorFilter(mContext.getResources().getColor(R.color.light_grey));
            }

            holder.row_hearted.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isHearted = data.isHearted();
                    isHearted = !isHearted;
                    RealmHelper.setHeart(data.getId(), isHearted);
                    notifyDataSetChanged();
                    showToast("This note has been bookmarked");
                }
            });

            holder.row_starred.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isFav = data.isFavourite();
                    isFav = !isFav;
                    RealmHelper.setFavorite(data.getId(), isFav);
                    notifyDataSetChanged();
                    showToast("This note added to favourites");
                }
            });

            holder.main_row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewNote(data.getId());
                }
            });

            deleteLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RealmHelper.deleNoteData(data.getId());
                    mDataSet.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    showToast("This note has been removed");
                }
            });
        }
    }

    private void viewNote(String id) {
        Intent intent = new Intent(mContext, AddNoteActivity.class);
        intent.putExtra("noteId", id);
        mContext.startActivity(intent);
    }
    private void showToast(String message){
        Toast.makeText(mContext, ""+message, Toast.LENGTH_SHORT).show();
    }

}
