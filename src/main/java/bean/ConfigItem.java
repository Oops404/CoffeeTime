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

    public ConfigItem(String configKey, int configValue) {
        this.configKey = new SimpleStringProperty(configKey);
        this.configValue = new SimpleIntegerProperty(configValue);
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

    @Override
    public String toString() {
        return configKey.get() + "=" + configValue.get() + "\r\n";
    }
}
