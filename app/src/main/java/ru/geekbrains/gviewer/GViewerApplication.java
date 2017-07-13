package ru.geekbrains.gviewer;

import android.app.Application;

import io.realm.Realm;

public class GViewerApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    protected void initStuff() {
        Realm.init(this);
    }
}