package engine;

import common.GameSettings;
import common.MoveType;

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

        MoveType moveType = popout ? MoveType.POPOUT : MoveType.INSERT;
        boolean turnPlayed =  popout ? playHumanPlayerPopout(col, playerID) : playHumanPlayerRegular(col, playerID);
        if (turnPlayed) {
            //record move
            this.lastMovePlayed = new Move(playerID, col, timeFromBegining(), moveType);
            playedMoves.add(this.lastMovePlayed);
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
        // columns counting statrs from 1, as ComputerPlayer makes a pseudo move in column 0
        int rand = r.nextInt(board.getCols());

        if (!super.board.isPopoutAvaliableForPlayer(playerID)) { return false; }

        while(!board.playPopoutMove(rand, playerID)) { rand = r.nextInt(board.getCols()); }

        //record move
        // columns counting statrs from 1, as ComputerPlayer makes a pseudo move in column 0
        this.lastMovePlayed = new Move(playerID, rand + 1, timeFromBegining(), MoveType.POPOUT);
        playedMoves.add(this.lastMovePlayed);


        //todo: After Popout check winning move must be for all column...
        Set<Integer> winners = checkWinningMove(rand);
        if (!winners.isEmpty()) {
            board.setWinner(winners);
            board.setHasWinner(true);
            this.hasWinner = true;
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

        for (int i = board.getRows() - 1; i >= 0; i--) {
            if ( board.leftRightSequencePopout(col, i) == targetSequence    ||
                    board.upDownSequencePopout(col, i) == targetSequence       ||
                    board.diagonalDownSequencePopout(col, i) == targetSequence ||
                    board.diagonalUpSequencePopout(col, i) == targetSequence)
            { res.add(board.getPlayerInDisc(col, i)); }
        }

        return res;
    }
}
