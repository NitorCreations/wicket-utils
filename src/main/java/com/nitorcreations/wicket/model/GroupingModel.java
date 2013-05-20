package com.nitorcreations.wicket.model;

import java.util.Collection;
import java.util.List;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.IWrapModel;

/**
 * A model that acts similarly as a MultiMap, but with sortable keys and corresponding
 * values.
 * @param <K> the type of the key
 * @param <V> the type of a single entry in the value list
 */
public interface GroupingModel<K,V> extends IWrapModel<Collection<V>> {
    /** Get the model of all keys */
    IModel<List<K>> getKeysModel();
    /** Get the model of values corresponding to a key */
    IModel<List<V>> getValuesModel(IModel<K> key);
}
