package com.nitorcreations.wicket.validator;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

/**
 * Validates that the checkbox is checked, i.e., it has the value {@link Boolean#TRUE}. Validation error is as follows:
 * <dl> <dt>Message keys</dt> <dd>CheckedValidator</dd> <dt>Variables</dt> <dd><b>field</b> - the validated field </dl>
 *
 * @author Reko Jokelainen / Nitor Creations
 */
public class CheckedValidator implements IValidator<Boolean> {
    private static final long serialVersionUID = 1L;

    @Override
    public void validate(IValidatable<Boolean> validatable) {
        if (!Boolean.TRUE.equals(validatable.getValue())) {
            validatable.error(new ValidationError(this));
        }
    }
}
