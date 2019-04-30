package util;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @Author CheneyJin
 * @Time 2019-01-14-13:23
 * @Email cheneyjin@outlook.com
 */
public class ConfigUtil {

    private Properties properties = new Properties();
    private List<String> text = new ArrayList<>();

    public ConfigUtil() throws Exception {
        if (!exist()) {
            throw new Exception("read settings failed.");
        }
    }

    public ConfigUtil(File file) throws Exception {
        if (!exist(file)) {
            throw new Exception("load config failed");
        }
    }

    public static String root() {
        return new File("").getAbsolutePath();
    }

    private boolean exist(File file) {
        try (InputStream fis = new BufferedInputStream(new FileInputStream(file))) {
            properties.load(fis);
            loadText(file);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean exist() {
        File file = new File(root() + "/" + "settings.ini");
        try (InputStream fis = new BufferedInputStream(
                new FileInputStream(file))) {
            properties.load(fis);
            loadText(file);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void loadText(File file) {
        try (BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                text.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
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

    public static void changeAttribute(Fishing.Properties props, String propName, int newValue) {
        Class cls = props.getClass();
        try {
            Field field = cls.getDeclaredField(propName);
            if (field.getGenericType().equals(boolean.class)) {
                field.set(props, int2Bool(newValue));
            } else {
                field.set(props, newValue);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public String getAnnotation(String key) {
        int len = text.size();
        for (int i = 0; i < len; i++) {
            if (text.get(i).contains(key) && i != 0) {
                String line = text.get(i - 1);
                int hasSharp = line.indexOf('#');
                if (hasSharp >= 0) {
                    return line.substring(hasSharp + 1);
                }
            }
        }
        return "";
    }

    public static boolean isNumeric(String str) {
        for (int i = str.length(); --i >= 0; ) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static int bool2Int(boolean bool) {
        return bool ? 1 : 0;
    }

    public static boolean int2Bool(int integer) {
        return integer > 0;
    }
}
