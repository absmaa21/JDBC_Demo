package database;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DB_Properties {
    private static final Properties PROPS = new Properties();

    static {
        InputStream is = DB_Properties.class.getResourceAsStream("/db_props.properties");
        try {
            PROPS.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String prop) {
        // Returns null if the property does not exist
        return PROPS.getProperty(prop);
    }

    public static void main(String[] args) {
        System.out.println(DB_Properties.getProperty("DB_URL"));
    }
}
