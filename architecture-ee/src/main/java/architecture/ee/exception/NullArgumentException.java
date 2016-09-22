/*
 * Copyright 2016 donghyuck
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

package architecture.ee.exception;

/**
 * Thrown to indicate that a method argument was <tt>null</tt> and should
 * <b>not</b> have been.
 */
public class NullArgumentException extends IllegalArgumentException {



    /** The name of the argument that was <tt>null</tt>. */
    protected final String name;

    /** The index of the argument or null if no index. */
    protected final Object index;

    /**
     * Construct a <tt>NullArgumentException</tt>.
     *
     * @param name
     *            Argument name.
     */
    public NullArgumentException(final String name) {
	super(makeMessage(name));

	this.name = name;
	this.index = null;
    }

    /**
     * Construct a <tt>NullArgumentException</tt>.
     *
     * @param name
     *            Argument name.
     * @param index
     *            Argument index.
     */
    public NullArgumentException(final String name, final long index) {
	super(makeMessage(name, new Long(index)));

	this.name = name;
	this.index = new Long(index);
    }

    /**
     * Construct a <tt>NullArgumentException</tt>.
     *
     * @param name
     *            Argument name.
     * @param index
     *            Argument index.
     */
    public NullArgumentException(final String name, final Object index) {
	super(makeMessage(name, index));

	this.name = name;
	this.index = index;
    }

    /**
     * Construct a <tt>NullArgumentException</tt>.
     */
    public NullArgumentException() {
	this.name = null;
	this.index = null;
    }

    /**
     * Get the argument name that was <tt>null</tt>.
     *
     * @return The argument name that was <tt>null</tt>.
     */
    public final String getArgumentName() {
	return name;
    }

    /**
     * Get the argument index.
     *
     * @return The argument index.
     */
    public final Object getArgumentIndex() {
	return index;
    }

    /**
     * Make a execption message for the argument name.
     */
    private static String makeMessage(final String name) {
	return "'" + name + "' is null";
    }

    /**
     * Make a execption message for the argument name and index
     */
    private static String makeMessage(final String name, final Object index) {
	return "'" + name + "[" + index + "]' is null";
    }
}
