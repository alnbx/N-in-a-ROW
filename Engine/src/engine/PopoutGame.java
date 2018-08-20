package engine;

import common.GameSettings;

import java.util.Random;

public class PopoutGame extends Game
{
    public PopoutGame(GameSettings gameSettings) {
        super(gameSettings);
    }

    @Override
    protected boolean playHumanPlayer(int col, boolean popout)
    {
        int playerID = this.currentPlayer.getId();
        col--;

        boolean turnPlayed =  popout ? playHumanPlayerRegular(col, playerID) : playHumanPlayerPopout(col, playerID);
        if (turnPlayed) {
            //record move
            playedMoves.add(new Move(playerID, col, timeFromBegining()));
            getPlayerById(playerID).increaseNumberOfTurnsPlayed();

            if (checkWinningMove(col, playerID)) {
                board.setWinner(playerID);
                board.setHasWinner(true);
                this.hasWinner = true;
            }
            return true;
        }

        return false;
    }

    private boolean playHumanPlayerPopout(int col, int playerID) { return board.playPopoutMove(col, playerID); }

    private boolean playHumanPlayerRegular(int col, int playerID) { return super.board.playMove(col, playerID); }

    private boolean playComputerPlayerRegular() { return super.playComputerPlayer(); }

    private boolean playComputerPlayerPopout()
    {
        Random r = new Random();
        int playerID = currentPlayer.getId();
        int rand = r.nextInt(board.getCols());

        if (!super.board.isPopoutAvaliableForPlayer(playerID)) { return false; }

        while(!board.playPopoutMove(rand, playerID)) { rand = r.nextInt(board.getCols()); }

        //record move
        //todo: record if it was a popout or not
        playedMoves.add(new Move(playerID, rand, timeFromBegining()));
        getPlayerById(playerID).increaseNumberOfTurnsPlayed();

        //todo: After Popout check winning move must be for all column...
        if (checkWinningMove(rand, playerID)) {
            board.setWinner(playerID);
            board.setHasWinner(true);
        }

        return true;
    }

    @Override
    protected boolean playComputerPlayer()
    {
        Random r = new Random();
        int playerID = currentPlayer.getId();
        int rand = r.nextInt(2);
        boolean canPopOut = false;

        if (1 == rand) { return playComputerPlayerRegular(); }
        else { canPopOut = playComputerPlayerPopout(); }

        if (!canPopOut) { return playComputerPlayerRegular(); }

        return true;
    }

    //todo: you do not know which player are you checking. handle it.
    protected boolean checkWinningMove(int col)
    {
        int targetSequence = this.gameSettings.getTarget();

        for (int i = board.getRows() - 1; i > 0; i++) {
            if ( board.leftRightSequencePopout(col, i) == targetSequence    ||
                    board.upDownSequencePopout(col, i) == targetSequence       ||
                    board.diagonalDownSequencePopout(col, i) == targetSequence ||
                    board.diagonalUpSequencePopout(col, i) == targetSequence)
            { return true; }
        }

        return false;
    }
}
