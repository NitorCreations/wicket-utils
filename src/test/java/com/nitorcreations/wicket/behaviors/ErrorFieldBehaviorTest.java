package com.nitorcreations.wicket.behaviors;

import org.junit.Before;
import org.junit.Test;

import org.apache.wicket.markup.Markup;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;

import static com.nitorcreations.WicketMatchers.hasError;
import static com.nitorcreations.WicketMatchers.hasTag;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

public class ErrorFieldBehaviorTest {

    private WicketTester wicketTester;
    private FormTester formTester;

    private TextField<String> field;

    @Before
    public void setup() {
        wicketTester = new WicketTester();

        Form<?> form = new Form<Void>("form");
        field = new TextField<String>("field", new Model<String>(), String.class);
        form.add(field.setRequired(true).setOutputMarkupId(true));

        field.add(ErrorFieldBehavior.instance());

        wicketTester.startComponentInPage(form, Markup.of(
                "<html><head><wicket:head /></head><body><form wicket:id=\"form\"><input type=\"text\" wicket:id=\"field\" class=\"foo\" /></form></body></html>"));
        formTester = createFormTester();
    }

    @Test
    public void hasClassWhenOnlyWhenErrorFeedbackExists() {
        formTester.setValue(field, "Foobar");
        assertThat(field, not(hasError()));
        assertThat(field, hasTag(wicketTester).with("class", "foo"));

        formTester = createFormTester();
        formTester.setValue(field, "");
        formTester.submit();
        assertThat(field, hasError());
        assertThat(field, hasTag(wicketTester).with("class", "foo error"));
    }

    private FormTester createFormTester() {return wicketTester.newFormTester("form");}
}
