package desktopApp;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableRow;

/**
 * Item change makes binding between table item and table row, so that we can force
 * UpdateItem call when active property changes. We don't
 * have to force the update it's skin with visibility true/false
 * trick. ex.
 *
 *  ((TableColumn) getTableView().getColumns().get(0)).setVisible(false);
 *  ((TableColumn) getTableView().getColumns().get(0)).setVisible(true);
 *
 * Binding created is a weak reference so garbage collection should work properly.
 *
 * @author jaakkju
 * @param <T>
 */
public class ToggleTableRow<T extends AbstractToggleTableItem> extends TableRow<T> {

    private final SimpleBooleanProperty active = new SimpleBooleanProperty();

    /* Current Item which is bound to this table row */
    private T currentItem = null;

    public ToggleTableRow() {
        super();

        active.addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {

            /* If item is the same we know that the update came
                from actual property change and not from the row reuse */

            if (currentItem != null && currentItem == getItem()) {
                updateItem(getItem(), isEmpty());
            }
        });

        /*
            JavaFX reuses rows in the same way as it reuses table cells,
            item behind the row changes ex. if row if scrolled so that it is not visible.
         */
        itemProperty().addListener((ObservableValue<? extends T> observable, T oldValue, T newValue) -> {

            /* When the item changes, we unbind the
                old property and start listening to the new */

            active.unbind();

            if (newValue != null) {
                active.bind(newValue.activeProperty());

                /* We change current item only after
                    binding since since it trickers change in the properties */
                currentItem = newValue;
            }
        });
    }

    @Override
    final protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);

        /* Setting disabled sets row's pseudoclass to disabled, we can use that
           value to assign inactive styling to the row. */

        setDisable(item != null && !item.isActive());
        setEditable(item != null && !item.isActive());
    }
}
