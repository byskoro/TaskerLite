package com.taskerlite.main;

import com.taskerlite.R;
import com.taskerlite.other.ScreenConverter;
import com.taskerlite.other.Vibro;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class TaskBuilder extends Fragment {

	TaskerBuilderView taskerView;
    Context context;

    private Bitmap background;
    private Bitmap picture;

    private int sizeIcon;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_task_builder, container, false);
        context = getActivity();

		taskerView = (TaskerBuilderView) view.findViewById(R.id.dot);
        taskerView.setViewCallBack(viewCallBack);

        background = BitmapFactory.decodeResource(getResources(), R.drawable.button);

		return view;
	}

    TaskerBuilderView.ViewCallBack viewCallBack = new TaskerBuilderView.ViewCallBack(){

        @Override
        public void shortPress(MotionEvent event) {

            Vibro.playShort(context);
        }

        @Override
        public void longPress(MotionEvent event) {

            Vibro.playLong(context);
        }

        @Override
        public void movement(MotionEvent event) {

            Vibro.playMovement(context);
            taskerView.postInvalidate();
        }

        @Override
        public void drawView(Canvas canvas, MotionEvent event) {

            try{

                canvas.drawColor(0xffff00ff);
                canvas.drawBitmap(picture, event.getX(), event.getY(), null);

            }catch (Exception e){ }
        }

        @Override
        public void prepareResource(int w, int h) {

            sizeIcon = w / 5;
            picture = Bitmap.createScaledBitmap(background, sizeIcon, sizeIcon, true);
        }
    };
}
