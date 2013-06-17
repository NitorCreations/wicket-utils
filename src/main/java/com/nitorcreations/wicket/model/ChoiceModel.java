package com.nitorcreations.wicket.model;

import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.lang.Args;

/**
 * A model that returns different values based on the given choice model.
 * Will return:
 * - the value of {@code trueValue} if {@code choice#getObject()} equals {@link Boolean#TRUE}
 * - the value of {@code falseValue} if {@code choice#getObject()} is {@code null} or {@code false}
 * @param <T>
 */
public class ChoiceModel<T> extends AbstractReadOnlyModel<T> {

    private static final long serialVersionUID = -4969828382989081892L;

    private final IModel<Boolean> choice;
    private final IModel<T> trueValue;
    private final IModel<T> falseValue;

    /**
     * Create a new choice model.
     * @param choice the choice model
     * @param trueValue the value to return if choice is true
     * @param falseValue the value to return if choice is false or null
     */
    public ChoiceModel(IModel<Boolean> choice, IModel<T> trueValue, IModel<T> falseValue) {
        this.choice = Args.notNull(choice, "Choice");
        this.trueValue = Args.notNull(trueValue, "True value");
        this.falseValue = Args.notNull(falseValue, "False value");
    }

    @Override
    public T getObject() {
        if (Boolean.TRUE.equals(choice.getObject())) {
            return trueValue.getObject();
        } else {
            return falseValue.getObject();
        }
    }
}
