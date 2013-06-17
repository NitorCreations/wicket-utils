package com.nitorcreations.wicket.event;

import static org.apache.wicket.event.Broadcast.BREADTH;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

import org.apache.wicket.Component;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

import com.nitorcreations.test.TestApplication;

@SuppressWarnings({ "unused", "serial" })
public class AnnotationEventDispatcherTest {
    private final TestApplication testApp = new TestApplication();
    private final WicketTester tester = new WicketTester(testApp);
    final EventMock mock = mock(EventMock.class);
    private Component component;

    @Before
    public void setup() {
        AnnotationEventDispatcher dispatcher = new AnnotationEventDispatcher();
        testApp.getComponentInstantiationListeners().add(dispatcher);
        testApp.getFrameworkSettings().add(dispatcher);
        reset(mock);
    }

    @Test
    public void eventIsPassedToCorrectEventMethods() {
        component = tester.startComponentInPage(new TestComponent("id"));
        component.send(testApp, BREADTH, "Hello!");
        verify(mock).onStringEvent();
        verify(mock).onStringEventInBaseClass();
    }

    @Test
    public void eventIsPassedToAllOnEventMethodsThatAcceptGivenEventType() {
        component = tester.startComponentInPage(new TestComponent("id"));
        component.send(testApp, BREADTH, new IllegalArgumentException());
        verify(mock).onExceptionEvent();
        verify(mock).onRuntimeExceptionEvent();
    }

    @Test(expected = RuntimeException.class)
    public void componentCannotBeInstantiatedIfAnnotatedMethodDoesNotContainOneParameter() {
        tester.startComponentInPage(new IncorrectNumberOfParameters("id"));
    }

    @Test(expected = RuntimeException.class)
    public void componentCannotBeInstantiatedIfAnnotatedMethodIsNotPublic() {
        tester.startComponentInPage(new IncorrectMethodVisibility("id"));
    }

    @Test
    public void eventIsNotPassedToMethodsThatAreNotAnnotated() {
        NotAnnotatedTestComponent testComponent = new NotAnnotatedTestComponent();
        tester.startComponentInPage(testComponent);
        testComponent.send(testApp, BREADTH, "Hello!");
        verify(mock, never()).onStringEvent();
    }

    private class TestComponent extends AbstractTestComponent {
        public TestComponent(final String id) {
            super(id);
        }

        @OnEvent
        public void onStringEvent(final String message) {
            mock.onStringEvent();
        }

        @OnEvent
        public void onExceptionEvent(final Exception e) {
            mock.onExceptionEvent();
        }

        @OnEvent
        public void onRuntimeExceptionEvent(final RuntimeException e) {
            mock.onRuntimeExceptionEvent();
        }
    }

    private abstract class AbstractTestComponent extends Component {
        public AbstractTestComponent(final String id) {
            super(id);
        }

        @Override
        protected void onRender() {
            // do nothing
        }

        @OnEvent
        public void onBaseClassEvent(final String message) {
            mock.onStringEventInBaseClass();
        }
    }

    private class IncorrectNumberOfParameters extends AbstractTestComponent {
        public IncorrectNumberOfParameters(final String id) {
            super(id);
        }

        @OnEvent
        public void onStringEvent(final String message, final String fails) {
            mock.onStringEvent();
        }
    }

    private class IncorrectMethodVisibility extends AbstractTestComponent {
        public IncorrectMethodVisibility(final String id) {
            super(id);
        }

        @OnEvent
        protected void onStringEvent(final String message) {
            mock.onStringEvent();
        }
    }

    private class NotAnnotatedTestComponent extends Component {
        public NotAnnotatedTestComponent() {
            super("id");
        }

        @Override
        protected void onRender() {
            // do nothing
        }

        public void onStringEvent(final String message, final String allowed) {
            mock.onStringEvent();
        }
    }

    private static class EventMock {
        public void onStringEvent() {
            // do nothing
        }

        public void onStringEventInBaseClass() {
            // do nothing
        }

        public void onExceptionEvent() {
            // do nothing
        }

        public void onRuntimeExceptionEvent() {
            // do nothing
        }
    }
}
