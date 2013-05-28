package com.nitorcreations.wicket.validator;

import org.junit.Before;
import org.junit.Test;

import org.apache.wicket.markup.Markup;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;

public class CheckedValidatorTest {

    private static final String markup = "<form wicket:id=\"form\"><input type=\"checkbox\" wicket:id=\"checkbox\" /></form>";

    WicketTester wicketTester;

    FormTester formTester;

    Form<?> form;

    CheckBox component;

    CheckedValidator validator;

    @Before
    public void create() {
        wicketTester = new WicketTester();

        form = new Form<Void>("form");
        component = new CheckBox("checkbox", new Model<Boolean>());
        component.add(new CheckedValidator());
        form.add(component);

        form = wicketTester.startComponentInPage(form, Markup.of(markup));
        formTester = wicketTester.newFormTester("form");
    }

    @Test
    public void validOnTrue() {
        formTester.setValue("checkbox", true);
        formTester.submit();
        wicketTester.assertNoErrorMessage();
    }

    @Test
    public void invalidOnFalse() {
        formTester.setValue("checkbox", false);
        formTester.submit();
        wicketTester.assertErrorMessages("'checkbox' must be selected");
    }

    @Test
    public void invalidOnNull() {
        formTester.submit();
        wicketTester.assertErrorMessages("'checkbox' must be selected");
    }

}
