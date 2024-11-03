package com.example.week5;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    static final String PLAYER_1 = "X";
    static final String PLAYER_2 = "O";
    boolean player1Turn = true;
    byte[][] board = new byte[3][3];
    TableLayout table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        table = findViewById(R.id.board);

        // Tablodaki butonları bulma ve dinleyici atama
        for (int i = 0; i < 3; i++) {
            TableRow row = (TableRow) table.getChildAt(i);
            for (int j = 0; j < 3; j++) {
                Button button = (Button) row.getChildAt(j);
                button.setOnClickListener(new CallListener(i, j));
            }
        }
    }

    class CallListener implements View.OnClickListener {
        int row, col;

        public CallListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void onClick(View v) {
            if (!isValidMove(row, col)) {
                Toast.makeText(MainActivity.this, "Cell already occupied", Toast.LENGTH_SHORT).show();
                return;
            }

            if (player1Turn) {
                ((Button) v).setText(PLAYER_1);
                board[row][col] = 1;
            } else {
                ((Button) v).setText(PLAYER_2);
                board[row][col] = 2;
            }

            int result = gameEnded(row, col);
            if (result == -1) {
                player1Turn = !player1Turn;
            } else if (result == 0) {
                Toast.makeText(MainActivity.this, "It's a draw!", Toast.LENGTH_SHORT).show();
            } else if (result == 1) {
                Toast.makeText(MainActivity.this, "Player 1 wins!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Player 2 wins!", Toast.LENGTH_SHORT).show();
            }
        }

        public int gameEnded(int row, int col) {
            int symbol = board[row][col];
            boolean win = true;

            // Satır kontrolü
            for (int i = 0; i < 3; i++) {
                if (board[row][i] != symbol) {
                    win = false;
                    break;
                }
            }
            if (win) return symbol;

            // Sütun kontrolü
            win = true;
            for (int i = 0; i < 3; i++) {
                if (board[i][col] != symbol) {
                    win = false;
                    break;
                }
            }
            if (win) return symbol;

            // Çapraz kontrol
            if (row == col) {
                win = true;
                for (int i = 0; i < 3; i++) {
                    if (board[i][i] != symbol) {
                        win = false;
                        break;
                    }
                }
                if (win) return symbol;
            }

            if (row + col == 2) {
                win = true;
                for (int i = 0; i < 3; i++) {
                    if (board[i][2 - i] != symbol) {
                        win = false;
                        break;
                    }
                }
                if (win) return symbol;
            }

            return -1; // Oyun devam ediyor
        }

        public boolean isValidMove(int row, int col) {
            return board[row][col] == 0;
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("player1Turn", player1Turn);

        // Board'u tek boyutlu diziye dönüştür
        byte[] boardSingle = new byte[9];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                boardSingle[3 * i + j] = board[i][j];
            }
        }
        outState.putByteArray("board", boardSingle);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        player1Turn = savedInstanceState.getBoolean("player1Turn");

        byte[] boardSingle = savedInstanceState.getByteArray("board");
        if (boardSingle != null) {
            for (int i = 0; i < 9; i++) {
                board[i / 3][i % 3] = boardSingle[i];
            }
        }

        // Butonları güncelle
        for (int i = 0; i < 3; i++) {
            TableRow row = (TableRow) table.getChildAt(i);
            for (int j = 0; j < 3; j++) {
                Button button = (Button) row.getChildAt(j);
                if (board[i][j] == 1) {
                    button.setText(PLAYER_1);
                } else if (board[i][j] == 2) {
                    button.setText(PLAYER_2);
                }
            }
        }
    }
}
