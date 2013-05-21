package com.nitorcreations.wicket.behaviors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.nitorcreations.junit.runners.NestedRunner;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.tester.WicketTester;

import static com.nitorcreations.WicketMatchers.visibleInHierarchy;
import static com.nitorcreations.wicket.behaviors.VisibilityNullBehavior.hiddenWhenDefaultModelNull;
import static com.nitorcreations.wicket.behaviors.VisibilityNullBehavior.visibleWhenCollectionModelEmpty;
import static com.nitorcreations.wicket.behaviors.VisibilityNullBehavior.visibleWhenCollectionModelNotEmpty;
import static com.nitorcreations.wicket.behaviors.VisibilityNullBehavior.visibleWhenDefaultModelNotNull;
import static com.nitorcreations.wicket.behaviors.VisibilityNullBehavior.visibleWhenNotNull;
import static com.nitorcreations.wicket.behaviors.VisibilityNullBehavior.visibleWhenNull;

import static org.junit.Assert.assertThat;

import static org.hamcrest.Matchers.not;

@RunWith(NestedRunner.class)
public class VisibilityNullBehaviorTest {

    Label component;
    WicketTester wicketTester = new WicketTester();

    public class WithVisibleWhenNull {

        public class OnComponentWithNonNullModel {
            @Before
            public void create() {
                IModel<?> model = Model.of("String");
                component = new Label("foo", new Model<String>("bar"));
                wicketTester.startComponentInPage(component);
                component.add(visibleWhenNull(model));
            }

            @Test
            public void visibleComponentBecomesHidden() {
                component.setVisible(true);
                component.configure();
                assertThat(component, not(visibleInHierarchy()));
            }

            @Test
            public void hiddenComponentStaysHidden() {
                component.setVisible(false);
                component.configure();
                assertThat(component, not(visibleInHierarchy()));
            }

        }

        public class OnComponentWithNullModel {
            @Before
            public void create() {
                Model<?> model = Model.of((String) null);
                component = new Label("foo", new Model<String>(null));
                wicketTester.startComponentInPage(component);
                component.add(visibleWhenNull(model));
            }

            @Test
            public void visibleComponentStaysVisible() {
                component.setVisible(true);
                component.configure();
                assertThat(component, visibleInHierarchy());
            }

            @Test
            public void hiddenComponentBecomesVisible() {
                component.setVisible(false);
                component.configure();
                assertThat(component, visibleInHierarchy());
            }
        }
    }

    public class WithVisibleWhenNotNull {

        public class NotNullOnComponentWithNonNullModel {

            @Before
            public void create() {
                component = new Label("foo", new Model<String>("bar"));
                wicketTester.startComponentInPage(component);
                component.add(visibleWhenDefaultModelNotNull());
            }

            @Test
            public void visibleComponentRemainsVisible() {
                component.setVisible(true);
                component.configure();
                assertThat(component, visibleInHierarchy());
            }

            @Test
            public void hiddenComponentBecomesVisible() {
                component.setVisible(false);
                component.configure();
                assertThat(component, visibleInHierarchy());
            }

        }

        public class NotNullOnComponentWithNullModel {

            @Before
            public void create() {
                component = new Label("foo", new Model<String>(null));
                wicketTester.startComponentInPage(component);
                component.add(visibleWhenDefaultModelNotNull());
            }

            @Test
            public void visibleComponentBecomesHidden() {
                component.setVisible(true);
                component.configure();
                assertThat(component, not(visibleInHierarchy()));
            }

            @Test
            public void hiddenComponentRemainsHidden() {
                component.setVisible(false);
                component.configure();
                assertThat(component, not(visibleInHierarchy()));
            }
        }

        public class OnComponentWithIndependentNonNullModel {

            @Before
            public void create() {
                Model<String> other = new Model<String>("baz");
                component = new Label("foo", new Model<String>("bar"));
                wicketTester.startComponentInPage(component);
                component.add(visibleWhenNotNull(other));
            }

            @Test
            public void visibleComponentRemainsVisible() {
                component.setVisible(true);
                component.configure();
                assertThat(component, visibleInHierarchy());
            }

            @Test
            public void hiddenComponentBecomesVisible() {
                component.setVisible(false);
                component.configure();
                assertThat(component, visibleInHierarchy());
            }

        }

        public class OnComponentWithIndependentNullModel {

            @Before
            public void create() {
                Model<String> other = new Model<String>(null);
                component = new Label("foo", new Model<String>("bar"));
                wicketTester.startComponentInPage(component);
                component.add(visibleWhenNotNull(other));
            }

