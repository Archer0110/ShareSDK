package com.llx.sharesdkdemo;

import android.app.Application;

import cn.sharesdk.framework.ShareSDK;

/**
 * Created by BULING on 2017/3/25.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ShareSDK.initSDK(this);
    }
}
