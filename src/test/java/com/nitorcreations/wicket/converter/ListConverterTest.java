package com.nitorcreations.wicket.converter;

import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.junit.Test;

import com.nitorcreations.test.TestApplication;
import org.apache.wicket.ConverterLocator;
import org.apache.wicket.IConverterLocator;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.tester.WicketTester;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ListConverterTest {

    private WicketTester wicketTester = new WicketTester(new TestApplication() {
        @Override
        protected IConverterLocator newConverterLocator() {
            ConverterLocator cl = new ConverterLocator();
            cl.set(TestObject.class, new TestObjectConverter());
            return cl;
        }
    });

    @Test
    public void convertsListOfStrings_ThereAndBackAgain() {
        ListConverter<String> converter = new ListConverter<String>(String.class);
        List<String> strings = converter
                .convertToObject("123345, abcdef;foobar     ,   another", null);
        assertThat(strings, contains("123345", "abcdef", "foobar", "another"));
        assertThat(converter.convertToString(strings, null), is("123345, abcdef, foobar, another"));
    }

    @Test
    public void customSeparators() {
        ListConverter<String> converter = new ListConverter<String>(String.class)
                .setSeparatorRegexp("A")
                .setJoiningSeparator("   ");
        List<String> stringList = converter.convertToObject("aAbAcAd", null);
        assertThat(stringList, contains("a", "b", "c", "d"));
        assertThat(converter.convertToString(stringList, null), is("a   b   c   d"));
    }

    @Test
    public void convertsListOfCustomObjects() {
        ListConverter<TestObject> converter = new ListConverter<TestObject>(TestObject.class);
        List<TestObject> testObjectList = converter.convertToObject("Foo, Bar, Baz", null);
        assertThat(testObjectList, contains(
                new TestObject("Foo"),
                new TestObject("Bar"),
                new TestObject("Baz")
        ));
        assertThat(converter.convertToString(testObjectList, null), is("Foo, Bar, Baz"));
    }

    private static class TestObject {
        private final String value;

        private TestObject(String value) {this.value = value;}

        private String getValue() {
            return value;
        }

        @Override
        public boolean equals(Object o) {
            return EqualsBuilder.reflectionEquals(this, o);
        }

        @Override
        public int hashCode() {
            return HashCodeBuilder.reflectionHashCode(this);
        }
    }

    private static class TestObjectConverter implements IConverter<TestObject> {

        @Override
        public TestObject convertToObject(String value, Locale locale) {
            return new TestObject(value);
        }

        @Override
        public String convertToString(TestObject testObject, Locale locale) {
            return testObject.getValue();
        }
    }
}
