package com.nitorcreations.wicket.components;


import org.junit.Test;

import org.apache.wicket.markup.Markup;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.tester.TagTester;
import org.apache.wicket.util.tester.WicketTester;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ShorteningLabelTest {

    private static final String MARKUP = "<span wicket:id=\"label\" />";

    private static final String INSIDE_MARKUP = "<div wicket:id=\"container\"><span wicket:id=\"labelValue\" /></div>";

    WicketTester wicketTester = new WicketTester();

    ShorteningLabel label;

    String labelValue;

    WebMarkupContainer container;

    private void start(String modelObject) {
        label = new ShorteningLabel("label", Model.of(modelObject));
        wicketTester.startComponentInPage(label, Markup.of(MARKUP));
    }

    private void start(String modelObject, int maxLength) {
        wicketTester = new WicketTester();
        label = new ShorteningLabel("label", Model.of(modelObject), maxLength);
        wicketTester.startComponentInPage(label, Markup.of(MARKUP));
    }

    @Test
    public void longContent() {
        start("This text has 22 chars");
        TagTester tag = wicketTester.getTagByWicketId("label");
        assertThat(tag.getValue(), is("This text has 22 cha…"));
        assertThat(tag.getAttribute("title"), is("This text has 22 chars"));
    }

    @Test
    public void shortContent() {
        start("This text has 22 chars", 22);
        TagTester tag = wicketTester.getTagByWicketId("label");
        assertThat(tag.getValue(), is("This text has 22 chars"));
        assertThat(tag.getAttribute("title"), is("This text has 22 chars"));
    }

    @Test
    public void nullContent() {
        start(null);
        TagTester tag = wicketTester.getTagByWicketId("label");
        assertThat(tag.getValue(), is(""));
        assertThat(tag.getAttribute("title"), is(""));
    }

    @Test
    public void insideCompoundModel() {
        labelValue = "This text has 27 characters";
        container = new WebMarkupContainer("container", new CompoundPropertyModel<ShorteningLabelTest>(this));
        container.add(new ShorteningLabel("labelValue"));
        wicketTester.startComponentInPage(container, Markup.of(INSIDE_MARKUP));

        TagTester tag = wicketTester.getTagByWicketId("labelValue");
        assertThat(tag.getValue(), is("This text has 27 cha…"));
        assertThat(tag.getAttribute("title"), is("This text has 27 characters"));
    }
}

