package architecture.common.event.internal;

import architecture.common.event.Event;

@SuppressWarnings("deprecation")
class _TestEvent extends Event {
    public _TestEvent(Object src) {
        super(src);
    }

    public static class TestSubEvent extends _TestEvent {
        public TestSubEvent(final Object src) {
            super(src);
        }
    }

    public static class TestSubSubEvent extends TestSubEvent {
        public TestSubSubEvent(final Object src) {
            super(src);
        }
    }

    public static class TestInterfacedEvent extends Event implements TestSubInterface {
        public TestInterfacedEvent(final Object src) {
            super(src);
        }
    }

    public static interface TestInterface {
    }

    public static interface TestSubInterface extends TestInterface {
    }
}
