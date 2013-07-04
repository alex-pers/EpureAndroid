package com.example.epure.model;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

public class TableGrid extends Element {

	/*Ilay*/
	float _physicGridScale = 100;
	float _realGridScale = 100;
	Point _offsetSize = new Point(100, 100);
	/*end Ilay*/
	
	Point point;
	Point pointDraw;
	Point p0;
	Rect bounds;

	float WIDTH;
	float HEIGHT;
	float sizeSquare;
	float sizeSquareDraw;
	private final float maxScale = (float) Math.pow(10, 3);
	private final float minScale = (float) Math.pow(0.1, 1);
	private float maxSizeSquare;
	private float minSizeSquare;

	Paint paintLine;
	Paint paintText;

	public TableGrid(float x, float y) {
		this.point = new Point(0, 0);
		this.p0 = new Point(0, 0);
		this.pointDraw = new Point(0, 0);
		init();
	}

	public TableGrid(Point point, Paint paintL) {
		this.point = point;
		this.p0 = point;
		this.pointDraw = point;
		this.paintLine = paintL;
		init();
	}

	void init() {

		paintLine = new Paint();
		paintLine.setStyle(Paint.Style.STROKE);
		paintLine.setColor(Color.GRAY);

		paintText = new Paint();
		paintText.setStyle(Paint.Style.STROKE);
		paintText.setColor(Color.DKGRAY);
		paintText.setTextSize(16);

		bounds = new Rect();

	}

	@Override
	public void draw(Canvas canvas) {

		
		
//		 point.x = (float) (Math.floor(-p0.x/sizeSquare)*sizeSquare);
//		 point.y = (float) (Math.floor(-p0.y/sizeSquare)*sizeSquare);

		 
		 
//		 float dx = p0.x% sizeSquareDraw;
//		 pointDraw.x = p0.x% sizeSquareDraw;
//		 pointDraw.y = p0.y%sizeSquareDraw;

		int countHorisontal = (int) (HEIGHT / _physicGridScale /*sizeSquareDraw*/) + 1;
		int countVertical = (int) (WIDTH / _physicGridScale /*sizeSquareDraw*/) + 1;

		for (int i = 0; i <= countVertical; i++) {

			float currDrawX = (float) i * sizeSquareDraw + pointDraw.x;
			float currDecartX = point.x + (float) i * sizeSquare;

			/*Ilay*/
			float lineCoordX = i * _physicGridScale + _offsetSize.x;
			/*end Ilay*/
			
			canvas.drawLine(lineCoordX/*currDrawX*/ , 0 , lineCoordX/*currDrawX*/, HEIGHT, paintLine);

//			paintText.getTextBounds("" + currDecartX, 0,
//					("" + currDecartX).length(), bounds);
//
//			canvas.save();
//			canvas.rotate(90, currDrawX, 0);
//			canvas.drawText(String.format(" %.2f", currDecartX), currDrawX,
//					(float) (bounds.height()), paintText);
//			canvas.restore();

		}

		for (int i = 0; i <= countHorisontal; i++) {
			float currDrawY = (float) i * sizeSquareDraw + pointDraw.y;
			float currDecartY = point.y + (float) i * sizeSquare;

			/*Ilay*/
			float lineCoordY = i * _physicGridScale + _offsetSize.y;
			/*end Ilay*/
			
			canvas.drawLine(0, lineCoordY/*currDrawY*/, WIDTH, lineCoordY/*currDrawY*/, paintText);
//			canvas.drawText(String.format(" %.2f", currDecartY), 0, currDrawY,
//					paintText);
		}

	}

	@Override
	public void onMove(float dx, float dy) {// P0 ÏÅÐÅÄÀÂÀÒÜ
		
		/*Ilay*/
		_offsetSize.x = -_physicGridScale + dx % _physicGridScale;
		_offsetSize.y = -_physicGridScale + dy % _physicGridScale;
		/*end Ilay*/
		
		Log.d("OFFSET", "X: "+_offsetSize.x+"; Y: "+_offsetSize.y);
		
		p0.x +=dx;
		p0.y +=dy;
		
		pointDraw.x += dx;
		pointDraw.y += dy;

		int ix = (int) Math.floor(Math.abs(dx) / sizeSquareDraw);
		int iy = (int) Math.floor(Math.abs(dy) / sizeSquareDraw);

		if (ix == 0)
			ix = 1;
		if (iy == 0)
			iy = 1;

		Log.d("GRID count Dx", "" + ix);
		Log.d("GRID count Dy", "" + iy);

		if (Math.abs(pointDraw.x) > sizeSquareDraw) {

			pointDraw.x -= sizeSquareDraw * (Math.signum(dx)) * ix;
			point.x -= sizeSquare * (Math.signum(dx)) * ix;
		}

		if (Math.abs(pointDraw.y) > sizeSquareDraw) {

			pointDraw.y -= sizeSquareDraw * (Math.signum(dy)) * iy;
			point.y -= sizeSquare * (Math.signum(dy)) * iy;

		}

	}
	
	
	@Override
	public void onMove(Point p0) {
		this.p0 = p0;
		
		float xxx  = (float) (Math.floor(-p0.x/sizeSquare));
		float yyy = (float)   (Math.floor(-p0.y/sizeSquare));
		
		 point.x = (xxx*sizeSquare);
		 point.y = (yyy*sizeSquare);
		 
		 pointDraw.x = p0.x% sizeSquareDraw;
		 pointDraw.y = p0.y%sizeSquareDraw;
		
		
	}

	
	final float MAX_CELL_SIZE = 200f;
	final float MIN_CELL_SIZE = 10f;
	@Override
	public void onScale(float scale) {

		/*Ilay*/
		_physicGridScale *= scale;
		if (_physicGridScale < MIN_CELL_SIZE) {
			_physicGridScale *= 10;
		} else if (_physicGridScale > MAX_CELL_SIZE) {
			_physicGridScale /= 10;
		}
		/*end Ilay*/
		
		if ((sizeSquareDraw <= minSizeSquare) && (sizeSquare < maxScale)) {
			sizeSquareDraw = maxSizeSquare;
			sizeSquare *= 10;
		}

		if ((sizeSquareDraw > maxSizeSquare) && (sizeSquare > minScale)) {
			sizeSquareDraw = minSizeSquare;
			sizeSquare /= 10;
		}
		
//		if ((scale >= 1 & sizeSquare > minScale) || (scale < 1 & sizeSquare <= maxScale)) {
			sizeSquareDraw = sizeSquare * scale;
//		}
	}

	public void sizeChange(float newWidth, float newHeight) {

		WIDTH = newWidth;
		HEIGHT = newHeight;

		if (WIDTH < HEIGHT) {
			minSizeSquare = WIDTH / 20;
			maxSizeSquare = WIDTH / 2f;

		} else {
			minSizeSquare = HEIGHT / 20;
			maxSizeSquare = HEIGHT / 2f;
		}

		sizeSquare = 100;
		sizeSquareDraw = 100;
	}

	@Override
	public int getColor() {
		return paintLine.getColor();
	}

	@Override
	public void setColor(int color) {
		paintLine.setColor(color);
	}

	@Override
	public float getWidthLine() {
		paintLine.getStrokeWidth();
		return 0;
	}

	@Override
	public void setWidthLine(float widthLine) {
		paintLine.setStrokeWidth(widthLine);

	}



}
