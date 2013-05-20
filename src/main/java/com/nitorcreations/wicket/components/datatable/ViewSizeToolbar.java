package com.nitorcreations.wicket.components.datatable;

import java.util.Arrays;
import java.util.List;

import com.nitorcreations.wicket.util.AjaxUtil;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

public class ViewSizeToolbar extends AbstractToolbar {
    private static final long serialVersionUID = 5443761350991819366L;
    private static final List<Long> CHOICES = Arrays.asList(10l, 20l, 50l);
    private static final String COLSPAN = "colspan";
    private static final String ON_CHANGE = "onChange";

    public ViewSizeToolbar(final DataTable<?,?> table) {
        super(table);
        WebMarkupContainer container = new WebMarkupContainer("span");
        add(container);
        container.add(new AttributeModifier(COLSPAN, new AbstractReadOnlyModel<Integer>() {
            private static final long serialVersionUID = -2447506256355846930L;

            @Override
            public Integer getObject() {
                return table.getColumns().size();
            }
        }));

        container.add(new ViewSizeChoice("viewSize", new PropertyModel<Long>(table, "itemsPerPage"), table, CHOICES));
    }

    private final class ViewSizeChoice extends DropDownChoice<Long> {
        private static final long serialVersionUID = -4383580482700438541L;

        public ViewSizeChoice(String id, IModel<Long> model, final DataTable<?,?> table, List<Long> choices) {
            super(id, model, choices, new ViewSizeChoiceRenderer());
            add(new AjaxFormComponentUpdatingBehavior(ON_CHANGE) {
                private static final long serialVersionUID = 6011886273083621871L;

                @Override
                protected void onUpdate(AjaxRequestTarget target) {
                    AjaxUtil.add(target, table);
                }
            });
        }
    }

    private class ViewSizeChoiceRenderer implements IChoiceRenderer<Long> {
        private static final long serialVersionUID = -4787874452282086668L;

        public ViewSizeChoiceRenderer() {}

        @Override
        public Object getDisplayValue(Long object) {
            return ViewSizeToolbar.this.getString("ViewSizeToolbar.item", Model.of(object));
        }

        @Override
        public String getIdValue(Long object, int index) {
            return String.valueOf(index);
        }

    }
}
