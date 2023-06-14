package client.gui;

import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

public class ResourceFactory {
    private ObjectProperty<ResourceBundle> resources = new SimpleObjectProperty<>();
    /**
     * Все доступные локали
     */
    public static final HashMap<String, Locale> LOCALE_MAP = new HashMap<>();

    static { //это что, статический блок инициализации?
        LOCALE_MAP.put("English", new Locale("en", "IE"));
        LOCALE_MAP.put("Русский", new Locale("ru", "RU"));
        LOCALE_MAP.put("Беларускі", new Locale("be", "BY"));
        LOCALE_MAP.put("Hrvatski", new Locale("hr", "HR"));
    }

    public ObjectProperty<ResourceBundle> resourcesProperty() {
        return resources;
    }

    public final ResourceBundle getResources() {
        return resources.get();
    }

    public final void setResources(ResourceBundle resources) {
        this.resources.set(resources);
    }

    public StringBinding getStringBinding(String key) {
        return new StringBinding() {
            {
                bind(resources);
            }

            @Override
            public String computeValue() {
                return getResources().getString(key);
            }
        };
    }
}
