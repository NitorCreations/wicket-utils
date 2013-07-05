package com.nitorcreations.wicket.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

import org.apache.wicket.Application;
import org.apache.wicket.util.convert.IConverter;

/**
 * A converter to convert a String to List and vice versa.
 * Uses the application's converters to further convert the
 * string representations to the elements of the list
 *
 * @param <T> type of the single element in a list
 */
public class ListConverter<T> implements IConverter<List<T>> {

    public static final String DEFAULT_SEPARATOR = "[,; ]+";
    public static final String DEFAULT_JOINING_SEPARATOR = ", ";

    private final Class<T> type;

    private String separatorRegexp = DEFAULT_SEPARATOR;
    private String joiningSeparator = DEFAULT_JOINING_SEPARATOR;

    public ListConverter(Class<T> type) {
        this.type = type;
    }

    public ListConverter<T> setSeparatorRegexp(String regexp) {
        this.separatorRegexp = regexp;
        return this;
    }

    public ListConverter<T> setJoiningSeparator(String separator) {
        this.joiningSeparator = separator;
        return this;
    }

    @Override
    public List<T> convertToObject(String value, Locale locale) {
        final IConverter<T> converter = getInternalConverter();
        final List<T> objects = new ArrayList<T>();
        for (String s : value.split(separatorRegexp)) {
            objects.add(converter.convertToObject(s, locale));
        }
        return objects;
    }

    @Override
    public String convertToString(List<T> list, Locale locale) {
        final IConverter<T> converter = getInternalConverter();
        final List<String> convertedStrings = new ArrayList<String>();
        for (T t : list) {
            convertedStrings.add(converter.convertToString(t, locale));
        }
        return StringUtils.join(convertedStrings, joiningSeparator);
    }

    private IConverter<T> getInternalConverter() {
        return Application.get().getConverterLocator().getConverter(type);
    }
}
