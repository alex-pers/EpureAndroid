package com.example.epure.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

import com.example.epure.R;
import com.example.epure.model.Field;
import com.example.epure.view.FieldForDraw;

public class MainActivity extends Activity {
	Field drawField;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
	}

	void init() {
			drawField = (Field)findViewById(R.id.draw_field);

	}


}
