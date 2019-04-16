package util;

import java.io.*;
import java.util.Properties;

/**
 * @Author CheneyJin
 * @Time 2019-01-14-13:23
 * @Email cheneyjin@outlook.com
 */
public class ConfigUtil implements AutoCloseable {

    private InputStream fis;
    private Properties properties = new Properties();

    public ConfigUtil() throws Exception {
        if (!exist()) {
            throw new Exception("read settings failed.");
        }
    }

    public static String root() {
        return new File("").getAbsolutePath();
    }

    private boolean exist() {
        try {
            fis = new BufferedInputStream(
                    new FileInputStream(root() + "/" + "settings.ini"));
            properties.load(fis);
            System.out.println(properties.propertyNames());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getValue(String key, int defaultVar) {
        String value = properties.getProperty(key, String.valueOf(defaultVar));
        int result = -1;
        if (!(null == value)) {
            value = value.trim();
            result = Integer.parseInt(value);
        }
        System.out.println(key + " value is: " + result);
        return result;
    }

    public boolean getValueBool(String key, boolean defaultVar) {
        String value = properties.getProperty(key, defaultVar ? "1" : "0");
        boolean result = false;
        if (!(null == value)) {
            value = value.trim();
            result = "1".equals(value);
        }
        System.out.println(key + " value is: " + result);
        return result;
    }

    @Override
    public void close() throws Exception {
        if (fis != null) {
            fis.close();
        }
    }

    public static boolean isNumeric(String str) {
        for (int i = str.length(); --i >= 0; ) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
