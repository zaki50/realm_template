package com.example.realm_template;

import android.os.SystemClock;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import io.realm.Realm;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
@PowerMockIgnore({"org.mockito.*", "org.robolectric.*", "android.*"})
@PrepareForTest({Realm.class})
public class ExampleUnitTest {
    // Robolectric, Using Power Mock https://github.com/robolectric/robolectric/wiki/Using-PowerMock

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    @Test
    public void mockRealm() throws Exception {
        final Realm mockRealm = PowerMockito.mock(Realm.class);
        PowerMockito.when(mockRealm.isAutoRefresh()).thenReturn(true);

        assertThat(mockRealm.isAutoRefresh(), is(true));
    }

    @Test
    public void alsoRobolectricAvailable() throws Exception {
        assertThat(SystemClock.uptimeMillis(), is(equalTo(100L)));
    }
}