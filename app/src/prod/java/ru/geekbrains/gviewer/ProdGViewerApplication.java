package ru.geekbrains.gviewer;

public class ProdGViewerApplication extends GViewerApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        //Fabric.with(this, new Crashlytics());
        logUser();
        initStuff();
    }

    private void logUser() {
        // TODO: Use the current user's information
        // You can call any combination of these three methods
       /* Crashlytics.setUserIdentifier("SUSFU");
        Crashlytics.setUserEmail("user@fabric.io");
        Crashlytics.setUserName("BOHICA");*/
    }


}