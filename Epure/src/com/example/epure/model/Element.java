package com.example.epure.model;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

abstract public class Element {

	public int ID;
//	public Paint paint;
	boolean active;

	abstract public void draw(Canvas canvas);

	abstract public void onMove(float dx, float dy);
	abstract public void onMove(Point p0);
	
	abstract public void onScale(float scale);

	abstract public int getColor();

	abstract public void setColor(int color);

	abstract public float getWidthLine();

	abstract public void setWidthLine(float widthLine);

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}


	
	

}