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
package net.sunyijun.resource.library;

/**
 * Library path utils.
 *
 * @author yijun.sun
 * @since 0.0.1
 */
public class BaseUtil {

    /**
     * Get "java.library.path" in system property.
     *
     * @return all library paths array.
     */
    public static String[] getLibraryPaths() {
        String libraryPathString = System.getProperty("java.library.path");
        String pathSeparator = System.getProperty("path.separator");
        return libraryPathString.split(pathSeparator);
    }

    /**
     * Get just one(first) library path in "java.library.path".
     *
     * @return one library path.
     */
    public static String getOneLibraryPath() {
        return getLibraryPaths()[0];
    }

}
