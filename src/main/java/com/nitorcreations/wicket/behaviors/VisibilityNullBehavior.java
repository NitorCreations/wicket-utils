package com.nitorcreations.wicket.behaviors;

import java.util.Collection;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * A behavior which sets the component's visibility based on the default model's or some other
 * components model object. Used to reduce boiler-plate code with typical scenarios of components
 * needing to be hidden or visible based on nullity of certain model object.
 */
public final class VisibilityNullBehavior extends Behavior {
    private static final long serialVersionUID = 1L;
    private static final Behavior hiddenDefaultModelNull = new Behavior() {
        private static final long serialVersionUID = 1L;

        @Override
        public void onConfigure(final Component component) {
            super.onConfigure(component);
            if (component.getDefaultModelObject() == null) {
                component.setVisible(false);
            }
        }
    };
    private static final Behavior visibleDefaultModelNotNull = new Behavior() {
        private static final long serialVersionUID = 1L;

        @Override
        public void onConfigure(final Component component) {
            super.onConfigure(component);
            component.setVisible(component.getDefaultModelObject() != null);
        }
    };

    private VisibilityNullBehavior() {}

    /**
     * Component is set visible when the given model object contains a {@code null}.
     * 
     * @param model
     *            model object which controls the visibility of the component this {@link Behavior}
     *            is attached to.
     * @return {@link Behavior} that controls components visibility
     */
    public static Behavior visibleWhenNull(final IModel<?> model) {
        return new Behavior() {
            private static final long serialVersionUID = 1L;

            @Override
            public void onConfigure(final Component component) {
                super.onConfigure(component);
                component.setVisible(model.getObject() == null);
            }
        };
    }

    /**
     * Component is set visible when the given model object does not contain a {@code null}.
     * 
     * @param model
     *            model object which controls the visibility of the component this {@link Behavior}
     *            is attached to.
     * @return {@link Behavior} that controls components visibility
     */
    public static Behavior visibleWhenNotNull(final IModel<?> model) {
        return new Behavior() {
            private static final long serialVersionUID = 1L;

            @Override
            public void onConfigure(final Component component) {
                super.onConfigure(component);
                component.setVisible(model.getObject() != null);
            }
        };
    }

    /**
     * A behavior which sets the component's visibility to {@code false} if the default object is
     * {@code null} and {@code true} when not {@code null}.
     * 
     * @return {@link Behavior} that controls components visibility
     */
    public static Behavior visibleWhenDefaultModelNotNull() {
        return visibleDefaultModelNotNull;
    }

    /**
     * A behavior which sets the component's visibility to {@code false} if the default object is
     * {@code null}. NOTE: Never sets component back to visible once hidden; for that behavior use
     * {@link VisibilityNullBehavior#visibleWhenDefaultModelNotNull()}.
     * 
     * @return {@link Behavior} that controls components visibility
     */
    public static Behavior hiddenWhenDefaultModelNull() {
        return hiddenDefaultModelNull;
    }

    /**
     * Visibility based on {@code null} is not good for {@link Model}s that contain Collections when
     * they are never null. In those cases you can use this behavior that checks for empty
     * collection instead of null.
     * 
     * @param model
     *            model that contains a {@link java.util.Collection}
     * @return {@link Behavior} that controls components visibility
     */
    public static Behavior visibleWhenCollectionModelNotEmpty(final IModel<? extends Collection<?>> model) {
        return new Behavior() {
            private static final long serialVersionUID = -6467968057103820613L;

            @Override
            public void onConfigure(final Component component) {
                super.onConfigure(component);
                component.setVisible(!isEmpty(model.getObject()));
            }
        };
    }

    /**
     * Visibility based on {@code null} is not good for {@link Model}s that contain Collections when
     * they are never null. In those cases you can use this behavior that checks for empty
     * collection instead of null.
     * 
     * @param model
     *            model that contains a {@link java.util.Collection}
     * @return {@link Behavior} that controls components visibility
     */
    public static Behavior visibleWhenCollectionModelEmpty(final IModel<? extends Collection<?>> model) {
        return new Behavior() {
            private static final long serialVersionUID = -6467968057103820613L;

            @Override
            public void onConfigure(final Component component) {
                super.onConfigure(component);
                component.setVisible(isEmpty(model.getObject()));
            }
        };
    }

    static boolean isEmpty(final Collection<?> object) {
        return object == null || object.isEmpty();
    }
}
