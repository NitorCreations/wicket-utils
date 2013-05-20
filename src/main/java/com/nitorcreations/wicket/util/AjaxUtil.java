package com.nitorcreations.wicket.util;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.request.cycle.RequestCycle;

public final class AjaxUtil {

    private AjaxUtil() {}

    /**
     * Add the component to the given {@link org.apache.wicket.ajax.AjaxRequestTarget} if it is not {@code null}, i.e., the request is an
     * ajax request.
     *
     * @param target the ajax request target
     * @param components the components to add to the ajax request
     */
    public static void add(AjaxRequestTarget target, Component... components) {
        if (target != null) {
            target.add(components);
        }
    }

    /**
     * @see #add(org.apache.wicket.ajax.AjaxRequestTarget, org.apache.wicket.Component...)
     * @see #getTarget()
     */
    public static void add(Component... components) {
        AjaxUtil.add(getTarget(), components);
    }

    /**
     * Enables adding the components to the ajax request. I.e. sets the {@link org.apache.wicket.Component#setOutputMarkupId(boolean)}
     * and {@link org.apache.wicket.Component#setOutputMarkupPlaceholderTag(boolean)} to {@code true}.
     * @param component the component to modify
     * @param <T> type of the component
     * @return the modified component
     */
    public static <T extends Component> T enableAjax(T component) {
        component.setOutputMarkupId(true);
        component.setOutputMarkupPlaceholderTag(true);
        return component;
    }

    /**
     * Gets the AjaxRequestTarget from the RequestCycle.
     */
    public static AjaxRequestTarget getTarget() {
        return RequestCycle.get().find(AjaxRequestTarget.class);
    }
}
