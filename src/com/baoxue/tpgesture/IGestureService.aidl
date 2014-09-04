package com.baoxue.tpgesture;

interface IGestureService { 
	void unlockScreen();
	void lockScreen();
	void setGestureEnable(boolean enable);
	void runGesture();
}