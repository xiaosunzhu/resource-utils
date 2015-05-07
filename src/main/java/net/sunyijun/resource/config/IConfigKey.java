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
 * Config key interface.<p/>
 * Key must implement {@link #getKeyString()} method to
 * provide key string in properties file.<p/>
 * <p/>
 * Normally, use enum to provide config keys like this:
 * <pre>{@code
 * public enum SelfConfig implements IConfigKey {
 *     CONFIG1("str"),
 *     CONFIG2("bool"),
 *     CONFIG3("num");
 *
 *     private final String key;
 *
 *     SelfConfig(String key) {
 *         this.key = key;
 *     }
 *
 *     public String getKeyString() {
 *         return key;
 *     }
 * }
 * }<pre/>
 *
 * @author yijun.sun
 * @since 0.0.1
 */
public interface IConfigKey {

    /**
     * @return key string in properties file
     */
    String getKeyString();

}
