package com.taskerlite.other;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.os.Environment;

import com.google.gson.GsonBuilder;
import com.taskerlite.logic.SceneListController;

public class Flash {

	public static void saveList(SceneListController obj){
		
		try {

			String data = new GsonBuilder().create().toJson(obj);
			
			File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "TaskerLite.json");		
			
			if(!file.exists())
				file.createNewFile();
			
	        FileOutputStream fOut = new FileOutputStream(file);
	        OutputStreamWriter outWriter = new OutputStreamWriter(fOut);
	        outWriter.append(data);
	        outWriter.close();
	        fOut.close();
	        
		} catch (Exception e) { }
	}
	
	public static SceneListController getList(){
		
		SceneListController sl = null;
		String rBuffer = "";
		
		try {
			
			File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "TaskerLite.json");
	        FileInputStream fIn = new FileInputStream(file);
	        BufferedReader myReader = new BufferedReader(new InputStreamReader(fIn));
	        String aDataRow = "";
	        while ((aDataRow = myReader.readLine()) != null) 
	            rBuffer += aDataRow ;
	        myReader.close();
	        
	        sl = new GsonBuilder().create().fromJson(rBuffer, SceneListController.class);
			
		} catch (Exception e) { }		

		return sl == null ? new SceneListController() : sl;
	}

    public static String getRawData(){

        SceneListController sl = null;
        String rBuffer = "";

        try {

            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "TaskerLite.json");
            FileInputStream fIn = new FileInputStream(file);
            BufferedReader myReader = new BufferedReader(new InputStreamReader(fIn));
            String aDataRow = "";
            while ((aDataRow = myReader.readLine()) != null)
                rBuffer += aDataRow ;
            myReader.close();

        } catch (Exception e) { }

        return rBuffer;
    }
}
