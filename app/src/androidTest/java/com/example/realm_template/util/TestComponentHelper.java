package com.example.realm_template.util;

import com.example.realm_template.ApplicationComponent;
import com.example.realm_template.MyApplication;

import java.util.Deque;
import java.util.LinkedList;

public class TestComponentHelper {
    private final Deque<ApplicationComponent> componentStack = new LinkedList<>();

    public void pushComponent(MyApplication app, ApplicationComponent component) {
        componentStack.push(app.component);
        app.component = component;
    }

    public void popComponent(MyApplication app) {
        app.component = componentStack.pop();
    }

    public void clear() {
        componentStack.clear();
    }
}
