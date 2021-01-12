package com.example.ceproject1.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ceproject1.R;
import com.example.ceproject1.util.AppContext;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SetingActivity extends AppCompatActivity implements View.OnClickListener{

    @Bind(R.id.reBack)
    RelativeLayout reBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.header)
    AppBarLayout header;

    @Bind(R.id.line1)
    LinearLayout line1;

    @Bind(R.id.line3)
    LinearLayout line3;
    @Bind(R.id.exit)
    TextView exit;
    @Bind(R.id.line2)
    LinearLayout line2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seting);
        ButterKnife.bind(this);
        AppContext.addActivity(this);
        addSettingItem(R.id.tv_one, "密码设置");
        addSettingItem(R.id.tv_two, "密码修改");
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppContext.finishActivity();
                SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                if (!TextUtils.isEmpty(sharedPreferences.getString("phone", null))) {
                    editor.remove("phone");
                }
                Toast.makeText(SetingActivity.this, "退出成功", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }
    protected void addSettingItem(int layout_id, String title) {
        RelativeLayout layout = (RelativeLayout) findViewById(layout_id);
        ((TextView) layout.findViewById(R.id.SettingItemTitle)).setText(title);
        layout.setOnClickListener(this);
    }
    @OnClick(R.id.reBack)
    public void onClick() {
        finish();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tv_one:
                //密码设置
                Intent intent=new Intent(SetingActivity.this, PassWordActivity.class);
                intent.putExtra("key","0");
                startActivity(intent);
                break;
            case R.id.tv_two:
                //密码修改
                Intent intents=new Intent(SetingActivity.this, PassWordActivity.class);
                intents.putExtra("key","1");
                startActivity(intents);
                break;
        }
    }
}