            @Test
            public void visibleComponentBecomesHidden() {
                component.setVisible(true);
                component.configure();
                assertThat(component, not(visibleInHierarchy()));
            }

            @Test
            public void hiddenComponentRemainsHidden() {
                component.setVisible(false);
                component.configure();
                assertThat(component, not(visibleInHierarchy()));
            }
        }
    }

    public class WithHiddenWhenNull {
        public class HiddenOnComponentWithNonNullModel {

            @Before
            public void create() {
                component = new Label("foo", new Model<String>("bar"));
                wicketTester.startComponentInPage(component);
                component.add(hiddenWhenDefaultModelNull());
            }

            @Test
            public void visibleComponentRemainsVisible() {
                component.setVisible(true);
                component.configure();
                assertThat(component, visibleInHierarchy());
            }

            @Test
            public void hiddenComponentRemainsHidden() {
                component.setVisible(false);
                component.configure();
                assertThat(component, not(visibleInHierarchy()));
            }
        }

        public class HiddenOnComponentWithNullModel {

            @Before
            public void create() {
                component = new Label("foo", new Model<String>(null));
                wicketTester.startComponentInPage(component);
                component.add(hiddenWhenDefaultModelNull());
            }

            @Test
            public void visibleComponentBecomesHidden() {
                component.setVisible(true);
                component.configure();
                assertThat(component, not(visibleInHierarchy()));
            }

            @Test
            public void hiddenComponentRemainsHidden() {
                component.setVisible(false);
                component.configure();
                assertThat(component, not(visibleInHierarchy()));
            }
        }
    }

    public class WithHideOnEmptyListModel {
        public class OnComponentWithIndependentNonNullModel {

            @Before
            public void create() {
                IModel<List<? extends String>> other = Model.ofList(Arrays.asList("baz", "quux"));
                component = new Label("foo", new Model<String>("bar"));
                wicketTester.startComponentInPage(component);
                component.add(visibleWhenCollectionModelNotEmpty(other));
            }

            @Test
            public void visibleComponentRemainsVisible() {
                component.setVisible(true);
                component.configure();
                assertThat(component, visibleInHierarchy());
            }

            @Test
            public void hiddenComponentBecomesVisible() {
                component.setVisible(false);
                component.configure();
                assertThat(component, visibleInHierarchy());
            }

        }

        public class OnComponentWithIndependentNullModel {

            @Before
            public void create() {
                IModel<List<? extends String>> other = Model.ofList(new ArrayList<String>());
                component = new Label("foo", new Model<String>("bar"));
                wicketTester.startComponentInPage(component);
                component.add(visibleWhenCollectionModelNotEmpty(other));
            }

            @Test
            public void visibleComponentBecomesHidden() {
                component.setVisible(true);
                component.configure();
                assertThat(component, not(visibleInHierarchy()));
            }

            @Test
            public void hiddenComponentRemainsHidden() {
                component.setVisible(false);
                component.configure();
                assertThat(component, not(visibleInHierarchy()));
            }
        }
    }

    public class WithShowOnEmptyListModel {
        public class OnComponentWithIndependentNonNullModel {

            @Before
            public void create() {
                IModel<List<? extends String>> other = Model.ofList(Arrays.asList("baz", "quux"));
                component = new Label("foo", new Model<String>("bar"));
                wicketTester.startComponentInPage(component);
                component.add(visibleWhenCollectionModelEmpty(other));
            }

            @Test
            public void visibleComponentBecomesHidden() {
                component.setVisible(true);
                component.configure();
                assertThat(component, not(visibleInHierarchy()));
            }

            @Test
            public void hiddenComponentRemainsHidden() {
                component.setVisible(false);
                component.configure();
                assertThat(component, not(visibleInHierarchy()));
            }

        }

        public class OnComponentWithIndependentNullModel {

            @Before
            public void create() {
                IModel<List<? extends String>> other = Model.ofList(new ArrayList<String>());
                component = new Label("foo", new Model<String>("bar"));
                wicketTester.startComponentInPage(component);
                component.add(visibleWhenCollectionModelEmpty(other));
            }

            @Test
            public void visibleComponentRemainsVisible() {
                component.setVisible(true);
                component.configure();
                assertThat(component, visibleInHierarchy());
            }

            @Test
            public void hiddenComponentBecomesVisible() {
                component.setVisible(false);
                component.configure();
                assertThat(component, visibleInHierarchy());
            }
        }
    }

}
