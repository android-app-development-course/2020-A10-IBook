package com.example.ceproject1.activity;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ceproject1.R;
import com.example.ceproject1.base.BaseActivity;
import com.example.ceproject1.util.AppContext;
import com.example.ceproject1.util.CommonUtil;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created
 */
public class AboutActivity extends BaseActivity {


    @Bind(R.id.bannner)
    ImageView bannner;
    @Bind(R.id.tv_version)
    TextView tvVersion;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @Bind(R.id.app_bar)
    AppBarLayout appBar;
    @Bind(R.id.coord)
    CoordinatorLayout coord;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_about;
    }

    @Override
    protected void initData() {
        AppContext.addActivity(this);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
        toolbarLayout.setTitle(getResources().getString(R.string.app_name));
        tvVersion.setText(String.format("当前版本: %s (Build %s)", CommonUtil.getVersion(this), CommonUtil.getVersionCode(this)));
    }

    @Override
    protected void initListener() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @OnClick({R.id.bannner, R.id.tv_version, R.id.toolbar, R.id.toolbar_layout, R.id.app_bar, R.id.coord})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bannner:
                break;
            case R.id.tv_version:
                break;
            case R.id.toolbar:
                break;
            case R.id.toolbar_layout:
                break;
            case R.id.app_bar:
                break;
            case R.id.coord:
                break;
        }
    }
}
