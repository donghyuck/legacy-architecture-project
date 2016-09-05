package architecture.common.event.api;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * <p>
 * Annotation to be used with events to tell whether they can be handled
 * asynchronously
 * </p>
 * <p>
 * This is the default annotation to be used with
 * {@link architecture.common.event.impl.AnnotationAsynchronousEventResolver}
 * </p>
 *
 * @see architecture.common.event.impl.AnnotationAsynchronousEventResolver
 * @since 2.0
 */
@Retention(RUNTIME)
@Target(TYPE)
@Documented
public @interface AsynchronousPreferred {
}
