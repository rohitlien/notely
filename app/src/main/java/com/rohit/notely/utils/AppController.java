package com.rohit.notely.utils;

import android.app.Application;

import com.rohit.notely.database.RealmHelper;

import io.realm.Realm;

/**
 * Created by oust on 4/22/18.
 */

public class AppController extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmHelper.setDefaultConfig();
    }
}
