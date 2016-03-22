/*
 * Copyright 2012 Donghyuck, Son
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package architecture.common.event.api;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * <p>
 * Annotation to be used with events to tell whether they can be handled
 * asynchronously
 * </p>
 * <p>
 * This is the default annotation to be used with
 * {@link com.atlassian.event.internal.AnnotationAsynchronousEventResolver}
 * </p>
 * 
 * @see architecture.common.event.internal.AnnotationAsynchronousEventResolver
 * @since 2.0
 */
@Retention(RUNTIME)
@Target(TYPE)
@Documented
public @interface AsynchronousPreferred {

}
