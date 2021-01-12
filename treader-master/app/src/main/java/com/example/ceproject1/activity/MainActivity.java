package com.example.ceproject1.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsoluteLayout;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ceproject1.R;
import com.example.ceproject1.adapter.ShelfAdapter;
import com.example.ceproject1.animation.ContentScaleAnimation;
import com.example.ceproject1.animation.Rotate3DAnimation;
import com.example.ceproject1.base.BaseActivity;
import com.example.ceproject1.db.BookList;
import com.example.ceproject1.filechooser.FileChooserActivity;
import com.example.ceproject1.util.AppContext;
import com.example.ceproject1.util.Config;
import com.example.ceproject1.util.DisplayUtils;
import com.example.ceproject1.view.DragGridView;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, Animation.AnimationListener {


    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.nav_view)
    NavigationView navigationView;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @Bind(R.id.bookShelf)
    DragGridView bookShelf;
    @Bind(R.id.meune)
    ImageView meune;

    @Bind(R.id.search)
    SearchView search;


    private WindowManager mWindowManager;
    private AbsoluteLayout wmRootView;
    private View rootView;
    private Typeface typeface;

    private List<BookList> bookLists;
    private ShelfAdapter adapter;
    //点击书本的位置
    private int itemPosition;
    private TextView itemTextView;
    //点击书本在屏幕中的x，y坐标
    private int[] location = new int[2];

    private static TextView cover;
    private static ImageView content;
    //书本打开动画缩放比例
    private float scaleTimes;
    //书本打开缩放动画
    private static ContentScaleAnimation contentAnimation;
    private static Rotate3DAnimation coverAnimation;
    //书本打开缩放动画持续时间
    public static final int ANIMATION_DURATION = 800;
    //打开书本的第一个动画是否完成
    private boolean mIsOpen = false;
    //动画加载计数器  0 默认  1一个动画执行完毕   2二个动画执行完毕
    private int animationCount = 0;

    private static Boolean isExit = false;

    private Config config;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        AppContext.addActivity(this);

        hideInput();
        config = Config.getInstance();
        // 删除窗口背景
        getWindow().setBackgroundDrawable(null);
        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        wmRootView = new AbsoluteLayout(this);
        rootView = getWindow().getDecorView();
//        SQLiteDatabase db = Connector.getDatabase();  //初始化数据库
        typeface = config.getTypeface();
        bookLists = DataSupport.findAll(BookList.class);
        adapter = new ShelfAdapter(MainActivity.this, bookLists);
        bookShelf.setAdapter(adapter);
        search.setIconified(false);
        search.setIconifiedByDefault(true);//设置搜索图标是否显示在搜索框内

        search.setImeOptions(2);//设置输入法搜索选项字段，默认是搜索，可以是：下一页、发送、完成等
//        mSearchView.setInputType(1);//设置输入类型
//        mSearchView.setMaxWidth(200);//设置最大宽度
        search.setQueryHint("输入书籍名称搜索");//设置查询提示字符串
