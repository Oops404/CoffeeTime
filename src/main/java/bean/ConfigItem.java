package bean;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * @author CheneyJin
 * @time 2019-04-19-17:34
 * @email cheneyjin@outlook.com
 */
public class ConfigItem {
    private SimpleStringProperty configKey;
    private SimpleIntegerProperty configValue;
    private SimpleStringProperty configNote;

    public ConfigItem(String configKey, int configValue, String configNote) {
        this.configKey = new SimpleStringProperty(configKey);
        this.configValue = new SimpleIntegerProperty(configValue);
        this.configNote = new SimpleStringProperty(configNote);
    }

    public ConfigItem(String configKey, int configValue) {
        this.configKey = new SimpleStringProperty(configKey);
        this.configValue = new SimpleIntegerProperty(configValue);
        this.configNote = new SimpleStringProperty("");
    }

    public String getConfigKey() {
        return configKey.get();
    }

    public SimpleStringProperty configKeyProperty() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey.set(configKey);
    }

    public int getConfigValue() {
        return configValue.get();
    }

    public SimpleIntegerProperty configValueProperty() {
        return configValue;
    }

    public void setConfigValue(int configValue) {
        this.configValue.set(configValue);
    }

    public String getConfigNote() {
        return configNote.get();
    }

    public SimpleStringProperty configNoteProperty() {
        return configNote;
    }

    public void setConfigNote(String configNote) {
        this.configNote.set(configNote);
    }

    @Override
    public String toString() {
        String noteValue = configNote.get();
        String annotation = "# " + noteValue + "\r\n";
        String prop = configKey.get() + "=" + configValue.get() + "\r\n";
        if ("".equals(noteValue)) {
            return prop;
        } else {
            return annotation + prop;
        }
    }
}
