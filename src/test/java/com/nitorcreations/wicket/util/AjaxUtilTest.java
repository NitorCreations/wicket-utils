package com.nitorcreations.wicket.util;

import org.junit.Test;
import org.mockito.Mockito;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.util.tester.WicketTester;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;

public class AjaxUtilTest {

    static WicketTester wicketTester = new WicketTester();

    @Test
    public void doesNothingIfNull() {
        AjaxUtil.add((AjaxRequestTarget)null, new Label("foo", "faa"));
    }

    @Test
    public void addsMultipleComponentsIfNotNull() {
        Component c1 = new Label("foo", "faa");
        Component c2 = new Label("bar", "bor");
        AjaxRequestTarget target = Mockito.mock(AjaxRequestTarget.class);
        AjaxUtil.add(target, c1, c2);
        Mockito.verify(target).add(c1, c2);
    }

    @Test
    public void enableAjaxEnablesAjaxAndReturnsTheInstance() {
        Label l = new Label("label");
        l.setOutputMarkupId(false);
        l.setOutputMarkupPlaceholderTag(false);

        assertThat(AjaxUtil.enableAjax(l), sameInstance(l));

        assertThat(l, allOf(
                hasProperty("outputMarkupId", equalTo(true)),
                hasProperty("outputMarkupPlaceholderTag", equalTo(true))
        ));
    }

}
