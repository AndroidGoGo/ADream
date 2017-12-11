package com.lzq.adream.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lzq.adream.R;
import com.lzq.adream.activity.SearcActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 搜索框记录搜索记录
 * Created by Administrator on 2017/6/15.
 */

public class SearchRvAdapter extends RecyclerView.Adapter {
    public Context mContext;
    List<String> infos = new ArrayList<>();

    public SearchRvAdapter(Context context , List<String> infos) {
        mContext = context;
        this.infos = infos;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView view = (TextView) LayoutInflater.from(mContext).inflate(R.layout.item_history, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setData(infos.get(position));
    }

    @Override
    public int getItemCount() {
        if(infos != null){
            return infos.size();
        }
        return 0;
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv)
        TextView mTv;

        ViewHolder(final View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String charSequence = (String) ((TextView) view).getText();
                    ((SearcActivity)mContext).mSearchEdittext.setText(charSequence);
                    ((SearcActivity)mContext).mSearchEdittext.setSelection(charSequence.length());
                }
            });
        }

        public void setData(String s) {
            mTv.setText(s);
        }
    }
}
