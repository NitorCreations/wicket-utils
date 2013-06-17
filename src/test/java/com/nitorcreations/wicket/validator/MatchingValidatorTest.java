package com.nitorcreations.wicket.validator;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidationError;
import org.apache.wicket.validation.ValidationError;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;

public class MatchingValidatorTest {
    @Test
    public void noErrorWhenMatched() {
        assertThat(validatorFor(equalTo(2)), not(addsErrorForValue(2)));
    }

    @Test
    public void errorWhenNotMatched() {
        assertThat(validatorFor(not(-1)), addsErrorForValue(-1));
    }

    private static <T> MatchingValidator<T> validatorFor(final Matcher<T> matcher) {
        return new MatchingValidator<T>() {
            private static final long serialVersionUID = 1L;

            @Override
            protected Matcher<T> getMatcher() {
                return matcher;
            }
        };
    }

    private static Matcher<MatchingValidator<Integer>> addsErrorForValue(final Integer value) {
        return new TypeSafeMatcher<MatchingValidator<Integer>>() {
            @Override
            protected boolean matchesSafely(MatchingValidator<Integer> item) {
                TestValidatable validatable = new TestValidatable(value);
                item.validate(validatable);
                return validatable.isErrorAdded();
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("adds error for ").appendValue(value);
            }
        };
    }

    private static class TestValidatable implements IValidatable<Integer> {
        private final Integer value;
        private boolean errorAdded = false;

        public TestValidatable(Integer value) {
            this.value = value;
        }

        @Override
        public Integer getValue() {
            return value;
        }

        public boolean isErrorAdded() {
            return errorAdded;
        }

        @Override
        public void error(IValidationError error) {
            assertThat(((ValidationError) error).getKeys(), contains("MatchingValidator"));
            errorAdded = true;
        }

        @Override
        public boolean isValid() {
            return !errorAdded;
        }

        @Override
        public IModel<Integer> getModel() {
            return Model.of(value);
        }
    }
}
