package com.tejadillas.armov3;

import java.util.ArrayList;

public class MySingleton 
{

	private static MySingleton instance;
	
	public ArrayList<String[]> landformsDB;	
	
	public static void initInstance()
	{
		if(instance == null)
		{
			instance = new MySingleton();
			instance.landformsDB = new ArrayList<String[]>();
		}
	}
	
	public static MySingleton getInstance()
	{
		return instance;
	}
	
	private MySingleton()
	{
		
	}
	
}
