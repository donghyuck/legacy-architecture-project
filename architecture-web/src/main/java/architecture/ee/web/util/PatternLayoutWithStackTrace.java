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
package architecture.ee.web.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

import javax.servlet.ServletException;

import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.ThrowableInformation;
import org.apache.velocity.exception.MethodInvocationException;


public class PatternLayoutWithStackTrace extends PatternLayout
{

    public PatternLayoutWithStackTrace()
    {
    }

    public boolean ignoresThrowable()
    {
        return false;
    }

    public String format(LoggingEvent loggingEvent)
    {
        ThrowableInformation throwableInformation = loggingEvent.getThrowableInformation();
        StringBuffer result = new StringBuffer(super.format(loggingEvent));
        appendBeforeStackTrace(result, loggingEvent);
        if(throwableInformation != null)
        {
            Throwable t = throwableInformation.getThrowable();
            appendStackTrace(result, t);
        }
        return result.toString();
    }

    void appendBeforeStackTrace(StringBuffer stringbuffer, LoggingEvent loggingevent)
    {
    }

    public static void appendStackTrace(StringBuffer buffer, Throwable t)
    {
        if(t != null)
        {
            StringWriter writer = new StringWriter();
            PrintWriter stackTrace = new PrintWriter(writer);
            t.printStackTrace(stackTrace);
            do
            {
                if(t == null)
                    break;
                t = getNonCauseUnderlying(t);
                if(t != null)
                {
                    stackTrace.print("caused by:\n");
                    t.printStackTrace(stackTrace);
                }
            } while(true);
            buffer.append(writer.getBuffer());
        }
    }

    public static Throwable getNonCauseUnderlying(Throwable t)
    {
        for(; t != null && t.getCause() != null; t = t.getCause());
        if(t != null)
            if(t instanceof InvocationTargetException)
                t = ((InvocationTargetException)t).getTargetException();
            else
            if(t instanceof MethodInvocationException)
                t = ((MethodInvocationException)t).getWrappedThrowable();
            else
            if(t instanceof ServletException)
                t = ((ServletException)t).getRootCause();
            else
/*            if(t instanceof PotentiallySecondary)
                t = ((PotentiallySecondary)t).getNestedThrowable();
            else*/
            if(t instanceof SQLException)
                t = ((SQLException)t).getNextException();
            else
                t = null;
        return t;
    }
}

