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
    private int xPointer;
    private int yPointer;
    private float scrollThreshold = 15;
    private boolean isOnClick;

    public interface ViewCallBack{
        public void fingerUp();
        public void prepareScreen(int w, int h);
        public void shortPress(int xPointer, int yPointer);
        public void longPress(int xPointer, int yPointer);
        public void movement(int xPointer, int yPointer);
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

                isOnClick = true;

    			mDownX = event.getX();
    			mDownY = event.getY();

                xPointer = (int) event.getX();
                yPointer = (int) event.getY();

                longPressDetect.sendEmptyMessageDelayed(0, 1000);

                break;
    		case MotionEvent.ACTION_CANCEL:
    		case MotionEvent.ACTION_MOVE:

    			if ((Math.abs(mDownX - event.getX()) > scrollThreshold ||Math.abs(mDownY - event.getY()) > scrollThreshold)) {

    				isOnClick = false;
                    longPressDetect.removeMessages(0);

                    xPointer = (int) event.getX();
                    yPointer = (int) event.getY();

                    viewCallBack.movement(xPointer, yPointer);
    			}

    			break; 
    		case MotionEvent.ACTION_UP:

                longPressDetect.removeMessages(0);

                xPointer = (int) event.getX();
                yPointer = (int) event.getY();

    			if (isOnClick)
                    viewCallBack.shortPress(xPointer, yPointer);
                else
                    viewCallBack.fingerUp();

    			break; 
    		}  
    
    	return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewCallBack.prepareScreen(w, h);
    }

    Handler longPressDetect = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            isOnClick = false;
            viewCallBack.longPress(xPointer, yPointer);
        };
    };

    public void draw(Canvas canvas) {
        viewCallBack.onDrawView(canvas, event);
	}
}
