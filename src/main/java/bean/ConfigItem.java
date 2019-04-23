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
    private SimpleStringProperty  configValue;

    public ConfigItem(String configKey, String configValue) {
        this.configKey = new SimpleStringProperty(configKey);
        this.configValue = new SimpleStringProperty(configValue);
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

    public String getConfigValue() {
        return configValue.get();
    }

    public SimpleStringProperty configValueProperty() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue.set(configValue);
    }

    @Override
    public String toString() {
        return "ConfigItem{" +
                "configKey=" + configKey +
                ", configValue=" + configValue +
                '}';
    }
}
