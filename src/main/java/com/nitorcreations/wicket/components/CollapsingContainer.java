package com.nitorcreations.wicket.components;

import com.nitorcreations.wicket.util.AjaxUtil;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;

/**
 * Similar to {@link CollapsingBorder} but with user-provided markup.
 * <p/>
 * Usage:
 *
 * <code><pre>
 *     CollapsingContainer c = new CollapsingContainer("togglableContainer");
 *     add(c);
 *     add(new ContentToggleLink("toggle", c));
 * </pre></code>
 *
 * And the following markup:
 *
 * <code><pre>
 *     &lt;a href="#" wicket:id="toggle">
 *       &lt;span wicket:id="toggleText" /&gt;
 *     &lt;/a>
 *
 *     &lt;div wicket:id="togglableContainer">
 *       Togglable content here
 *     &lt;/div>
 * </pre></code>
 */
public class CollapsingContainer extends WebMarkupContainer {
    private static final long serialVersionUID = -3385212703086878145L;

    private final IModel<Boolean> contentVisible;

    public CollapsingContainer(String id) {
        this(id, false);
    }

    public CollapsingContainer(String id, boolean visibleByDefault) {
        super(id);
        this.contentVisible = Model.of(visibleByDefault);
        setOutputMarkupId(true);
    }

    @Override
    protected void onConfigure() {
        super.onConfigure();
        setVisibilityAllowed(isContentVisible());
    }

    public CollapsingContainer setContentVisible(boolean visible) {
        this.contentVisible.setObject(visible);
        return this;
    }

    public Boolean isContentVisible() {
        return contentVisible.getObject();
    }

    public CollapsingContainer toggleVisibility() {
        setContentVisible(!isContentVisible());
        return this;
    }

    public static class ContentToggleLink extends AjaxFallbackLink<Void> {
        private static final long serialVersionUID = 5336746033204778794L;

        private final CollapsingContainer container;

        public ContentToggleLink(String id, CollapsingContainer container) {
            super(id);
            this.container = container;

            add(new Label("toggleText", new StringResourceModel("toggleText.${}", this,
                    new PropertyModel<Boolean>(this.container, "contentVisible"))));
        }

        @Override
        public void onClick(AjaxRequestTarget target) {
            container.toggleVisibility();
            AjaxUtil.add(target, container);
            AjaxUtil.add(target, this);
        }
    }


}
