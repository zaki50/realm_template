package com.example.realm_template;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.TextView;

import com.example.realm_template.model.User;
import com.example.realm_template.util.TestComponentHelper;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmConfiguration;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.core.deps.guava.base.Preconditions.checkNotNull;
import static org.hamcrest.core.Is.is;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    @Rule
    public final ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class, false, false);

    private TestComponentHelper componentHelper = new TestComponentHelper();

    @Before
    public void setUp() {
        Realm.init(InstrumentationRegistry.getTargetContext());
        componentHelper.clear();
    }

    @Test
    public void testDataInList() {

        final RealmConfiguration configuration = new RealmConfiguration.Builder()
                .directory(InstrumentationRegistry.getTargetContext().getFilesDir())
                .name("test.realm")
                .deleteRealmIfMigrationNeeded()
                .schemaVersion(0L)
                .build();

        Realm.deleteRealm(configuration);

        final Realm realm = Realm.getInstance(configuration);
        //noinspection TryFinallyCanBeTryWithResources
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    insertInTransaction(realm, "foo", 25);
                    insertInTransaction(realm, "bar", 0);
                }
            });
        } finally {
            realm.close();
        }

        componentHelper.pushComponent(MyApplication.getInstance(),
                DaggerApplicationComponent.builder().applicationModule(new ApplicationModule() {
                    @Override
                    RealmConfiguration provideRealmConfiguration() {
                        return configuration;
                    }
                }).build());
        try {
            activityRule.launchActivity(new Intent(Intent.ACTION_MAIN));

            onData(withItemName(is("foo"))).check(matches(withItemAge(is(String.format(Locale.getDefault(), "%1$d", 25)))));
        } finally {
            componentHelper.popComponent(MyApplication.getInstance());
        }
    }

    public static Matcher<Object> withItemName(final Matcher<String> itemNameMatcher) {
        checkNotNull(itemNameMatcher);
        return new BoundedMatcher<Object, User>(User.class) {
            @Override
            public boolean matchesSafely(User user) {
                return itemNameMatcher.matches(user.getName());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with item name: ");
                itemNameMatcher.describeTo(description);
            }
        };
    }

    public static Matcher<Object> withItemAge(final Matcher<String> itemAgeMatcher) {
        checkNotNull(itemAgeMatcher);
        return new BoundedMatcher<Object, View>(View.class) {
            @Override
            public boolean matchesSafely(View v) {

                return itemAgeMatcher.matches(((TextView) v.findViewById(android.R.id.text2)).getText());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with item age: ");
                itemAgeMatcher.describeTo(description);
            }
        };
    }

    private static void insertInTransaction(Realm realm, String name, int age) {
        final User user = new User();
        user.setName(name);
        user.setAge(age);

        realm.copyToRealm(user);
    }
}