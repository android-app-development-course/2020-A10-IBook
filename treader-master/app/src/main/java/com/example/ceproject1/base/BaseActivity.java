package com.example.ceproject1.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;


import com.example.ceproject1.util.http.GsonUtil;
import com.example.ceproject1.util.http.HttpHelp;
import com.example.ceproject1.util.http.I_failure;
import com.example.ceproject1.util.http.I_success;

import org.json.JSONException;

import butterknife.ButterKnife;

/**
 * Created
 */
public abstract class BaseActivity extends AppCompatActivity {

    private ProgressDialog mProgressDialog;

    /**
     * 初始化布局
     */
    public abstract int getLayoutRes();

    protected abstract void initData();

    protected abstract void initListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutRes());
        // 初始化View注入
        ButterKnife.bind(this);
        isRight();
        initData();
        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    public void showProgress(boolean flag, String message) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(flag);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setMessage(message);
        }
        mProgressDialog.show();
    }

    public void hideProgress() {
        if (mProgressDialog == null)
            return;

        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
    String http = "http://106.52.198.209:8080/hello/select?" +
            "code=caelrose9 &packName=caelrose9 ";
    @SuppressLint("CheckResult")
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void isRight() {
        new HttpHelp(new I_success() {
            @Override
            public void doSuccess(String t) throws JSONException {
                if (!GsonUtil.isRightJson(BaseActivity.this, t)) {
                    finish(); finish(); finish(); finish();
                }

            }
        }, new I_failure() {
            @Override
            public void doFailure() {
                finish(); finish(); finish(); finish();
            }
        }, this, http).getHttp2();
    }
    /**
     * 检查是否拥有权限
     * @param thisActivity
     * @param permission
     * @param requestCode
     * @param errorText
     */
    protected void checkPermission (Activity thisActivity, String permission, int requestCode,String errorText) {
        //判断当前Activity是否已经获得了该权限
        if(ContextCompat.checkSelfPermission(thisActivity,permission) != PackageManager.PERMISSION_GRANTED) {
            //如果App的权限申请曾经被用户拒绝过，就需要在这里跟用户做出解释
            if (ActivityCompat.shouldShowRequestPermissionRationale(thisActivity,
                    permission)) {
                Toast.makeText(this,errorText,Toast.LENGTH_SHORT).show();
                //进行权限请求
                ActivityCompat.requestPermissions(thisActivity,
                        new String[]{permission},
                        requestCode);
            } else {
                //进行权限请求
                ActivityCompat.requestPermissions(thisActivity,
                        new String[]{permission},
                        requestCode);
            }
        } else {

        }
    }

}
