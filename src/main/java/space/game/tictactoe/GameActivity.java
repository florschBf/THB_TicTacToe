package space.game.tictactoe;


import androidx.appcompat.app.AppCompatActivity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    int gameState;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        dialog = new Dialog(this);

        gameState = 1; //1-gewonnen, 2-verloren, 3-Unentschieden
    }

    public void GameBoardClick(View view) {
        ImageView selectedImage = (ImageView) view;
        int selectedBlock = 0;

        switch (selectedImage.getId()) {
            case R.id.row1_col1: selectedBlock = 1; break;
            case R.id.row1_col2: selectedBlock = 2; break;
            case R.id.row1_col3: selectedBlock = 3; break;
            case R.id.row2_col1: selectedBlock = 4; break;
            case R.id.row2_col2: selectedBlock = 5; break;
            case R.id.row2_col3: selectedBlock = 6; break;
            case R.id.row3_col1: selectedBlock = 7; break;
            case R.id.row3_col2: selectedBlock = 8; break;
            case R.id.row3_col3: selectedBlock = 9; break;
        }

        PlayGame(selectedBlock, selectedImage);
    }



    int activePlayer = 1;
    ArrayList<Integer> player1 = new ArrayList<Integer>();
    ArrayList<Integer> player2 = new ArrayList<Integer>();


    private void PlayGame(int selectedBlock, android.widget.ImageView selectedImage) {
        if(gameState == 1) {
            if(activePlayer == 1) {
                selectedImage.setImageResource(R.drawable.cross);
                player1.add(selectedBlock);
                activePlayer = 2;
                Autoplay();
            } else if(activePlayer == 2) {
                selectedImage.setImageResource(R.drawable.zero);
                player2.add(selectedBlock);
                activePlayer = 1;
            }

            selectedImage.setEnabled(false);
            CheckWinner();
        }
    }

    private void Autoplay() {
        ArrayList<Integer> emptyBlocks = new ArrayList<Integer>();

        for (int i = 1; i < 10; i++) {
            if(!(player1.contains(i) || player2.contains(i))) {
                emptyBlocks.add(i);
            }
        }

        if(emptyBlocks.size() == 0) {
            CheckWinner();
            if(gameState == 1) {
                openWinDialog();
            }
            gameState = 3; // Draw
        } else {
            Random r = new Random();
            int randomIndex = r.nextInt(emptyBlocks.size());
            int selectedBlock = emptyBlocks.get(randomIndex);
            ImageView selectedImage = (ImageView) findViewById(R.id.row1_col1);
            switch (selectedBlock) {
                case 1: selectedImage = (ImageView) findViewById(R.id.row1_col1); break;
                case 2: selectedImage = (ImageView) findViewById(R.id.row1_col2); break;
                case 3: selectedImage = (ImageView) findViewById(R.id.row1_col3); break;
                case 4: selectedImage = (ImageView) findViewById(R.id.row2_col1); break;
                case 5: selectedImage = (ImageView) findViewById(R.id.row2_col2); break;
                case 6: selectedImage = (ImageView) findViewById(R.id.row2_col3); break;
                case 7: selectedImage = (ImageView) findViewById(R.id.row3_col1); break;
                case 8: selectedImage = (ImageView) findViewById(R.id.row3_col2); break;
                case 9: selectedImage = (ImageView) findViewById(R.id.row3_col3); break;
            }
            PlayGame(selectedBlock, selectedImage);
        }
    }

    private void openWinDialog() {
        dialog.setContentView(R.layout.layout_dialog_win);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageView imageViewClose = dialog.findViewById(R.id.imageViewClose);
        Button btnOk = dialog.findViewById(R.id.btn_ok);

        imageViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResetGame();
                dialog.dismiss();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResetGame();
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    void ResetGame() {
        gameState = 1; //Playing
        activePlayer = 1;
        player1.clear();
        player2.clear();

        ImageView row;
        row = (ImageView) findViewById(R.id.row1_col1); row.setImageResource(0); row.setEnabled(true);
        row = (ImageView) findViewById(R.id.row1_col2); row.setImageResource(0); row.setEnabled(true);
        row = (ImageView) findViewById(R.id.row1_col3); row.setImageResource(0); row.setEnabled(true);
        row = (ImageView) findViewById(R.id.row2_col1); row.setImageResource(0); row.setEnabled(true);
        row = (ImageView) findViewById(R.id.row2_col2); row.setImageResource(0); row.setEnabled(true);
        row = (ImageView) findViewById(R.id.row2_col3); row.setImageResource(0); row.setEnabled(true);
        row = (ImageView) findViewById(R.id.row3_col1); row.setImageResource(0); row.setEnabled(true);
        row = (ImageView) findViewById(R.id.row3_col2); row.setImageResource(0); row.setEnabled(true);
        row = (ImageView) findViewById(R.id.row3_col3); row.setImageResource(0); row.setEnabled(true);
    }

    private void CheckWinner() {
        int winner = 0;
        /* prüfen Player 1 gewonnen  */
        if(player1.contains(1) && player1.contains(2) && player1.contains(3)) { winner = 1; }
        if(player1.contains(4) && player1.contains(5) && player1.contains(6)) { winner = 1; }
        if(player1.contains(7) && player1.contains(8) && player1.contains(9)) { winner = 1; }
        if(player1.contains(1) && player1.contains(4) && player1.contains(7)) { winner = 1; }
        if(player1.contains(2) && player1.contains(5) && player1.contains(8)) { winner = 1; }
        if(player1.contains(3) && player1.contains(6) && player1.contains(9)) { winner = 1; }
        if(player1.contains(1) && player1.contains(5) && player1.contains(9)) { winner = 1; }
        if(player1.contains(3) && player1.contains(5) && player1.contains(7)) { winner = 1; }

        /* prüfen Player 2 gewonnen  */
        if(player2.contains(1) && player2.contains(2) && player2.contains(3)) { winner = 2; }
        if(player2.contains(4) && player2.contains(5) && player2.contains(6)) { winner = 2; }
        if(player2.contains(7) && player2.contains(8) && player2.contains(9)) { winner = 2; }
        if(player2.contains(1) && player2.contains(4) && player2.contains(7)) { winner = 2; }
        if(player2.contains(2) && player2.contains(5) && player2.contains(8)) { winner = 2; }
        if(player2.contains(3) && player2.contains(6) && player2.contains(9)) { winner = 2; }
        if(player2.contains(1) && player2.contains(5) && player2.contains(9)) { winner = 2; }
        if(player2.contains(3) && player2.contains(5) && player2.contains(7)) { winner = 2; }

        if(winner != 0 && gameState ==1) {
            if(winner == 1) {
                openWinDialog();
            } else if(winner == 2) {
                openLoseDialog();
            }
            gameState = 2; // GameOver
        }

    }

    private void openLoseDialog() {
        dialog.setContentView(R.layout.layout_dialog_lost);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageView imageViewClose = dialog.findViewById(R.id.imageViewClose);
        Button btnOk2 = dialog.findViewById(R.id.btn_ok);

        imageViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResetGame();
                dialog.dismiss();
            }
        });
        btnOk2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResetGame();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

}