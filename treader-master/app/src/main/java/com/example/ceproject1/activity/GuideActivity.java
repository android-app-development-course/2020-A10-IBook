package com.example.ceproject1.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.example.ceproject1.R;
import com.example.ceproject1.util.AppContext;

/**
 * Created
 */
public class GuideActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutRes());
        AppContext.addActivity(this);
        initData();
        initListener();
    }

    public int getLayoutRes() {
        return R.layout.activity_guide;
    }

    protected void initData() {
        AppContext.addActivity(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(GuideActivity.this,LoginActivity.class);
                GuideActivity.this.startActivity(intent);
                GuideActivity.this.finish();
            }
        },1000);
    }

    protected void initListener() {

    }

}
