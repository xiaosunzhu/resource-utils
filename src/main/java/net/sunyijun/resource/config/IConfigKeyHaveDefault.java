/*
 * Copyright 2015 yijun.sun
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sunyijun.resource.config;

/**
 * Config key interface, that can provide default value
 * when there is not config in config file.<p/>
 * Key must implement {@link #getDefaultValueStr()} method to
 * provide default value string for current key if not config in properties file.
 *
 * @author yijun.sun
 * @since 0.0.1
 */
public interface IConfigKeyHaveDefault extends IConfigKey {

    /**
     * @return default value string for current key if not config in properties file
     */
    String getDefaultValueStr();

}
