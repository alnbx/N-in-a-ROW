package engine;

import common.GameSettings;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

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

        boolean turnPlayed =  popout ? playHumanPlayerPopout(col, playerID) : playHumanPlayerRegular(col, playerID);
        if (turnPlayed) {
            //record move
            this.lastMovePlayed = new Move(playerID, col, timeFromBegining());
            playedMoves.add(lastMovePlayed);
            getPlayerById(playerID).increaseNumberOfTurnsPlayed();

            if (popout) {
                if (checkWinningMove(col, playerID)) {
                    board.setWinner(playerID);
                    board.setHasWinner(true);
                    this.hasWinner = true;
                }
            } else {
                Set<Integer> winners = checkWinningMove(col);
                if (!winners.isEmpty()) {
                    board.setWinner(winners);
                    board.setHasWinner(true);
                }
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

        this.lastMovePlayed = new Move(playerID, rand, timeFromBegining());
        playedMoves.add(lastMovePlayed);


        //todo: After Popout check winning move must be for all column...
        Set<Integer> winners = checkWinningMove(rand);
        if (!winners.isEmpty()) {
            board.setWinner(winners);
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
    private Set<Integer> checkWinningMove(int col)
    {
        int targetSequence = this.gameSettings.getTarget();
        Set<Integer> res = new HashSet<>();

        for (int i = board.getRows() - 1; i > 0; i++) {
            if ( board.leftRightSequencePopout(col, i) == targetSequence    ||
                    board.upDownSequencePopout(col, i) == targetSequence       ||
                    board.diagonalDownSequencePopout(col, i) == targetSequence ||
                    board.diagonalUpSequencePopout(col, i) == targetSequence)
            { res.add(board.getPlayerInDisc(col, i)); }
        }

        return res;
    }
}
