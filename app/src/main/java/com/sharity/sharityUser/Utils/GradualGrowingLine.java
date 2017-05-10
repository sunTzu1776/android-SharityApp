package com.sharity.sharityUser.Utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.sharity.sharityUser.R;

import java.util.ArrayList;
import java.util.List;

public class GradualGrowingLine extends View {

	private int x1,y1,x2,y2;
	private List<PointF> listOfPoints;
	private int inte = 0;
	private Paint paint;

	public GradualGrowingLine(Context context, AttributeSet attrs) {
		super(context, attrs);

		listOfPoints = new ArrayList<>();
		paint = new Paint();
		paint.setColor(getResources().getColor(R.color.grey));
		paint.setStrokeCap(Paint.Cap.ROUND);
		paint.setStrokeWidth(5);
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		
		//	starting point
		x1 = 0;
		y1 = 140;
		
		//	ending point
		x2 = getWidth();
		y2 = getHeight();
		
		Log.d("line xy xy", x1 + " : "+y1+" : "+x2 + " : "+y2);
		
		divideLineIntoEqualParts();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		if(inte < listOfPoints.size()){
			canvas.drawLine(listOfPoints.get(0).x, listOfPoints.get(0).y, listOfPoints.get(inte).x,listOfPoints.get(0).y, paint);
			inte++;
			
			if(inte < listOfPoints.size()){
				invalidate();
			}
		}		
	}

	//	dividing line into 50 equal parts
	private void divideLineIntoEqualParts() {
		
		/*
		 * Courtesy : www.dummies.com
		 * (x,y) = (x1 + k(x2 - x1),y1 + k(y2 - y1))
		 * */
		
		listOfPoints.clear();
		for (int k = 1; k <= 50; k++) {
			listOfPoints.add(new PointF(x1 + ((k * (x2 - x1)) / 50),y1 + (k * (y2 - y1)) / 50));			
		}
		
		Log.d("listOfPoints : size : ",listOfPoints.size()+"");
	}
}