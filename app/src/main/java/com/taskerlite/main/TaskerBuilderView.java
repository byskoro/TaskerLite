package com.taskerlite.main;

import com.taskerlite.other.ScreenConverter;
import com.taskerlite.other.Vibro;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class TaskerBuilderView extends View{
	
    Context context;

    private float x = 0;
    private float y = 0;
    private Paint myPaint;
    private Paint backgroundPaint;

    public TaskerBuilderView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;
        
        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.BLUE);

        myPaint = new Paint();
        myPaint.setColor(Color.WHITE);
        myPaint.setAntiAlias(true);
    }
    
    final Handler handler = new Handler(); 
    Runnable mLongPressed = new Runnable() { 
        public void run() {
            Vibro.playLong(context);
            isOnClick = false;
        }   
    };

    private float mDownX;
    private float mDownY;
    private float SCROLL_THRESHOLD = 10;
    private boolean isOnClick;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

    	switch (event.getAction() & MotionEvent.ACTION_MASK) {
    		case MotionEvent.ACTION_DOWN:

    			mDownX = event.getX(); 
    			mDownY = event.getY(); 
    			isOnClick = true;
                handler.postDelayed(mLongPressed, 1000);

                break;
    		case MotionEvent.ACTION_CANCEL:
    		case MotionEvent.ACTION_MOVE:

    			if ((Math.abs(mDownX - event.getX()) > SCROLL_THRESHOLD ||Math.abs(mDownY - event.getY()) > SCROLL_THRESHOLD)) {

    				isOnClick = false;
                    handler.removeCallbacks(mLongPressed);
                    Vibro.playMovement(context);

                    x = event.getX();
                    y = event.getY();

                    postInvalidate();
    			}

    			break; 
    		case MotionEvent.ACTION_UP:

                handler.removeCallbacks(mLongPressed);

    			if (isOnClick) {
                    Vibro.playShort(context);

    			}
    			break; 
    		}  
    
    	return true;
    }

	public void draw(Canvas canvas) {
		
		int width = canvas.getWidth();
		int height = canvas.getHeight();
		canvas.drawRect(0, 0, width, height, backgroundPaint);
        canvas.drawRect(x, y, x + ScreenConverter.dp2px(context, 56), y + ScreenConverter.dp2px(context, 56), myPaint);
		//invalidate();
	}

}
