package com.example.realm_template;

import android.app.Application;
import android.support.annotation.VisibleForTesting;

import com.example.realm_template.prngfix.PRNGFixes;

import java.util.Deque;
import java.util.LinkedList;

public class MyApplication extends Application {
    private static MyApplication self;
    private static Deque<ApplicationComponent> componentStack;

    public static MyApplication getInstance() {
        return self;
    }

    public static ApplicationComponent getComponent() {
        return componentStack.peek();
    }

    @VisibleForTesting
    public static void pushComponent(ApplicationComponent component) {
        componentStack.push(component);
    }

    @VisibleForTesting
    public static ApplicationComponent popComponent() {
        return componentStack.pop();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        self = this;

        PRNGFixes.apply();

        componentStack = new LinkedList<>();
        componentStack.add(DaggerApplicationComponent.create());
    }

}
