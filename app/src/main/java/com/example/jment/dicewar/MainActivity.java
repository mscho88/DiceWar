package com.example.jment.dicewar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;


public class MainActivity extends Activity {

    static public int player_num = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Splash Event
        startActivity(new Intent(this, LogoActivity.class));

        setContentView(R.layout.activity_main);
        RadioButton rd_btn = (RadioButton) findViewById(R.id.player2);
        rd_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player_num = 2;
            }
        });
        rd_btn = (RadioButton) findViewById(R.id.player3);
        rd_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player_num = 3;
            }
        });
        rd_btn = (RadioButton) findViewById(R.id.player4);
        rd_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player_num = 4;
            }
        });
        rd_btn = (RadioButton) findViewById(R.id.player5);
        rd_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player_num = 5;
            }
        });
        rd_btn = (RadioButton) findViewById(R.id.player6);
        rd_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player_num = 6;
            }
        });

        // Read start_button and set the OnClickListener
        Button start_btn = (Button) findViewById(R.id.start_button);
        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, StartGame.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
