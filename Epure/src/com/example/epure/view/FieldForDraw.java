package com.example.epure.view;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.example.epure.model.Dot;
import com.example.epure.model.Grid;
import com.example.epure.model.Point;

public class FieldForDraw extends View {

	static final int STATE_MOVE = 1;
	static final int STATE_SCALE = 2;
	static final int STATE_POINT = 0;
	static final int STATE_NOTHING = 4;
	public static float INCREMENT_SCALE =1;

	/**
	 * 
	 * @param p
	 *            decart coordinate
	 * @param pDraw
	 *            draw coordinate
	 * @param scale
	 * @param p0
	 *            shift coordinate
	 * @return calculated draw coordinate
	 */
	static public Point convertDecartToDraw(Point p, Point pDraw, float scale,Point p0) {
		pDraw.x = p.x*scale + p0.x;
		pDraw.y = p.y*scale + p0.y;
		return pDraw;
	}

	/**
	 * 
	 * @param pDraw
	 *            draw coordinate
	 * @param p
	 *            decart coordinate
	 * @param scale
	 * @param p0
	 *            shift coordinate
	 * @return calculated decart coordinate
	 */
	static public Point convertDrawToDecart(Point pDraw, Point p, float scale, Point p0) {
		p.x = (pDraw.x - p0.x) / scale;
		p.y = (pDraw.y - p0.y) / scale;
		return p;
	}
	
	
	 public float convertLenghtDecToDraw (float lenghtDec){
		 return  lenghtDec*scale;
	}
	 
	 public float convertLenghtDrawtoDec (float lenghtDec){
		 return  lenghtDec/scale;
	}

	Point p0 = new Point(0, 0); // сдвиг координат
	Point pCenter = new Point(0,0);

	float scale = 1;
//	float scale0 = 1;

	Grid grid = new Grid(0, 0);
	ArrayList<Dot> points = new ArrayList<Dot>();
//	FingerDetect firstDetect = new FingerDetect(new Point(0, 0));
//	FingerDetect secondDetect = new FingerDetect(new Point(0, 0));

	Point firstFingerDown = new Point();
	double lenghtBetweenFingersLast;
	double lenghtBetweenFingersNow;
	int stateFinger = 0;
	int idFirstFinger;
	int idSecondFinger;
	private ScaleGestureDetector mScaleDetector;

	public FieldForDraw(Context context) {
		super(context);
		mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
	}

	public FieldForDraw(Context context, AttributeSet attrs) {
		super(context, attrs);
		mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);

