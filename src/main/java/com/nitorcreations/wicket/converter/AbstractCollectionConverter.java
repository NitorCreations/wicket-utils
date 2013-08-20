package com.nitorcreations.wicket.converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

import org.apache.wicket.Application;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.lang.Args;

/**
 * @param <T> type of the single element in a list
 * @param <S> type of the collection
 * @see ListConverter
 */
public abstract class AbstractCollectionConverter<T, S extends Collection<T>> implements IConverter<S> {
    private static final long serialVersionUID = 4711743213938938686L;

    public static final String DEFAULT_SEPARATOR = "[,; ]+";
    public static final String DEFAULT_JOINING_SEPARATOR = ", ";

    private final Class<T> type;
    private final IConverter<T> internalConverter;

    private String separatorRegexp = DEFAULT_SEPARATOR;
    private String joiningSeparator = DEFAULT_JOINING_SEPARATOR;

    protected AbstractCollectionConverter(Class<T> type) {
        this(type, null);
    }

    protected AbstractCollectionConverter(Class<T> type, IConverter<T> internalConverter) {
        this.type = Args.notNull(type, "type");
        this.internalConverter = internalConverter;
    }

    public AbstractCollectionConverter<T, S> setSeparatorRegexp(String regexp) {
        this.separatorRegexp = regexp;
        return this;
    }

    public AbstractCollectionConverter<T, S> setJoiningSeparator(String separator) {
        this.joiningSeparator = separator;
        return this;
    }

    /**
     * Create an empty collection of type S to hold the converted values.
     */
    protected abstract S createEmptyCollection();

    /**
     * Get a sorted copy of the elements for display. Override if you want to change
     * the order of the elements to be deterministic.
     *
     * @param collection the elements
     * @return arbitrarily sorted copy for display string. Defaults to returning the parameter collection.
     */
    protected Iterable<? extends T> getSortedForDisplay(S collection) {
        return collection;
    }

    @Override
    public S convertToObject(String value, Locale locale) {
        final IConverter<T> converter = getInternalConverter();
        final S objects = createEmptyCollection();
        if (StringUtils.isNotBlank(value)) {
            for (String s : value.split(separatorRegexp)) {
                objects.add(converter.convertToObject(s, locale));
            }
        }

        return objects;
    }

    @Override
    public String convertToString(S collection, Locale locale) {
        final IConverter<T> converter = getInternalConverter();
        final List<String> convertedStrings = new ArrayList<String>();
        for (T t : getSortedForDisplay(collection)) {
            convertedStrings.add(converter.convertToString(t, locale));
        }
        return StringUtils.join(convertedStrings, joiningSeparator);
    }

    private IConverter<T> getInternalConverter() {
        return internalConverter != null ?
                internalConverter :
                Application.get().getConverterLocator().getConverter(type);
    }
}
