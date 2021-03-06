package com.example.tangyangkai.testdemo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 用来测试登录的界面中输入号码的动画，提示逐渐放大的效果
 */
public class ScaleAnimatorActivityNew extends Activity implements View.OnClickListener{
    Button button;
    Button loginButton;
    EditText editText;
    TextView login_toast_phone;
    LinearLayout phoneToastLayout;

    public int loginBtnWidth;
    public int loginBtnHeight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scale_animator_new);

        button = (Button) findViewById(R.id.btn);
        loginButton = (Button) findViewById(R.id.loginBtn);
        editText = (EditText) findViewById(R.id.editText);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String content  = s.toString();
                toggle(content);
            }
        });
        login_toast_phone = (TextView) findViewById(R.id.login_toast_phone);
        phoneToastLayout = (LinearLayout) findViewById(R.id.phoneToastLayout);

        button.setOnClickListener(this);
        loginButton.post(new Runnable() {
            @Override
            public void run() {
                loginBtnWidth = loginButton.getWidth();
                loginBtnHeight = loginButton.getHeight();
            }
        });
    }

    private void toggle(String content) {
        if (TextUtils.isEmpty(content)) {
            phoneToastCollapse();
//            createDropAnimator(phoneToastLayout, PixelUtils.dip2px(getApplicationContext(), 60), 0);
        } else {
            if (login_toast_phone.getVisibility() == View.GONE) {
                phoneToastExpand(content, true);
//                createDropAnimator(phoneToastLayout, 0, PixelUtils.dip2px(getApplicationContext(), 60));
            } else {
                phoneToastExpand(content, false);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn) {
            if (login_toast_phone.getVisibility() == View.VISIBLE) {
                phoneToastCollapse();
//                createDropAnimator(phoneToastLayout, PixelUtils.dip2px(getApplicationContext(), 60), 0);
            }

            if (!TextUtils.isEmpty(editText.getText().toString())) {
                editText.setText(null);
            }
        }
    }

    private void phoneToastExpand(String content, boolean isNeedExpand) {
        if (!isNeedExpand) {
            login_toast_phone.setText(content);
            return;
        }

        int pivotX = editText.getMeasuredWidth() / 2;
        int pivotY = 0;
        login_toast_phone.setPivotX(pivotX);
        login_toast_phone.setPivotY(pivotY);
        login_toast_phone.setText(content);
        login_toast_phone.setVisibility(View.VISIBLE);

        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", 0f, 1f);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", 0f, 1f);
        PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat("alpha", 0.2f, 1f);
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(login_toast_phone, scaleX, scaleY, alpha);
        objectAnimator.setDuration(300);
        objectAnimator.setInterpolator(new AccelerateInterpolator());
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                login_toast_phone.setVisibility(View.VISIBLE);
            }
        });
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                /**
                 * TODO!!!特别注意一点就是这里的login_toast_phone.getScaleY()的变化范围为0~1之间
                 */
                float scaleYFactor = (float)animation.getAnimatedValue("scaleY");
                ViewGroup.LayoutParams layoutParams = phoneToastLayout.getLayoutParams();
                layoutParams.height = (int)(scaleYFactor * PixelUtils.dip2px(getApplicationContext(), 60));
                phoneToastLayout.setLayoutParams(layoutParams);
            }
        });
        objectAnimator.start();
    }

    private void phoneToastCollapse() {
        int pivotX = editText.getMeasuredWidth() / 2;
        int pivotY = 0;
        login_toast_phone.setPivotX(pivotX);
        login_toast_phone.setPivotY(pivotY);

        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", 1f, 0f);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", 1f, 0f);
        PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat("alpha", 1f, 0.2f);
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(login_toast_phone, scaleX, scaleY, alpha);
        objectAnimator.setDuration(300);
        objectAnimator.setInterpolator(new AccelerateInterpolator());
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                login_toast_phone.setVisibility(View.GONE);
                login_toast_phone.setText(null);
            }
        });
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                /**
                 * TODO!!!要注意一点就是这里如果使用login_toast_phone.getScaleY()来做的话，会导致最终的大小会小一点，
                 * 原因其实很简单因为getScaleY()会滞后一点。
                 */
                /*ViewGroup.LayoutParams layoutParams = phoneToastLayout.getLayoutParams();
                layoutParams.height = (int)(login_toast_phone.getScaleY() * PixelUtils.dip2px(getApplicationContext(), 60));
                phoneToastLayout.setLayoutParams(layoutParams);*/
                float scaleYFactor = (float)animation.getAnimatedValue("scaleY");
                ViewGroup.LayoutParams layoutParams = phoneToastLayout.getLayoutParams();
                layoutParams.height = (int)(scaleYFactor * PixelUtils.dip2px(getApplicationContext(), 60));
                Log.e("xxxxxxxxxx", "------------------------------------>height:" + layoutParams.height);
                phoneToastLayout.setLayoutParams(layoutParams);
            }
        });
        objectAnimator.start();
    }

}

/**
 * TODO!!!这里还要注意一点就是当TextView中，文字显示不能居中，有可能的情况就是，TextView设置了大小，然后这个大小跟字体大小相比
 * TODO!!!要小一些，会导致不能居中的情况显示出来
 */
