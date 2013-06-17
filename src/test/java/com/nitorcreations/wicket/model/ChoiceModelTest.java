package com.nitorcreations.wicket.model;

import org.junit.Before;
import org.junit.Test;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import static com.nitorcreations.Matchers.serializable;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ChoiceModelTest {

    public static final String TRUE = "TRUE";

    public static final String FALSE = "FALSE";

    private IModel<Boolean> valueModel;
    private ChoiceModel<String> model;

    @Before
    public void setup() {
        valueModel = new Model<Boolean>();
        model = new ChoiceModel<String>(valueModel, Model.of(TRUE), Model.of(FALSE));
    }

    @Test
    public void serializes() {
        assertThat(model, serializable());
    }

    @Test
    public void returnsFalseValueWhenNull() {
        valueModel.setObject(null);
        assertThat(model.getObject(), is(FALSE));
    }

    @Test
    public void returnsFalseValueWhenFalse() {
        valueModel.setObject(false);
        assertThat(model.getObject(), is(FALSE));
    }

    @Test
    public void returnsTrueValueWhenTrue() {
        valueModel.setObject(true);
        assertThat(model.getObject(), is(TRUE));
    }
}

