package com.taskerlite.main;

import com.taskerlite.R;
import com.taskerlite.TaskerBuilderView;
import com.taskerlite.logic.ActionElement;
import com.taskerlite.logic.SceneList.*;
import com.taskerlite.logic.TaskElement;
import com.taskerlite.other.Flash;
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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.taskerlite.logic.tasks.tApp;
import com.taskerlite.logic.tasks.mTask.*;
import com.taskerlite.logic.actions.aTimer;
import com.taskerlite.logic.actions.mAction.*;

import java.util.ArrayList;

public class TaskBuilder extends Fragment implements View.OnClickListener, TextView.OnEditorActionListener {

    private TaskerBuilderView taskerView;
    private Context context;
    private Scene scene;
    private static int sceneIndex;

    private int iconSizeElement = mActivity.iconSizeElement;
    private int iconSizeDelete  = mActivity.iconSizeDelete;
    Bitmap selectIcon;
    Bitmap deleteIcon;
    DelElement gcElement;

    public static int screenWidth;
    public static int screenHeight;

    Dialog dialogMenu;
    Dialog dialogActions;
    Dialog dialogTasks;

    Button backBtn, saveBtn, clearBtn;
    EditText nameScene;
    LinearLayout layForClearReqwest, clearReqwestLay;

    public static TaskBuilder getInstance(int sceneIndex){
        TaskBuilder.sceneIndex = sceneIndex;
        return new TaskBuilder();
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_task_builder, container, false);
        context = getActivity();

        scene = mActivity.sceneList.getScene(sceneIndex);

		taskerView = (TaskerBuilderView) view.findViewById(R.id.drawBuilder);
        taskerView.setViewCallBack(viewCallBack);
        backBtn = (Button) view.findViewById(R.id.backBtn);
        backBtn.setOnClickListener(this);
        saveBtn = (Button) view.findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(this);
        clearBtn= (Button) view.findViewById(R.id.clearBtn);
        clearBtn.setOnClickListener(this);
        nameScene = (EditText) view.findViewById(R.id.sceneName);
        nameScene.setOnEditorActionListener(this);
        nameScene.setText(scene.getName());
        clearReqwestLay = (LinearLayout) view.findViewById(R.id.clearReqwestLay);

