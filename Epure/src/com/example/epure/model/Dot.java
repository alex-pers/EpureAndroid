package com.example.epure.model;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Dot extends Element {
	
	public Point point; // сделвать массив из одного элементва
	public Point pointDraw;
	
	
	int radiusCircle=8;
	float circleWidht=6;
	Paint paintCircle;
	Paint paintText;

	float drawX;
	float drawY;


	public Dot(Point p) {
		point =p;
		pointDraw = new Point();

		init();

	}

	public Dot(Point p, int id) {
		this.point.x = p.x;
		this.point.y = p.y;
		this.ID = id;
		init();
	}
	
	public Dot(Point p, Paint paintCir, Paint paintTxt){
		this.point.x = p.x;
		this.point.y = p.y;
		this.paintCircle = paintCir;
		this.paintText = paintTxt;
	}
	
	void init() {
		paintCircle = new Paint();
		paintText = new Paint();
		
		paintCircle.setStyle(Paint.Style.STROKE);
		paintCircle.setColor(Color.GREEN);
		paintCircle.setStrokeWidth(circleWidht);
		
		paintText.setStyle(Paint.Style.STROKE);
		paintText.setColor(Color.BLACK);
		paintText.setTextSize(16);
	}


	@Override
	public
	void draw(Canvas canvas) {
		
		
		canvas.drawCircle(pointDraw.x, pointDraw.y, radiusCircle, paintCircle);

		StringBuilder strText = new StringBuilder();
		strText.append((int)point.x).append(" , ").append((int)point.y);
		String label = strText.toString();

		Paint paint = new Paint();
		Rect bounds = new Rect();
		paint.getTextBounds(label, 0, label.length(), bounds);

		bounds.width();

		canvas.drawText(label, pointDraw.x - (bounds.width() / 2), pointDraw.y
				- (radiusCircle + 3 + bounds.height()), paintText);
		
	}
	
	
//	@Override
//	public
//	void onMove(float dx, float dy) {
//		
//		point.x += dx;
//		point.y +=dy;
//		
//	}
	
	

	@Override
	public void onScale(float scale) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getColor() {
		
		return paintCircle.getColor();
	}

	@Override
	public void setColor(int color) {
		paintCircle.setColor(color);
		
	}

	@Override
	public float getWidthLine() {
		return circleWidht;
	}

	@Override
	public void setWidthLine(float widthLine) {
		circleWidht = widthLine;
		paintCircle.setStrokeWidth(circleWidht);
		
	}

	@Override
	public void onMove(Point p0) {
		
	}

	@Override
	public void onMove(float dx, float dy) {
		// TODO Auto-generated method stub
		
	}

}
