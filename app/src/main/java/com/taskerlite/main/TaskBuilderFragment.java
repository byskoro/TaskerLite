package com.taskerlite.main;

import com.taskerlite.R;
import com.taskerlite.TaskerBuilderView;
import com.taskerlite.logic.ActionElement;
import com.taskerlite.logic.SceneList;
import com.taskerlite.logic.SceneList.*;
import com.taskerlite.logic.TaskElement;
import com.taskerlite.other.Flash;
import com.taskerlite.other.Vibro;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.taskerlite.logic.tasks.tApp;
import com.taskerlite.logic.actions.aTimer;
import com.taskerlite.main.TaskerTypes.*;

import java.util.ArrayList;

public class TaskBuilderFragment extends Fragment implements View.OnClickListener, TextView.OnEditorActionListener {

    public static enum LIST{NULL, DIALOG_MENU, DIALOG_ACTIONS, DIALOG_TASK};

    private TaskerBuilderView taskerView;
    private Context context;
    private Scene scene;
    private static int sceneIndex = 0;
    private SceneList sceneList = com.taskerlite.main.mActivity.sceneList;

    private int iconSizeElement = TaskerIcons.builderSize;
    Bitmap selectIcon = TaskerIcons.getInstance().getSelectIcons();
    Bitmap deleteIcon = TaskerIcons.getInstance().getDeleteIcon();
    DelElement gcElement;

    public static int screenWidth;
    public static int screenHeight;

    CustomDialog dialog;

    ImageButton backBtn, clearBtn;
    EditText nameScene;
    LinearLayout clearRequestLay;

    public static TaskBuilderFragment getInstance(int sceneIndex){
        TaskBuilderFragment.sceneIndex = sceneIndex;
        return new TaskBuilderFragment();
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_task_builder, container, false);
        context = getActivity();

        scene = com.taskerlite.main.mActivity.sceneList.getScene(sceneIndex);

		taskerView = (TaskerBuilderView) view.findViewById(R.id.drawBuilder);
        taskerView.setViewCallBack(viewCallBack);
        backBtn = (ImageButton) view.findViewById(R.id.backBtn);
        backBtn.setOnClickListener(this);
        clearBtn= (ImageButton) view.findViewById(R.id.clearBtn);
        clearBtn.setOnClickListener(this);
        nameScene = (EditText) view.findViewById(R.id.sceneName);
        nameScene.setOnEditorActionListener(this);
        nameScene.setText(scene.getName());
        clearRequestLay = (LinearLayout) view.findViewById(R.id.clearRequestLay);
        clearRequest(nameScene);

        gcElement = new DelElement(TaskerIcons.deleteSize);

        dialog = new CustomDialog();

