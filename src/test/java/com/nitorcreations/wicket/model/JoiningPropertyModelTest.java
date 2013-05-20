package com.nitorcreations.wicket.model;

import java.util.Arrays;

import org.junit.Test;

import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.ListModel;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class JoiningPropertyModelTest {

    @Test
    public void joinsByAddingComma() {
        JoiningPropertyModel<TestObject> model = new JoiningPropertyModel<TestObject>(Model.ofList(Arrays.asList(
                new TestObject(6),
                new TestObject(1),
                new TestObject(4),
                new TestObject(3),
                new TestObject(2),
                new TestObject(5)
        )), "value");
        assertThat(model.getObject(), equalTo("1, 2, 3, 4, 5, 6"));
    }

    @Test
    public void reordersStringsToNaturalOrdering() {
        JoiningPropertyModel<String> model = new JoiningPropertyModel<String>(Model.ofList(Arrays.asList(
                "Foo", "Bar", "Baz"
        )), "");
        assertThat(model.getObject(), equalTo("Bar, Baz, Foo"));
    }

    @Test
    public void isNullSafe() {
        JoiningPropertyModel<TestObject> model = new JoiningPropertyModel<TestObject>(Model.ofList(Arrays.asList(
                new TestObject(1),
                new TestObject(null),
                null
        )), "value");
        assertThat(model.getObject(), equalTo("1"));
    }

    @Test
    public void isNullListSafe() {
        assertThat(new JoiningPropertyModel<TestObject>(new ListModel<TestObject>(), "value").getObject(),
                nullValue());
    }

    private static class TestObject {
        final Integer value;

        public TestObject(Integer value) {this.value = value;}

        public Integer getValue() {
            return value;
        }
    }
}
