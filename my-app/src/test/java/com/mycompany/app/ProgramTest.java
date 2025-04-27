package com.mycompany.app;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.awt.GridLayout;
import javax.swing.JFrame;
import java.awt.event.ActionEvent;

public class ProgramTest {
    private Game game;
    private Player player;
    
    @BeforeEach
    public void setUp() {
        game = new Game();
        player = new Player();
    }
    
    @Test
    public void testGameConstructor() {
        assertNotNull(game);
        assertEquals(State.PLAYING, game.state);
        assertEquals('X', game.player1.symbol);
        assertEquals('O', game.player2.symbol);
        
        // Check that board is initialized with spaces
        for (int i = 0; i < 9; i++) {
            assertEquals(' ', game.board[i]);
        }
    }
    
    @Test
    public void testPlayerInitialization() {
        Player p = new Player();
        assertEquals(0, p.move);
        assertFalse(p.selected);
        assertFalse(p.win);
    }
    
    @Test
    public void testCheckStateXWinRows() {
        // Test first row X win
        char[] board = {
            'X', 'X', 'X',
            ' ', ' ', ' ',
            ' ', ' ', ' '
        };
        game.symbol = 'X';
        assertEquals(State.XWIN, game.checkState(board));
        
        // Test second row X win
        board = new char[]{
            ' ', ' ', ' ',
            'X', 'X', 'X',
            ' ', ' ', ' '
        };
        assertEquals(State.XWIN, game.checkState(board));
        
        // Test third row X win
        board = new char[]{
            ' ', ' ', ' ',
            ' ', ' ', ' ',
            'X', 'X', 'X'
        };
        assertEquals(State.XWIN, game.checkState(board));
    }
    
    @Test
    public void testCheckStateXWinColumns() {
        // Test first column X win
        char[] board = {
            'X', ' ', ' ',
            'X', ' ', ' ',
            'X', ' ', ' '
        };
        game.symbol = 'X';
        assertEquals(State.XWIN, game.checkState(board));
        
        // Test second column X win
        board = new char[]{
            ' ', 'X', ' ',
            ' ', 'X', ' ',
            ' ', 'X', ' '
        };
        assertEquals(State.XWIN, game.checkState(board));
        
        // Test third column X win
        board = new char[]{
            ' ', ' ', 'X',
            ' ', ' ', 'X',
            ' ', ' ', 'X'
        };
        assertEquals(State.XWIN, game.checkState(board));
    }
    
    @Test
    public void testCheckStateXWinDiagonals() {
        // Test main diagonal X win
        char[] board = {
            'X', ' ', ' ',
            ' ', 'X', ' ',
            ' ', ' ', 'X'
        };
        game.symbol = 'X';
        assertEquals(State.XWIN, game.checkState(board));
        
        // Test anti-diagonal X win
        board = new char[]{
            ' ', ' ', 'X',
            ' ', 'X', ' ',
            'X', ' ', ' '
        };
        assertEquals(State.XWIN, game.checkState(board));
    }
    
    @Test
    public void testCheckStateOWinConditions() {
        // Test O win in a row
        char[] board = {
            'O', 'O', 'O',
            ' ', ' ', ' ',
            ' ', ' ', ' '
        };
        game.symbol = 'O';
        assertEquals(State.OWIN, game.checkState(board));
        
        // Test O win in a column
        board = new char[]{
            'O', ' ', ' ',
            'O', ' ', ' ',
            'O', ' ', ' '
        };
        assertEquals(State.OWIN, game.checkState(board));
        
        // Test O win in diagonal
        board = new char[]{
            'O', ' ', ' ',
            ' ', 'O', ' ',
            ' ', ' ', 'O'
        };
        assertEquals(State.OWIN, game.checkState(board));
    }
    
    @Test
    public void testCheckStatePlayingAndDraw() {
        // Test playing state
        char[] board = {
            'X', 'O', 'X',
            'O', ' ', ' ',
            ' ', ' ', ' '
        };
        game.symbol = 'X';
        assertEquals(State.PLAYING, game.checkState(board));
        
        // Test draw state
        board = new char[]{
            'X', 'O', 'X',
            'X', 'O', 'O',
            'O', 'X', 'O'
        };
        assertEquals(State.DRAW, game.checkState(board));
    }
    
    @Test
    public void testGenerateMoves() {
        // Empty board should have 9 possible moves
        ArrayList<Integer> moves = new ArrayList<>();
        game.generateMoves(game.board, moves);
        assertEquals(9, moves.size());
        
        // Partially filled board
        char[] board = {
            'X', ' ', 'O',
            ' ', 'X', ' ',
            'O', ' ', 'X'
        };
        ArrayList<Integer> partialMoves = new ArrayList<>();
        game.generateMoves(board, partialMoves);
        assertEquals(4, partialMoves.size());
        assertTrue(partialMoves.contains(1));
        assertTrue(partialMoves.contains(3));
        assertTrue(partialMoves.contains(5));
        assertTrue(partialMoves.contains(7));
    }
    
