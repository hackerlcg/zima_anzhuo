package com.beihui.market.helper;


import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.util.LinkedList;

public class ActivityTracker implements Application.ActivityLifecycleCallbacks {

    private static ActivityTracker sInstance;

    private LinkedList<Activity> activityLink;

    private ActivityTracker() {
    }

    public static ActivityTracker getInstance() {
        if (sInstance == null) {
            synchronized (ActivityTracker.class) {
                if (sInstance == null) {
                    sInstance = new ActivityTracker();
                }
            }
        }
        return sInstance;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        if (activityLink == null) {
            activityLink = new LinkedList<>();
        }
        activityLink.add(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        //use this lifecycle callback to maintain activity active link since activity'destroy may not response
        //immediately, producing wrong last activity exception as it id not removed as quickly as we excepted
        int index = activityLink.indexOf(activity);
        for (int i = activityLink.size() - 1; i > index; --i) {
            activityLink.remove(i);
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        if (activityLink != null && activityLink.contains(activity)) {
            activityLink.remove(activity);
        }
    }

    public Activity getLastActivity() {
        if (activityLink != null && activityLink.size() >= 2) {
            return activityLink.get(activityLink.size() - 2);
        }
        return null;
    }

    public void removeTrackImmediately(Activity activity) {
        if (activityLink != null && activityLink.contains(activity)) {
            activityLink.remove(activity);
        }
    }
}
