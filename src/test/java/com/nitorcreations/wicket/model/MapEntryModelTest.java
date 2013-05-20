package com.nitorcreations.wicket.model;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.MapModel;
import org.apache.wicket.util.tester.WicketTester;

import static com.nitorcreations.Matchers.serializable;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class MapEntryModelTest {

    WicketTester wicketTester = new WicketTester();

    Map<Integer, String> map;

    MapEntryModel<Integer, String> model;

    @Before
    public void setup() {
        map = new HashMap<Integer, String>();
        map.put(1, "First");
        map.put(2, "Second");
        map.put(3, "Third");
    }

    private void createModelForKey(int key) {
        model = new MapEntryModel<Integer, String>(Model.ofMap(map), Model.of(key));
    }

    @Test
    public void isSerializable() {
        createModelForKey(3);
        assertThat(model, serializable());
    }

    @Test
    public void nonExistingKeySetObject() {
        createModelForKey(123);
        assertThat(model.getObject(), nullValue());
        model.setObject("Foobar");
        assertThat(map.get(123), is("Foobar"));
    }

    @Test
    public void existingKeySetObject() {
        createModelForKey(1);
        assertThat(model.getObject(), is("First"));
        model.setObject("Barbar");
        assertThat(map.get(1), is("Barbar"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void doesNotAcceptNull_mapModel() {
        new MapEntryModel<Integer, String>(null, Model.of(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void doesNotAcceptNull_mapModelsObject() {
        new MapEntryModel<Integer, String>(new MapModel<Integer, String>(), Model.of(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void doesNotAcceptNull_keyModel() {
        new MapEntryModel<Integer, String>(new MapModel<Integer, String>(new HashMap<Integer, String>()), null);
    }
}
