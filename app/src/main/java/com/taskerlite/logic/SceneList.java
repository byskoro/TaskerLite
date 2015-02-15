package com.taskerlite.logic;

import com.taskerlite.logic.actions.mAction;
import com.taskerlite.logic.tasks.mTask;

import java.util.ArrayList;


public class SceneList {

	public static enum ACTION_TYPE {TIMER, FINISHBOOT, SCREENON, SCREENOFF};
	public static enum TASK_TYPE {APP, ALARM, WIFI, MOBILEDATA};
	
	private ArrayList<Scene> sceneList = new ArrayList<Scene>();
	
	public void addNewSnene(String sceneName){
		
		sceneList.add(new Scene(sceneName));
	}
	
	public Scene getScene(int index){
		
		return sceneList.get(index);
	}
	
	public ArrayList<Scene> getSceneList() {
		return sceneList;
	}
	
	public int getSceneListSize(){
		return sceneList.size();
	}
	
	public void removeSceneFromList(int index){
		sceneList.remove(index);
		// need add save to flash
	}
	
	public class Scene{
		
		private String name;
		private ArrayList<ActionDescription> actionList = new ArrayList<ActionDescription>();
		private ArrayList<TaskDescription>   taskList = new ArrayList<TaskDescription>();
		
		public Scene(String sceneName){
			
			this.name = sceneName;
		}
		
		public void addNewAction(String actionName, mAction actionObject, ACTION_TYPE actionType, int xCoordinate, int yCoordinate){
			
			actionList.add(new ActionDescription(actionName, actionObject, actionType, xCoordinate, yCoordinate));
		}
		
		public void addNewTask(String objName, mTask obj, TASK_TYPE objType){
			
			taskList.add(new TaskDescription(objName, obj, objType));
			
			//for test
			taskList.get(0).setTaskActionId(actionList.get(0).getActionId());
		}		
		
		public String getName() {
			return name;
		}
		public ArrayList<TaskDescription> getTaskList() {
			return taskList;
		}
		public ArrayList<ActionDescription> getActionList() {
			return actionList;
		}
	}
}

