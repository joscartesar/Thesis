package com.tejadillas.armov3;

import android.app.Application;

public class MyApplication extends Application
{

	@Override
	public void onCreate()
	{
		super.onCreate();
		
		initSingletons();
	}
	
	protected void initSingletons()
	{
		MySingleton.initInstance();
	}
	
}
