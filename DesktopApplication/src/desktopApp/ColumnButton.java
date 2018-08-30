package desktopApp;

import common.MoveType;
import javafx.scene.control.Button;

public class ColumnButton extends Button {
    // to keep consistency with gameLogic, columns counting statrs from 1
    // as ComputerPlayer makes a pseudo move in column 0
    final private int col;
    final private MoveType buttonType;

    public ColumnButton(int col, MoveType buttonType) {
        super();
        this.col = col;
        this.buttonType = buttonType;
        this.setDisable(true);
    }

    public int getCol() {
        return col;
    }

    public MoveType getButtonType() {
        return buttonType;
    }
}
