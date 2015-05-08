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


import net.sunyijun.resource.ResourceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>Methods to prepare(copy or load) libraries.
 * </p>
 * If library file will change, must use {@link #initialLibFiles} method before
 * {@link #loadLibFiles}. If won't changed at all, just {@link #loadLibFiles}.
 * This may call System.loadLibrary()<br>
 * If library file is dll or so files, can use {@link #loadDllFiles} or {@link #loadSoFiles}
 * instead of {@link #loadLibFiles}.
 *
 * @author yijun.sun
 * @since 0.0.1
 */
public class PrepareLibs {

    private final static String ONE_SYSTEM_LIBRARY_PATH = BaseUtil.getOneLibraryPath();

    /**
     * <p>Initial library resources: copy resources from class path
     * to library path either library file exists or not exists in library path.</p>
     * Use this method can use newest library file.
     *
     * @param libAbsoluteClassPaths library absolute class path(start with"/"), include file name.
     * @throws IOException
     */
    public static void initialLibFiles(String... libAbsoluteClassPaths) throws IOException {
        for (String libAbsoluteClassPath : libAbsoluteClassPaths) {
            prepareLibFile(true, libAbsoluteClassPath);
        }
    }

    /**
     * Load dll libraries which placed in class path.
     *
     * @param dllAbsoluteClassPaths dll library absolute class path(start with"/"), include file name.
     * @return dll library class paths that failed to load. If all success, return array size is 0.
     */
    public static String[] loadDllFiles(String... dllAbsoluteClassPaths) {
        return loadLibFiles(".dll", dllAbsoluteClassPaths);
    }

    /**
     * Load so libraries which placed in class path.
     *
     * @param dllAbsoluteClassPaths so library absolute class path(start with"/"), include file name.
     * @return so library class paths that failed to load. If all success, return array size is 0.
     */
    public static String[] loadSoFiles(String... dllAbsoluteClassPaths) {
        return loadLibFiles(".dll", dllAbsoluteClassPaths);
    }

    /**
     * Load libraries which placed in class path.
     *
     * @param fileSuffix            suffix include".". eg:".dll"
     * @param libAbsoluteClassPaths library absolute class path(start with"/"), include file name.
     * @return library class paths that failed to load. If all success, return array size is 0.
     */
    public static String[] loadLibFiles(String fileSuffix, String... libAbsoluteClassPaths) {
        Set<String> failedDllPaths = new HashSet<String>();
        for (String libAbsoluteClassPath : libAbsoluteClassPaths) {
            String libraryName = libAbsoluteClassPath
                    .substring(libAbsoluteClassPath.lastIndexOf("/") + 1, libAbsoluteClassPath.lastIndexOf(fileSuffix));
            try {
                prepareLibFile(true, libAbsoluteClassPath);
                System.loadLibrary(libraryName);
                LOGGER.info("Success load library: " + libraryName);
            } catch (Exception e) {
                LOGGER.info("Load library: " + libraryName + " failed!", e);
                failedDllPaths.add(libAbsoluteClassPath);
            }
        }
        return failedDllPaths.toArray(new String[failedDllPaths.size()]);
    }

    /**
     * <p>Prepare library resources: copy resources from class path
     * to library path when library file not exists in library path.</p>
     * Use this method can avoid copy same lib more than once.
     * But if lib changed, it will not work till use {@link #initialLibFiles}.
     *
     * @param libAbsoluteClassPaths library absolute class path(start with"/"), include file name.
     * @throws IOException
     */
    public static void prepareLibFiles(String... libAbsoluteClassPaths) throws IOException {
        for (String libAbsoluteClassPath : libAbsoluteClassPaths) {
            prepareLibFile(false, libAbsoluteClassPath);
        }
    }

    private static void prepareLibFile(boolean recoverExistsFile, String libAbsoluteClassPath) throws IOException {
        File targetFile = new File(ONE_SYSTEM_LIBRARY_PATH + File.separator +
                libAbsoluteClassPath.substring(libAbsoluteClassPath.lastIndexOf("/") + 1));
        if (!recoverExistsFile && targetFile.exists()) {
            LOGGER.info("Skip copy lib file " + libAbsoluteClassPath);
            return;
        }
        LOGGER.info("Copy lib file " + libAbsoluteClassPath + " to " + targetFile.getAbsolutePath());
        try {
            ResourceUtil.copyResourceToFile(libAbsoluteClassPath, targetFile);
        } catch (IOException e) {
            LOGGER.error("Copy lib file " + libAbsoluteClassPath + " failed.");
            throw e;
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(PrepareLibs.class);

}