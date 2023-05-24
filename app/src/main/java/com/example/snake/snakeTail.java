package com.example.snake;

import android.widget.ImageView;

//TODO même chose pour le corps du serpent, mais ils ne pourront pas manger la pomme et auront une collision
public class snakeTail {
    private ImageView imagesnakeTail;


    public snakeTail(ImageView imagesnakeTail) {
        this.imagesnakeTail = imagesnakeTail;
    }

    public void followPreviousSegment(float posX, float posY, float rotationAngle) {
        imagesnakeTail.setX(posX);
        imagesnakeTail.setY(posY);
        this.imagesnakeTail.setRotation(rotationAngle);
    }

    public float getPosX() {
        return this.imagesnakeTail.getX();
    }

    public float getPosY() {
        return this.imagesnakeTail.getY();
    }
}
