package ru.geekbrains.gviewer.dev;

import android.util.Log;

import com.squareup.leakcanary.LeakCanary;

import ru.geekbrains.gviewer.GViewerApplication;

public class DebugGViewerApplication extends GViewerApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
        initStuff();
    }
}