package com.nitorcreations.wicket.components.form;

import org.junit.BeforeClass;
import org.junit.Test;

import com.nitorcreations.test.TestApplication;
import org.apache.wicket.markup.Markup;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class MaxLengthTextFieldTest {
    public static final String FIELD_ID = "input";

    static WicketTester wicketTester;

    static Markup markup;

    @BeforeClass
    public static void setupWicket() {
        wicketTester = new WicketTester(new TestApplication());
        markup = Markup.of("<form wicket:id=\"form\"><input type=\"text\" wicket:id=\"input\" /></form>");
        TestApplication.expectStringMessage("input.MaximumLengthExceeded", "Field '${label}' exceeded maximum length of ${maxLength}");
    }

    private void start(MaxLengthTextField<String> field) {
        Form<?> form = new Form<Void>("form");
        form.add(field);
        wicketTester.startComponentInPage(form, markup);
    }

    @Test
    public void defaultMaxLength() {
        start(new MaxLengthTextField<String>(FIELD_ID));
        assertThat(wicketTester.getTagByWicketId(FIELD_ID).getAttribute("maxlength"), is("100"));
    }

    @Test
    public void validateOnNullInput() {
        MaxLengthTextField<String> field = new MaxLengthTextField<String>(FIELD_ID, new Model<String>(), String.class);
        assertThat(field.isValid(), is(true));
    }

    @Test
    public void forcedLongerValueDoesNotPass() {
        start(new MaxLengthTextField<String>(FIELD_ID, 3, Model.of("a"), String.class));
        FormTester ft = wicketTester.newFormTester("form");
        ft.setValue("input", "abcd12312312313");
        ft.submit();
        wicketTester.assertErrorMessages("Field 'input' exceeded maximum length of 3");
    }
}

