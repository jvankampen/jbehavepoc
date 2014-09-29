package jbehavepoc;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Provides access to resources, either as files on the filesystem or as
 * resources in a jar or website.
 */
public class Res {

    /**
     * Get a resource from the classpath if it exists. If such a resource does
     * not exist on the classpath, will retrieve the URL of the file from the
     * resource folder. If the resource does not exist there, then it will be
     * retrieved as a file from the working directory.
     * 
     * @param name
     *            The resource to retrieve.
     * @return The URL of the resource or null if it does not exist.
     */
    public static URL get(String name) {
        URL res = Thread.currentThread().getContextClassLoader()
                .getResource(name);
        if (res == null) {
            File resFile = new File("res", name);
            if (!resFile.exists()) {
                resFile = new File(name);
            }
            try {
                res = resFile.getCanonicalFile().toURI().toURL();
            } catch (MalformedURLException e) {
                throw new RuntimeException("Could not transform \"" + name
                        + "\" into a URL");
            } catch (IOException e) {
                throw new RuntimeException(
                        "Unable to retrieve teh canonical path for \"" + name
                        + "\"");
            }
        }

        return res;
    }

    /**
     * Get a resource from the classpath if it exists, and if it is not a local
     * file, it will copy the resource as a temporary file stored in the temp
     * directory.
     * 
     * @param name
     *            The resource to retrieve.
     * @return The resource as a file.
     * @throws IOException
     *             If the file could not be acquired.
     */
    public static File getFile(String name) throws IOException {
        File res = new File(name);
        if (!res.exists()) {
            URL resUrl = get(name);
            if (resUrl != null) {
                String filename = getFileFriendlyName(name);
                int extIndex = filename.lastIndexOf(".");
                String prefix = filename;
                String ext = "";

                if (extIndex >= 1) {
                    ext = filename.substring(extIndex);
                    prefix = filename.substring(0, extIndex);
                }
                res = File.createTempFile(prefix, ext);
                FileUtils.copyURLToFile(resUrl, res);
            } else {
                res = null;
            }
        }

        return res;
    }

    /**
     * Get a filename based upon the resource name, if possible.
     * 
     * @param resourceName
     *            The resource name to base the file name upon.
     * @return A file name appropriate for the target OS.
     */
    private static String getFileFriendlyName(String resourceName) {
        File res = new File(resourceName);

        return res.getName();
    }
}
