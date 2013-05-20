package com.nitorcreations.wicket.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.Ordering;
import com.google.common.collect.TreeMultimap;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.lang.Args;

import ch.lambdaj.function.argument.Argument;

/**
 * A model that groups a list of items by a Lambdaj argument specified by {@link #getKeyArgument()}.
 * <p/>
 * The lists will be kept in order by specifying {@link com.google.common.collect.Ordering} for
 * keys and values in {@link #getKeyOrdering()} and {@link #getValueOrdering()}, correspondingly.
 * <p/>
 * <b>WARNING: </b> The used {@link com.google.common.collect.Ordering}s must be in accordance with the
 * {@code equals} and {@code hashCode} of the keys and values.
 * <p/>
 * Example
 * <pre>
 * Team:
 *   name: String
 * Player:
 *   name: String
 *   team: Team
 * </pre>
 * A list of players could be grouped using {@code argument(on(Player.class).getTeam())}:
 * <pre>
 * Team: Dallas Stars
 *   - Player: Jaromir Jagr
 *   - Player: Derek Roy
 * Team: Anaheim Ducks
 *   - Player: Teemu Selänne
 *   - Player: Ryan Getzlaf
 * </pre>
 * @param <K> the type of the key
 * @param <V> the type of a single item in the original list and values list
 */
public abstract class GroupingMultimapModel<K, V> implements GroupingModel<K, V> {
    private static final long serialVersionUID = 5096283540351480281L;

    private final IModel<Collection<V>> model;

    private transient TreeMultimap<K, V> map;

    @SuppressWarnings("unchecked")
    public GroupingMultimapModel(IModel<? extends Collection<? extends V>> model) {
        this.model = (IModel<Collection<V>>) Args.notNull(model, "Model");
    }

    @Override
    public void detach() {
        model.detach();
        map = null;
    }

    /**
     * @return the argument path to group the items by
     */
    protected abstract Argument<K> getKeyArgument();

    /**
     * Get the ordering for the keys specified by {@link #getKeyArgument()}
     *
     * <p><b>Warning:</b> The comparators or comparables used must be <i>consistent
     * with equals</i> as explained by the {@link Comparable} class specification.
     * Otherwise, the resulting multiset will violate the general contract of {@link
     * com.google.common.collect.SetMultimap}, which it is specified in terms of {@link Object#equals}.
     *
     * @return the ordering for the values
     */
    protected abstract Ordering<K> getKeyOrdering();

    /**
     * Get the ordering for the values.
     *
     * <p><b>Warning:</b> The comparators or comparables used must be <i>consistent
     * with equals</i> as explained by the {@link Comparable} class specification.
     * Otherwise, the resulting multiset will violate the general contract of {@link
     * com.google.common.collect.SetMultimap}, which it is specified in terms of {@link Object#equals}.
     *
     * @return the ordering for the values
     */
    protected abstract Ordering<V> getValueOrdering();

    protected TreeMultimap<K, V> getMap() {
        if (map == null) {
            final Argument<K> keyArgument = getKeyArgument();
            map = TreeMultimap.create(getKeyOrdering(), getValueOrdering());
            for (V original : model.getObject()) {
                map.put(keyArgument.evaluate(original), original);
            }
        }
        return map;
    }

    @Override
    public IModel<List<K>> getKeysModel() {
        return new AbstractReadOnlyModel<List<K>>()  {
            private static final long serialVersionUID = 4243834867222027035L;

            @Override
            public List<K> getObject() {
                return new ArrayList<K>(getMap().keySet());
            }
        };
    }

    @Override
    public IModel<List<V>> getValuesModel(final IModel<K> key) {
        return new ValuesModel(key);
    }

    @Override
    public IModel<?> getWrappedModel() {
        return model;
    }

    @Override
    public Collection<V> getObject() {
        return model.getObject();
    }

    @Override
    public void setObject(Collection<V> object) {
        model.getObject().clear();
        model.getObject().addAll(object);
        this.detach();
    }

    private class ValuesModel implements IModel<List<V>> {
        private static final long serialVersionUID = 4243834867222027035L;

        private final IModel<K> key;

        public ValuesModel(IModel<K> key) {
            this.key = key;
        }

        @Override
        public List<V> getObject() {
            return new ArrayList<V>(getMap().get(key.getObject()));
        }

        @Override
        public void setObject(List<V> object) {
            final TreeMultimap<K, V> multimap = getMap();
            final K keyObject = key.getObject();
            multimap.removeAll(keyObject);
            multimap.putAll(keyObject, object);
            GroupingMultimapModel.this.setObject(multimap.values());
        }

        @Override
        public void detach() {
            key.detach();
        }
    }
}
