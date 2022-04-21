package com.bpkad;

//import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Queries {
    private static Properties properties = null;

    private static Properties getProperties() throws IOException {
        if(properties == null) {
            ClassLoader classLoader = Queries.class.getClassLoader();
            java.io.InputStream  stream = classLoader.getResourceAsStream("queries.properties");
            //FileReader reader = new FileReader("queries.properties");

            properties = new Properties();
            try {
                //properties.load(reader);
                properties.load(stream);
            }
            catch(IOException ex) {
                throw ex;
            }
        }

        return properties;
    }

    public static String getQuery(String queryId) throws IOException {
        return getProperties().getProperty(queryId);
    }
}