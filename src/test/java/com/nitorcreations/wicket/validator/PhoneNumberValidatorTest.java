package com.nitorcreations.wicket.validator;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Test;

import org.apache.wicket.markup.Markup;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;

import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

public class PhoneNumberValidatorTest {

    private WicketTester wicketTester;

    private TextField<String> field;

    @Before
    public void setup() {
        wicketTester = new WicketTester();
        Form<?> form = new Form<Void>("form");
        field = new TextField<String>("phone", new Model<String>(), String.class);
        field.add(new PhoneNumberValidator());
        form.add(field);
        wicketTester.startComponentInPage(form,
                Markup.of("<form wicket:id=\"form\"><input type=\"text\" wicket:id=\"phone\" /></form>"));
    }

    @Test
    public void validPhoneNumberIsValid() {
        assertThat("+358 40 1234567", valid());
        assertThat("(09) 505 0505", valid());
        assertThat("045-1234 567", valid());
    }

    @Test
    public void alphabetsNotAllowed() {
        assertThat("+358 40 ABC", not(valid()));
    }

    private Matcher<String> valid() {
        return new TypeSafeMatcher<String>() {
            @Override
            protected boolean matchesSafely(String s) {
                FormTester formTester = wicketTester.newFormTester("form");
                formTester.setValue("phone", s);
                formTester.submit();
                return !field.hasErrorMessage();
            }

            @Override
            public void describeTo(Description description) {
            }
        };
    }
}
