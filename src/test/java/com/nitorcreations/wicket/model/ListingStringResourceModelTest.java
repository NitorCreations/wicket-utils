package com.nitorcreations.wicket.model;

import static com.nitorcreations.WicketMatchers.label;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.markup.Markup;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.mock.MockApplication;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

import com.nitorcreations.test.wicket.resources.MockStringResourceLoader;

public class ListingStringResourceModelTest {
    private MockStringResourceLoader stringResourceLoader;
    private WicketTester wicketTester;
    private ListingStringResourceModel<?> model;
    private Label c;

    @Before
    public void setup() {
        stringResourceLoader = new MockStringResourceLoader();
        wicketTester = new WicketTester(new MockApplication() {
            @Override
            protected void init() {
                super.init();
                getResourceSettings().getStringResourceLoaders().clear();
                getResourceSettings().getStringResourceLoaders().add(stringResourceLoader);
            }
        });
        stringResourceLoader.expectStringMessage(Label.class, "message", "value(${value})");
    }

    private void startComponent() {
        c = new Label("component", model);
        wicketTester.startComponentInPage(c);
    }

    @Test
    public void showsLengthForStrings() {
        stringResourceLoader.expectStringMessage(Label.class, "stringLength", "len: ${length}");
        model = new ListingStringResourceModel<String>("stringLength", Model.ofList(Arrays.asList("Ab", "Cde", "Fghi")));
        startComponent();
        assertThat(c, label("len: 2, len: 3, len: 4"));
    }

    @Test
    public void showsDefaultMessageWhenListIsEmpty() {
        model = new ListingStringResourceModel<TestObject>("foo", Model.ofList(new ArrayList<TestObject>()), Model.of("Default message"), null);
        startComponent();
        assertThat(c, label("Default message"));
    }

    @Test
    public void concatenatesStringResourceModels() {
        List<TestObject> list = Arrays.asList(new TestObject(5), new TestObject(4), new TestObject(1));
        model = new ListingStringResourceModel<TestObject>("message", Model.ofList(list));
        startComponent();
        assertThat(c, label("value(5), value(4), value(1)"));
    }

    @Test
    public void usesDifferentComponentIfSupplied() {
        stringResourceLoader.expectStringMessage(WebMarkupContainer.class, "message", "foo = ${value}");
        List<TestObject> list = Arrays.asList(new TestObject(123));
        WebMarkupContainer wmc = new WebMarkupContainer("wmc");
        model = new ListingStringResourceModel<TestObject>("message", Model.ofList(list), Model.of("-"), wmc);
        wmc.add(c = new Label("label", model));
        wicketTester.startComponentInPage(wmc, Markup.of("<div wicket:id=\"wmc\"><span wicket:id=\"label\" /></div>"));
        assertThat(c, label("foo = 123"));
    }

    @Test
    public void separatorChangesSeparator() {
        List<TestObject> list = Arrays.asList(new TestObject(5), new TestObject(4), new TestObject(1));
        model = new ListingStringResourceModel<TestObject>("message", Model.ofList(list));
        model.setSeparator("\n");
        assertThat(model.getSeparator(), is("\n"));
        startComponent();
        assertThat(c, label("value(5)\nvalue(4)\nvalue(1)"));
    }

    static class TestObject implements Serializable {
        private static final long serialVersionUID = -5270433583289888830L;
        private final Integer value;

        public TestObject(final Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }
    }
}
