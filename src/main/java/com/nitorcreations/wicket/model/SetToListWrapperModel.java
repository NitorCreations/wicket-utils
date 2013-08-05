package com.nitorcreations.wicket.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.IWrapModel;
import org.apache.wicket.util.lang.Args;

/**
 * Wraps a model containing a {@link java.util.Set} to a model with a {@link java.util.List}
 * of the same generic type.
 * <p/>
 * <b>NOTE: </b> {@link #getObject()} default to no sorting in the list and will return the
 * order of the underlying collection. To override this, either use {@link #of(org.apache.wicket.model.IModel)} or
 * override {@link #sortForDisplay(java.util.List)}.
 *
 * @param <T> type of a single element in the set and the list
 */
public class SetToListWrapperModel<T> implements IWrapModel<List<T>> {
    private static final long serialVersionUID = -7296526163056877302L;

    private final IModel<Set<T>> wrappedModel;

    /**
     * @param setModel the wrapped model
     * @see SetToListWrapperModel
     */
    public SetToListWrapperModel(IModel<Set<T>> setModel) {
        this.wrappedModel = Args.notNull(setModel, "Set model");
    }

    /**
     * Creates a SetToListWrapperModel that returns a naturally sorted list with
     * {@link #getObject}
     * @param setModel the wrapped model
     * @param <T> type of a single element in the set and the list
     * @return the model
     */
    public static <T extends Comparable<T>> IModel<List<T>> of(IModel<Set<T>> setModel) {
        return new SetToListWrapperModel<T>(setModel) {
            private static final long serialVersionUID = 7758689218417453747L;

            @Override
            protected void sortForDisplay(List<T> list) {
                Collections.sort(list);
            }
        };
    }

    /**
     * Creates a SetToListWrapperModel that returns a list sorted by the given comparator with
     * {@link #getObject}
     * @param setModel the wrapped model
     * @param comparator the comparator to use
     * @param <T> type of a single element in the set and the list
     * @return the model
     */
    public static <T> IModel<List<T>> of(IModel<Set<T>> setModel, final Comparator<? super T> comparator) {
        Args.notNull(comparator, "Comparator");
        return new SetToListWrapperModel<T>(setModel) {
            private static final long serialVersionUID = 3208007448006238899L;

            @Override
            protected void sortForDisplay(List<T> list) {
                Collections.sort(list, comparator);
            }
        };
    }

    @Override
    public IModel<Set<T>> getWrappedModel() {
        return wrappedModel;
    }

    @Override
    public List<T> getObject() {
        if (wrappedModel.getObject() == null) {
            return null;
        } else {
            final List<T> list = new ArrayList<T>();
            list.addAll(wrappedModel.getObject());
            sortForDisplay(list);
            return list;
        }
    }

    /**
     * Override to provide sorting for the list transformed from the set.
     * Defaults to no action.
     * @param list the list to sort.
     */
    protected void sortForDisplay(List<T> list) {}

    @Override
    public void setObject(List<T> object) {
        if (object == null) {
            wrappedModel.setObject(null);
        } else {
            wrappedModel.setObject(new HashSet<T>(object));
        }
    }

    @Override
    public void detach() {
        wrappedModel.detach();
    }
}
