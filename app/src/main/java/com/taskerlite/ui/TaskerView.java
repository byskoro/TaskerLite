package com.taskerlite.ui;

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

public class TaskerView extends View{
	
    Context context;
    
    private static final float RADIUS = 20;
    private float x = 30;
    private float y = 30;
    private float initialX;
    private float initialY;
    private float offsetX;
    private float offsetY;
    private Paint myPaint;
    private Paint backgroundPaint;

    public TaskerView(Context context, AttributeSet attrs) {
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
            Toast.makeText(context, "Long press", Toast.LENGTH_SHORT).show();
            Vibro.playShort(context);
        }   
    };
    
    private float mDownX; 
    private float mDownY;
    private final float SCROLL_THRESHOLD = 10; 
    private boolean isOnClick;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
  /*  	
    	if(event.getAction() == MotionEvent.ACTION_DOWN) 
    		handler.postDelayed(mLongPressed, 1000); 
    	if((event.getAction() == MotionEvent.ACTION_UP)) 
    		handler.removeCallbacks(mLongPressed);     	
   */ 	
    	switch (event.getAction() & MotionEvent.ACTION_MASK) {
    		case MotionEvent.ACTION_DOWN: 
    			mDownX = event.getX(); 
    			mDownY = event.getY(); 
    			isOnClick = true;
    			break;
    		case MotionEvent.ACTION_CANCEL:
    		case MotionEvent.ACTION_MOVE:
    			if (isOnClick && (Math.abs(mDownX - event.getX()) > SCROLL_THRESHOLD ||Math.abs(mDownY - event.getY()) > SCROLL_THRESHOLD)) {   
    				// movement detected
    				isOnClick = false; 
    			}
    			break; 
    		case MotionEvent.ACTION_UP:
    			if (isOnClick) {
    				// onClick code 
    			}
    			break; 
    		}  
    
    	return true;
    }
    	
 /*   	
        int action = event.getAction();
        switch (action) {
        case MotionEvent.ACTION_DOWN:
          initialX = x;
          initialY = y;
          offsetX = event.getX();
          offsetY = event.getY();
          break;
        case MotionEvent.ACTION_MOVE:
        case MotionEvent.ACTION_UP:
        case MotionEvent.ACTION_CANCEL:
          x = initialX + event.getX() - offsetX;
          y = initialY + event.getY() - offsetY;
          break;
        }
        return (true);
*/

	public void draw(Canvas canvas) {
		
		int width = canvas.getWidth();
		int height = canvas.getHeight();
		canvas.drawRect(0, 0, width, height, backgroundPaint);
		canvas.drawCircle(x, y, RADIUS, myPaint);
		invalidate();
	}

}