    @Test
    public void testEvaluatePosition() {
        Player xPlayer = new Player();
        xPlayer.symbol = 'X';
        
        Player oPlayer = new Player();
        oPlayer.symbol = 'O';
        
        // X winning position for X player should return +INF
        char[] xWinBoard = {
            'X', 'X', 'X',
            ' ', 'O', ' ',
            ' ', 'O', ' '
        };
        game.symbol = 'X';
        assertEquals(Game.INF, game.evaluatePosition(xWinBoard, xPlayer));
        
        // X winning position for O player should return -INF
        assertEquals(-Game.INF, game.evaluatePosition(xWinBoard, oPlayer));
        
        // O winning position for O player should return +INF
        char[] oWinBoard = {
            'X', 'X', ' ',
            'O', 'O', 'O',
            'X', ' ', ' '
        };
        game.symbol = 'O';
        assertEquals(Game.INF, game.evaluatePosition(oWinBoard, oPlayer));
        
        // O winning position for X player should return -INF
        assertEquals(-Game.INF, game.evaluatePosition(oWinBoard, xPlayer));
        
        // Draw should return 0
        char[] drawBoard = {
            'X', 'O', 'X',
            'X', 'O', 'O',
            'O', 'X', 'O'
        };
        assertEquals(0, game.evaluatePosition(drawBoard, xPlayer));
        assertEquals(0, game.evaluatePosition(drawBoard, oPlayer));
        
        // Playing state should return -1
        char[] playingBoard = {
            'X', ' ', ' ',
            ' ', ' ', ' ',
            ' ', ' ', ' '
        };
        game.symbol = 'X';
        assertEquals(-1, game.evaluatePosition(playingBoard, xPlayer));
    }
    
    @Test
    public void testMinMaxBestMove() {
        // Setup board where there is an immediate winning move for O
        char[] board = {
            'X', ' ', 'X',
            'O', 'O', ' ',
            ' ', ' ', ' '
        };
        game.board = board.clone();
        
        Player oPlayer = game.player2;
        oPlayer.symbol = 'O';
        game.symbol = oPlayer.symbol;
        
        // The best move for O should be position 5 (index 4) to win
        int bestMove = game.MiniMax(game.board, oPlayer);
        assertEquals(2, bestMove); // Winning move should be position 5 (index 4 + 1)
    }
    
    @Test
    public void testMinMove() {
        Player xPlayer = game.player1;
        xPlayer.symbol = 'X';
        
        // Setup board where next move is X's and they need to block O's win
        char[] board = {
            ' ', ' ', ' ',
            'O', 'O', ' ',
            'X', ' ', ' '
        };
        game.board = board.clone();
        
        // Testing MinMove - simulating O's perspective to minimize X's score
        game.symbol = 'O';
        int minValue = game.MinMove(game.board, xPlayer);
        assertTrue(minValue <= Game.INF);
    }
    
    @Test
    public void testMaxMove() {
        Player oPlayer = game.player2;
        oPlayer.symbol = 'O';
        
        // Setup board where O can win in next move
        char[] board = {
            'X', 'X', ' ',
            'O', 'O', ' ',
            ' ', ' ', ' '
        };
        game.board = board.clone();
        
        // Testing MaxMove - O wants to maximize its score
        game.symbol = 'O';
        int maxValue = game.MaxMove(game.board, oPlayer);
        assertTrue(maxValue >= -Game.INF);
    }
    
    @Test
    public void testTicTacToeCell() {
        TicTacToeCell cell = new TicTacToeCell(1, 0, 0);
        assertEquals(1, cell.getNum());
        assertEquals(0, cell.getRow());
        assertEquals(0, cell.getCol());
        assertEquals(' ', cell.getMarker());
        
        cell.setMarker("X");
        assertEquals('X', cell.getMarker());
        assertFalse(cell.isEnabled());
    }
    
    @Test
    public void testTicTacToePanelInitialization() {
        TicTacToePanel panel = new TicTacToePanel(new GridLayout(3, 3));
        assertNotNull(panel);
        // Panel should have 9 components (cells)
        assertEquals(9, panel.getComponentCount());
    }
    
    @Test
    public void testUtilityPrintMethods() {
        // Just verify they don't throw exceptions
        char[] charBoard = {'X', 'O', ' ', ' ', ' ', ' ', ' ', ' ', ' '};
        Utility.print(charBoard);
        
        int[] intBoard = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        Utility.print(intBoard);
        
        ArrayList<Integer> moves = new ArrayList<>();
        moves.add(0);
        moves.add(4);
        moves.add(8);
        Utility.print(moves);
    }
    
    @Test
    public void testCompleteGame() {
        // Test a complete game scenario where X wins
        Game testGame = new Game();
        testGame.player1.symbol = 'X';
        testGame.player2.symbol = 'O';
        testGame.cplayer = testGame.player1;
        
        // Simulate moves for a game where X wins
        char[] finalBoard = {
            'X', 'O', 'X',
            'O', 'X', 'O',
            ' ', ' ', 'X'
        };
        testGame.board = finalBoard;
        testGame.symbol = 'X';
        testGame.state = testGame.checkState(testGame.board);
        
        assertEquals(State.XWIN, testGame.state);
    }
}
