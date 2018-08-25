package desktopApp;

import javafx.scene.control.Button;

public class ColumnButton extends Button {
    // to keep consistency with gameLogic, columns counting statrs from 1
    final private int col;
    final private ButtonType buttonType;

    public ColumnButton(int col, ButtonType buttonType) {
        super();
        this.col = col;
        this.buttonType = buttonType;
    }

    public int getCol() {
        return col;
    }

    public ButtonType getButtonType() {
        return buttonType;
    }
}
