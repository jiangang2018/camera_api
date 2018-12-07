package com.example.library;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

public class SquImageView extends AppCompatImageView {
    public SquImageView(Context context) {
        super(context);
    }

    public SquImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