		grid.draw(canvas);
		drawPoints(canvas);

	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		grid.onSizeChange(w, h);
	}
	
	
	void recalcDrawCoordinatesAll(){
		for (Dot dot : points) {
			calculateDrawCoodrinate(dot);
			Log.d("field", "move");
		}
//		calculateDrawCoodrinate(grid);
	}
	
	void recalcDecartCoordinatesAll(){
		for (Dot dot : points) {
			calculateDecarteCoordinate(dot);
		}
//		calculateDecarteCoordinate(grid);
	}

	void drawPoints(Canvas canvas) {
		for (Dot dot : points) {
			dot.draw(canvas);
			Log.d("field", "draw point");
		}
	}

	void changePositionDraw(float dx, float dy) {
		p0.x += dx;
		p0.y += dy;
	}
	

	void changeDecartCoordinateAll(Point center){
		grid.point.x = convertDrawToDecart(center, grid.point, scale, p0).x;
		grid.point.y = convertDrawToDecart(center, grid.point, scale, p0).y;
		for (Dot dot : points) {
			calculateDecarteCoordinate(dot);
		}
		
	}
	
	

	void changeGridScale(float scaleTouch) {
		grid.scaleGrid(scaleTouch);

	}

	void calculateDrawCoodrinate(Dot element) {

		element.pointDraw = convertDecartToDraw(element.point,
				element.pointDraw, scale, p0);
	}

	void calculateDecarteCoordinate(Dot element) {
		element.point = convertDrawToDecart(element.pointDraw, element.point,
				scale, p0);
	}
	
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// событие
		mScaleDetector.onTouchEvent(event);
		Log.d("touch", "touchEvent");

		int i = event.getActionIndex();

		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		// case MotionEvent.ACTION_POINTER_1_DOWN:
		//
		// firstFingerDown.x = event.getX();
		// firstFingerDown.y = event.getY();
		// stateFinger = STATE_POINT;
		// break;
		//
		// case MotionEvent.ACTION_POINTER_1_UP:
		// if(stateFinger == STATE_POINT){
		// Point pointDecart = convertDrawToDecart(new Point(event.getX(),
		// event.getY()), new Point(), scale, p0); // перевожу
		// // коордитаны
		// // экрана
		// // в координаты
		// // нашей сетки
		// Dot dot = new Dot(pointDecart);
		// calculateDrawCoodrinate(dot);
		// points.add(dot);
		// Log.d("field",
		// "touch x:" + event.getX() + "  y:" + event.getY()
		// + "   count:" + event.getPointerCount());
		// invalidate();}
		// stateFinger = STATE_POINT;
		// break;
		//
		// case MotionEvent.ACTION_POINTER_2_DOWN:
		// idSecondFinger = event.getPointerId(i);
		// secondFingerDown.x = event.getX(i);
		// secondFingerDown.y = event.getY(i);
		// lenghtBetweenFingersLast = (float) Math.pow(secondFingerDown.x
		// - firstFingerDown.x, 2)
		// + Math.pow(secondFingerDown.y - firstFingerDown.y, 2);
		// stateFinger = STATE_SCALE;
		// break;
		//
		// case MotionEvent.ACTION_POINTER_2_UP:
		// // points.add(new Dot(new Point(event.getX(i), event.getY(i))));
		// // Log.d("field", "touch x:" + event.getX(i) + "  y:" +
		// // event.getY(i)
		// // + "   count:" + event.getPointerCount());
		// // invalidate();
		// stateFinger = STATE_NOTHING;
		// break;
		case MotionEvent.ACTION_DOWN:
			idFirstFinger = event.getPointerId(i);
			firstFingerDown.x = event.getX();
			firstFingerDown.y = event.getY();
			stateFinger = STATE_POINT;

		case MotionEvent.ACTION_POINTER_DOWN:
			// stateFinger = STATE_SCALE;

			if (event.getPointerCount() == 2) {

				// idSecondFinger = event.getPointerId(i);
				// secondFingerDown.x = event.getX(i);
				// secondFingerDown.y = event.getY(i);
				// lenghtBetweenFingersLast = (float)
				// Math.pow(secondFingerDown.x
				// - firstFingerDown.x, 2)
				// + Math.pow(secondFingerDown.y - firstFingerDown.y, 2);

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
			// points.add(new Dot(new Point(event.getX(i), event.getY(i))));
			// Log.d("field", "touch x:" + event.getX(i) + "  y:" +
			// event.getY(i)
			// + "   count:" + event.getPointerCount());
			// invalidate();
			stateFinger = STATE_NOTHING;
			break;
		case MotionEvent.ACTION_MOVE:
			
			switch (event.getPointerCount()) {
			case 1:
				// int id = event.getPointerId(i);
				if (stateFinger != STATE_NOTHING) {
					changePositionDraw((event.getX(0) - firstFingerDown.x),(event.getY(0) - firstFingerDown.y));
					recalcDrawCoordinatesAll();
					
					grid.onMove(event.getX(0) - firstFingerDown.x, event.getY(0) - firstFingerDown.y);
					
					firstFingerDown.x = event.getX(0);
					firstFingerDown.y = event.getY(0);
					invalidate();
					stateFinger = STATE_MOVE;
				}
				break;

			case 2:
				// // for (int a = 0; a < event.getPointerCount(); a++) {
				// // if (event.getPointerId(i) == idFirstFinger) {
				// firstFingerDown.x = event.getX(0);
				// firstFingerDown.y = event.getY(0);
				// // }
				//
				//
				// // if (event.getPointerId(i) == idSecondFinger) {
				// secondFingerDown.x = event.getX(1);
				// secondFingerDown.y = event.getY(1);
				// // }
				// // }
				// firstDetect.pointDraw.x = firstFingerDown.x;
				// firstDetect.pointDraw.y = firstFingerDown.y;
				//
				// secondDetect.pointDraw.x = secondFingerDown.x;
				// secondDetect.pointDraw.y = secondFingerDown.y;
				//
				// lenghtBetweenFingersNow = Math.pow(secondFingerDown.x
				// - firstFingerDown.x, 2)
				// + Math.pow(secondFingerDown.y - firstFingerDown.y, 2);
				// float s = (float) (Math.pow((lenghtBetweenFingersNow /
				// lenghtBetweenFingersLast),1d / 10d));
				// Log.d("scale", String.format("s = %.08f", s));
//				 changeScale(s);
				// lenghtBetweenFingersLast= lenghtBetweenFingersNow;
				// invalidate();
//				 mScaleDetector.onTouchEvent(event);
			}
		}
				

		return true;
	}
		
	void correctScaleP0 (float scaleLoc, Point focusPDraw){
		 Point  dpDec =  new Point();
		 convertDrawToDecart(focusPDraw, dpDec, scaleLoc, p0);
		 
		 float dx = convertLenghtDecToDraw(dpDec.x-dpDec.x*scaleLoc);
		 p0.x +=dx;
		 float dy = convertLenghtDecToDraw(dpDec.y-dpDec.y*scaleLoc);
		 p0.y += dy;
		 grid.onMove(dx, dy);
		 
	}

	private class ScaleListener extends
			ScaleGestureDetector.SimpleOnScaleGestureListener {
		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			if(detector.isInProgress()){
				
			 float s =  (float) ((float) 1/((detector.getPreviousSpan() / detector.getCurrentSpan()-1d ) * 0.5d + 1));// current SCALE
			 scale *= s; 
			
			 
			 scale = Math.max(0.1f, Math.min(scale, 2.0f));
			 correctScaleP0(s, new Point(detector.getFocusX(), detector.getFocusY()));
			 changeGridScale(s);
			 recalcDrawCoordinatesAll();
				}

			invalidate();
			return true;
		}
	};
	
}