//        mSearchView.setSubmitButtonEnabled(true);//设置是否显示搜索框展开时的提交按钮
        //设置SearchView下划线透明
        setUnderLinetransparent(search);
        // 设置搜索文本监听
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {

                Log.e("cc", "=====query=" + query);
                return false;
            }

            //当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    bookLists = DataSupport.findAll(BookList.class);
                } else {
                    bookLists = DataSupport.where("bookname like ?", "%" + newText + "%")
                            .find(BookList.class);
                }

                adapter.updateAll(bookLists);
                Log.e("cc", "=====newText=" + newText);
                return false;
            }
        });
    }

    /**
     * 设置SearchView下划线透明
     **/
    private void setUnderLinetransparent(SearchView searchView) {
        try {
            Class<?> argClass = searchView.getClass();
            // mSearchPlate是SearchView父布局的名字
            Field ownField = argClass.getDeclaredField("mSearchPlate");
            ownField.setAccessible(true);
            View mView = (View) ownField.get(searchView);
            mView.setBackgroundColor(Color.TRANSPARENT);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initListener() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent intent = new Intent(MainActivity.this, FileChooserActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
        meune.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        navigationView.setNavigationItemSelectedListener(this);

        bookShelf.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (bookLists.size() > position) {
                    itemPosition = position;
                    String bookname = bookLists.get(itemPosition).getBookname();

                    adapter.setItemToFirst(itemPosition);
//                bookLists = DataSupport.findAll(BookList.class);
                    final BookList bookList = bookLists.get(itemPosition);
                    bookList.setId(bookLists.get(0).getId());
                    final String path = bookList.getBookpath();
                    File file = new File(path);
                    if (!file.exists()) {
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle(MainActivity.this.getString(R.string.app_name))
                                .setMessage(path + "文件不存在,是否删除该书本？")
                                .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        DataSupport.deleteAll(BookList.class, "bookpath = ?", path);
                                        bookLists = DataSupport.findAll(BookList.class);
                                        adapter.setBookList(bookLists);
                                    }
                                }).setCancelable(true).show();
                        return;
                    }

                    ReadActivity.openBook(bookList, MainActivity.this);

//                    if (!isOpen){
//                        bookLists = DataSupport.findAll(BookList.class);
//                        adapter.notifyDataSetChanged();
//                    }
//                    itemTextView = (TextView) view.findViewById(R.id.tv_name);
//                    //获取item在屏幕中的x，y坐标
//                    itemTextView.getLocationInWindow(location);
//
//                    //初始化dialog
//                    mWindowManager.addView(wmRootView, getDefaultWindowParams());
//                    cover = new TextView(getApplicationContext());
//                    cover.setBackgroundDrawable(getResources().getDrawable(R.mipmap.cover_default_new));
//                    cover.setCompoundDrawablesWithIntrinsicBounds(null,null,null,getResources().getDrawable(R.mipmap.cover_type_txt));
//                    cover.setText(bookname);
//                    cover.setTextColor(getResources().getColor(R.color.read_textColor));
//                    cover.setTypeface(typeface);
//                    int coverPadding = (int) CommonUtil.convertDpToPixel(getApplicationContext(), 10);
//                    cover.setPadding(coverPadding, coverPadding, coverPadding, coverPadding);
//
//                    content = new ImageView(getApplicationContext());
//                    Bitmap contentBitmap = Bitmap.createBitmap(itemTextView.getMeasuredWidth(),itemTextView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
//                    contentBitmap.eraseColor(getResources().getColor(R.color.read_background_paperYellow));
//                    content.setImageBitmap(contentBitmap);
//
//                    AbsoluteLayout.LayoutParams params = new AbsoluteLayout.LayoutParams(
//                            itemTextView.getLayoutParams());
//                    params.x = location[0];
//                    params.y = location[1];
//                    wmRootView.addView(content, params);
//                    wmRootView.addView(cover, params);
//
//                    initAnimation();
//                    if (contentAnimation.getMReverse()) {
//                        contentAnimation.reverse();
//                    }
//                    if (coverAnimation.getMReverse()) {
//                        coverAnimation.reverse();
//                    }
//                    cover.clearAnimation();
//                    cover.startAnimation(coverAnimation);
//                    content.clearAnimation();
//                    content.startAnimation(contentAnimation);
                }
            }
        });
    }

    protected void hideInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        View v = getWindow().peekDecorView();
        if (null != v) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        DragGridView.setIsShowDeleteButton(false);
        bookLists = DataSupport.findAll(BookList.class);
        adapter.setBookList(bookLists);
        closeBookAnimation();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        DragGridView.setIsShowDeleteButton(false);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        DragGridView.setIsShowDeleteButton(false);
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                drawerLayout.closeDrawers();
            } else {
                exitBy2Click();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 在2秒内按下返回键两次才退出
     */
    private void exitBy2Click() {
        // press twice to exit
        Timer tExit;
        if (!isExit) {
            isExit = true; // ready to exit
            if (DragGridView.getShowDeleteButton()) {
                DragGridView.setIsShowDeleteButton(false);
                //要保证是同一个adapter对象,否则在Restart后无法notifyDataSetChanged
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this, this.getResources().getString(R.string.press_twice_to_exit), Toast.LENGTH_SHORT).show();
            }
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // cancel exit
                }
            }, 2000); // 2 seconds cancel exit task

        } else {
            finish();
            // call fragments and end streams and services
            System.exit(0);
        }
    }


    public void closeBookAnimation() {

        if (mIsOpen && wmRootView != null) {
            //因为书本打开后会移动到第一位置，所以要设置新的位置参数
            contentAnimation.setmPivotXValue(bookShelf.getFirstLocation()[0]);
            contentAnimation.setmPivotYValue(bookShelf.getFirstLocation()[1]);
            coverAnimation.setmPivotXValue(bookShelf.getFirstLocation()[0]);
            coverAnimation.setmPivotYValue(bookShelf.getFirstLocation()[1]);

            AbsoluteLayout.LayoutParams params = new AbsoluteLayout.LayoutParams(
                    itemTextView.getLayoutParams());
            params.x = bookShelf.getFirstLocation()[0];
            params.y = bookShelf.getFirstLocation()[1];//firstLocation[1]在滑动的时候回改变,所以要在dispatchDraw的时候获取该位置值
            wmRootView.updateViewLayout(cover, params);
            wmRootView.updateViewLayout(content, params);
            //动画逆向运行
            if (!contentAnimation.getMReverse()) {
                contentAnimation.reverse();
            }
            if (!coverAnimation.getMReverse()) {
                coverAnimation.reverse();
            }
            //清除动画再开始动画
            content.clearAnimation();
            content.startAnimation(contentAnimation);
            cover.clearAnimation();
            cover.startAnimation(coverAnimation);
        }
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        //有两个动画监听会执行两次，所以要判断
        if (!mIsOpen) {
            animationCount++;
            if (animationCount >= 2) {
                mIsOpen = true;
                adapter.setItemToFirst(itemPosition);
//                bookLists = DataSupport.findAll(BookList.class);
                BookList bookList = bookLists.get(itemPosition);
                bookList.setId(bookLists.get(0).getId());
                ReadActivity.openBook(bookList, MainActivity.this);
            }

        } else {
            animationCount--;
            if (animationCount <= 0) {
                mIsOpen = false;
                wmRootView.removeView(cover);
                wmRootView.removeView(content);
                mWindowManager.removeView(wmRootView);
            }
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    //获取dialog属性
    private WindowManager.LayoutParams getDefaultWindowParams() {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                0, 0,
                WindowManager.LayoutParams.TYPE_APPLICATION_PANEL,//windown类型,有层级的大的层级会覆盖在小的层级
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                PixelFormat.RGBA_8888);

        return params;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }else if (id == R.id.action_select_file){
//            Intent intent = new Intent(MainActivity.this, FileChooserActivity.class);
//            startActivity(intent);
//        }

        if (id == R.id.action_select_file) {
            Intent intent = new Intent(MainActivity.this, FileChooserActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_feedback) {
            //系统设置
            Intent intent = new Intent(MainActivity.this, SetingActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_about) {
            //关于
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
        }


        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
