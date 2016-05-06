package org.al.game;

import ar.com.hjg.pngj.ImageInfo;
import ar.com.hjg.pngj.ImageLineHelper;
import ar.com.hjg.pngj.ImageLineInt;
import ar.com.hjg.pngj.PngWriter;
import org.al.api.Api;
import org.al.generic.Coords;
import org.al.quadrisbase.Constants;
import org.al.quadrisbase.Piece;
import org.al.quadrisexceptions.NonexistentTetrisPieceException;
import org.al.statisticsutils.Deviation;
import org.al.training.Situation;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final int runs = 10;

    private static boolean displayGame = true;

    private static JTextArea printedTetrisBoard;

    public static void main(String[] args) throws InterruptedException, ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException, NonexistentTetrisPieceException {
        /* App init stuff */

        init();



        
    }

    private static void init() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        // set the look and feel
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        if (displayGame) {
            printedTetrisBoard = new JTextArea("Loading...");

            final JFrame frame = new JFrame();

            printedTetrisBoard.setFont(new Font("monospaced", Font.PLAIN, 12));

            printedTetrisBoard.setColumns(10);
            printedTetrisBoard.setRows(20);

            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setLayout(new GridLayout());

            frame.getContentPane().add(printedTetrisBoard);
            frame.pack();

            frame.setVisible(true);
            frame.setLocationRelativeTo(null);
        }
    }
}
