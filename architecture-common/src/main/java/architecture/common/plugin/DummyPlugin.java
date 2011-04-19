/*
 * Copyright 2010, 2011 INKIUM, Inc.
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
package architecture.common.plugin;


public class DummyPlugin
    implements Plugin
{
    private String name;
    
    public DummyPlugin(String name)
    {
        this.name = name;
    }

    public void destroy()
    {
    }

    public void initialize()
    {
    }

    public boolean equals(Object o)
    {
        if(this == o)
            return true;
        if(o == null || getClass() != o.getClass())
        {
            return false;
        } else
        {
            DummyPlugin that = (DummyPlugin)o;
            return name == null ? that.name != null : !name.equals(that.name);
        }
    }

    public int hashCode()
    {
        return name == null ? 0 : name.hashCode();
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("DummyPlugin");
        sb.append("{name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }

}

