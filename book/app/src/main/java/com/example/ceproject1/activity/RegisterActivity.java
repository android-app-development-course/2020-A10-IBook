package com.example.ceproject1.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ceproject1.R;
import com.example.ceproject1.db.DbHelper;
import com.example.ceproject1.db.UserBean;
import com.example.ceproject1.util.AppContext;

import org.litepal.crud.DataSupport;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.header)
    AppBarLayout header;
    @Bind(R.id.account_input)
    EditText accountInput;
    @Bind(R.id.password_input)
    EditText passwordInput;
    @Bind(R.id.et_code)
    EditText etCode;
    @Bind(R.id.btn_login)
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        AppContext.addActivity(this);
    }

    @OnClick(R.id.btn_login)
    public void onClick() {
        if (TextUtils.isEmpty(accountInput.getText().toString())) {
            Toast.makeText(RegisterActivity.this, "用户名不能为空!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(passwordInput.getText().toString())) {
            Toast.makeText(RegisterActivity.this, "密码不能为空!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(etCode.getText().toString())) {
            Toast.makeText(RegisterActivity.this, "密码不能为空!", Toast.LENGTH_SHORT).show();
            return;
        }

        UserBean userBean = new UserBean();
        userBean.setName(accountInput.getText().toString());
        userBean.setPassword(passwordInput.getText().toString());
        boolean hasUser = DbHelper.getInstance(RegisterActivity.this)
                .saveUser(userBean);
        if (hasUser) {
            Toast.makeText(RegisterActivity.this, "该信息已注册！", Toast.LENGTH_SHORT).show();
        } else {
            SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            if (!TextUtils.isEmpty(sharedPreferences.getString("phone", null))) {
                editor.remove("phone");
            }
            Toast.makeText(RegisterActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
            finish();
        }


    }
}
