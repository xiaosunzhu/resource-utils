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
 * <p>Config key interface, that can
 * fix implementor map to one properties file.
 * So you can use {@link Configs#getHavePathSelfConfig(IConfigKeyWithPath)} to fetch value,
 * without provide path.</p>
 * Key must implement {@link #getConfigPath()} method to
 * set the key is in what path.
 *
 * @author yijun.sun
 * @since 0.0.1
 */
public interface IConfigKeyWithPath extends IConfigKey {

    /**
     * @return key in which config file path (absolute class path).
     */
    String getConfigPath();

}
