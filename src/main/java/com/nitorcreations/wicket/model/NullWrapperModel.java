package com.nitorcreations.wicket.model;

import java.io.Serializable;

import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.lang.Args;

/**
 * Returns the model object of the wrapped {@link org.apache.wicket.model.IModel}, if it is not {@code null}. Otherwise, returns the auxiliary
 * value.
 *
 * @param <T>
 */
public class NullWrapperModel<T extends Serializable> extends AbstractReadOnlyModel<T> {
    private static final long serialVersionUID = -2722807116471271339L;

    private final IModel<? extends T> target;

    private final IModel<? extends T> returnIfNull;

    /**
     * Create a model that returns {@code target}'s model object if not null
     * and {@code returnIfNull} otherwise.
     *
     * @param target the value to return if it is not {@code null}
     * @param returnIfNull the value to return if {@code target} is null
     */
    public NullWrapperModel(IModel<T> target, T returnIfNull) {
        this(target, Model.of(Args.notNull(returnIfNull, "returnIfNull")));
    }

    /**
     * Create a model that returns {@code target}'s model object if not null
     * and {@code returnIfNull} otherwise.
     *
     * @param target the value to return if it is not {@code null}
     * @param returnIfNull the value to return if {@code target} is null
     */
    public NullWrapperModel(IModel<T> target, IModel<T> returnIfNull) {
        this.target = Args.notNull(target, "target model");
        this.returnIfNull = Args.notNull(returnIfNull, "returnIfNull");
    }

    @Override
    public T getObject() {
        if (target.getObject() == null) {
            return returnIfNull.getObject();
        }
        return target.getObject();
    }

    @Override
    public void detach() {
        this.target.detach();
        this.returnIfNull.detach();
    }
}
