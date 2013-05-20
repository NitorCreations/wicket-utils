package com.nitorcreations.test;

import java.util.Locale;

import com.nitorcreations.test.wicket.resources.MockStringResourceLoader;
import org.apache.wicket.Application;
import org.apache.wicket.Component;
import org.apache.wicket.mock.MockApplication;

public class TestApplication extends MockApplication {

    private final MockStringResourceLoader mockStringResourceLoader = new MockStringResourceLoader();

    @Override
    protected void init() {
        super.init();
        getResourceSettings().getStringResourceLoaders().clear();
        getResourceSettings().getStringResourceLoaders().add(mockStringResourceLoader);
    }

    public MockStringResourceLoader getMockStringResourceLoader() {
        return mockStringResourceLoader;
    }

    public static TestApplication get() {
        return (TestApplication) Application.get();
    }

    public static MockStringResourceLoader getStringResourceLoader() {
        return get().getMockStringResourceLoader();
    }

    public static void expectStringMessage(String key, String message) {
        TestApplication.get().mockStringResourceLoader.expectStringMessage(key, message);
    }

    public static void expectStringMessage(String key, Locale locale, String message) {
        TestApplication.get().mockStringResourceLoader.expectStringMessage(locale, key, message);
    }

    public static void expectStringMessage(Class<? extends Component> clazz, String key, String message) {
        TestApplication.get().mockStringResourceLoader.expectStringMessage(clazz, key, message);
    }
}
