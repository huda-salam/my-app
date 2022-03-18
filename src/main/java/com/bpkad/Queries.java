package com.bpkad;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Queries {
    private static Properties properties = null;

    private static Properties getProperties() throws IOException {
        if(properties == null) {
            FileReader reader = new FileReader("queries.properties");

            properties = new Properties();
            try {
                properties.load(reader);
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