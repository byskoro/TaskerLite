package com.taskerlite;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class TaskerBuilderView extends View{

    private ViewCallBack viewCallBack;
    private MotionEvent event;

    private float mDownX;
    private float mDownY;
    private float scrollThreshold = 15;
    private boolean isOnClick;

    public interface ViewCallBack{
        public void fingerUp();
        public void shortPress(MotionEvent event);
        public void longPress(MotionEvent event);
        public void movement(MotionEvent event);
        public void onDrawView(Canvas canvas, MotionEvent event);
    }

    public TaskerBuilderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setViewCallBack(ViewCallBack viewCallBack){
        this.viewCallBack = viewCallBack;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        this.event = event;

    	switch (event.getAction() & MotionEvent.ACTION_MASK) {
    		case MotionEvent.ACTION_DOWN:

    			mDownX = event.getX();
    			mDownY = event.getY();

    			isOnClick = true;

                longPressDetect.sendEmptyMessageDelayed(0, 1000);

                break;
    		case MotionEvent.ACTION_CANCEL:
    		case MotionEvent.ACTION_MOVE:

    			if ((Math.abs(mDownX - event.getX()) > scrollThreshold ||Math.abs(mDownY - event.getY()) > scrollThreshold)) {

    				isOnClick = false;
                    longPressDetect.removeMessages(0);
                    viewCallBack.movement(event);
    			}

    			break; 
    		case MotionEvent.ACTION_UP:

                longPressDetect.removeMessages(0);

    			if (isOnClick)
                    viewCallBack.shortPress(event);
                else
                    viewCallBack.fingerUp();

    			break; 
    		}  
    
    	return true;
    }

    Handler longPressDetect = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            isOnClick = false;
            viewCallBack.longPress(event);
        };
    };

    public void draw(Canvas canvas) {
        viewCallBack.onDrawView(canvas, event);
	}
}
