package com.nitorcreations.wicket.model;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.collections.Sets;

import com.google.common.collect.Ordering;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.util.SetModel;
import org.apache.wicket.util.tester.WicketTester;

import static com.nitorcreations.Matchers.serializable;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

public class SetToListWrapperModelTest {

    private IModel<Set<String>> setModel;
    private IModel<List<String>> model;

    @Before
    public void setup() {
        new WicketTester();
        setModel = new TestSetModel<String>(Sets.newSet("foo", "bar"));
    }

    @Test
    public void allConstructsSerializable() {
        assertThat(new SetToListWrapperModel<String>(new SetModel<String>()), serializable());
        assertThat(SetToListWrapperModel.of(new SetModel<String>(), Ordering.natural()), serializable());
        assertThat(SetToListWrapperModel.of(new SetModel<String>()), serializable());
    }

    @Test
    public void setObject_nullSetsToNull() {
        model = SetToListWrapperModel.of(setModel);
        assertThat(setModel.getObject(), notNullValue());
        model.setObject(null);
        assertThat(setModel.getObject(), nullValue());
        assertThat(model.getObject(), nullValue());
    }

    @Test
    public void setObject_listSetsSet() {
        model = SetToListWrapperModel.of(setModel, Ordering.natural().reverse());
        model.setObject(Arrays.asList("1", "2", "3"));
        assertThat(setModel.getObject(), containsInAnyOrder("1", "2", "3"));
        // Reverse ordering
        assertThat(model.getObject(), contains("3", "2", "1"));
    }

    @Test
    public void getWrappedModelCorrect() {
        SetToListWrapperModel<String> wrapperModel = new SetToListWrapperModel<String>(setModel);
        assertThat(wrapperModel.getWrappedModel(), equalTo(setModel));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void innerModelDetached() {
        IModel<Set<String>> mock = mock(IModel.class);
        new SetToListWrapperModel<String>(mock).detach();
        verify(mock, only()).detach();
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullModelNotAllowed() {
        SetToListWrapperModel.of((IModel<Set<String>>) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullComparatorNotAllowed() {
        SetToListWrapperModel.of(new SetModel<String>(), null);
    }

    private class TestSetModel<T> implements IModel<Set<T>> {
        private static final long serialVersionUID = -77911274673709626L;

        private Set<T> set;

        private TestSetModel(Set<T> set) {
            this.set = set;
        }

        @Override
        public Set<T> getObject() {
            return set;
        }

        @Override
        public void setObject(Set<T> object) {
            this.set = object;
        }

        @Override
        public void detach() {
        }
    }

}
