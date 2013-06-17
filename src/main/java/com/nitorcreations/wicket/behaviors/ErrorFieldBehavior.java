package com.nitorcreations.wicket.behaviors;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.util.lang.Args;

public final class ErrorFieldBehavior extends AttributeAppender {
    private static final long serialVersionUID = 1L;
    public static final String ATTRIBUTE = "class";
    public static final String SEPARATOR = " ";
    public static final String ERROR_CLASS = "error";
    private final ErrorFieldModel errorFieldModel;

    private ErrorFieldBehavior() {
        super(ATTRIBUTE, new ErrorFieldModel(), SEPARATOR);
        errorFieldModel = (ErrorFieldModel) getReplaceModel();
    }

    public static ErrorFieldBehavior instance() {
        return new ErrorFieldBehavior();
    }

    @Override
    public void renderHead(Component component, IHeaderResponse response) {
        response.render(CssHeaderItem.forReference(ErrorFieldCssReference.get()));
    }

    @Override
    public void bind(Component component) {
        errorFieldModel.bind(component);
    }

    /**
     * Css reference for the ErrorFieldBehavior styles.
     */
    public static class ErrorFieldCssReference extends PackageResourceReference {
        private static final long serialVersionUID = 1L;
        private static final ErrorFieldCssReference INSTANCE = new ErrorFieldCssReference();

        private ErrorFieldCssReference() {
            super(ErrorFieldBehavior.class, "ErrorFieldBehavior.css");
        }

        public static ErrorFieldCssReference get() {
            return INSTANCE;
        }
    }

    /**
     * Model that returns "error" if the bound component has validation errors.
     */
    static class ErrorFieldModel extends AbstractReadOnlyModel<String> {
        private static final long serialVersionUID = 1L;
        private Component component;

        public void bind(Component toComponent) {
            component = Args.notNull(toComponent, "Component");
        }

        @Override
        public String getObject() {
            Args.notNull(component, "Component is null. ErrorFieldModel#bind(Component) has not been called yet.");
            if (component instanceof FormComponent && !((FormComponent<?>) component).isValid()) {
                return ERROR_CLASS;
            } else if (component.hasErrorMessage()) {
                return ERROR_CLASS;
            }
            return "";
        }
    }
}
