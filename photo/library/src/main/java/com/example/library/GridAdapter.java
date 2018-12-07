package com.example.library;

import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;


public class GridAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public GridAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        ImageView iv = helper.getView(R.id.iv);
        if (item == null) {
            iv.setImageDrawable(null);
            iv.setImageDrawable(new ColorDrawable(0xFFFF0000));
        } else {
            Glide.with(mContext).load(item).into(iv);
        }
    }
}
