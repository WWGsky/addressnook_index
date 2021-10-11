package com.wwg.addressnook_index;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 自定义装饰器（实现分组+吸顶效果）
 * Create by: chenWei.li
 * Date: 2018/11/2
 * Time: 上午1:14
 * Email: lichenwei.me@foxmail.com
 */
public class StickHeaderDecoration extends RecyclerView.ItemDecoration {

    //头部的高
    private int mItemHeaderHeight;
    private int mTextPaddingLeft;

    //画笔，绘制头部和分割线
    private Paint mItemHeaderPaint;
    private Paint mTextPaint;
    private Paint mLinePaint;

    private Rect mTextRect;


    public StickHeaderDecoration(Context context) {

        mItemHeaderHeight = dp2px(30);
        mTextPaddingLeft = dp2px(14);

        mTextRect = new Rect();

        mItemHeaderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mItemHeaderPaint.setColor(Color.parseColor("#F3F3F3"));

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(sp2px(12));
        mTextPaint.setColor(Color.parseColor("#6C6C6C"));

        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setColor(Color.GRAY);

    }

    /**
     * 绘制Item的分割线和组头
     *
     * @param c
     * @param parent
     * @param state
     */
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (parent.getAdapter() instanceof AddressBook_Adapter) {
            AddressBook_Adapter adapter = (AddressBook_Adapter) parent.getAdapter();
            int count = parent.getChildCount();//获取可见范围内Item的总数
            for (int i = 0; i < count; i++) {
                View view = parent.getChildAt(i);
                int position = parent.getChildLayoutPosition(view);
                boolean isHeader = adapter.isItemHeader(position);
                int left = parent.getPaddingLeft();
                int right = parent.getWidth() - parent.getPaddingRight();
                if (isHeader) {
                    c.drawRect(left, view.getTop() - mItemHeaderHeight, right, view.getTop(), mItemHeaderPaint);
                    mTextPaint.getTextBounds(adapter.getGroupName(position), 0, adapter.getGroupName(position).length(), mTextRect);
                    c.drawText(adapter.getGroupName(position), left + mTextPaddingLeft, (view.getTop() - mItemHeaderHeight) + mItemHeaderHeight / 2 + mTextRect.height() / 2, mTextPaint);
                } else {
                    c.drawRect(left, view.getTop() - 1, right, view.getTop(), mLinePaint);
                }
            }
        }
    }


    /**
     * 绘制Item的顶部布局（吸顶效果）
     *
     * @param c
     * @param parent
     * @param state
     */
    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (parent.getAdapter() instanceof AddressBook_Adapter) {
            AddressBook_Adapter adapter = (AddressBook_Adapter) parent.getAdapter();
            int position = ((LinearLayoutManager) (parent.getLayoutManager())).findFirstVisibleItemPosition();
            View view = parent.findViewHolderForAdapterPosition(position).itemView;
            boolean isHeader = adapter.isItemHeader(position + 1);
            int top = parent.getPaddingTop();
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();
            if (isHeader) {
                int bottom = Math.min(mItemHeaderHeight, view.getBottom());
                c.drawRect(left, top + view.getTop() - mItemHeaderHeight, right, top + bottom, mItemHeaderPaint);
                mTextPaint.getTextBounds(adapter.getGroupName(position), 0, adapter.getGroupName(position).length(), mTextRect);
                c.drawText(adapter.getGroupName(position), left + mTextPaddingLeft, top + mItemHeaderHeight / 2 + mTextRect.height() / 2 - (mItemHeaderHeight - bottom), mTextPaint);
            } else {
                c.drawRect(left, top, right, top + mItemHeaderHeight, mItemHeaderPaint);
                mTextPaint.getTextBounds(adapter.getGroupName(position), 0, adapter.getGroupName(position).length(), mTextRect);
                c.drawText(adapter.getGroupName(position), left + mTextPaddingLeft, top + mItemHeaderHeight / 2 + mTextRect.height() / 2, mTextPaint);
            }
            c.save();
        }

    }

    /**
     * 设置Item的间距
     *
     * @param outRect
     * @param view
     * @param parent
     * @param state
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getAdapter() instanceof AddressBook_Adapter) {
            AddressBook_Adapter adapter = (AddressBook_Adapter) parent.getAdapter();
            int position = parent.getChildLayoutPosition(view);
            boolean isHeader = adapter.isItemHeader(position);
            if (isHeader) {
                outRect.top = mItemHeaderHeight;
            } else {
                outRect.top = 1;
            }
        }
    }


    private int dp2px(final float dpValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private int sp2px(final float spValue) {
        final float fontScale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
