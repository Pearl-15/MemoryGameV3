package iss.workshop.memorygamev3;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ImageView currView = null;
    int currentPos;

    //turn
    int turn = 1;

    //score
    int p1_score = 0;
    int p2_score = 0;

    //Position List
    private ArrayList<String> text = new ArrayList<>();

    //Image Array
    private ArrayList<Integer> imageList = new ArrayList<>();

    TextView tv_p1;
    TextView tv_p2;
    TextView timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        tv_p1 = (TextView) findViewById(R.id.tv_ply1);
        tv_p2 = (TextView) findViewById(R.id.tv_ply2);

        //starts from player 1
        tv_p1.setTextColor(Color.GREEN);
        tv_p2.setTextColor(Color.GRAY);

        //Timer
        timer = findViewById(R.id.playtime);

        new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                timer.setText("TIME LEFT: " + millisUntilFinished / 1000);
                timer.setTextColor(Color.BLACK);
            }

            public void onFinish() {
                alertDialog();
            }
        }.start();


        GridView gridView = (GridView) findViewById(R.id.gridView);
        fillArray();
        ImageAdapter imageAdapter = new ImageAdapter(this);
        gridView.setAdapter(imageAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //first clicked
                if (currentPos < 0) {
                    currentPos = position;
                    currView = ((ImageView) view);
                    ((ImageView) view).setImageResource(imageList.get(position));

                }
                else {
                    if (currentPos != position) {
                        ((ImageView) view).setImageResource(imageList.get(position));
                    }

                    if (imageList.get(currentPos) != imageList.get(position)) {
                        currView.setImageResource(R.drawable.hidden);
                        ((ImageView) view).setImageResource(R.drawable.hidden);
                    }
                    if (currView != null) {
                        if (imageList.get(currentPos) == imageList.get(position)) {
                            if (turn == 1) {
                                p1_score++;
                                tv_p1.setText("P1 : " + p1_score);
                            } else if (turn == 2) {
                                p2_score++;
                                tv_p2.setText("P2 : " + p2_score);
                            }

                        }
                        if (turn == 1) {
                            turn = 2;
                            tv_p1.setTextColor(Color.GRAY);
                            tv_p2.setTextColor(Color.GREEN);
                        } else if (turn == 2) {
                            turn = 1;
                            tv_p2.setTextColor(Color.GRAY);
                            tv_p1.setTextColor(Color.GREEN);
                        }

                    }

                    if(p1_score+p2_score==6){
                        alertDialog();
                    }

                    currentPos = -1;
                }
            }
        });
    }


    public void fillArray() {

        for (Integer i = 0; i < 12; i++) {
            text.add(i.toString());
        }

        List images = List.of(
                R.drawable.image_101,
                R.drawable.image_102,
                R.drawable.image_103,
                R.drawable.image_104,
                R.drawable.image_105,
                R.drawable.image_106);

        for (int i = 0; i < 2; i++) {
            imageList.addAll(images);
        }

        Collections.shuffle(imageList);


    }

    private void alertDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder
                .setMessage("Game Over!\nP1: " + p1_score+ "\nP2: " + p2_score)
                .setCancelable(false)
                .setPositiveButton("NEW", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("EXIT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


}
