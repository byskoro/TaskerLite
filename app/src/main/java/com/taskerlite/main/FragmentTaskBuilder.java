package com.taskerlite.main;

import com.taskerlite.R;
import com.taskerlite.TaskerBuilderView;
import com.taskerlite.dialogs.ActionBuilderDialog;
import com.taskerlite.dialogs.TaskBuilderDialog;
import com.taskerlite.logic.ActionElement;
import com.taskerlite.logic.SceneList;
import com.taskerlite.logic.SceneList.*;
import com.taskerlite.logic.TaskElement;
import com.taskerlite.other.Flash;
import com.taskerlite.other.Vibro;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class FragmentTaskBuilder extends Fragment implements View.OnClickListener, TextView.OnEditorActionListener {

    private TaskerBuilderView taskerView;
    private Context context;
    private Scene scene;
    private int sceneIndex = 0;
    private SceneList sceneList = com.taskerlite.main.mActivity.sceneList;

    private int iconSizeElement = TaskerIcons.builderSize;
    private Bitmap pimpaIcon = TaskerIcons.getInstance().getPimpaIcon();
    DelElement gcElement;

    public static int screenWidth;
    public static int screenHeight;

    ImageButton backBtn, clearBtn, actionElement, taskElement;
    EditText nameScene;
    LinearLayout clearRequestLay;

    private Paint textPaint, linePaint;

    public static FragmentTaskBuilder getInstance(int sceneIndex){

        Bundle inSceneIndex = new Bundle();
        inSceneIndex.putInt("index", sceneIndex);

        FragmentTaskBuilder taskBuilder = new FragmentTaskBuilder();
        taskBuilder.setArguments(inSceneIndex);

        return taskBuilder;
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle bundle = this.getArguments();
        sceneIndex = bundle.getInt("index", 0);

		View view = inflater.inflate(R.layout.fragment_task_builder, container, false);
        context = getActivity();

        scene = com.taskerlite.main.mActivity.sceneList.getScene(sceneIndex);

		taskerView = (TaskerBuilderView) view.findViewById(R.id.drawBuilder);
        taskerView.setViewCallBack(viewCallBack);
        backBtn = (ImageButton) view.findViewById(R.id.backBtn);
        backBtn.setOnClickListener(this);
        clearBtn= (ImageButton) view.findViewById(R.id.clearBtn);
        clearBtn.setOnClickListener(this);
        actionElement = (ImageButton) view.findViewById(R.id.actionElementID);
        actionElement.setOnClickListener(this);
        taskElement = (ImageButton) view.findViewById(R.id.taskElementID);
        taskElement.setOnClickListener(this);
        nameScene = (EditText) view.findViewById(R.id.sceneName);
        nameScene.setOnEditorActionListener(this);
        nameScene.setText(scene.getName());
        clearRequestLay = (LinearLayout) view.findViewById(R.id.clearRequestLay);
        clearRequest(nameScene);

        gcElement = new DelElement(TaskerIcons.deleteSize);

        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(getResources().getInteger(R.integer.builder_text_size));
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        textPaint.setTextAlign(Paint.Align.CENTER);

        linePaint = new Paint();
        linePaint.setTextAlign(Paint.Align.CENTER);
        linePaint.setStrokeWidth(5);

		return view;
	}

    @Override
    public void onPause() {
        super.onPause();

        for(ActionElement action : scene.getActionList()) {
            action.unselect();
        }
        for(TaskElement task : scene.getTaskList()) {
            task.unselect();
        }

        handlerLogic.removeMessages(1);
    }

    @Override
    public void onClick(View view) {

        switch(view.getId()){

            case R.id.backBtn:

                scene.invalidateData();

                Flash.saveList(sceneList);
                getFragmentManager().beginTransaction().
                setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right).
                replace(R.id.fragmentConteiner, new FragmentTaskList()).
                addToBackStack(null).
                commit();
                break;
            case R.id.clearBtn:
                sceneList.removeAllFromScene(sceneIndex);
                Flash.saveList(sceneList);
                break;
            case R.id.actionElementID:
                ActionBuilderDialog actionDialog = new ActionBuilderDialog();
                actionDialog.setTargetFragment(this, 0);
                actionDialog.show(getFragmentManager().beginTransaction(), "actionList");
                break;
            case R.id.taskElementID:
                TaskBuilderDialog taskDialog = new TaskBuilderDialog();
                taskDialog.setTargetFragment(this, 0);
                taskDialog.show(getFragmentManager().beginTransaction(), "taskList");
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

    private Handler handlerLogic = new Handler() {

        public void handleMessage(Message msg) {

            if (msg.what == 0) {

                for(ActionElement action : scene.getActionList())
                    action.unselect();
                for(TaskElement task : scene.getTaskList())
                    task.unselect();
            }

            if (msg.what == 1){
                taskerView.postInvalidate();
                handlerLogic.sendEmptyMessageDelayed(1, 50);
            }
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

            handlerLogic.sendEmptyMessageDelayed(1, 50);
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

                findTouchedTask(xPointer, yPointer).getTaskObject().show(getFragmentManager());
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

            } else
                unselectAll();

            Vibro.playLong(context);
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

            unselectAll();
        }

        @Override
        public void onDrawView(Canvas canvas, MotionEvent event) {

            try{

                // 1. Print action and task icons
                for(ActionElement action : scene.getActionList()){
                    canvas.drawBitmap(action.getIcon(), action.getX(), action.getY(), null);
                    float textX = action.getX() + iconSizeElement /2;
                    float textY = action.getY() + iconSizeElement + getResources().getInteger(R.integer.builder_icon_text_margin);
                    canvas.drawText(action.getActionObject().getName(), textX, textY, textPaint);
                }

                for(TaskElement task : scene.getTaskList()){
                    canvas.drawBitmap(task.getIcon(), task.getX(), task.getY(), null);
                    float textX = task.getX() + iconSizeElement /2;
                    float textY = task.getY() + iconSizeElement + getResources().getInteger(R.integer.builder_icon_text_margin);
                    canvas.drawText(task.getTaskObject().getName(), textX, textY, textPaint);
                }

                // 2. If present relationship between action and task - draw a lines
                int indexColor = 0;
                for (ActionElement action : scene.getActionList()) {
                    for (TaskElement task : scene.getTaskList()) {
                        if (action.isTaskElementIdPresent(task.getTaskId())) {
                            linePaint.setColor(TaskerTypes.getColor(indexColor));
                            canvas.drawLine(action.getX() + iconSizeElement /2, action.getY() + iconSizeElement/10, task.getX()   + iconSizeElement /2, task.getY()   + iconSizeElement/10, linePaint);
                            indexColor++;
                        }
                    }
                }

                // 3. Draw pimpa
                for(ActionElement action : scene.getActionList())
                    canvas.drawBitmap(pimpaIcon, action.getX(), action.getY(), null);

                for(TaskElement task : scene.getTaskList())
                    canvas.drawBitmap(pimpaIcon, task.getX(), task.getY(), null);

                // 4. Draw delete icons
                for(DelElement.PressedElement element : gcElement.getDelList())
                    canvas.drawBitmap(TaskerIcons.getInstance().getDeleteIcon(), element.x, element.y, null);

            }catch (Exception e){ }
        }
    };

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


    public Scene getScene() {
        return scene;
    }
}
