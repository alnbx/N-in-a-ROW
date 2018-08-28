package desktopApp;

import javafx.scene.control.Button;

public class ColumnButton extends Button {
    // to keep consistency with gameLogic, columns counting statrs from 1
    final private int col;
    final private ColButtonType buttonType;

    public ColumnButton(int col, ColButtonType buttonType) {
        super();
        this.col = col;
        this.buttonType = buttonType;
        this.setDisable(true);
    }

    public int getCol() {
        return col;
    }

    public ColButtonType getButtonType() {
        return buttonType;
    }
}
