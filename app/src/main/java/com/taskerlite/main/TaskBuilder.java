package com.taskerlite.main;

import com.taskerlite.R;
import com.taskerlite.TaskerBuilderView;
import com.taskerlite.logic.ActionElement;
import com.taskerlite.logic.SceneList.*;
import com.taskerlite.logic.TaskElement;
import com.taskerlite.other.Vibro;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.Toast;

import com.taskerlite.logic.tasks.tApp;
import com.taskerlite.logic.tasks.mTask.*;
import com.taskerlite.logic.actions.aTimer;
import com.taskerlite.logic.actions.mAction.*;

import java.util.ArrayList;

public class TaskBuilder extends Fragment {

    private TaskerBuilderView taskerView;
    private Context context;
    private Scene scene;
    private static int sceneIndex;

    private int iconSizeElement = mActivity.iconSizeElement;
    private int iconSizeDelete  = mActivity.iconSizeDelete;
    Bitmap selectIcon;
    Bitmap deleteIcon;

    DelElement gcElement;

    Dialog dialogMenu;
    Dialog dialogActions;
    Dialog dialogTasks;

    public static TaskBuilder getInstance(int sceneIndex){
        TaskBuilder.sceneIndex = sceneIndex;
        return new TaskBuilder();
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_task_builder, container, false);
        context = getActivity();

		taskerView = (TaskerBuilderView) view.findViewById(R.id.dot);
        taskerView.setViewCallBack(viewCallBack);

        scene = mActivity.sceneList.getScene(sceneIndex);

