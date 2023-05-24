package com.example.snake;

import android.widget.ImageView;

//TODO même chose pour le corps du serpent, mais ils ne pourront pas manger la pomme et auront une collision
public class snakeBody {
    private ImageView imageSnakeBody;
    private float previousPosX;
    private float previousPosY;


    public snakeBody(ImageView imageSnakeBody) {
        this.imageSnakeBody = imageSnakeBody;
    }

    public void followPreviousSegment(float posX, float posY, float rotationAngle) {
        previousPosX = imageSnakeBody.getX();
        previousPosY = imageSnakeBody.getY();

        imageSnakeBody.setX(posX);
        imageSnakeBody.setY(posY);
        this.imageSnakeBody.setRotation(rotationAngle);
    }

    public float getPreviousPosX() {
        return this.previousPosX;
    }

    public float getPreviousPosY() {
        return this.previousPosY;
    }

    public float getPosX() {
        return this.imageSnakeBody.getX();
    }

    public float getPosY() {
        return this.imageSnakeBody.getY();
    }
}
