package com.nitorcreations.wicket.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import org.apache.wicket.core.util.lang.PropertyResolver;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

/**
 * A model that uses {@link org.apache.wicket.core.util.lang.PropertyResolver} to fetch the given property of each
 * of the given collection model's items and join them to a String array.
 * <p/>
 * Uses natural ordering of strings to represent the collection.
 * Example:
 * <code><pre>
 * List&lt;String> list = Arrays.asList("DEF", "A", "BC");
 * add(new Label("label", new JoiningPropertyModel&ltString>(Model.ofList(list), "length")));
 *
 * Would render the label's text as "1, 2, 3"
 * </pre></code>
 *
 * @param <T>
 */
public class JoiningPropertyModel<T> extends AbstractReadOnlyModel<String> {
    private static final long serialVersionUID = -2810734991454424296L;

    private final IModel<? extends Collection<? extends T>> collectionModel;

    private final String property;

    public JoiningPropertyModel(IModel<? extends Collection<? extends T>> collectionModel, String property) {
        this.collectionModel = collectionModel;
        this.property = property;
    }

    @Override
    public String getObject() {
        final Collection<? extends T> collection = this.collectionModel.getObject();
        if (null == collection) {
            return null;
        }
        final List<String> list = new ArrayList<String>();
        for (T object : collection) {
            Object value = PropertyResolver.getValue(property, object);
            if (null != value) {
                list.add(String.valueOf(value));
            }
        }
        Collections.sort(list);
        return StringUtils.join(list, ", ");
    }
}
