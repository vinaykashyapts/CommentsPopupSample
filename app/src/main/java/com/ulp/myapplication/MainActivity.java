package com.ulp.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private float initialY;
    private int lastAction;
    private PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout postContent = findViewById(R.id.postContent);
        initData(postContent, "Posts ", true);
    }

    public void initPopup(View v) {

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View commentsView = layoutInflater.inflate(R.layout.popup_layout, null, false);

        LinearLayout commentsContent = commentsView.findViewById(R.id.commentsContent);

        initData(commentsContent, "Comments ", false);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        popupWindow = new PopupWindow(commentsView, width, height - 50, true);
        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setAnimationStyle(R.style.PopupAnimation_UpToDown);
        popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 100);

        commentsView.setOnTouchListener((view, motionEvent) -> {

            switch (motionEvent.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:

                    initialY = view.getY() - motionEvent.getRawY();
                    lastAction = MotionEvent.ACTION_DOWN;
                    break;

                case MotionEvent.ACTION_MOVE:

                    view.setY(motionEvent.getRawY() + initialY);
                    lastAction = MotionEvent.ACTION_MOVE;
                    break;

                case MotionEvent.ACTION_UP:

                    if (lastAction != MotionEvent.ACTION_DOWN) {
                        popupWindow.dismiss();
                    }

                    break;

                default:
                    return false;
            }
            return true;
        });
    }

    void initData(LinearLayout view, String data, Boolean isClick) {
        int contentCount = 20;
        for (int i = 0; i < contentCount; i++) {

            TextView tv = new TextView(this);
            tv.setText(data + (i + 1));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(20, 40, 20, 40);
            tv.setLayoutParams(params);

            if(isClick) {
                tv.setOnClickListener(v -> {
                    initPopup(v);
                });
            }

            view.addView(tv);
        }
    }
}