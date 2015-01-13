package com.example.jment.dicewar;

/**
 * Created by Kevin on 2014-12-26.
 */
public class Field {
    private int player;
    private int dice_num;
    private int layout_id;
    private float X;
    private float Y;
    public void Field(){

    }

    public int getPlayer() {
        return player;
    }

    public void setPlayer(int player) {
        this.player = player;
    }

    public int getDice_num() {
        return dice_num;
    }

    public void setDice_num(int dice_num) {
        this.dice_num = dice_num;
    }

    public int getLayout_id() {
        return layout_id;
    }

    public void setLayout_id(int layout_id) {
        this.layout_id = layout_id;
    }

    public float getX() {
        return X;
    }

    public void setX(float x) {
        X = x;
    }

    public float getY() {
        return Y;
    }

    public void setY(float y) {
        Y = y;
    }
}
