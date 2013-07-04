package com.example.epure.model;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class TouchView extends View {
	private static final String TAG = "TouchView";
	ArrayList<PointT> points = new ArrayList<PointT>();

	float x0, y0, scale = 1, cellSize = 1;
	int w, h;
	int n = 10;

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		this.w = w;
		this.h = h;
		cellSize = h / n;
		cellSize *= scale;
	}

	public TouchView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public TouchView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public TouchView(Context context) {
		super(context);
		init();
	}

	Paint paint1, paint0, paint, paintText;

	private void init() {
		Log.d(TAG, "init");
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setDither(true);
		paint.setColor(0xFF000000);
		paint.setStrokeWidth(1f);

		paint0 = new Paint();
		paint0.setAntiAlias(true);
		paint0.setDither(true);
		paint0.setColor(0xFFff0000);
		paint0.setStrokeWidth(1f);

		paint1 = new Paint();
		paint1.setAntiAlias(true);
		paint1.setDither(true);
		paint1.setColor(0xFF00ff00);
		paint1.setStrokeWidth(1f);

		paintText = new Paint();
		paintText.setAntiAlias(true);
		paintText.setDither(true);
		paintText.setColor(0xff000000);
		paintText.setTextAlign(Align.CENTER);
		paintText.setTextSize(15);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		for (PointT point : points) {
			switch (point.id) {
			case 0:
				canvas.drawCircle(point.x, point.y, 10, paint0);
				break;
			case 1:
				canvas.drawCircle(point.x, point.y, 10, paint1);
				break;
			default:
				canvas.drawCircle(point.x, point.y, 10, paint);
				break;
			}

			canvas.drawText(point.x + ":" + point.y, point.x, point.y - 10,
					paintText);
		}
		canvas.drawText("Points Size = " + points.size(), 40, 20, paintText);
		canvas.drawText("cell Size = " + cellSize, 40, 50, paintText);
		for (int i = 0; i < n * 3; i++) {

			float y = cellSize * i;
			float x = cellSize * i;
			y = y0 % (cellSize*scale) + scale * y;
			x = x0 % (cellSize*scale) + scale * x;
			canvas.drawLine(0, y, w, y, paint);
			canvas.drawLine(x, 0, x, h, paint);
		}
		super.onDraw(canvas);
	}

	boolean inTouch;
	int upPI = 0;
	int downPI = 0;

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		// событие
		int actionMask = event.getActionMasked();
		// индекс касания
		int pointerIndex = event.getActionIndex();
		// число касаний
		int pointerCount = event.getPointerCount();

		// Log.d(TAG, "pointerCount = " + pointerCount + ";  pointerIndex = "
		// + pointerIndex);
		
		if (pointerCount<2){
//		switch (pointerCount) {
//		case 1:
			switch (actionMask) {
			case MotionEvent.ACTION_DOWN: // первое касание
				inTouch = true;
				points.add(new PointT(event.getPointerId(pointerIndex), event
						.getX(pointerIndex), event.getY(pointerIndex)));
				break;
			case MotionEvent.ACTION_UP: // прерывание последнего касания
				inTouch = false;
				points.remove(0);
				break;

			case MotionEvent.ACTION_MOVE: // движение
				x0 -= points.get(0).x - event.getX();
				y0 -= points.get(0).y - event.getY();
				points.get(0).x = (int) event.getX();
				points.get(0).y = (int) event.getY();
				break;
			}
//			break;
		}else{
//		case 2:
			switch (actionMask) {
			case MotionEvent.ACTION_DOWN: // первое касание
				inTouch = true;
			case MotionEvent.ACTION_POINTER_DOWN: // последующие касания
				downPI = pointerIndex;
				points.add(new PointT(event.getPointerId(pointerIndex), event
						.getX(pointerIndex), event.getY(pointerIndex)));
				dist = Math.pow(points.get(0).x - points.get(1).x, 2)
						+ Math.pow(points.get(0).y - points.get(1).y, 2);
				break;

			case MotionEvent.ACTION_UP: // прерывание последнего касания
				inTouch = false;
			case MotionEvent.ACTION_POINTER_UP: // прерывания касаний
				upPI = pointerIndex;
				int i = 0;
				for (; i < points.size(); i++) {
					if (points.get(i).id == event.getPointerId(pointerIndex)) {
						break;
					}
				}
				points.remove(i);
				break;

			case MotionEvent.ACTION_MOVE: // движение

				for (int j = 0; j < pointerCount; j++) {

					for (PointT point : points) {
						if (point.id == event.getPointerId(j)) {
							point.set((int) event.getX(j), (int) event.getY(j));
						}
					}
				}

			}
			calcValues();
//			break;
			}
//		}

		invalidate();

		return true;
	}

	double dist;

	private void calcValues() {
		if (points.size() >= 2) {
			double dist2 = Math.pow(points.get(0).x - points.get(1).x, 2)
					+ Math.pow(points.get(0).y - points.get(1).y, 2);
			scale *= (dist / dist2 - 1d) * 0.2d + 1;
			dist = Math.pow(points.get(0).x - points.get(1).x, 2)
					+ Math.pow(points.get(0).y - points.get(1).y, 2);
			calcCell();
		}
	}

	private void calcCell() {
		int c = (int) (h / scale /cellSize);
		if (c > n + 3 || c < n - 4) {
			Log.d(TAG, "c = " + c + "; cell = " + cellSize+"; scale = " + scale);
			cellSize = (float)(h/scale)/n;
		}
	}

	class PointT extends Point {
		public int id;

		public PointT(int id, float x, float y) {
			super((int) x, (int) y);
			this.id = id;
		}
	}
}
