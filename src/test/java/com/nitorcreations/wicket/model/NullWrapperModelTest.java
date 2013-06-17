package com.nitorcreations.wicket.model;

import static com.nitorcreations.Matchers.serializable;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.junit.Test;

@SuppressWarnings("unused")
public class NullWrapperModelTest {
    public static final Model<String> STRING_MODEL = Model.of("String");
    public static final Model<String> NULL_MODEL = new Model<String>(null);
    private NullWrapperModel<String> model;

    @Test
    public void isSerializable() {
        model = new NullWrapperModel<String>(NULL_MODEL, STRING_MODEL);
        assertThat(model, serializable());
    }

    @Test
    public void returnsTargetIfNotNull() {
        model = new NullWrapperModel<String>(Model.of("Bar"), STRING_MODEL);
        assertThat(model.getObject(), is("Bar"));
    }

    @Test
    public void returnsAuxiliaryIfNull() {
        model = new NullWrapperModel<String>(NULL_MODEL, "Test for null");
        assertThat(model.getObject(), is("Test for null"));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void detachesBothModels() {
        IModel<Integer> model1 = mock(IModel.class);
        IModel<Integer> model2 = mock(IModel.class);
        new NullWrapperModel<Integer>(model1, model2).detach();
        verify(model1).detach();
        verify(model2).detach();
    }

    @Test(expected = IllegalArgumentException.class)
    public void auxiliaryModelShouldNotBeNull() {
        new NullWrapperModel<String>(STRING_MODEL, (String) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void targetModelShouldNotBeNull() {
        new NullWrapperModel<String>(null, "String");
    }
}
