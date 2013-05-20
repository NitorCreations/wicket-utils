package com.nitorcreations.wicket.converter;

import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.lang.Args;

/**
 * Converter to convert String to LocalTime and vice versa.
 */
public class DateTimeConverter implements IConverter<DateTime> {

    private static final long serialVersionUID = 1L;

    public static final String DEFAULT_PATTERN = "d.M.yyyy HH:mm";

    private final String pattern;

    /**
     * Create a new LocalDateConverter for the pattern "d.M.yyyy".
     * 
     * @see org.joda.time.format.DateTimeFormat
     */
    public DateTimeConverter() {
        this(DEFAULT_PATTERN);
    }

    /**
     * Create a new {@link DateTimeConverter} for the given pattern. See {@link org.joda.time.format.DateTimeFormat} for
     * the patterns.
     *
     * @param pattern
     *            the pattern
     * @see org.joda.time.format.DateTimeFormat
     */
    public DateTimeConverter(String pattern) {
        this.pattern = Args.notNull(pattern, "Pattern");
    }

    private DateTimeFormatter getFormatter() {
        return DateTimeFormat.forPattern(pattern);
    }

    @Override
    public DateTime convertToObject(String value, Locale locale) {
        try {
            return DateTime.parse(value, getFormatter());
        } catch (final RuntimeException e) {
            throw new ConversionException(e.getMessage(), e);
        }
    }

    @Override
    public String convertToString(DateTime value, Locale locale) {
        return value == null ? "" : value.toString(getFormatter());
    }

    public String getPattern() {
        return pattern;
    }
}
