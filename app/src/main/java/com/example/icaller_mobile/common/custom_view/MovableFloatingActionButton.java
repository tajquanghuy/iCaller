package com.example.icaller_mobile.common.custom_view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.icaller_mobile.common.constants.Constants;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class MovableFloatingActionButton extends FloatingActionButton implements View.OnTouchListener {
    private float downRawX, downRawY;
    private float dX, dY;
    private float currentX, currentY;

    public MovableFloatingActionButton(Context context) {
        super(context);
        init();
    }

    public MovableFloatingActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MovableFloatingActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();

        int action = motionEvent.getAction();
        int viewWidth = view.getWidth();
        int viewHeight = view.getHeight();
        View viewParent = (View) view.getParent();
        int parentWidth = viewParent.getWidth();
        int parentHeight = viewParent.getHeight();

        if (action == MotionEvent.ACTION_DOWN) {

            downRawX = motionEvent.getRawX();
            downRawY = motionEvent.getRawY();
            dX = view.getX() - downRawX;
            dY = view.getY() - downRawY;

            return true; // Consumed

        } else if (action == MotionEvent.ACTION_MOVE) {
            float newX = motionEvent.getRawX() + dX;
            newX = Math.max(layoutParams.leftMargin, newX); // Don't allow the FAB past the left hand side of the parent
            newX = Math.min(parentWidth - viewWidth - layoutParams.rightMargin, newX); // Don't allow the FAB past the right hand side of the parent

            float newY = motionEvent.getRawY() + dY;
            newY = Math.max(layoutParams.topMargin, newY); // Don't allow the FAB past the top of the parent
            newY = Math.min(parentHeight - viewHeight - layoutParams.bottomMargin, newY); // Don't allow the FAB past the bottom of the parent


            view.animate()
                    .x(newX)
                    .y(newY)
                    .setDuration(0)
                    .start();
            currentX = newX;
            currentY = newY;
            return true; // Consumed

        } else if (action == MotionEvent.ACTION_UP) {

            float upRawX = motionEvent.getRawX();
            float upRawY = motionEvent.getRawY();

            float upDX = upRawX - downRawX;
            float upDY = upRawY - downRawY;

            view.animate()
                    .x((currentX >= (float) parentHeight / 2) ? (parentWidth - viewWidth - layoutParams.rightMargin) : layoutParams.leftMargin)
                    .y(currentY)
                    .setDuration(0)
                    .start();

            if (Math.abs(upDX) < Constants.CLICK_DRAG_TOLERANCE && Math.abs(upDY) < Constants.CLICK_DRAG_TOLERANCE) { // A click
                return performClick();
            } else { // A drag
                return true; // Consumed
            }

        } else if (action == MotionEvent.ACTION_CANCEL) {
            view.animate()
                    .x((currentX >= (float) parentHeight / 2) ? (parentWidth - viewWidth - layoutParams.rightMargin) : layoutParams.leftMargin)
                    .y(currentY)
                    .setDuration(200)
                    .start();
            return true;
        } else {
            return super.onTouchEvent(motionEvent);
        }

    }
}
