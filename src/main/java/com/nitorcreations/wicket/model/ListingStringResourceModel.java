package com.nitorcreations.wicket.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import org.apache.wicket.Component;
import org.apache.wicket.model.IComponentAssignedModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.IWrapModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.util.lang.Args;

/**
 * A model that lists the items given in the {@code model} parameter by creating a transient {@link org.apache.wicket.model.StringResourceModel}
 * for each item in the list with the given {@code messageKey}. If no entries are in the list, {@code defaultValue} is shown. Example
 *
 * <code><pre>
 * SomeComponent.properties
 * ---
 * stringLength=len: ${length}
 *
 * SomeComponent.java
 * ---
 * List&lt;String> list = Arrays.asList("A", "BC", "DEF");
 * add(new Label("label", new ListingStringResourceModel&lt;String>("stringLength", Model.ofList(list));
 *
 * Renders the text as:
 * ---
 * len: 1, len: 2, len: 3
 *
 * </pre></code>
 * The model implements {@link org.apache.wicket.model.IComponentAssignedModel} and thus, retrieves the correct
 * message based on component hierarchy, when the model is assigned to a component.
 *
 *
 * @param <T> the type of a single item in the list
 * @see org.apache.wicket.model.StringResourceModel#StringResourceModel(String, org.apache.wicket.model.IModel, Object...)
 */
public class ListingStringResourceModel<T extends Serializable> extends LoadableDetachableModel<String>
    implements IComponentAssignedModel<String> {

    private static final long serialVersionUID = 714388179253718513L;

    public static final String DEFAULT_SEPARATOR = ", ";

    private final IModel<String> defaultMessage;
    private final String messageKey;
    private final IModel<? extends List<? extends T>> model;
    private final Component component;

    private String separator = DEFAULT_SEPARATOR;

    @SuppressWarnings("unchecked")
    public ListingStringResourceModel(String messageKey, IModel<? extends List<? extends T>> model,
            IModel<String> defaultMessage, Component component) {
        this.component = component;
        this.defaultMessage = Args.notNull(defaultMessage, "defaultMessage");
        this.messageKey = Args.notNull(messageKey, "message key");
        this.model = Args.notNull(model, "list model");
    }

    public ListingStringResourceModel(String messageKey, IModel<? extends List<? extends T>> model) {
        this(messageKey, model, Model.of(""), null);
    }

    @Override
    public void detach() {
        super.detach();
        defaultMessage.detach();
        model.detach();
    }

    private List<IModel<String>> getWrappedModels(Component component) {
        List<IModel<String>> list = new ArrayList<IModel<String>>();
        for (T item : model.getObject()) {
            list.add(new StringResourceModel(messageKey, component, Model.of(item)));
        }
        return list;
    }

    private String getString(Component component) {
        List<IModel<String>> models = getWrappedModels(component);
        if (models.isEmpty()) {
            return defaultMessage.getObject();
        }
        List<String> strings = new ArrayList<String>();
        for (IModel<String> stringIModel : models) {
            strings.add(stringIModel.getObject());
        }
        return StringUtils.join(strings, separator);
    }

    private String getString() {
        return getString(component);
    }

    public String getSeparator() {
        return separator;
    }

    public ListingStringResourceModel<T> setSeparator(String separator) {
        this.separator = separator;
        return this;
    }

    @Override
    protected String load() {
        return getString();
    }

    @Override
    public IWrapModel<String> wrapOnAssignment(Component component) {
        return new AssignmentWrapper(component);
    }

    private class AssignmentWrapper extends LoadableDetachableModel<String> implements IWrapModel<String> {
        private static final long serialVersionUID = -7181093446442329177L;

        private final Component assignedComponent;

        public AssignmentWrapper(Component component) {
            this.assignedComponent = component;
        }

        @Override
        public void detach() {
            super.detach();
            ListingStringResourceModel.this.detach();
        }

        @Override
        protected void onDetach() {
            if (ListingStringResourceModel.this.component == null) {
                ListingStringResourceModel.this.onDetach();
            }
        }

        @Override
        public IModel<?> getWrappedModel() {
            return ListingStringResourceModel.this;
        }

        @Override
        protected String load() {
            if (ListingStringResourceModel.this.component != null) {
                return ListingStringResourceModel.this.getObject();
            } else {
                return getString(assignedComponent);
            }
        }
    }
}
