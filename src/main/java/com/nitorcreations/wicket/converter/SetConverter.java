package com.nitorcreations.wicket.converter;

import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.Ordering;
import org.apache.wicket.util.convert.IConverter;

/**
 * A converter to convert a String to Set and vice versa.
 * Uses the application's converters to further convert the
 * string representations to the elements of the list
 * <p/>
 * Example usage:
 * <pre><code>
 *     new TextField&lt;Set&lt;Integer>>(..) {
 *         public &lt;C> IConverter&lt;C> getConverter(Class&lt;C> type) {
 *              return (IConverter&lt;C>) new SetConverter&lt;Integer>(Integer.class);
 *          }
 *     }.setType(Set.class);
 * </code></pre>
 * <p/>
 *
 * <b>NOTE: </b> Defaults to toString ordering of the elements for display to get
 * deterministic ordering of objects.
 *
 * @param <T> type of the single element in a set
 * @see ListConverter
 * @see #getSortedForDisplay(java.util.Set)
 */
public class SetConverter<T> extends AbstractCollectionConverter<T, Set<T>> {
    private static final long serialVersionUID = 7544350429791459627L;

    /**
     * Constructor.
     * @param type the type of the single element in the set
     */
    public SetConverter(Class<T> type) {
        super(type);
    }

    /**
     * Constructor.
     * @param type the type of the single element in the set
     * @param internalConverter the converter to use for the elements of the set
     */
    public SetConverter(Class<T> type, IConverter<T> internalConverter) {
        super(type, internalConverter);
    }

    @Override
    protected Set<T> createEmptyCollection() {
        return new HashSet<T>();
    }

    @Override
    public SetConverter<T> setSeparatorRegexp(String regexp) {
        super.setSeparatorRegexp(regexp);
        return this;
    }

    @Override
    public SetConverter<T> setJoiningSeparator(String separator) {
        super.setJoiningSeparator(separator);
        return this;
    }

    @Override
    protected Iterable<? extends T> getSortedForDisplay(Set<T> collection) {
        return Ordering.usingToString().sortedCopy(collection);
    }
}
