package com.example.jment.dicewar;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by Kevin on 2014-12-25.
 */
public class StartGame extends Activity implements View.OnClickListener {
    final int map_height = 8;
    final int map_width = 8;
    int max_field = 30; // max_field value should not exceed the value map_height * map_width
    //Integer[][] map_model = new Integer[map_height][map_width];
    ImageView prev_view = null;
    ImageView cur_view = null;
    ArrayList<Field[]> map_model;//
    int dice_res = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_game);

        // Since 30 is not divisible by 4, set the number of field to 28
        if(MainActivity.player_num == 4){
            max_field = 28;
        }

        // Randomly generate the map
        initMap();
        //drawMap();
        genRandomMap();
        drawMap();

        ((Button) findViewById(R.id.yes_button)).setOnClickListener(this);
        ((Button) findViewById(R.id.no_button)).setOnClickListener(this);
    }

    public void onClick(View v){
//        TextView txt = (TextView) findViewById(R.id.textView);

        if (v.getId() == R.id.yes_button) {
            enableEvent();
            TextView txt = (TextView) findViewById(R.id.textView);
            txt.setText("Game Start !");
            LinearLayout btn_container = (LinearLayout) findViewById(R.id.button_container);
            btn_container.removeAllViews();
            Button new_btn = new Button(this);
            new_btn.setText("End Your Turn");
            new_btn.setId(2000);
            btn_container.addView(new_btn);
        }else if(v.getId() == R.id.no_button) {
            initMap();
            genRandomMap();
            drawMap();
        }else if(v.getId() == getApplicationContext().getResources().getIdentifier("2000", "id", getPackageName())){
            disableEvent();
            executeComputerTurn();
            enableEvent();
        }
    }

    private void initMap(){
        // Initialize the map model
        map_model = new ArrayList<Field[]>();
        for(int i = 0; i < map_height; i++){
            Field[] fields = new Field[8];
            for(int j = 0; j < map_width; j++){
                fields[j] = new Field();
            }
            map_model.add(fields);
        }

//        Display display = getWindowManager().getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);

        // landscape
//        int width = (size.x - 100) / 9;
//        BitmapDrawable bitmap = (BitmapDrawable) this.getResources().getDrawable(R.drawable.purple_field_big);
//        float ratio = ((float) width) / bitmap.getBitmap().getWidth();

        for(int i = 0; i < map_height; i++){
            for(int j = 0; j < map_width; j++){
//                // Draw purple field on all fields
//                ImageView iv = new ImageView(this);
//                iv.setId(200 + (i + 1) * 10 + (j + 1));
//                iv.setBackgroundResource(R.drawable.purple_field_big);
//
//
//                RelativeLayout.LayoutParams iv_param = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//                if(j == 0 && i % 2 == 1) {
//                    iv_param.setMargins(25 * 2, 43 * i * 2, 0, 0);
//                }else{
//                    iv_param.setMargins(0, 43 * i * 2, 0, 0);
//                    iv_param.addRule(RelativeLayout.RIGHT_OF, (200 + (i + 1) * 10 + j));
//                    iv_param.addRule(RelativeLayout.RIGHT_OF, (100 + (i + 1) * 10 + j));
//                }
//                iv.setLayoutParams(iv_param);
//                //iv.setScaleX(ratio);
//                //iv.setScaleY(ratio);
//
//                RelativeLayout layout = (RelativeLayout) findViewById(R.id.map_layout);
//                layout.addView(iv);

                // Initialize the map model with all zeros
                map_model.get(i)[j].setPlayer(0);
                map_model.get(i)[j].setDice_num(0);
                map_model.get(i)[j].setLayout_id(200 + (i + 1) * 10 + (j + 1));
            }
        }


    }

    private void genRandomMap(){
        Integer[] num_map_for_player;
        num_map_for_player = new Integer[MainActivity.player_num];
        for(int i = 0; i < MainActivity.player_num; i++){
            num_map_for_player[i] = 0;
        }

        int i = 0;

        // Randomly fill up the map with the number of max_field of 1
        Random r1 = new Random();
        int max_num_field_for_player = max_field / MainActivity.player_num;
        while (i < max_field){
            int height = r1.nextInt(map_height);
            int width = r1.nextInt(map_width);
            if (map_model.get(height)[width].getPlayer() == 0){
                int player = r1.nextInt(MainActivity.player_num);
                while(num_map_for_player[player] == max_num_field_for_player){
                    player = r1.nextInt(MainActivity.player_num);
                }
                num_map_for_player[player]++;
                player++;
                map_model.get(height)[width].setPlayer(player);
                map_model.get(height)[width].setLayout_id(map_model.get(height)[width].getLayout_id() - 100);
                map_model.get(height)[width].setDice_num(1);
                switch (player) {
                    case 1:
                        map_model.get(height)[width].setPlayer(1);
                        break;
                    case 2:
                        map_model.get(height)[width].setPlayer(2);
                        break;
                    case 3:
                        map_model.get(height)[width].setPlayer(3);
                        break;
                    case 4:
                        map_model.get(height)[width].setPlayer(4);
                        break;
                    case 5:
                        map_model.get(height)[width].setPlayer(5);
                        break;
                    case 6:
                        map_model.get(height)[width].setPlayer(6);
                        break;
                }
                i++;
            }
        }

        // Squeeze the map model
        for(int j = 0; j < map_height; j++){
            for(int k = map_width / 2 - 1; k >= 0; k--){
                if(map_model.get(j)[k].getPlayer() != 0){
                    for(int m = map_width / 2 - 1; m > k; m--){
                        if(map_model.get(j)[m].getPlayer() == 0){
                            map_model.get(j)[m].setPlayer(map_model.get(j)[k].getPlayer());
                            map_model.get(j)[k].setPlayer(0);
                            map_model.get(j)[m].setLayout_id(map_model.get(j)[m].getLayout_id() - 100);
                            map_model.get(j)[k].setLayout_id(map_model.get(j)[k].getLayout_id() + 100);
                            map_model.get(j)[m].setDice_num(map_model.get(j)[k].getDice_num());
                            map_model.get(j)[k].setDice_num(0);
                            break;
                        }
                    }
                }
            }
            for(int k = map_width / 2; k < map_width; k++){
                if(map_model.get(j)[k].getPlayer() != 0){
                    for(int m = map_width / 2; m < map_width; m++){
                        if(map_model.get(j)[m].getPlayer() == 0){
                            map_model.get(j)[m].setPlayer(map_model.get(j)[k].getPlayer());
                            map_model.get(j)[k].setPlayer(0);
                            map_model.get(j)[m].setLayout_id(map_model.get(j)[m].getLayout_id() - 100);
                            map_model.get(j)[k].setLayout_id(map_model.get(j)[k].getLayout_id() + 100);
                            map_model.get(j)[m].setDice_num(map_model.get(j)[k].getDice_num());
                            map_model.get(j)[k].setDice_num(0);
                            break;
                        }
                    }
                }
            }
        }
        for(int j = 0; j < map_width; j++){
            for(int k = map_height / 2 - 1; k >= 0; k--){
                if(map_model.get(k)[j].getPlayer() != 0){
                    for(int m = map_height / 2 - 1; m > k; m--){
                        if(map_model.get(m)[j].getPlayer() == 0){
                            map_model.get(m)[j].setPlayer(map_model.get(k)[j].getPlayer());
                            map_model.get(k)[j].setPlayer(0);
                            map_model.get(m)[j].setLayout_id(map_model.get(m)[j].getLayout_id() - 100);
                            map_model.get(k)[j].setLayout_id(map_model.get(k)[j].getLayout_id() + 100);
                            map_model.get(m)[j].setDice_num(map_model.get(k)[j].getDice_num());
                            map_model.get(k)[j].setDice_num(0);
                            break;
                        }
                    }
                }
            }
            for(int k = map_height / 2; k < map_height; k++){
                if(map_model.get(k)[j].getPlayer() != 0){
                    for(int m = map_height / 2; m < map_height; m++){
                        if(map_model.get(m)[j].getPlayer() == 0){
                            map_model.get(m)[j].setPlayer(map_model.get(k)[j].getPlayer());
                            map_model.get(k)[j].setPlayer(0);
                            map_model.get(m)[j].setLayout_id(map_model.get(m)[j].getLayout_id() - 100);
                            map_model.get(k)[j].setLayout_id(map_model.get(k)[j].getLayout_id() + 100);
                            map_model.get(m)[j].setDice_num(map_model.get(k)[j].getDice_num());
                            map_model.get(k)[j].setDice_num(0);
                            break;
                        }
                    }
                }
            }
        }

        Integer[] num_dice_player = new Integer[MainActivity.player_num];
        for(i = 0; i < MainActivity.player_num; i++){
            num_dice_player[i] = 2 * max_num_field_for_player;
        }

        // Randomly place the number of dices, total number of dices for each player must be same
        for(i = 0; i < map_height; i++){
            for(int j = 0; j < map_width; j++){
                if(map_model.get(i)[j].getPlayer() != 0){
                    if(num_dice_player[map_model.get(i)[j].getPlayer() - 1] > 0) {
                        //num_dice_player[map_model.get(i)[j].getPlayer() - 1] / 2)
                        int dice_num;
                        if(num_dice_player[map_model.get(i)[j].getPlayer() - 1] > 4){
                            dice_num = r1.nextInt(4);
                        }else{
                            dice_num = r1.nextInt(num_dice_player[map_model.get(i)[j].getPlayer() - 1]);
                        }
                        dice_num++;
                        num_dice_player[map_model.get(i)[j].getPlayer() - 1] = num_dice_player[map_model.get(i)[j].getPlayer() - 1] - dice_num;
                        map_model.get(i)[j].setDice_num(map_model.get(i)[j].getDice_num() + dice_num);
                    }
                }
            }
        }
    }

    private void drawMap(){
        for(int i = 0; i < map_height; i++){
            for(int j = 0; j < map_width; j++){
                int id = map_model.get(i)[j].getLayout_id();
                int field_layout_id = getApplicationContext().getResources().getIdentifier("row" + (i + 1) + "col" + (j + 1), "id", getPackageName());

                if(id > 200){
                    ImageView field_iv = (ImageView) findViewById(field_layout_id);
                    field_iv.setBackgroundResource(R.drawable.empty_field_big);
                }else {
                    ImageView field_iv = (ImageView) findViewById(field_layout_id);

                    switch (map_model.get(i)[j].getPlayer()) {
                        case 1:
                            field_iv.setBackgroundResource(R.drawable.red_field_big);
                            break;
                        case 2:
                            field_iv.setBackgroundResource(R.drawable.orange_field_big);
                            break;
                        case 3:
                            field_iv.setBackgroundResource(R.drawable.yellow_field_big);
                            break;
                        case 4:
                            field_iv.setBackgroundResource(R.drawable.green_field_big);
                            break;
                        case 5:
                            field_iv.setBackgroundResource(R.drawable.sky_field_big);
                            break;
                        case 6:
                            field_iv.setBackgroundResource(R.drawable.purple_field_big);
                            break;
                    }
                }
                int dice_layout_id = getApplicationContext().getResources().getIdentifier("dices" + (i + 1) + (j + 1), "id", getPackageName());
                ImageView dice_iv = (ImageView) findViewById(dice_layout_id);
                switch(map_model.get(i)[j].getDice_num()){
                    case 0:
                        dice_iv.setBackgroundResource(R.drawable.empty_dice);
                        break;
                    case 1:
                        switch(map_model.get(i)[j].getPlayer()){
                            case 1:
                                dice_iv.setBackgroundResource(R.drawable.red_one);
                                break;
                            case 2:
                                dice_iv.setBackgroundResource(R.drawable.orange_one);
                                break;
                            case 3:
                                dice_iv.setBackgroundResource(R.drawable.yellow_one);
                                break;
                            case 4:
                                dice_iv.setBackgroundResource(R.drawable.green_one);
                                break;
                            case 5:
                                dice_iv.setBackgroundResource(R.drawable.sky_one);
                                break;
                            case 6:
                                dice_iv.setBackgroundResource(R.drawable.purple_one);
                                break;
                        }
                        break;
                    case 2:
                        switch(map_model.get(i)[j].getPlayer()){
                            case 1:
                                dice_iv.setBackgroundResource(R.drawable.red_two);
                                break;
                            case 2:
                                dice_iv.setBackgroundResource(R.drawable.orange_two);
                                break;
                            case 3:
                                dice_iv.setBackgroundResource(R.drawable.yellow_two);
                                break;
                            case 4:
                                dice_iv.setBackgroundResource(R.drawable.green_two);
                                break;
                            case 5:
                                dice_iv.setBackgroundResource(R.drawable.sky_two);
                                break;
                            case 6:
                                dice_iv.setBackgroundResource(R.drawable.purple_two);
                                break;
                        }
                        break;
                    case 3:
                        switch(map_model.get(i)[j].getPlayer()){
                            case 1:
                                dice_iv.setBackgroundResource(R.drawable.red_three);
                                break;
                            case 2:
                                dice_iv.setBackgroundResource(R.drawable.orange_three);
                                break;
                            case 3:
                                dice_iv.setBackgroundResource(R.drawable.yellow_three);
                                break;
                            case 4:
                                dice_iv.setBackgroundResource(R.drawable.green_three);
                                break;
                            case 5:
                                dice_iv.setBackgroundResource(R.drawable.sky_three);
                                break;
                            case 6:
                                dice_iv.setBackgroundResource(R.drawable.purple_three);
                                break;
                        }
                        break;
                    case 4:
                        switch(map_model.get(i)[j].getPlayer()){
                            case 1:
                                dice_iv.setBackgroundResource(R.drawable.red_four);
                                break;
                            case 2:
                                dice_iv.setBackgroundResource(R.drawable.orange_four);
                                break;
                            case 3:
                                dice_iv.setBackgroundResource(R.drawable.yellow_four);
                                break;
                            case 4:
                                dice_iv.setBackgroundResource(R.drawable.green_four);
                                break;
                            case 5:
                                dice_iv.setBackgroundResource(R.drawable.sky_four);
                                break;
                            case 6:
                                dice_iv.setBackgroundResource(R.drawable.purple_four);
                                break;
                        }
                        break;
                    case 5:
                        switch(map_model.get(i)[j].getPlayer()){
                            case 1:
                                dice_iv.setBackgroundResource(R.drawable.red_five);
                                break;
                            case 2:
                                dice_iv.setBackgroundResource(R.drawable.orange_five);
                                break;
                            case 3:
                                dice_iv.setBackgroundResource(R.drawable.yellow_five);
                                break;
                            case 4:
                                dice_iv.setBackgroundResource(R.drawable.green_five);
                                break;
                            case 5:
                                dice_iv.setBackgroundResource(R.drawable.sky_five);
                                break;
                            case 6:
                                dice_iv.setBackgroundResource(R.drawable.purple_five);
                                break;
                        }
                        break;
                    case 6:
                        switch(map_model.get(i)[j].getPlayer()){
                            case 1:
                                dice_iv.setBackgroundResource(R.drawable.red_six);
                                break;
                            case 2:
                                dice_iv.setBackgroundResource(R.drawable.orange_six);
                                break;
                            case 3:
                                dice_iv.setBackgroundResource(R.drawable.yellow_six);
                                break;
                            case 4:
                                dice_iv.setBackgroundResource(R.drawable.green_six);
                                break;
                            case 5:
                                dice_iv.setBackgroundResource(R.drawable.sky_six);
                                break;
                            case 6:
                                dice_iv.setBackgroundResource(R.drawable.purple_six);
                                break;
                        }
                        break;
                    case 7:
                        switch(map_model.get(i)[j].getPlayer()){
                            case 1:
                                dice_iv.setBackgroundResource(R.drawable.red_seven);
                                break;
                            case 2:
                                dice_iv.setBackgroundResource(R.drawable.orange_seven);
                                break;
                            case 3:
                                dice_iv.setBackgroundResource(R.drawable.yellow_seven);
                                break;
                            case 4:
                                dice_iv.setBackgroundResource(R.drawable.green_seven);
                                break;
                            case 5:
                                dice_iv.setBackgroundResource(R.drawable.sky_seven);
                                break;
                            case 6:
                                dice_iv.setBackgroundResource(R.drawable.purple_seven);
                                break;
                        }
                        break;
                    case 8:
                        switch(map_model.get(i)[j].getPlayer()){
                            case 1:
                                dice_iv.setBackgroundResource(R.drawable.red_eight);
                                break;
                            case 2:
                                dice_iv.setBackgroundResource(R.drawable.orange_eight);
                                break;
                            case 3:
                                dice_iv.setBackgroundResource(R.drawable.yellow_eight);
                                break;
                            case 4:
                                dice_iv.setBackgroundResource(R.drawable.green_eight);
                                break;
                            case 5:
                                dice_iv.setBackgroundResource(R.drawable.sky_eight);
                                break;
                            case 6:
                                dice_iv.setBackgroundResource(R.drawable.purple_eight);
                                break;
                        }
                        break;
                }

            }
        }
    }

    private ImageView findImageViewByCustomId(int id){
        if(findViewById(100 + id) != null){
            return (ImageView) findViewById(100 + id);
        }else{
            return (ImageView) findViewById(200 + id);
        }
    }

    private void enableEvent(){

        for(int i = 0; i < map_height; i++){
            for(int j = 0; j < map_width; j++){
                if(map_model.get(i)[j].getPlayer() == 1) {
                    ImageView image_view = (ImageView) findViewById(getApplicationContext().getResources().getIdentifier("row" + (i + 1) + "col" + (j + 1), "id", getPackageName()));
                    image_view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(cur_view == null) {
                                cur_view = (ImageView) v;
                                v.setBackgroundResource(R.drawable.selected_field);
                            }else{
                                if(cur_view.getId() == v.getId()){
                                    cur_view.setBackgroundResource(R.drawable.red_field_big);
                                    cur_view = null;
                                }else {
                                    cur_view.setBackgroundResource(R.drawable.red_field_big);
                                    cur_view = (ImageView) v;
                                    v.setBackgroundResource(R.drawable.selected_field);
                                }
                            }
                        }
                    });
                }else if(map_model.get(i)[j].getPlayer() == 2 ||
                        map_model.get(i)[j].getPlayer() == 3 ||
                        map_model.get(i)[j].getPlayer() == 4 ||
                        map_model.get(i)[j].getPlayer() == 5 ||
                        map_model.get(i)[j].getPlayer() == 6){
                    ImageView image_view = (ImageView) findViewById(getApplicationContext().getResources().getIdentifier("row" + (i + 1) + "col" + (j + 1), "id", getPackageName()));
                    image_view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(cur_view != null) {
                                prev_view = cur_view;
                                cur_view = (ImageView) v;
                                if(isAdjacent()){
                                    Drawable drawable = ((ImageView) v).getBackground();
                                    cur_view.setBackgroundResource(R.drawable.selected_field);
                                    // if cur player win, rollDice returns true
                                    // if cur player lose, rollDice returns false
                                    if(rollDice()){
                                        prev_view.setBackgroundResource(R.drawable.red_field_big);
                                        cur_view.setBackgroundResource(R.drawable.red_field_big);
                                        //map_model.get(i)[j].setDice_num();
                                        //map_model.get
                                        prev_view = null;
                                        cur_view = null;
                                    }else{
                                        prev_view.setBackgroundResource(R.drawable.red_field_big);
                                        cur_view.setBackground(drawable);
                                        ImageView dice_iv = (ImageView) findViewById(getApplicationContext().getResources().getIdentifier(getDiceIDfromViewID(prev_view), "id", getPackageName()));
                                        dice_iv.setBackgroundResource(R.drawable.red_one);
                                        prev_view = null;
                                        cur_view = null;
                                    }
                                }else{
                                    cur_view = prev_view;
                                    prev_view = null;
                                    ((LinearLayout) findViewById(R.id.defender_dice)).removeAllViews();
                                    ((LinearLayout) findViewById(R.id.offender_dice)).removeAllViews();
                                    ((TextView) findViewById(R.id.defender_text)).setText("");
                                    ((TextView) findViewById(R.id.offender_text)).setText("");

                                    TextView txt = (TextView) findViewById(R.id.textView);
                                    txt.setText("Invalid field to conquer the enemy's field !");
                                }

                            }else{
                                ((LinearLayout) findViewById(R.id.defender_dice)).removeAllViews();
                                ((LinearLayout) findViewById(R.id.offender_dice)).removeAllViews();
                                ((TextView) findViewById(R.id.defender_text)).setText("");
                                ((TextView) findViewById(R.id.offender_text)).setText("");

                                TextView txt = (TextView) findViewById(R.id.textView);
                                txt.setText("You have to choose your field first !");
                            }

                        }
                    });
                }
            }
        }
    }

    private String getDiceIDfromViewID(ImageView iv){
        String dlm = "[/]";

        String resourceName = iv.getResources().getResourceName(iv.getId());
        String prev_id = resourceName.split(dlm)[1];
        return "dices" + prev_id.charAt(3) + prev_id.charAt(7);
    }

    private boolean rollDice() {
        Random r1 = new Random();
        Handler mHandler = new Handler();
        Runnable mRunnable = new Runnable(){
            public void run() {
                TextView mid_txt = (TextView) findViewById(R.id.textView);
                mid_txt.setText(" VS ");
            }
        };
        mHandler.postDelayed(mRunnable, 100);

        // Defender(cur_view's) information
        LinearLayout def_layout = (LinearLayout) findViewById(R.id.defender_dice);
        String cur_id = cur_view.getResources().getResourceName(cur_view.getId()).split("[/]")[1];
        Field def_field = map_model.get(Integer.parseInt(cur_id.charAt(3) + "") - 1)[Integer.parseInt(cur_id.charAt(7) + "") - 1];
        int def_sum = 0;

        // Offender(prev_view's) information
        LinearLayout off_layout = (LinearLayout) findViewById(R.id.offender_dice);
        String prev_id = prev_view.getResources().getResourceName(prev_view.getId()).split("[/]")[1];
        Field off_field = map_model.get(Integer.parseInt(prev_id.charAt(3) + "") - 1)[Integer.parseInt(prev_id.charAt(7) + "") - 1];
        int off_sum = 0;
        def_layout.removeAllViews();
        for (int i = 0; i < def_field.getDice_num(); i++) {
            ImageView iv = new ImageView(StartGame.this);
            def_layout.addView(iv);
            def_sum += rollOneDice(def_field.getPlayer(), iv);
        }
        off_layout.removeAllViews();
        for (int i = 0; i < off_field.getDice_num(); i++) {
            ImageView iv = new ImageView(StartGame.this);
            off_layout.addView(iv);
            off_sum += rollOneDice(off_field.getPlayer(), iv);
        }
        final int defense_sum = def_sum;
        final int offense_sum = off_sum;

        mRunnable = new Runnable(){
            public void run(){
                TextView defender_text = (TextView) findViewById(R.id.defender_text);
                defender_text.setText(String.valueOf(defense_sum));
                TextView offender_text = (TextView) findViewById(R.id.offender_text);
                offender_text.setText(String.valueOf(offense_sum));
            }
        };
        mHandler.postDelayed(mRunnable, 2000);

        return def_sum > off_sum ? false : true;
    }

    class ImageSetter implements Runnable {
        public int a;
        public void run(){

        }
    }

    private int isWin(){
        int user_exists = 0;
        for(int i = 0; i < map_height; i++){
            for(int j = 0; j < map_width; j++){
                if(map_model.get(i)[j].getPlayer() == 1){
                    user_exists++;
                }
            }
        }

        if(user_exists == 0){
            return 1; // lost
        }else if(user_exists == max_field){
            return 2; // won
        }else{
            return 3; // still playing
        }
    }

    private int rollOneDice(final int player_num, final ImageView iv){
        Random r1 = new Random();
        Handler mHandler = new Handler();

        String drawable_str = "";
        switch(player_num){
            case 1:
                drawable_str = "red";
                break;
            case 2:
                drawable_str = "orange";
                break;
            case 3:
                drawable_str = "yellow";
                break;
            case 4:
                drawable_str = "green";
                break;
            case 5:
                drawable_str = "sky";
                break;
            case 6:
                drawable_str = "purple";
                break;
        }
        int drawable = getApplicationContext().getResources().getIdentifier("dice_" + drawable_str + r1.nextInt(3), "drawable", getPackageName());

        iv.setBackgroundResource(drawable);
        AnimationDrawable anim = (AnimationDrawable) iv.getBackground();
        anim.start();

        Runnable mRunnable = new Runnable(){
            //int dice_res = 0;
            public void run(){
                String drawable_str = "";
                switch(player_num){
                    case 1:
                        drawable_str = "red";
                        break;
                    case 2:
                        drawable_str = "orange";
                        break;
                    case 3:
                        drawable_str = "yellow";
                        break;
                    case 4:
                        drawable_str = "green";
                        break;
                    case 5:
                        drawable_str = "sky";
                        break;
                    case 6:
                        drawable_str = "purple";
                        break;
                }
                Random r1 = new Random();
                int rnd = r1.nextInt(6);
                String drawable_num = "";
                switch(rnd + 1){
                    case 1:
                        drawable_num = "one";
                        dice_res = 1;
                        break;
                    case 2:
                        drawable_num = "two";
                        dice_res = 2;
                        break;
                    case 3:
                        drawable_num = "three";
                        dice_res = 3;
                        break;
                    case 4:
                        drawable_num = "four";
                        dice_res = 4;
                        break;
                    case 5:
                        drawable_num = "five";
                        dice_res = 5;
                        break;
                    case 6:
                        drawable_num = "six";
                        dice_res = 6;
                        break;
                }
                int drawable = getApplicationContext().getResources().getIdentifier("dice_" + drawable_str + "_" + drawable_num, "drawable", getPackageName());
                iv.setBackgroundResource(drawable);

            }
        };
        mHandler.postDelayed(mRunnable, 1000 + r1.nextInt(1500));
        return dice_res;
    }

    private boolean isAdjacent(){
        ImageView prev_iv = (ImageView) findViewById(prev_view.getId());
        ImageView cur_iv = (ImageView) findViewById(cur_view.getId());
        String dlm = "[/]";

        String resourceName = prev_iv.getResources().getResourceName(prev_iv.getId());
        String prev_id = resourceName.split(dlm)[1];
        resourceName = cur_iv.getResources().getResourceName(cur_iv.getId());
        String cur_id = resourceName.split(dlm)[1];

        int prev_row = Integer.parseInt(prev_id.charAt(3) + "");
        int prev_col = Integer.parseInt(prev_id.charAt(7) + "");

        int cur_row = Integer.parseInt(cur_id.charAt(3) + "");
        int cur_col = Integer.parseInt(cur_id.charAt(7) + "");

        if(prev_row == cur_row){
            if(prev_col - cur_col == 1 || cur_col - prev_col == 1){
                return true;
            }else{
                return false;
            }
        }else{
            if(prev_row - cur_row == 1 || cur_row - prev_row == 1){
                if(prev_row % 2 == 0){
                    if(prev_col == cur_col || cur_col - prev_col == 1){
                        return true;
                    }else{
                        return false;
                    }
                }else{
                    if(prev_col == cur_col || prev_col - cur_col == 1){
                        return true;
                    }else{
                        return false;
                    }
                }
            }else{
                return false;
            }
        }
    }

    private void disableEvent(){
        for(int i = 0; i < map_height; i++){
            for(int j = 0; j < map_width; j++){
                ImageView iv = getViewFromMapModel(i + 1, j + 1);
                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {}
                });
            }
        }
    }

    private ImageView getViewFromMapModel(int i, int j){
        int drawable = getApplicationContext().getResources().getIdentifier("row" + i + "col" + j, "id", getPackageName());
        return ((ImageView) findViewById(drawable));
    }

    private void executeComputerTurn(){
        Button btn = (Button) findViewById(getApplicationContext().getResources().getIdentifier("2000", "id", getPackageName()));
        btn.setEnabled(false);

        for(int i = 2; i <= MainActivity.player_num; i++){
            for(int j = 0; j < map_height; j++){
                for(int k = 0; k < map_width; k++){
                    if(map_model.get(j)[k].getPlayer() == i){
                        //findAvailableAdjacent(i, j, k);
                    }
                }
            }
        }
        btn.setEnabled(true);
    }
    private Field findAvailableAdjacent(int player, int i, int j){
        return new Field();
    }
}
