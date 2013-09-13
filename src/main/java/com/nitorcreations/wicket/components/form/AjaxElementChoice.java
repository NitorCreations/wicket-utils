package com.nitorcreations.wicket.components.form;

import java.io.Serializable;

import com.nitorcreations.wicket.util.AjaxUtil;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.IGenericComponent;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.lang.Args;

public class AjaxElementChoice<T extends Serializable> extends FormComponent<T> {
    private static final long serialVersionUID = 1711555970707895563L;

    public static final String SELECTED_CLASS = "ajaxElementChoice-selected";

    private final IModel<T> selection = new Model<T>();

    public AjaxElementChoice(String id, IModel<T> model) {
        super(id, model);
        setOutputMarkupId(true);
        selection.setObject(getModelObject());
    }

    protected void select(T object, AjaxRequestTarget target) {
        this.selection.setObject(object);
        AjaxUtil.add(target, this);
    }

    protected boolean isSelected(T object) {
        return object.equals(selection.getObject());
    }

    @Override
    protected void convertInput() {
        setConvertedInput(selection.getObject());
    }

    public static class ChoiceItem<T extends Serializable> extends WebMarkupContainer implements IGenericComponent<T> {
        private static final long serialVersionUID = -7175689672489559406L;

        private transient AjaxElementChoice<T> parent;

        public ChoiceItem(String id, IModel<T> model) {
            super(id, model);
            setOutputMarkupId(true);
            add(new ChoiceClickBehavior());
            add(AttributeModifier.append("class", new IsSelectedModel()));
        }


        public boolean isSelected() {
            return getChoice().isSelected(getModelObject());
        }

        @SuppressWarnings("unchecked")
        private AjaxElementChoice<T> getChoice() {
            if (parent == null) {
                parent = (AjaxElementChoice<T>) findParent(AjaxElementChoice.class);
            }
            Args.notNull(parent, "No parent of type AjaxElementChoice found");
            return parent;
        }

        @SuppressWarnings("unchecked")
        @Override
        public IModel<T> getModel() {
            return (IModel<T>) getDefaultModel();
        }

        @Override
        public void setModel(IModel<T> model) {
            setDefaultModel(model);
        }

        @Override
        public void setModelObject(T object) {
            setDefaultModelObject(object);
        }

        @SuppressWarnings("unchecked")
        @Override
        public T getModelObject() {
            return (T) getDefaultModelObject();
        }

        private class ChoiceClickBehavior extends AjaxEventBehavior {
            private static final long serialVersionUID = -1732082937259155342L;
            public static final String ON_CLICK = "onClick";

            public ChoiceClickBehavior() {
                super(ON_CLICK);
            }

            @Override
            protected void onEvent(AjaxRequestTarget target) {
                getChoice().select(getModelObject(), target);
            }
        }

        private class IsSelectedModel extends AbstractReadOnlyModel<String> {
            private static final long serialVersionUID = 5415421270671013329L;

            @Override
            public String getObject() {
                return isSelected() ? SELECTED_CLASS : "";
            }
        }
    }

}
