package com.nitorcreations.wicket.model;

import static ch.lambdaj.Lambda.argument;
import static ch.lambdaj.Lambda.on;
import static com.nitorcreations.Matchers.containsElements;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

import ch.lambdaj.function.argument.Argument;

import com.google.common.collect.Ordering;

public class GroupingMultimapModelTest {
    WicketTester wicketTester = new WicketTester();
    GroupingMultimapModel<Integer, TestObject> model;
    List<TestObject> strings = entries("Foobarbaz", "c", "a", "b", "dac", "dec", "Foobar");

    @Before
    public void setup() {
        IModel<List<TestObject>> stringsModel = new PropertyModel<List<TestObject>>(this, "strings");
        model = new TestObjectGroupingModel(stringsModel);
    }

    @Test
    public void getObjectReturnsInOrder() {
        assertThat(model.getObject(), containsElements(strings));
    }

    private static List<TestObject> entries(final String... strings) {
        List<TestObject> tos = new ArrayList<TestObject>();
        for (String s : strings) {
            tos.add(to(s));
        }
        return tos;
    }

    @Test
    public void getKeysReturnsLenghtsInCorrectOrder() {
        assertThat(model.getKeysModel().getObject(), contains(1, 3, 6, 9));
    }

    @Test
    public void keyModelChangesValueWhenValuesChange() {
        IModel<List<Integer>> keysModel = model.getKeysModel();
        assertThat(keysModel.getObject(), contains(1, 3, 6, 9));
        strings.add(new TestObject("ab"));
        model.detach();
        assertThat(keysModel.getObject(), contains(1, 2, 3, 6, 9));
    }

    @Test
    public void keyModelReturnsEmptyIfAllValuesRemoved() {
        IModel<List<Integer>> keysModel = model.getKeysModel();
        strings.clear();
        model.detach();
        assertThat(keysModel.getObject(), hasSize(0));
    }

    @Test
    public void getValuesForKeyReturnsValuesInCorrectOrder() {
        assertThat(model.getValuesModel(Model.of(1)).getObject(), contains(to("a"), to("b"), to("c")));
        assertThat(model.getValuesModel(Model.of(3)).getObject(), contains(to("dac"), to("dec")));
    }

    @Test
    public void getValuesModelChangesWhenValuesChange() {
        IModel<List<TestObject>> valuesModel = model.getValuesModel(Model.of(1));
        assertThat(valuesModel.getObject(), contains(to("a"), to("b"), to("c")));
        strings.remove(to("a"));
        strings.add(0, to("f"));
        model.detach();
        assertThat(valuesModel.getObject(), contains(to("b"), to("c"), to("f")));
    }

    @Test
    public void getValuesModelReturnsEmptyIfAllValuesRemoves() {
        IModel<List<TestObject>> valuesModel = model.getValuesModel(Model.of(6));
        strings.remove(to("Foobar"));
        model.detach();
        assertThat(valuesModel.getObject(), hasSize(0));
    }

    @Test
    public void setObjectSetsUnderlyingModelObject() {
        IModel<List<Integer>> keysModel = model.getKeysModel();
        IModel<List<TestObject>> valuesModel = model.getValuesModel(Model.of(3));
        model.setObject(Arrays.asList(new TestObject("Foo"), new TestObject("Bar"), new TestObject("Baz")));
        // Without detach to check that the object is updated
        assertThat(keysModel.getObject(), contains(3));
        assertThat(valuesModel.getObject(), contains(to("Bar"), to("Baz"), to("Foo")));
    }

    @Test
    public void valuesModelSetObjectClearAndSetsForASingleKey() {
        IModel<List<TestObject>> valuesModel = model.getValuesModel(Model.of(3));
        valuesModel.setObject(Arrays.asList(to("Bur"), to("Buz")));
        assertThat(model.getObject(), contains(to("a"), to("b"), to("c"), to("Bur"), to("Buz"), to("Foobar"), to("Foobarbaz")));
    }

    private static TestObject to(final String s) {
        return new TestObject(s);
    }

    public static class TestObject implements Comparable<TestObject> {
        private String value;

        public TestObject(final String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public void setValue(final String value) {
            this.value = value;
        }

        public Integer getLength() {
            return value.length();
        }

        @Override
        public int compareTo(final TestObject testObject) {
            return CompareToBuilder.reflectionCompare(this, testObject);
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof TestObject)) {
                return false;
            }
            TestObject that = (TestObject) o;
            if (value != null ? !value.equals(that.value) : that.value != null) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            return value != null ? value.hashCode() : 0;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this).append("value", value).toString();
        }
    }

    private static class TestObjectGroupingModel extends GroupingMultimapModel<Integer, TestObject> {
        private static final long serialVersionUID = -7682320612230664599L;

        public TestObjectGroupingModel(final IModel<List<TestObject>> stringsModel) {
            super(stringsModel);
        }

        @Override
        protected Argument<Integer> getKeyArgument() {
            return argument(on(TestObject.class).getLength());
        }

        @Override
        protected Ordering<Integer> getKeyOrdering() {
            return Ordering.natural();
        }

        @Override
        protected Ordering<TestObject> getValueOrdering() {
            return Ordering.natural();
        }
    }
}
