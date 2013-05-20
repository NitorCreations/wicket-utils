package com.nitorcreations.wicket.components.datatable;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.nitorcreations.test.wicket.resources.MockStringResourceLoader;
import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.ISortableDataProvider;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.Markup;
import org.apache.wicket.markup.html.form.AbstractChoice;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.mock.MockApplication;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;

import static com.nitorcreations.test.wicket.Selection.select;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ViewSizeToolbarTest {

    private static final String MARKUP_STRING = "<wicket:head /><form wicket:id=\"form\"><table wicket:id=\"table\" /></form>";

    WicketTester wicketTester;

    Markup markup;

    @Mock
    ISortableDataProvider<Integer,String> dataProvider;

    Form<?> form;

    DataTable<Integer,String> table;

    ViewSizeToolbar toolbar;

    private MockStringResourceLoader mockStringResourceLoader;

    public void startWithColumns(int colNumber) {
        MockitoAnnotations.initMocks(this);
        mockStringResourceLoader = new MockStringResourceLoader();
        wicketTester = new WicketTester(new MockApplication() {
            @Override
            protected void init() {
                super.init();
                getResourceSettings().getStringResourceLoaders().clear();
                getResourceSettings().getStringResourceLoaders().add(mockStringResourceLoader);
            }
        });
        mockStringResourceLoader.expectStringMessage(ViewSizeToolbar.class, "ViewSizeToolbar.item", "${} pcs.");

        List<IColumn<Integer,String>> columns = new ArrayList<IColumn<Integer,String>>(colNumber);
        for (int i = 0; i < colNumber; i++) {
            columns.add(new PropertyColumn<Integer,String>(Model.of("Int"), "intValue()"));
        }

        form = new Form<Void>("form");
        table = new DefaultDataTable<Integer,String>("table", columns, dataProvider, 10);
        table.setOutputMarkupId(true);
        form.add(table);
        table.addTopToolbar(toolbar = new ViewSizeToolbar(table));
        wicketTester.startComponentInPage(form, Markup.of(MARKUP_STRING));
    }

    @Test
    public void selectionChangeChangesItemsPerPageByAjax() {
        startWithColumns(1);
        assertThat(table.getItemsPerPage(), is(10l));
        FormTester formTester = wicketTester.newFormTester("form");
        formTester.select(String.format("table:topToolbars:toolbars:%s:span:viewSize", toolbar.getId()), 2);
        wicketTester.executeAjaxEvent(viewSizeChoice(), "onChange");
        wicketTester.assertComponentOnAjaxResponse(table);
        assertThat(table.getItemsPerPage(), is(50l));
    }

    @Test
    public void displayValuesCorrect() {
        startWithColumns(2);
        assertThat(viewSizeChoice(), hasDisplayValueFor(5L, is("5 pcs.")));
        assertThat(viewSizeChoice(), hasDisplayValueFor(10L, is("10 pcs.")));
    }

    @Test
    public void colspanChangesWithTableWidth() {
        startWithColumns(2);
        assertThat(wicketTester.getTagByWicketId("span").getAttribute("colspan"),
                is("2"));

        startWithColumns(4);
        assertThat(wicketTester.getTagByWicketId("span").getAttribute("colspan"),
                is("4"));
    }

    private DropDownChoice<Long> viewSizeChoice() {
        return select(DropDownChoice.class).firstFrom(toolbar);
    }

    @SuppressWarnings("unchecked")
    @Factory
    public static <T, X extends Component> Matcher<X> hasDisplayValueFor(final T object,
            final Matcher<?> displayValue) {
        return (Matcher<X>) new TypeSafeDiagnosingMatcher<AbstractChoice<?, T>>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("display value for ").appendValue(object).appendText(" should be ")
                        .appendDescriptionOf(displayValue);
            }

            @Override
            protected boolean matchesSafely(AbstractChoice<?, T> item, Description mismatchDescription) {
                if (!displayValue.matches(item.getChoiceRenderer().getDisplayValue(object))) {
                    mismatchDescription.appendText("display value ");
                    displayValue.describeMismatch(object, mismatchDescription);
                    return false;
                }
                return true;
            }
        };
    }
}
