package com.example.epure.model;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

public class Grid extends Element {
	public Point point;
	public Point pointDraw;
	
	private float maxSizeSquare;
	private float minSizeSquare;
	Paint paintField;
	Paint paintLine;
	Paint paintCountText;
	Paint paint;
	Rect bounds;

	public float sizeSquare;
	public float sizeSquareDraw;
	float countGrid;
	float maxScale = (float) Math.pow(10, 3);
	float minScale = (float) Math.pow(0.1, 1);

	public Grid(float x, float y) {
		point = new Point(x, y);
		pointDraw = new Point(x, y);
//		init();
	}

	

	public void scaleGrid(float scale) {

		if ((scale >= 1 & sizeSquare > minScale)|| (scale < 1 & sizeSquare <= maxScale)) {
			sizeSquareDraw *= scale;
		}
		if (sizeSquareDraw <= minSizeSquare & sizeSquare < maxScale) {
			sizeSquareDraw = maxSizeSquare;
			sizeSquare *= 10;
		}

		if (sizeSquareDraw > maxSizeSquare & sizeSquare > minScale) {
			sizeSquareDraw = minSizeSquare;
			sizeSquare /= 10;
		}
	}

	public void onSizeChange(int widht, int height) {
		
		if (widht < height) {
			minSizeSquare = widht / 20;
			maxSizeSquare = (float) (widht / 2);

		} else {
			minSizeSquare = height / 20;
			maxSizeSquare = (float) (height / 2);
		}
		countGrid = 5;
		sizeSquare = 100;
		sizeSquareDraw = 100;
	}

	@Override
	public void draw(Canvas canvas) {

		canvas.drawPaint(paintField);

		// // размерность
		// int countHorisontal = (int) (canvas.getHeight() / sizeSquareDraw);
		// for (int i = 0; i <= countHorisontal +5; i++) {
		//
		// float currDrawY = i * sizeSquareDraw + pointDraw.y;
		// float currDecartY = point.y + i * sizeSquare;
		// canvas.drawLine(0, currDrawY, canvas.getWidth(), currDrawY, line);
		//
		// canvas.drawText(String.format(" %.4f", currDecartY), 0, currDrawY,
		// countText);
		// }
		//
		// int costVerticalLine = (int) (canvas.getWidth() / sizeSquareDraw);
		// for (int i = 0; i <= costVerticalLine + 5; i++) {
		// float currX = i * sizeSquareDraw + pointDraw.x;
		// float currDecartX = point.x + i * sizeSquare;
		// canvas.drawLine(currX, 0, currX, canvas.getHeight(), line);
		//

		// paint.getTextBounds("" + currDecartX, 0,
		// ("" + currDecartX).length(), bounds);
		//
		// canvas.drawText(String.format(" %.4f", currDecartX), currX + 7,
		// (float) (bounds.height() * 1.5), countText);
		//
		// }

		pointDraw.x = pointDraw.x % sizeSquareDraw;
		pointDraw.y = pointDraw.y % sizeSquareDraw;

		int countHorisontal = (int) (canvas.getHeight() / sizeSquareDraw) + 1;
		int countVertical = (int) (canvas.getWidth() / sizeSquareDraw) + 1;

		// Log.d("GRIDpointDraw.x", ""+pointDraw.x);
		// Log.d("GRIDpointDraw.y", ""+pointDraw.y);
		// Log.d("GRIDcountHorisontal", ""+countHorisontal);
		// Log.d("GRIDcountVertical", ""+countVertical);
		//
		// Log.d("GRIDpoint.x", ""+point.x);
		// Log.d("GRIDpoint.y/n", ""+point.y);
		for (int i = 0; i <= countHorisontal; i++) {

			float currDrawY = (float) i * sizeSquareDraw + pointDraw.y;
			float currDecartY = point.y + (float) i * sizeSquare;


			canvas.drawLine(0, currDrawY, canvas.getWidth(), currDrawY, paintLine);

			canvas.drawText(String.format(" %.4f", currDecartY), 0, currDrawY,
					paintCountText);
		}

		for (int i = 0; i <= countVertical; i++) {

			float currDrawX = (float) i * sizeSquareDraw + pointDraw.x;
			float currDecartX = point.x + (float) i * sizeSquare;

			canvas.drawLine(currDrawX, 0, currDrawX, canvas.getHeight(), paintLine);

			paint.getTextBounds("" + currDecartX, 0,
					("" + currDecartX).length(), bounds);

			canvas.drawText(String.format(" %.4f", currDecartX), currDrawX + 1,
					(float) (bounds.height() * 1.5), paintCountText);
		}

		Log.d("field", "draw field");

	}

	@Override
	public void onMove(float dx, float dy) {

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

			pointDraw.x -= sizeSquareDraw *(Math.signum(dx)) * ix;
			point.x -= sizeSquare * (Math.signum(dx)) * ix;
		}

		if (Math.abs(pointDraw.y) > sizeSquareDraw) {

			pointDraw.y -= sizeSquareDraw * (Math.signum(dy)) * iy;
			point.y -= sizeSquare * (Math.signum(dy)) * iy;

		}

	}

	@Override
	public void onScale(float scale) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public int getColor() {
		// TODO Auto-generated method stub
		return 0;
	}



	@Override
	public void setColor(int color) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public float getWidthLine() {
		// TODO Auto-generated method stub
		return 0;
	}



	@Override
	public void setWidthLine(float widthLine) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onMove(Point p0) {
		// TODO Auto-generated method stub
		
	}


}
