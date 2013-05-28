package com.nitorcreations.wicket.validator;

import org.hamcrest.Matcher;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

/**
 * Validated that the value of target {@link org.apache.wicket.validation.IValidatable} matches the matcher returned by {@link #getMatcher()}.
 *
 * Needs to be abstract since {@link org.hamcrest.Matcher} does not extend {@link java.io.Serializable}.
 *
 * @param <T>
 *         the type of the validatable's value
 */
public abstract class MatchingValidator<T> implements IValidator<T> {

    private static final long serialVersionUID = 930181690154630166L;

    /**
     * The matcher that the {@link org.apache.wicket.validation.IValidatable}'s
     * value must match. Validation error is raised if the matcher does not
     * match {@link org.apache.wicket.validation.IValidatable#getValue()}.
     */
    protected abstract Matcher<T> getMatcher();

    @Override
    public void validate(IValidatable<T> validatable) {
        final Matcher<T> matcher = getMatcher();
        final T item = validatable.getValue();
        if (!matcher.matches(item)) {
            validatable.error(createValidationError(item));
        }
    }

    /**
     * Override this if you want to, e.g., add new variables to the validation error
     * @param item the {@link org.apache.wicket.validation.IValidatable}'s value
     * @return the error to add to the {@link org.apache.wicket.validation.IValidatable}
     */
    protected ValidationError createValidationError(T item) {
        return new ValidationError(this);
    }
}
