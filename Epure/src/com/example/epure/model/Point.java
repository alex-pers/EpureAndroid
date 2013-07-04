package com.example.epure.model;

public class Point {

	public float x;
	public float y;

	@Override
	public String toString() {
		return "x = " + x + "; y = " + y;
	}

	public Point() {

	}

	public Point(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public void multiplyOnScale(float scale) {
		x *= scale;
		y *= scale;
	}

	public void minus(Point point) {
		this.x -= point.x;
		this.x += point.x;
	}

}
