package engine;

import common.GameSettings;
import common.GameVariant;
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
    protected boolean playHumanPlayer(int col, boolean popout) {
        int playerID = this.currentPlayer.getId();
        col--;

        MoveType moveType = popout ? MoveType.POPOUT : MoveType.INSERT;
        boolean turnPlayed = popout ? playHumanPlayerPopout(col, playerID) : playHumanPlayerRegular(col, playerID);
        if (turnPlayed) {
            //record move
            this.lastMovePlayed = new Move(playerID, col, timeFromBegining(), moveType);
            playedMoves.add(this.lastMovePlayed);
            getPlayerById(playerID).increaseNumberOfTurnsPlayed();

            Set<Integer> winners = checkWinningMove(col);
            if (!winners.isEmpty()) {
                board.setWinner(winners);
                board.setHasWinner(true);
                this.hasWinner = true;
            }

            return true;
        }

        return false;
    }

    private boolean playHumanPlayerPopout(int col, int playerID) {
        if (!this.board.playPopoutMove(col, playerID)) { return false; }

        this.lastMovePlayed = new Move(playerID, col, timeFromBegining(), MoveType.POPOUT);
        playedMoves.add(this.lastMovePlayed);

        Set<Integer> winners = checkWinningMove(col);
        if (!winners.isEmpty()) {
            board.setWinner(winners);
            board.setHasWinner(true);
            this.hasWinner = true;
        }

        return true;
    }

    private boolean playHumanPlayerRegular(int col, int playerID) {
        if (!this.board.playMove(col, playerID)) { return false; }

        this.lastMovePlayed = new Move(playerID, col, timeFromBegining(), MoveType.INSERT);
        playedMoves.add(this.lastMovePlayed);

        Set<Integer> winners = checkWinningMove(col);
        if (!winners.isEmpty()) {
            board.setWinner(winners);
            board.setHasWinner(true);
            this.hasWinner = true;
        }

        return true;
    }

    private boolean playComputerPlayerRegular() {
        Random r = new Random();
        int playerID = currentPlayer.getId();
        // columns counting statrs from 1, as ComputerPlayer makes a pseudo move in column 0
        int rand = r.nextInt(board.getCols());

        while(!board.playMove(rand, playerID)) { rand = r.nextInt(board.getCols()); }
        this.lastMovePlayed = new Move(playerID, rand, timeFromBegining(), MoveType.INSERT);
        playedMoves.add(this.lastMovePlayed);

        Set<Integer> winners = checkWinningMove(rand);
        if (!winners.isEmpty()) {
            board.setWinner(winners);
            board.setHasWinner(true);
            this.hasWinner = true;
        }

        return true;
    }

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
        this.lastMovePlayed = new Move(playerID, rand, timeFromBegining(), MoveType.POPOUT);
        playedMoves.add(this.lastMovePlayed);

        Set<Integer> winners = checkWinningMove(rand);
        if (!winners.isEmpty()) {
            board.setWinner(winners);
            board.setHasWinner(true);
            this.hasWinner = true;
        }

        return true;
    }

    @Override
    protected boolean playComputerPlayer() {
        Random r = new Random();
        int playerID = currentPlayer.getId();
        int rand = r.nextInt(2);
        boolean canPopOut = false;

        if (!this.board.isFull() && 1 == rand) {
            return playComputerPlayerRegular();
        } else {
            canPopOut = playComputerPlayerPopout();
        }

        if (!canPopOut) {
            return playComputerPlayerRegular();
        }

        return true;
    }

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

    public GameVariant getGameVariant() { return GameVariant.POPOUT; }
}