		return view;
	}

    @Override
    public void onClick(View view) {

        switch(view.getId()){

            case R.id.backBtn:
                Flash.saveList(sceneList);
                getFragmentManager().beginTransaction().
                setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right).
                replace(R.id.fragmentConteiner, new TaskListFragment()).
                addToBackStack(null).
                commit();
                break;
            case R.id.clearBtn:
                sceneList.removeAllFromScene(sceneIndex);
                Flash.saveList(sceneList);
                updateScreenUI();
                break;
        }
    }


    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

        scene.setName(textView.getText().toString());
        clearRequest(textView);

        return true;
    }

    private void clearRequest(TextView textView){

        clearRequestLay.requestFocus();
        InputMethodManager imm = (InputMethodManager)textView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(textView.getApplicationWindowToken(), 0);
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

        @Override
        public void fingerUp() {

            for(TaskElement task : scene.getTaskList())
                task.clearMoving();

            for(ActionElement action : scene.getActionList())
                action.clearMoving();
        }

        @Override
        public void prepareScreen(int w, int h) {

            screenWidth  = w;
            screenHeight = h;
        }

        @Override
        public void shortPress(int xPointer, int yPointer) {

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

                findTouchedTask(xPointer, yPointer).getTaskObject().show(context);
                Vibro.playShort(context);

            } else if (findTouchedAction(xPointer, yPointer) != null){

                findTouchedAction(xPointer, yPointer).getActionObject().show(context);
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
                dialog.setType(LIST.DIALOG_MENU);
                dialog.show(getFragmentManager().beginTransaction(), null);
            }

            Vibro.playLong(context);
            updateScreenUI();
        }

        @Override
        public void movement(int xPointer, int yPointer) {

            boolean taskMoving = false;
            boolean actionMoving = false;
            TaskElement   taskElement   = findTouchedTask(xPointer, yPointer);
            ActionElement actionElement = findTouchedAction(xPointer, yPointer);

            for(TaskElement task : scene.getTaskList()) {
                if (task.isMoving()) {
                    taskMoving = true;
                    break;
                }
            }

            for(ActionElement action : scene.getActionList()) {
                if (action.isMoving()) {
                    actionMoving = true;
                    break;
                }
            }

            if( taskElement != null &&  actionMoving == false){

                taskElement.setMoving();
                taskElement.setNewCoordinate(xPointer, yPointer);

            } else if( actionElement != null &&  taskMoving == false) {

                actionElement.setMoving();
                actionElement.setNewCoordinate(xPointer, yPointer);
            }

            updateScreenUI();
            unselectAll();
        }

        @Override
        public void onDrawView(Canvas canvas, MotionEvent event) {

            try{

                Bitmap pimpaIcon = TaskerIcons.getInstance().getPimpaIcon();

                Paint p = new Paint();
                p.setColor(Color.WHITE);
                p.setTextSize(getResources().getInteger(R.integer.builder_text_size));
                p.setTextAlign(Paint.Align.CENTER);
                p.setStrokeWidth(5);

                // 1. If present relationship between action and task - draw a lines
                for (ActionElement action : scene.getActionList()) {
                    for (TaskElement task : scene.getTaskList()) {
                        if (action.isTaskElementIdPresent(task.getTaskId())) {
                            canvas.drawLine(action.getX() + iconSizeElement /2, action.getY() + iconSizeElement /2,
                                            task.getX()   + iconSizeElement /2, task.getY()   + iconSizeElement /2, p);
                        }
                    }
                }

                // 2. Print action and task icons
                for(ActionElement action : scene.getActionList()){
                    //if(action.isSelect())
                        //canvas.drawBitmap(selectIcon, action.getX(), action.getY(), null);
                    canvas.drawBitmap(action.getIcon(), action.getX(), action.getY(), null);
                    canvas.drawBitmap(pimpaIcon, action.getX(), action.getY(), null);
                    float textX = action.getX() + iconSizeElement /2;
                    float textY = action.getY() + iconSizeElement + getResources().getInteger(R.integer.builder_icon_text_margin);
                    canvas.drawText(action.getActionName(), textX, textY, p);
                }

                for(TaskElement task : scene.getTaskList()){
                    //if(task.isSelect())
                        //canvas.drawBitmap(selectIcon, task.getX(), task.getY(), null);
                    canvas.drawBitmap(task.getIcon(), task.getX(), task.getY(), null);
                    canvas.drawBitmap(pimpaIcon, task.getX(), task.getY(), null);
                    float textX = task.getX() + iconSizeElement /2;
                    float textY = task.getY() + iconSizeElement + getResources().getInteger(R.integer.builder_icon_text_margin);
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
                if(task.isMoving())
                    return task;
        }

        for(TaskElement task : scene.getTaskList()){
            if(task.isTouched(xPointer, yPointer, iconSizeElement))
                return task;
        }
        return null;
    }

    private ActionElement findTouchedAction(int xPointer, int yPointer){

        for(ActionElement action : scene.getActionList()){
            if(action.isTouched(xPointer, yPointer, iconSizeElement))
                if(action.isMoving())
                    return action;
        }

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

    public class DelElement {

        private ArrayList<PressedElement> delElementList = new ArrayList<PressedElement>();
        private int elementSize;

        public DelElement(int elementSize){

            this.elementSize = elementSize;
        }

        public void addPressedElement(long id, int x, int y){

            delElementList.add(new PressedElement(id, x + iconSizeElement - elementSize, y));
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

        public void clearList(){

            delElementList.clear();
        }

        public int deleteListGetSize(){

            return delElementList.size();
        }

        public ArrayList<PressedElement> getDelList(){

            return delElementList;
        }

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

    @SuppressLint("ValidFragment")
    public class CustomDialog extends DialogFragment {

        private LIST val;
        private Dialog mDialog;

        public CustomDialog(){

        }

        public void setType(LIST val){

            this.val = val;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            mDialog = new Dialog(getActivity());
            mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

            switch(val){

                case DIALOG_MENU:

                    mDialog.setContentView(R.layout.dialog_menu);

                    ((ImageButton) mDialog.findViewById(R.id.actionElement)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(getActivity(),"Open Action List", Toast.LENGTH_SHORT).show();
                            scene.addNewAction("Timer 2", new aTimer(18, 47), TYPES.TIME, 0, 0);
                            updateScreenUI();
                            mDialog.dismiss();
                        }
                    });

                    ((ImageButton) mDialog.findViewById(R.id.taskElement)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(getActivity(),"Open Task List", Toast.LENGTH_SHORT).show();
                            scene.addNewTask("Skype 2", new tApp("com.skype.raider"), TYPES.APP, 0, 0);
                            updateScreenUI();
                            mDialog.dismiss();
                        }
                    });

                    break;
            }

            return mDialog;
        }
    }
}
