package crea;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class ResourceReader {

    public final static String CONFIG_FILE = "config.properties";

    public File getResourceFile(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(fileName);

        if (resource == null) {
            System.err.println("Resource is not found!");
            System.exit(1);
        }

        return new File(resource.getFile());
    }

    public String getProperty(String propertyKey) {
        File file = getResourceFile(CONFIG_FILE);
        String propertyValue = "";
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            Properties properties = new Properties();

            properties.load(fileInputStream);
            propertyValue = properties.getProperty(propertyKey);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return propertyValue;
    }

}
