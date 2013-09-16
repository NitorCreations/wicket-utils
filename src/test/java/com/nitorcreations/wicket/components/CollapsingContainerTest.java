package com.nitorcreations.wicket.components;

import org.junit.Before;
import org.junit.Test;

import com.nitorcreations.test.TestApplication;
import com.nitorcreations.wicket.components.CollapsingContainer.ContentToggleLink;
import org.apache.wicket.markup.Markup;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.util.tester.WicketTester;

import static com.nitorcreations.WicketMatchers.label;
import static com.nitorcreations.WicketMatchers.visibleInHierarchy;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

public class CollapsingContainerTest {

    private CollapsingContainer container;
    private ContentToggleLink toggleLink;

    private WicketTester wicketTester;

    @Before
    public void setup() {
        wicketTester = new WicketTester(new TestApplication());
        TestApplication.expectStringMessage("toggleText.false", "Show content");
        TestApplication.expectStringMessage("toggleText.true", "Hide content");
        WebMarkupContainer wmc = new WebMarkupContainer("wmc");
        wmc.add(container = new CollapsingContainer("container"));
        wmc.add(toggleLink = new ContentToggleLink("toggle", container));
        wicketTester.startComponentInPage(wmc, Markup.of("<div wicket:id=\"wmc\"><a wicket:id=\"toggle\"><span wicket:id=\"toggleText\" /></a><div wicket:id=\"container\" /></div>"));
    }

    @Test
    public void linkClickTogglesContent() {
        assertThat(container, not(visibleInHierarchy()));
        assertThat(toggleLink.get("toggleText"), label("Show content"));

        wicketTester.executeAjaxEvent(toggleLink, "click");
        wicketTester.assertComponentOnAjaxResponse(container);
        wicketTester.assertComponentOnAjaxResponse(toggleLink);

        assertThat(container, visibleInHierarchy());
        assertThat(toggleLink.get("toggleText"), label("Hide content"));

        wicketTester.executeAjaxEvent(toggleLink, "click");
        wicketTester.assertComponentOnAjaxResponse(container);
        wicketTester.assertComponentOnAjaxResponse(toggleLink);

        assertThat(container, not(visibleInHierarchy()));
        assertThat(toggleLink.get("toggleText"), label("Show content"));
    }
}
