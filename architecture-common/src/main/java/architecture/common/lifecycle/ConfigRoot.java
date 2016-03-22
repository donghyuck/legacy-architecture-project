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
package architecture.common.lifecycle;

import java.io.File;

/**
 * Configuration Repository Location 에 대한 접근을 제공하는 클래스 인턴페이스.
 * 
 * @author donghyuck
 *
 */
public interface ConfigRoot {

    public static int NODE = 0;

    public abstract String getConfigRootPath();

    public abstract String getURI(String name);

    public abstract File getFile(String name);

    public String getRootURI();

}
