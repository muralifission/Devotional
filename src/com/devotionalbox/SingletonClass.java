package com.devotionalbox;

public class SingletonClass {
	SingletonClass instance;
	private SingletonClass()
	{
		
	}
	SingletonClass getInstance()
	{
		if(instance == null)
		{
			instance = new SingletonClass();
		}
		return instance;
		
	}
}
