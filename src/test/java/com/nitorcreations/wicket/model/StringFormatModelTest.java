package com.nitorcreations.wicket.model;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.nitorcreations.junit.runners.NestedRunner;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import static com.nitorcreations.Matchers.serializable;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(NestedRunner.class)
public class StringFormatModelTest {

    StringFormatModel model;

    public class WithStrings {
        @Test
        public void isSerializable() {
            assertThat(new StringFormatModel("String %d", 123), serializable());
        }

        @Test
        public void getObjectReturnsCorrect() {
            model = new StringFormatModel("String %s, %d, %.2f", "Test", 123, 55.42325);
            assertThat(model.getObject(), equalTo("String Test, 123, 55.42"));
        }

        @Test
        public void setObjectChangesFormat() {
            model = new StringFormatModel("String %s, %d, %.2f", "Test", 123, 55.42325);

            model.setObject("This is a test (%s, %04d, %.4f)");
            assertThat(model.getObject(), equalTo("This is a test (Test, 0123, 55.4233)"));
        }
    }

    public class WithModels {
        String format;

        String test;

        @SuppressWarnings("unchecked")
        @Test
        public void isSerializable() {
            final IModel<String> format = new Model<String>("String %d");
            final IModel<Integer> number = new Model<Integer>(123);
            assertThat(new StringFormatModel(format, number), serializable());
        }

        @Test
        public void getObjectIsCorrect() {
            format = "The string was: '%s'";
            test = "Test string";
            @SuppressWarnings("unchecked")
            final StringFormatModel model = new StringFormatModel(new PropertyModel<String>(this, "format"),
                    new PropertyModel<String>(this, "test"));

            assertThat(model.getObject(), equalTo("The string was: 'Test string'"));
            format = "And '%s' this time";
            test = "Foo";
            assertThat(model.getObject(), equalTo("And 'Foo' this time"));
        }
    }
}
