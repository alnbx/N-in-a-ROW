package desktopApp;

import javafx.beans.property.SimpleBooleanProperty;

/**
 * Simple abstract class that has active boolean property
 *
 * @author jaakkju
 */
abstract public class AbstractToggleTableItem {

    private final SimpleBooleanProperty active = new SimpleBooleanProperty(true);

    public AbstractToggleTableItem(boolean active) {
        this.active.set(active);
    }

    public SimpleBooleanProperty activeProperty() {
        return active;
    }

    public void setActive(boolean active) {
        this.active.set(active);
    }

    public boolean isActive() {
        return active.get();
    }
}
