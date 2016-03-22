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

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Used to annotate event listener methods. Methods should be public and take
 * one parameter which is the event to be handled.
 * <p/>
 * For example, the following class implements a simple event listener:
 * 
 * <pre>
 * <tt>      public class TestListener {
 *        &#64;EventListener
 *        public void onEvent(SampleEvent event) {
 *            System.out.println("Handled an event: " + event);
 *        }
 *    }
 * </tt>
 * </pre>
 * 
 * @see architecture.ee.event.internal.AnnotatedMethodsListenerHandler
 * @since 2.0
 */

@Retention(RUNTIME)
@Target(METHOD)
@Documented
public @interface EventListener {
}
