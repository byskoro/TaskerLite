package com.taskerlite.logic;

import com.taskerlite.logic.actions.mAction;
import com.taskerlite.logic.tasks.mTask;
import com.taskerlite.source.Settings;
import com.taskerlite.source.Types.*;

import java.util.ArrayList;

public class ProfileController {
	
	private ArrayList<Profile> profileList = new ArrayList<Profile>();

	public void newProfile(String name){ profileList.add(new Profile(name)); }
	public Profile getProfile(int index){ return profileList.get(index); }
	public ArrayList<Profile> getProfileList() { return profileList; }
    public int getProfileListSize(){ return profileList.size(); }

    public void removeProfileFromList(int index){
		profileList.remove(index);
        profileList.trimToSize();
        saveAllProfile();
	}

    public void saveAllProfile(){
        Settings.getInstance().saveList(this);
    }
	
	public class Profile {
		
		private String name;
		private ArrayList<ActionElement> actionList = new ArrayList<ActionElement>();
		private ArrayList<TaskElement> taskList = new ArrayList<TaskElement>();

        public Profile(String name){
            this.name = name;
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

