package com.nitorcreations.wicket.converter;

import java.util.ArrayList;
import java.util.List;

/**
 * A converter to convert a String to List and vice versa.
 * Uses the application's converters to further convert the
 * string representations to the elements of the list.
 * <p/>
 * Example usage:
 * <pre><code>
 *     new TextField&lt;List&lt;Integer>>(..) {
 *         public &lt;C> IConverter&lt;C> getConverter(Class&lt;C> type) {
 *              return (IConverter&lt;C>) new ListConverter&lt;Integer>(Integer.class);
 *          }
 *     }.setType(List.class);
 * </code></pre>
 *
 * @param <T> type of the single element in a list
 * @see SetConverter
 */
public class ListConverter<T> extends AbstractCollectionConverter<T, List<T>> {

    /**
     * Constructor.
     * @param type the type of the single element in the list
     */
    public ListConverter(Class<T> type) {
        super(type);
    }

    @Override
    protected List<T> createEmptyCollection() {
        return new ArrayList<T>();
    }

    @Override
    public ListConverter<T> setSeparatorRegexp(String regexp) {
        super.setSeparatorRegexp(regexp);
        return this;
    }

    @Override
    public ListConverter<T>  setJoiningSeparator(String separator) {
        super.setJoiningSeparator(separator);
        return this;
    }
}
