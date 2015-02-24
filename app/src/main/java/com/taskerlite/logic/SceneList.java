package com.taskerlite.logic;

import com.taskerlite.logic.actions.mAction;
import com.taskerlite.logic.actions.mAction.*;
import com.taskerlite.logic.tasks.mTask;
import com.taskerlite.logic.tasks.mTask.*;
import com.taskerlite.main.mActivity;
import com.taskerlite.other.Flash;
import com.taskerlite.main.TaskerTypes.*;

import java.util.ArrayList;

public class SceneList {
	
	private ArrayList<Scene> sceneList = new ArrayList<Scene>();
	
	public void addNewScene(String sceneName){ sceneList.add(new Scene(sceneName)); }
	public Scene getScene(int index){ return sceneList.get(index); }
	public ArrayList<Scene> getSceneList() { return sceneList; }
    public int getSceneListSize(){ return sceneList.size(); }
	
	public void removeSceneFromList(int index){

		sceneList.remove(index);
        sceneList.trimToSize();
        Flash.saveList(mActivity.sceneList);
	}

    public void removeAllFromScene(int index){

        sceneList.get(index).getActionList().clear();
        sceneList.get(index).getActionList().trimToSize();
        sceneList.get(index).getTaskList().clear();
        sceneList.get(index).getTaskList().trimToSize();
    }
	
	public class Scene{
		
		private String name;
		private ArrayList<ActionElement> actionList = new ArrayList<ActionElement>();
		private ArrayList<TaskElement>   taskList = new ArrayList<TaskElement>();

		public Scene(String sceneName){

			this.name = sceneName;
		}
		
		public void addNewAction(mAction actionObject, TYPES actionType, int xCoordinate, int yCoordinate){
			
			actionList.add(new ActionElement(actionObject, actionType, xCoordinate, yCoordinate));
		}

        public void deleteAction(ActionElement actionElement){
            actionList.remove(actionElement);
            actionList.trimToSize();
        }
		
		public void addNewTask(mTask obj, TYPES objType, int x, int y){
			
			taskList.add(new TaskElement(obj, objType, x, y));
		}

        public void deleteTask(TaskElement taskElement){
            for(ActionElement actionElement : actionList)
                actionElement.deleteTaskElementId(taskElement.getTaskId());
            taskList.remove(taskElement);
            taskList.trimToSize();
        }

        public void invalidateData(){

            for(ActionElement action : getActionList())
                action.invalidateData();
            for(TaskElement task : getTaskList())
                task.invalidateData();
        }
		
		public String getName() { return name; }
		public ArrayList<TaskElement> getTaskList() { return taskList; }
		public ArrayList<ActionElement> getActionList() { return actionList; }
        public void setName(String name) { this.name = name; }
    }
}

