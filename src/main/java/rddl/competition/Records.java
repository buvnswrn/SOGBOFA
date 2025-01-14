package rddl.competition;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

public class Records {
	public boolean fileAppend(String fileName, String context)
	{
		try {
			System.out.println("file name: " + fileName);
			MyPath myPath = new MyPath();
			String absPath = myPath.getProjectPath();
			File file = new File(absPath + System.getProperties().getProperty("file.separator") + fileName);
			FileWriter writer = new FileWriter(file, true);

			writer.write(context+"\r\n");
			writer.close();
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("somehow we fail!"); 
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean fileAppend(String fileName, String context, boolean replaceOld)
	{
		try {
			System.out.println("file name: " + fileName);
			
			
			String absPath = System.getProperty("user.home");
			System.out.println(absPath);
			File file = new File(absPath + System.getProperties().getProperty("file.separator") + fileName);
			if(replaceOld && file.exists()){
				file.delete();
			}
			FileWriter writer = new FileWriter(file, true);

			writer.write(context+"\r\n");
			writer.close();
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("somehow we fail!"); 
			e.printStackTrace();
		}
		return false;
	}
	
	boolean fileAppend(String fileName, String x, String y)
	{
		return fileAppend(fileName, x + " " + y);
	}
	public static void main(String[] args) {
		Records record = new Records();
		record.fileAppend("hehe", "1 2");
		record.fileAppend("hehe", "3 4");
		record.fileAppend("hehe", "3", "4");
	}
}
