
package com.nitorcreations.wicket.components;

import org.junit.Before;
import org.junit.Test;

import com.nitorcreations.test.TestApplication;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.markup.Markup;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.util.tester.WicketTester;

import static com.nitorcreations.WicketMatchers.label;
import static com.nitorcreations.WicketMatchers.visibleInHierarchy;
import static com.nitorcreations.test.wicket.Selection.select;
import static com.nitorcreations.test.wicket.Selection.selectWithId;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

public class CollapsingBorderTest {

    private static final String MARKUP = "<span wicket:id=\"border\"><span wicket:id=\"label\" /></span>";

    private WicketTester wicketTester = new WicketTester(new TestApplication());

    private CollapsingBorder border;

    private Label content;

    @Before
    public void setup() {
        TestApplication.expectStringMessage("toggleText.true", "Hide");
        TestApplication.expectStringMessage("toggleText.false", "Show");
        content = new Label("label", "Text");
    }

    private  void start() {
        start(new CollapsingBorder("border"));
    }

    private void start(boolean visible) {
        start(border = new CollapsingBorder("border", visible));
    }

    private void start(CollapsingBorder border) {
        this.border = border;
        border.add(content);
        wicketTester.startComponentInPage(border, Markup.of(MARKUP));
    }

    @Test
    public void defaultConstructorContentHidden() {
        start();
        assertThat(container(), not(visibleInHierarchy()));
    }

    @Test
    public void visibleByDefaultIfSpecifiedInConstructor() {
        start(true);
        assertThat(container(), visibleInHierarchy());
    }

    @Test
    public void toggleLinkChangesStateAndText() {
        start();
        assertThat(container(), not(visibleInHierarchy()));
        assertThat(linkText(), label("Show"));

        wicketTester.clickLink(toggleLink().getPageRelativePath(), true);
        wicketTester.assertComponentOnAjaxResponse(border);
        assertThat(container(), visibleInHierarchy());
        assertThat(linkText(), label("Hide"));

        wicketTester.clickLink(toggleLink().getPageRelativePath(), true);
        wicketTester.assertComponentOnAjaxResponse(border);
        assertThat(container(), not(visibleInHierarchy()));
        assertThat(linkText(), label("Show"));
    }

    @Test
    public void toggleLinkChangesStateAndText_withoutAjax() {
        start();
        assertThat(container(), not(visibleInHierarchy()));
        assertThat(linkText(), label("Show"));

        wicketTester.clickLink(toggleLink().getPageRelativePath(), false);
        assertThat(container(), visibleInHierarchy());
        assertThat(linkText(), label("Hide"));

        wicketTester.clickLink(toggleLink().getPageRelativePath(), false);
        assertThat(container(), not(visibleInHierarchy()));
        assertThat(linkText(), label("Show"));
    }

    @Test
    public void setContentVisible() {
        start(new CollapsingBorder("border", false).setContentVisible(true));
        assertThat(container(), visibleInHierarchy());
        assertThat(linkText(), label("Hide"));
    }

    private Label linkText() {
        return selectWithId("toggleText").firstFrom(border);
    }

    private Component container() {
        return selectWithId("container").firstFrom(border);
    }

    private AjaxFallbackLink<Void> toggleLink() {
        return select(AjaxFallbackLink.class).firstFrom(border);
    }
}
