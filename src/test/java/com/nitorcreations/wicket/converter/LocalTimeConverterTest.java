package com.nitorcreations.wicket.converter;

import static com.nitorcreations.Matchers.serializable;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.apache.wicket.util.convert.ConversionException;
import org.joda.time.LocalTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.nitorcreations.junit.runners.NestedRunner;

@RunWith(NestedRunner.class)
public class LocalTimeConverterTest {
    LocalTimeConverter converter;

    void assertConverted(String str, int hour, int minute) {
        assertThat(converter.convertToObject(str, null), is(new LocalTime(hour, minute)));
    }

    void assertNotConverted(final String str) {
        try {
            converter.convertToObject(str, null);
            fail("Did not raise ConversionException");
        } catch (ConversionException ce) {
            // Check that the exception contains stack trace
            assertThat(ce.getCause(), notNullValue());
        }
    }

    void assertString(LocalTime lt, String target) {
        assertThat(converter.convertToString(lt, null), is(target));
    }

    void assertString(int hour, int min, String target) {
        assertString(new LocalTime(hour, min), target);
    }

    public class DefaultsTo24HourFormat {
        @Before
        public void setup() {
            converter = new LocalTimeConverter();
        }

        @Test
        public void isSerializable() {
            assertThat(converter, serializable());
        }

        @Test
        public void conversionFromStringWorks() {
            assertConverted("8:05", 8, 5); // AM
            assertConverted("08:05", 8, 5); // AM
            assertConverted("15:09", 15, 9); // PM
            assertConverted("00:00", 0, 0);
            assertConverted("23:59", 23, 59);
            assertNotConverted("asd");
            assertNotConverted("23.59");
            assertNotConverted("23:59.59");
            assertNotConverted("23:59:59");
        }

        @Test
        public void conversionToStringWorks() {
            assertString(0, 0, "00:00");
            assertString(23, 59, "23:59");
            assertString(11, 32, "11:32");
            assertString(null, "");
        }
    }

    public class WithCustomPattern {
        @Before
        public void setup() {
            converter = new LocalTimeConverter("hh:mm a");
        }

        @Test
        public void conversionFromStringWorks() {
            assertConverted("8:05 AM", 8, 5); // AM
            assertConverted("3:09 PM", 15, 9); // PM
            assertConverted("12:00 AM", 0, 0);
            assertConverted("12:00 PM", 12, 0);
            assertConverted("11:59 PM", 23, 59);
            assertNotConverted("8:05");
            assertNotConverted("13:01");
        }

        @Test
        public void conversionToStringWorks() {
            assertString(0, 0, "12:00 AM");
            assertString(23, 59, "11:59 PM");
            assertString(11, 32, "11:32 AM");
            assertString(null, "");
        }
    }
}
