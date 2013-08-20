package com.nitorcreations.wicket.converter;

import java.util.Locale;
import java.util.Set;

import org.junit.Test;

import com.nitorcreations.test.TestApplication;
import com.nitorcreations.wicket.converter.ListConverterTest.TestObject;
import com.nitorcreations.wicket.converter.ListConverterTest.TestObjectConverter;
import org.apache.wicket.ConverterLocator;
import org.apache.wicket.IConverterLocator;
import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.tester.WicketTester;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class SetConverterTest {

    private WicketTester wicketTester = new WicketTester(new TestApplication());

    @Test
    public void convertsListOfStrings_ThereAndBackAgain() {
        SetConverter<String> converter = new SetConverter<String>(String.class);
        Set<String> strings = converter
                .convertToObject("123345, abcdef;foobar     ,   another", null);
        assertThat(strings, containsInAnyOrder("123345", "abcdef", "foobar", "another"));
        assertThat(converter.convertToString(strings, null), is("123345, abcdef, another, foobar"));
    }

    @Test
    public void customSeparators() {
        SetConverter<String> converter = new SetConverter<String>(String.class)
                .setSeparatorRegexp("A")
                .setJoiningSeparator("   ");
        Set<String> stringList = converter.convertToObject("aAbAcAd", null);
        assertThat(stringList, containsInAnyOrder("a", "b", "c", "d"));
        assertThat(converter.convertToString(stringList, null), is("a   b   c   d"));
    }

    @Test
    public void convertsListOfCustomObjects_implicitConverter() {
        wicketTester = new WicketTester(new TestApplication() {
            @Override
            protected IConverterLocator newConverterLocator() {
                ConverterLocator cl = new ConverterLocator();
                cl.set(TestObject.class, new TestObjectConverter());
                return cl;
            }
        });
        SetConverter<TestObject> converter = new SetConverter<TestObject>(TestObject.class);
        Set<TestObject> testObjectList = converter.convertToObject("Foo, Bar, Baz", null);
        assertThat(testObjectList, containsInAnyOrder(
                new TestObject("Foo"),
                new TestObject("Bar"),
                new TestObject("Baz")
        ));
        assertThat(converter.convertToString(testObjectList, null), is("Bar, Baz, Foo"));
    }

    @Test
    public void convertsListOfCustomObjects_explicitConverter() {
        SetConverter<TestObject> converter = new SetConverter<TestObject>(TestObject.class, new TestObjectConverter());
        Set<TestObject> testObjectList = converter.convertToObject("Foo, Bar, Baz", null);
        assertThat(testObjectList, containsInAnyOrder(
                new TestObject("Foo"),
                new TestObject("Bar"),
                new TestObject("Baz")
        ));
        assertThat(converter.convertToString(testObjectList, null), is("Bar, Baz, Foo"));
    }

    @Test(expected = ConversionException.class)
    public void convertsListOfCustomObjects_noConverter() {
        new SetConverter<TestObject>(TestObject.class).convertToObject("Foo", null);
    }

    @Test
    public void convertsEmptyStringToEmptyList() {
        SetConverter<Integer> converter = new SetConverter<Integer>(Integer.class);
        Set<Integer> integers = converter.convertToObject("", Locale.ENGLISH);
        assertThat(integers, empty());
        assertThat(converter.convertToString(integers, Locale.ENGLISH), is(""));
    }
}
