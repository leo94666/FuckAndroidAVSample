package com.top.av.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.top.av.R;
import com.top.av.bean.FFmpegCommand;

import java.util.List;

public class FFmpegRecyclerViewAdapter extends RecyclerView.Adapter<FFmpegRecyclerViewAdapter.MyViewHolder> implements View.OnClickListener {
    private List<FFmpegCommand> mTitles;
    private Context mContext;
    private int mSelectIndex = 0;
    private OnItemClickListener mOnItemClickListener = null;

    public FFmpegRecyclerViewAdapter(Context context, List<FFmpegCommand> titles) {
        mContext = context;
        mTitles = titles;
    }

    public void setSelectIndex(int index) {
        mSelectIndex = index;
        notifyDataSetChanged();
    }

    public int getSelectIndex() {
        return mSelectIndex;
    }

    public void addOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_sample_item_layout, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        view.setOnClickListener(this);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        FFmpegCommand fFmpegCommand = mTitles.get(position);
        holder.mTitle.setText(fFmpegCommand.getTitle());
        if (position == mSelectIndex) {
            holder.mRadioButton.setChecked(true);
            holder.mTitle.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
        } else {
            holder.mRadioButton.setChecked(false);
            holder.mTitle.setText(fFmpegCommand.getTitle());
            holder.mTitle.setTextColor(Color.GRAY);
        }
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mTitles.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v, (Integer) v.getTag());
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        RadioButton mRadioButton;
        TextView mTitle;

        public MyViewHolder(View itemView) {
            super(itemView);
            mRadioButton = itemView.findViewById(R.id.radio_btn);
            mTitle = itemView.findViewById(R.id.item_title);
        }
    }
}