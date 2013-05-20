package com.nitorcreations.wicket.converter;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.nitorcreations.junit.runners.NestedRunner;
import org.apache.wicket.util.convert.ConversionException;

import static com.nitorcreations.Matchers.serializable;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(NestedRunner.class)
public class LocalDateConverterTest {

    LocalDateConverter converter;

    private void assertConverted(String str, int year, int month, int day) {
        assertThat(converter.convertToObject(str, null), is(new LocalDate(year, month, day)));
    }

    private void assertNotConverted(final String str) {
        try {
            converter.convertToObject(str, null);
        } catch (ConversionException ce) {
            assertThat(ce.getCause(), notNullValue());
        }
    }

    private void assertString(LocalDate lt, String target) {
        assertThat(converter.convertToString(lt, null), is(target));
    }

    private void assertString(int year, int month, int day, String target) {
        assertString(new LocalDate(year, month, day), target);
    }

    public class DefaultsToShort {
        @Before
        public void create() {
            converter = new LocalDateConverter();
        }

        @Test
        public void isSerializable() {
            assertThat(converter, serializable());
        }

        @Test
        public void conversionFromStringWorks() {
            assertConverted("1.1.2013", 2013, 1, 1);
            assertConverted("13.12.2011", 2011, 12, 13);
            assertConverted("1.1.13", 13, 1, 1);
            assertConverted("13.12.11", 11, 12, 13);

            assertNotConverted("asd");
            assertNotConverted("23.59");
            assertNotConverted("13.1.");
            assertNotConverted("12.13.2011");
            assertNotConverted("11/11/11");
            assertNotConverted("01/01/2011");
        }

        @Test
        public void conversionToStringWorks() {
            assertString(2011, 11, 11, "11.11.2011");
            assertString(2013, 1, 12, "12.1.2013");
            assertString(null, "");
        }
    }

    public class WithCustomFormat {
        @Before
        public void create() {
            converter = new LocalDateConverter("MM/dd/yyyy");
        }

        @Test
        public void isSerializable() {
            assertThat(converter, serializable());
        }

        @Test
        public void patternWorks() {
            assertConverted("12/07/2052", 2052, 12, 7);
        }

        @Test
        public void conversionToStringWorks() {
            assertString(2011, 11, 11, "11/11/2011");
            assertString(2013, 1, 12, "01/12/2013");
            assertString(null, "");
        }
    }
}
