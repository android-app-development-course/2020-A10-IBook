package com.example.ceproject1.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ceproject1.R;
import com.example.ceproject1.util.AppContext;
import com.example.ceproject1.util.SharedPrefsUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PassWordActivity extends AppCompatActivity {

    @Bind(R.id.reBack)
    RelativeLayout reBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.header)
    AppBarLayout header;
    @Bind(R.id.password_inputs)
    EditText passwordInputs;
    @Bind(R.id.password_inputss)
    EditText passwordInputss;
    @Bind(R.id.btn_login)
    Button btnLogin;
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_word);
        ButterKnife.bind(this);
        AppContext.addActivity(this);
        Intent intent = getIntent();
        key = getIntent().getStringExtra("key");
        if (key.equals("0")) {
            tvTitle.setText("密码设置");
        }else {
            tvTitle.setText("密码修改");
        }
    }


    @OnClick({R.id.reBack, R.id.btn_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.reBack:
                finish();
                break;
            case R.id.btn_login:
                if (TextUtils.isEmpty(passwordInputs.getText().toString())) {
                    Toast.makeText(PassWordActivity.this, "密码不能为空!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(passwordInputss.getText().toString())) {
                    Toast.makeText(PassWordActivity.this, "密码不能为空!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!passwordInputs.getText().toString().equals(passwordInputss.getText().toString())) {
                    Toast.makeText(PassWordActivity.this, "两次密码不一致!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (key.equals("0")) {
                    //密码设置
                    if (!TextUtils.isEmpty(SharedPrefsUtils.getString(PassWordActivity.this,"pass"))){
                        SharedPrefsUtils.remove(PassWordActivity.this);
                    }
                    SharedPrefsUtils.putString(PassWordActivity.this,"pass",passwordInputss.getText().toString());
                    Toast.makeText(PassWordActivity.this, "密码设置成功 !", Toast.LENGTH_SHORT).show();

                }else {
                    if (!TextUtils.isEmpty(SharedPrefsUtils.getString(PassWordActivity.this,"pass"))){
                        SharedPrefsUtils.remove(PassWordActivity.this);
                    }
                    SharedPrefsUtils.putString(PassWordActivity.this,"pass",passwordInputss.getText().toString());
                    Toast.makeText(PassWordActivity.this, "密码修改成功 !", Toast.LENGTH_SHORT).show();

                }
                break;
        }
    }
}
