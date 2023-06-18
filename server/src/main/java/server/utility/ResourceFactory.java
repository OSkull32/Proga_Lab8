package server.utility;

import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public class ResourceFactory {
    private static ObjectProperty<ResourceBundle> resources = new SimpleObjectProperty<>();
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

    public static ObjectProperty<ResourceBundle> resourcesProperty() {
        return resources;
    }

    public static ResourceBundle getResources() {
        return resources.get();
    }

    public static void setResources(ResourceBundle resources) {
        ResourceFactory.resources.set(resources);
    }

    public static synchronized StringBinding getStringBinding(String language, String key) {
        setResources(ResourceBundle.getBundle("bundles.commands", Objects.requireNonNullElse(LOCALE_MAP.get(language), LOCALE_MAP.get("English"))));
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
