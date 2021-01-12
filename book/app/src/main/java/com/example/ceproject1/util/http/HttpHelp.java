package com.example.ceproject1.util.http;

import android.content.Context;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;


import org.json.JSONException;


/**
 * Created by seven on 2016/5/21.
 */
public class HttpHelp {


    I_success i_success;
    I_failure i_failure;
    Context context;
    String url;


    public HttpHelp(I_success i_success, I_failure i_failure, Context context, String url) {
        this.i_failure = i_failure;
        this.i_success = i_success;
        this.context = context;
        this.url = url;
    }

    //普通请求 get
    public void getHttp2( ) {

        OkGo.<String>get(url).tag(context).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {

                String s = response.body();
                try {
                    i_success.doSuccess(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }






}
