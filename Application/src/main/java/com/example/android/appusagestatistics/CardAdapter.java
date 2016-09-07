package com.example.android.appusagestatistics;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    List<CallInfo> mItems;

    public CardAdapter(ArrayList<String> info) {
        super();
        mItems = new ArrayList<CallInfo>();
        for (int i = 0; i < info.size(); i++) {
            CallInfo CallInfo = new CallInfo();
            CallInfo.setCInfo(info.get(i));
            mItems.add(CallInfo);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_view_card_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        CallInfo CallInfo = mItems.get(i);
        viewHolder.tvMovie.setText(CallInfo.getCInfo());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvMovie;

        public ViewHolder(View itemView) {
            super(itemView);
            tvMovie = (TextView) itemView.findViewById(R.id.tv_movie);
        }
    }
}