        Bitmap selectBigIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_select);
        selectIcon = Bitmap.createScaledBitmap(selectBigIcon, iconSizeElement, iconSizeElement, true);
        Bitmap deleteBigIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.delete);
        deleteIcon = Bitmap.createScaledBitmap(deleteBigIcon, iconSizeDelete, iconSizeDelete, true);

        gcElement = new DelElement(iconSizeDelete);

		return view;
	}

    @Override
    public void onClick(View view) {

        switch(view.getId()){
            case R.id.backBtn:
                getFragmentManager().beginTransaction().
                replace(R.id.fragmentConteiner, new TaskList()).
                commit();
                break;
            case R.id.saveBtn:
                Flash.saveList(mActivity.sceneList);
                break;
            case R.id.clearBtn:
                mActivity.sceneList.removeAllFromScene(sceneIndex);
                Flash.saveList(mActivity.sceneList);
                updateScreenUI();
                break;
        }

    }

    Handler handlerLogic = new Handler() {
        public void handleMessage(Message msg) {

            if (msg.what == 0) {

                for(ActionElement action : scene.getActionList())
                    action.unselect();
                for(TaskElement task : scene.getTaskList())
                    task.unselect();

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
        public void prepareScreen(int w, int h) {
            screenWidth  = w;
            screenHeight = h;
        }

        @Override
        public void shortPress(int xPointer, int yPointer) {

            // look for delete icon press
            if(gcElement.deleteListGetSize() != 0){

                if(gcElement.getIdPressedElement(xPointer, yPointer) != 0){

                    for(TaskElement task : scene.getTaskList()){
                        if(task.getTaskId() == gcElement.getIdPressedElement(xPointer, yPointer)){
                            scene.deleteTask(task);
                            break;
                        }
                    }

                    for(ActionElement action : scene.getActionList()){
                        if(action.getActionId() == gcElement.getIdPressedElement(xPointer, yPointer)){
                            scene.deleteAction(action);
                            break;
                        }
                    }
                }

            }else if(findTouchedTask(xPointer, yPointer) != null) {

                Toast.makeText(getActivity(),"Open Task", Toast.LENGTH_SHORT).show();

                //findTouchedTask(event).getTaskObject().show();

                Vibro.playShort(context);
            } else if (findTouchedAction(xPointer, yPointer) != null){

                Toast.makeText(getActivity(),"Open Action", Toast.LENGTH_SHORT).show();

                //findTouchedAction(event).getTaskObject().show();

                Vibro.playShort(context);
            }

            unselectAll();
        }

        @Override
        public void longPress(int xPointer, int yPointer) {

            TaskElement   findTaskElement   = findTouchedTask(xPointer, yPointer);
            ActionElement findActionElement = findTouchedAction(xPointer, yPointer);

            if (findActionElement != null) {

                if (!findActionElement.isSelect()) {

                    gcElement.addPressedElement(findActionElement.getActionId(), findActionElement.getX(), findActionElement.getY());
                    findActionElement.select();
                    checkConnection();
                }else
                    findActionElement.unselect();

            } else if (findTaskElement != null) {

                if (!findTaskElement.isSelect()) {

                    gcElement.addPressedElement(findTaskElement.getTaskId(), findTaskElement.getX(), findTaskElement.getY());
                    findTaskElement.select();
                    checkConnection();
                }else
                    findTaskElement.unselect();

            }else{

                unselectAll();
                showMenuDialog();
            }

            updateScreenUI();
            Vibro.playLong(context);
        }

        @Override
        public void movement(int xPointer, int yPointer) { // present bug of move the same element

            TaskElement   taskElement   = findTouchedTask(xPointer, yPointer);
            ActionElement actionElement = findTouchedAction(xPointer, yPointer);

            if( (taskElement != null && isAnyElementMoving == 0) || (taskElement != null && isAnyElementMoving == 1)) {

                isAnyElementMoving = 1;
                taskElement.setNewCoordinate(xPointer, yPointer);
                Vibro.playMovement(context);
            }

            if( (actionElement != null && isAnyElementMoving == 0) || (actionElement != null && isAnyElementMoving == 2)) {

                isAnyElementMoving = 2;
                actionElement.setNewCoordinate(xPointer, yPointer);
                Vibro.playMovement(context);
            }

            updateScreenUI();
            unselectAll();
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
                    if(action.isSelect())
                        canvas.drawBitmap(selectIcon, action.getX(), action.getY(), null);
                    Bitmap icon = action.getIcon(context, iconSizeElement);
                    canvas.drawBitmap(icon, action.getX(), action.getY(), null);
                    float textX = action.getX() + iconSizeElement /2;
                    float textY = action.getY() + iconSizeElement + getResources().getInteger(R.integer.icon_text_margin);
                    canvas.drawText(action.getActionName(), textX, textY, p);
                }

                for(TaskElement task : scene.getTaskList()){
                    if(task.isSelect())
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

    private void unselectAll(){

        gcElement.clearList();

        if(!handlerLogic.hasMessages(0))
            handlerLogic.sendEmptyMessageDelayed(0, 300);
    }

    private TaskElement findTouchedTask(int xPointer, int yPointer){

        for(TaskElement task : scene.getTaskList()){
            if(task.isTouched(xPointer, yPointer, iconSizeElement))
                return task;
        }
        return null;
    }

    private ActionElement findTouchedAction(int xPointer, int yPointer){

        for(ActionElement action : scene.getActionList()){
            if(action.isTouched(xPointer, yPointer, iconSizeElement))
                return action;
        }
        return null;
    }

    private void checkConnection( ){

        for(ActionElement action : scene.getActionList()) {
            if( action.isSelect() ){
                for(TaskElement task : scene.getTaskList()) {
                    if (task.isSelect()) {
                        if(!action.isTaskElementIdPresent(task.getTaskId()))
                            action.addNewTaskElementId(task.getTaskId());
                        else
                            action.deleteTaskElementId(task.getTaskId());

                        unselectAll();
                    }
                }
            }
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

        try {

            scene.setName(textView.getText().toString());

            clearReqwestLay.requestFocus();
            InputMethodManager imm = (InputMethodManager)textView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(textView.getApplicationWindowToken(), 0);

        } catch (Exception e) { }

        return true;
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
        public long getIdPressedElement(int xPointer, int yPointer){
            for(PressedElement element : delElementList) {
                if ((xPointer > element.x) && (xPointer < element.x + elementSize)
                        && (yPointer > element.y) && (yPointer < element.y + elementSize)) {
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
