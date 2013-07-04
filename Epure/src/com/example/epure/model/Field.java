package com.example.epure.model;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

public class Field extends View {
	
	//for fingers
	int stateFinger = 0;
	static final int STATE_MOVE = 1;
	static final int STATE_SCALE = 2;
	static final int STATE_POINT = 0;
	static final int STATE_NOTHING = 4;
	Point firstFingerDown = new Point();
	int idFirstFinger;
	private ScaleGestureDetector mScaleDetector;
	
	
	// far draw
	private float scale = 1;
	private Point p0 = new Point(0, 0);
	TableGrid tableGrid = new TableGrid(0,0);
	ArrayList<Dot> points = new ArrayList<Dot>();
	
	public Field(Context context) {
		super(context);
		mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
	}
	public Field(Context context, AttributeSet attrs) {
		super(context, attrs);
		mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
	}
	

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		tableGrid.draw(canvas);
		drawPoints(canvas);
	}
	void drawPoints(Canvas canvas) {
		for (Dot dot : points) {
			dot.draw(canvas);
		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		tableGrid.sizeChange(w, h);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		mScaleDetector.onTouchEvent(event);// если два пальца, передаётся в scaleListener
		
		
		int i = event.getActionIndex();

		switch (event.getAction() & MotionEvent.ACTION_MASK) {

		case MotionEvent.ACTION_DOWN:
			idFirstFinger = event.getPointerId(i);
			firstFingerDown.x = event.getX();
			firstFingerDown.y = event.getY();
			stateFinger = STATE_POINT;
			break;
		case MotionEvent.ACTION_POINTER_DOWN:

			if (event.getPointerCount() == 2) {
				stateFinger = STATE_SCALE;
			}

			break;
		case MotionEvent.ACTION_UP:
			if (stateFinger == STATE_POINT) {
				Point pointDecart = convertDrawToDecart(new Point(event.getX(),
						event.getY()), new Point(), scale, p0); // перевожу
																// коордитаны
																// экрана
																// в координаты
																// нашей сетки
				Dot dot = new Dot(pointDecart);
				calculateDrawCoodrinate(dot);
				points.add(dot);
				Log.d("field",
						"touch x:" + event.getX() + "  y:" + event.getY()
								+ "   count:" + event.getPointerCount());
				invalidate();


			}
			stateFinger = STATE_POINT;
			break;
		case MotionEvent.ACTION_POINTER_UP:
			stateFinger = STATE_NOTHING;
			break;
		case MotionEvent.ACTION_MOVE:
			Log.d("field","move");
			switch (event.getPointerCount()) {
			case 1:
				if (stateFinger != STATE_NOTHING) {
					moveFinger(event.getX(0) - firstFingerDown.x, event.getY(0) - firstFingerDown.y);
					firstFingerDown.x = event.getX(0);
					firstFingerDown.y = event.getY(0);
					invalidate();
					stateFinger = STATE_MOVE;
				}
				break;

			}
		}
		
		return true;
	}

	public void moveFinger(float dx, float dy){
		p0.x += dx;
		p0.y += dy;
		tableGrid.onMove(p0);
//		tableGrid.onMove(dx, dy);
		recalcAllDrawCoordDot();
		
		
	}

	static public Point convertDecartToDraw(Point p, Point pDraw, float scale,Point p0) {
		pDraw.x = p.x*scale + p0.x;
		pDraw.y = p.y*scale + p0.y;
		return pDraw;
	}

	static public Point convertDrawToDecart(Point pDraw, Point p, float scale, Point p0) {
		p.x = (pDraw.x - p0.x) / scale;
		p.y = (pDraw.y - p0.y) / scale;
		return p;
	}
	
	void recalcAllDrawCoordDot(){
		for (Dot dot : points) {
			calculateDrawCoodrinate(dot);
		}
		//тут передаю p0 в table
	}
	
	void recalcAllDrawCoordDot(float curScale, Point focusPoint){
		float diffX = focusPoint.x - focusPoint.x * curScale;
		float diffY = focusPoint.y - focusPoint.y * curScale;
		for (Dot dot : points) {
//			float diffX = dot.pointDraw.x - focusPoint.x;
//			float diffY = dot.pointDraw.y - focusPoint.y;
			
//			diffX *= curScale;
//			diffY *= curScale;
			
			dot.pointDraw.x = dot.pointDraw.x + diffX;//focusPoint.x + diffX;
			dot.pointDraw.y = dot.pointDraw.y + diffY;//focusPoint.y + diffY;
		}
		//тут передаю p0 в table
	}
	
	void calculateDrawCoodrinate(Dot element) {

		element.pointDraw = convertDecartToDraw(element.point,
				element.pointDraw, scale, p0);
	}
	
	
	private class ScaleListener extends
			ScaleGestureDetector.SimpleOnScaleGestureListener {
		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			if (detector.isInProgress()) {
				
				 float s =  (float) ((float) 1/((detector.getPreviousSpan() / detector.getCurrentSpan()-1d ) * 0.5d + 1));// current SCALE
				 Log.d("SCALE", ""+s);
				 scale *= s; 
				
				 
				 tableGrid.onScale(/*scale*/s);
				 Point point = new Point(detector.getFocusX(), detector.getFocusY());
//				 correctScaleP0(s, point);
				 recalcAllDrawCoordDot(s, point);
				 
				float dx = point.x * s;
				float dy = point.y * s;
					
				float diffX = dx - point.x;
				float diffY = dy - point.y;
				 
				tableGrid.onMove(point.x, point.y);
			}

			invalidate();
			return true;
		}
	};
	void correctScaleP0 (float scaleLoc, Point focusPDraw){
//		 Point  dpDec =  new Point();
//		 convertDrawToDecart(focusPDraw, dpDec, scaleLoc, p0);
//		 
//		 float dx = convertLenghtDecToDraw(dpDec.x-dpDec.x*scaleLoc);
////		 p0.x *= scale;
////		 p0.y *= scale;
//		
//		 p0.x +=dx;
//		 float dy = convertLenghtDecToDraw(dpDec.y-dpDec.y*scaleLoc);
//		 p0.y +=dy;
//		 tableGrid.onMove(dx, dy);
//		 fignia(dx, dy);
		float dx = focusPDraw.x * scaleLoc;
		float dy = focusPDraw.y * scaleLoc;
		
		float diffX = dx - focusPDraw.x;
		float diffY = dy - focusPDraw.y;
		
		p0.x -= diffX;
		p0.y -= diffY;
		
//		tableGrid.onMove(-dx, -dy);
	}
	
	
	void fignia(float dx, float dy){
		for (Dot dot : points) {
			dot.pointDraw.x += dx;
			dot.pointDraw.y += dy;
			
		}
	}
	
	
	 public float convertLenghtDecToDraw (float lenghtDec){
		 return  lenghtDec*scale;
	}
	 
	 public float convertLenghtDrawtoDec (float lenghtDec){
		 return  lenghtDec/scale;
	}
}
