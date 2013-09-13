package com.nitorcreations.wicket.components.form;

import org.junit.Test;

import com.nitorcreations.test.TestApplication;
import com.nitorcreations.wicket.components.form.AjaxElementChoice.ChoiceItem;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.tester.WicketTester;

import static com.nitorcreations.WicketMatchers.hasModelObject;
import static com.nitorcreations.WicketMatchers.hasTag;
import static com.nitorcreations.test.wicket.Selection.select;
import static com.nitorcreations.wicket.components.form.AjaxElementChoice.CLASS_ATTRIBUTE;
import static com.nitorcreations.wicket.components.form.AjaxElementChoice.ITEM_CLASS;
import static com.nitorcreations.wicket.components.form.AjaxElementChoice.PARENT_CLASS;
import static com.nitorcreations.wicket.components.form.AjaxElementChoice.SELECTED_CLASS;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class AjaxElementChoiceTest {

    private WicketTester wicketTester = new WicketTester(new TestApplication());

    private TestPage page;

    private IModel<Choice> model = new Model<Choice>();

    private void start(Choice choice, boolean required) {
        model.setObject(choice);
        page = new TestPage(model, required);
        wicketTester.startPage(page);
    }

    @Test
    public void requiredValidated() {
        start(null, true);
        submitForm();
        wicketTester.assertErrorMessages("choice.Required");
        clickChoiceItem(Choice.FIRST);
        submitForm();
        wicketTester.assertNoErrorMessage();
    }

    @Test
    public void changesSelectionOnClickAndModelValueOnSubmit() {
        start(null, false);
        assertThat(model.getObject(), nullValue());

        clickChoiceItem(Choice.FIRST);
        assertThat(model.getObject(), nullValue());
        submitForm();
        assertThat(model.getObject(), is(Choice.FIRST));

        clickChoiceItem(Choice.SECOND);
        assertThat(model.getObject(), is(Choice.FIRST));
        submitForm();
        assertThat(model.getObject(), is(Choice.SECOND));
    }

    @Test
    public void cssClassesAdded_startedWithCorrectSelection() {
        start(Choice.SECOND, false);
        assertThat(select(AjaxElementChoice.class).firstFrom(page), hasTag(wicketTester).with(CLASS_ATTRIBUTE, PARENT_CLASS));

        assertThat(choiceItem(Choice.FIRST), hasTag(wicketTester).with(CLASS_ATTRIBUTE, ITEM_CLASS));
        assertThat(choiceItem(Choice.SECOND), hasTag(wicketTester).with(CLASS_ATTRIBUTE, allOf(
                containsString(SELECTED_CLASS), containsString(ITEM_CLASS)
        )));

        clickChoiceItem(Choice.FIRST);
        assertThat(choiceItem(Choice.FIRST), hasTag(wicketTester).with(CLASS_ATTRIBUTE, allOf(
                containsString(SELECTED_CLASS), containsString(ITEM_CLASS)
        )));
        assertThat(choiceItem(Choice.SECOND), hasTag(wicketTester).with(CLASS_ATTRIBUTE, ITEM_CLASS));

    }

    private void clickChoiceItem(Choice choice) {
        wicketTester.executeAjaxEvent(choiceItem(choice), "onClick");
        wicketTester.assertComponentOnAjaxResponse("form:choice");
    }

    private ChoiceItem choiceItem(Choice choice) {
        return select(ChoiceItem.class).that(hasModelObject(choice)).firstFrom(page);
    }

    private void submitForm() {
        wicketTester.newFormTester("form").submit();
    }

    public enum Choice {
        FIRST,
        SECOND
    }

    public class TestPage extends WebPage {

        private static final long serialVersionUID = -7303890976885603732L;

        public TestPage(IModel<Choice> model, boolean required) {
            Form<Void> form = new Form<Void>("form");
            add(form);
            final AjaxElementChoice<Choice> choice = new AjaxElementChoice<Choice>("choice", model);
            form.add(choice);
            choice.setRequired(required);
            choice.add(new ChoiceItem<Choice>("first", Model.of(Choice.FIRST))
                    .add(new Label("name", Model.of("I primi"))));
            choice.add(new ChoiceItem<Choice>("second", Model.of(Choice.SECOND)).add(new Label("name", Model.of("I secondi"))));
        }
    }
}
