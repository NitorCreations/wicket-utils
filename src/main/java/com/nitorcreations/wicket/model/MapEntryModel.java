package com.nitorcreations.wicket.model;

import java.util.Map;

import org.apache.wicket.model.IModel;
import org.apache.wicket.util.lang.Args;

public class MapEntryModel<K,V> implements IModel<V> {
    private static final long serialVersionUID = -5278952770811642093L;

    private final IModel<Map<K, V>> map;
    private final IModel<K> key;

    public MapEntryModel(IModel<Map<K, V>> map, IModel<K> key) {
        this.map = Args.notNull(map, "map");
        this.key = Args.notNull(key, "key");
        Args.notNull(map.getObject(), "map.getObject()");
    }

    private Map<K,V> getMap() {
        return Args.notNull(map.getObject(), "map.getObject()");
    }

    @Override
    public V getObject() {
        return getMap().get(key.getObject());
    }

    @Override
    public void setObject(V object) {
        getMap().put(key.getObject(), object);
    }

    @Override
    public void detach() {
        map.detach();
        key.detach();
    }
}
