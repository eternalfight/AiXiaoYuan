package com.tita.aixiaoyuan.app.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tita.aixiaoyuan.app.i.ScrollableContainer;

/**
 * @author lmz14
 * @date 2018.8.20
 */
public class MyNestedScrollView extends NestedScrollView {
    public static final int STATUS_EXPANDED = 1;
    public static final int STATUS_COLLAPSED = 2;
    private int mInterceptX,mInterceptY;
    private ScrollableContainer currentScrollableContainer;
    private int maxOffSetY;
    private int offsetY;
    private int headStatus;

    public MyNestedScrollView(@NonNull Context context) {
        super(context);
    }

    public MyNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setCurrentScrollableContainer(ScrollableContainer scrollableContainer){
        this.currentScrollableContainer = scrollableContainer;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(maxOffSetY == 0 && getChildCount()>0){
            View child = getChildAt(0);
            if(child!=null && child instanceof NestedVerticalLinearLayout){
                maxOffSetY = ((NestedVerticalLinearLayout) child).getMaxOffsetY();
            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                mInterceptX = x;
                mInterceptY = y;

                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = x - mInterceptX;
                int deltaY = y- mInterceptY;
                if(Math.abs(deltaY)> Math.abs(deltaX)){
                    //垂直方向滑动
                    if(isTop() && offsetY != maxOffSetY){
                        //列表置顶，广告可见
                        return true;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                mInterceptX = 0;
                mInterceptY = 0;
                break;
            default:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    private boolean isTop(){
        View scrollableView = getScrollableView();
        if(scrollableView == null){
            return true;
        }
        if(scrollableView instanceof RecyclerView){
            return isRecyclerViewTop((RecyclerView) scrollableView);
        }

        return true;
    }

    private View getScrollableView(){
        if(currentScrollableContainer == null){
            return null;
        }
        return currentScrollableContainer.getScrollableView();
    }

    private boolean isRecyclerViewTop(RecyclerView recyclerView) {
        if (recyclerView != null) {
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof LinearLayoutManager) {
                int firstVisibleItemPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                View childAt = recyclerView.getChildAt(0);
                if (childAt == null || (firstVisibleItemPosition <= 1 && childAt.getTop() == 0)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        //offsetY=0 展开  offset=MaxOffset隐藏
        offsetY = t;
        if(offsetY == 0){
            headStatus = STATUS_EXPANDED;
        }else if(offsetY == maxOffSetY){
            headStatus = STATUS_COLLAPSED;
        }else{
            headStatus = 0;
        }
    }


    public boolean isExpanded(){
        return headStatus == STATUS_EXPANDED && isTop();
    }

}