        Bitmap selectBigIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_select);
        selectIcon = Bitmap.createScaledBitmap(selectBigIcon, iconSizeElement, iconSizeElement, true);
        Bitmap deleteBigIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.delete);
        deleteIcon = Bitmap.createScaledBitmap(deleteBigIcon, iconSizeDelete, iconSizeDelete, true);

        gcElement = new DelElement(iconSizeDelete);

		return view;
	}

    Handler handlerLogic = new Handler() {
        public void handleMessage(Message msg) {

            if (msg.what == 0) {

                for(ActionElement action : scene.getActionList())
                    action.unSelectElement();
                for(TaskElement task : scene.getTaskList())
                    task.unSelectElement();

                gcElement.clearList();
                taskerView.postInvalidate();
            }

            if (msg.what == 1)
                taskerView.postInvalidate();
        }
    };

    TaskerBuilderView.ViewCallBack viewCallBack = new TaskerBuilderView.ViewCallBack(){

        private int isAnyElementMoving = 0;

        @Override
        public void fingerUp() {
            isAnyElementMoving = 0;
        }

        @Override
        public void shortPress(MotionEvent event) {

            // look for delete icon press
            if(gcElement.deleteListGetSize() != 0){

                if(gcElement.getIdPressedElement(event) != 0){

                    for(TaskElement task : scene.getTaskList()){
                        if(task.getTaskId() == gcElement.getIdPressedElement(event)){
                            scene.deleteTask(task);
                            break;
                        }
                    }

                    for(ActionElement action : scene.getActionList()){
                        if(action.getActionId() == gcElement.getIdPressedElement(event)){
                            scene.deleteAction(action);
                            break;
                        }
                    }
                }

            }else if(findTouchedTask(event) != null) {

                Toast.makeText(getActivity(),"Open Task", Toast.LENGTH_SHORT).show();

                //findTouchedTask(event).getTaskObject().show();

                Vibro.playShort(context);
            } else if (findTouchedAction(event) != null){

                Toast.makeText(getActivity(),"Open Action", Toast.LENGTH_SHORT).show();

                //findTouchedAction(event).getTaskObject().show();

                Vibro.playShort(context);
            }

            unselectAllElements();
        }

        @Override
        public void longPress(MotionEvent event) {

            TaskElement   findTaskElement   = findTouchedTask(event);
            ActionElement findActionElement = findTouchedAction(event);

            if (findActionElement != null) {

                if (!findActionElement.isElementSelect()) {
                    gcElement.addPressedElement(findActionElement.getActionId(), findActionElement.getX(), findActionElement.getY());
                    findActionElement.selectElement();
                    checkConnection();
                }else
                    findActionElement.unSelectElement();

            } else if (findTaskElement != null) {

                if (!findTaskElement.isElementSelect()) {
                    gcElement.addPressedElement(findTaskElement.getTaskId(), findTaskElement.getX(), findTaskElement.getY());
                    findTaskElement.selectElement();
                    checkConnection();
                }else
                    findTaskElement.unSelectElement();

            }else{

                unselectAllElements();
                showMenuDialog();
            }

            updateScreenUI();
            Vibro.playLong(context);
        }

        @Override
        public void movement(MotionEvent event) { // present bug of move the same element

            TaskElement   taskElement   = findTouchedTask(event);
            ActionElement actionElement = findTouchedAction(event);

            if( (taskElement != null && isAnyElementMoving == 0) || (taskElement != null && isAnyElementMoving == 1)) {

                isAnyElementMoving = 1;
                taskElement.setNewCoordinate(event);
                Vibro.playMovement(context);
            }

            if( (actionElement != null && isAnyElementMoving == 0) || (actionElement != null && isAnyElementMoving == 2)) {

                isAnyElementMoving = 2;
                actionElement.setNewCoordinate(event);
                Vibro.playMovement(context);
            }

            updateScreenUI();
            unselectAllElements();
        }

        @Override
        public void onDrawView(Canvas canvas, MotionEvent event) {

            try{

                Paint p = new Paint();
                p.setColor(Color.WHITE);
                p.setTextSize(getResources().getInteger(R.integer.icon_text_size));
                p.setTextAlign(Paint.Align.CENTER);
                p.setStrokeWidth(5);

                // 1. If present relationship between action and task - draw a lines
                for (ActionElement action : scene.getActionList()) {
                    for (TaskElement task : scene.getTaskList()) {
                        if (action.isTaskElementIdPresent(task.getTaskId())) {
                            canvas.drawLine(action.getX() + iconSizeElement /2, action.getY() + iconSizeElement /2,
                                    task.getX() + iconSizeElement /2, task.getY() + iconSizeElement /2, p);
                        }
                    }
                }

                // 2. Print action and task icons
                for(ActionElement action : scene.getActionList()){
                    if(action.isElementSelect())
                        canvas.drawBitmap(selectIcon, action.getX(), action.getY(), null);
                    Bitmap icon = action.getIcon(context, iconSizeElement);
                    canvas.drawBitmap(icon, action.getX(), action.getY(), null);
                    float textX = action.getX() + iconSizeElement /2;
                    float textY = action.getY() + iconSizeElement + getResources().getInteger(R.integer.icon_text_margin);
                    canvas.drawText(action.getActionName(), textX, textY, p);
                }

                for(TaskElement task : scene.getTaskList()){
                    if(task.isElementSelect())
                        canvas.drawBitmap(selectIcon, task.getX(), task.getY(), null);
                    Bitmap icon = task.getIcon(context, iconSizeElement);
                    canvas.drawBitmap(icon, task.getX(), task.getY(), null);
                    float textX = task.getX() + iconSizeElement /2;
                    float textY = task.getY() + iconSizeElement + getResources().getInteger(R.integer.icon_text_margin);
                    canvas.drawText(task.getTaskName(), textX, textY, p);
                }

                // 3. Print delete icons
                for(DelElement.PressedElement element : gcElement.getDelList())
                    canvas.drawBitmap(deleteIcon, element.x, element.y, null);

            }catch (Exception e){ }
        }
    };

    private void updateScreenUI(){

        if(!handlerLogic.hasMessages(1))
            handlerLogic.sendEmptyMessageDelayed(1, 50);
    }

    private void unselectAllElements( ){

        if(!handlerLogic.hasMessages(0))
            handlerLogic.sendEmptyMessageDelayed(0, 300);
    }

    private TaskElement findTouchedTask(MotionEvent event){

        for(TaskElement task : scene.getTaskList()){
            if(task.isTouched(event, iconSizeElement))
                return task;
        }
        return null;
    }

    private ActionElement findTouchedAction(MotionEvent event){

        for(ActionElement action : scene.getActionList()){
            if(action.isTouched(event, iconSizeElement))
                return action;
        }
        return null;
    }

    private void checkConnection( ){

        for(ActionElement action : scene.getActionList()) {
            if( action.isElementSelect() ){
                for(TaskElement task : scene.getTaskList()) {
                    if (task.isElementSelect()) {
                        if(!action.isTaskElementIdPresent(task.getTaskId()))
                            action.addNewTaskElementId(task.getTaskId());
                        else
                            action.deleteTaskElementId(task.getTaskId());

                        unselectAllElements();
                    }
                }
            }
        }
    }

    public class DelElement {

        private ArrayList<PressedElement> delElementList = new ArrayList<PressedElement>();
        private int elementSize;

        public DelElement(int elementSize){
            this.elementSize = elementSize;
        }

        public void addPressedElement(long id, int x, int y){
            delElementList.add(new PressedElement(id, x + iconSizeElement - elementSize/2, y - elementSize/2));
        }
        public long getIdPressedElement(MotionEvent event){
            for(PressedElement element : delElementList) {
                if ((event.getRawX() > element.x) && (event.getRawX() < element.x + elementSize)
                        && (event.getRawY() > element.y) && (event.getRawY() < element.y + elementSize)) {
                    return element.id;
                }
            }
            return 0;
        }
        public void clearList(){ delElementList.clear(); }
        public int deleteListGetSize(){ return delElementList.size(); }
        public ArrayList<PressedElement> getDelList(){ return delElementList; }

        public class PressedElement {
            long id;
            int x,y;
            public PressedElement(long id, int x, int y){
                this.id = id;
                this.x = x;
                this.y=y;
            }
        }

    }

    private void showMenuDialog( ){

        dialogMenu = new Dialog(context);
        dialogMenu.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogMenu.setContentView(R.layout.dialog_menu);

        ((ImageButton) dialogMenu.findViewById(R.id.actionElement)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"Open Action List", Toast.LENGTH_SHORT).show();
                scene.addNewAction("Timer 2", new aTimer(18, 47), ACTION_TYPE.TIMER, 0, 0);
                updateScreenUI();
                dialogMenu.dismiss();
            }
        });

        ((ImageButton) dialogMenu.findViewById(R.id.taskElement)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"Open Task List", Toast.LENGTH_SHORT).show();
                scene.addNewTask("Skype 2", new tApp("com.skype.raider"), TASK_TYPE.APP);
                updateScreenUI();
                dialogMenu.dismiss();
            }
        });

        dialogMenu.show();
    }
}
