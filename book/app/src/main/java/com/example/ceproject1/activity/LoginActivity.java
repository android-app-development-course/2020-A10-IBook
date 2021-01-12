package com.example.ceproject1.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ceproject1.R;
import com.example.ceproject1.db.DbHelper;
import com.example.ceproject1.db.UserBean;
import com.example.ceproject1.util.AppContext;
import com.example.ceproject1.util.SharedPrefsUtils;

import org.litepal.crud.DataSupport;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.header)
    AppBarLayout header;
    @Bind(R.id.account_input)
    EditText accountInput;
    @Bind(R.id.password_input)
    EditText passwordInput;
    @Bind(R.id.btn_login)
    Button btnLogin;
    @Bind(R.id.regiseter)
    TextView regiseter;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    /**
     * 登录页面
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AppContext.addActivity(this);
        ButterKnife.bind(this);
        sharedPreferences = getSharedPreferences(this.getPackageName(), this.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        if (!TextUtils.isEmpty(sharedPreferences.getString("phone", null))) {
            startActivity(new Intent(LoginActivity.this,MainActivity.class));

        }
        regiseter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });
    }

    @OnClick(R.id.btn_login)
    public void onClick() {
        if (TextUtils.isEmpty(accountInput.getText().toString())) {
            Toast.makeText(LoginActivity.this, "用户名不能为空!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(passwordInput.getText().toString())) {
            Toast.makeText(LoginActivity.this, "密码不能为空!", Toast.LENGTH_SHORT).show();
            return;
        }
        //查询数据
        String[] args = new String[]{accountInput.getText().toString()};
        UserBean userBean = DbHelper.getInstance(this).findUser(args);
        if (TextUtils.isEmpty(userBean.getName()) || TextUtils.isEmpty(userBean.getName())) {
            Toast.makeText(LoginActivity.this, "此用户不存在!", Toast.LENGTH_SHORT).show();
            return;
        } else {
            if (userBean.getPassword().equals(passwordInput.getText().toString())) {
                editor.putString("phone", accountInput.getText().toString());
                editor.commit();
                Toast.makeText(LoginActivity.this, "登陆成功!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(LoginActivity.this, "密码错误!", Toast.LENGTH_SHORT).show();
            }

        }


    }
}
