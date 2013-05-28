package com.nitorcreations.wicket.validator;

import java.util.regex.Pattern;

import org.apache.wicket.validation.validator.PatternValidator;

/**
 * Validates a phone number string. The number should contain only numbers 0-9, whitespace or the characters ()-+
 */
public class PhoneNumberValidator extends PatternValidator {

    private static final long serialVersionUID = 1L;

    public PhoneNumberValidator() {
        super(Pattern.compile("[0-9\\+\\-\\(\\)\\s]*"));
    }

}
