package com.example.snake;

import android.widget.ImageView;

//TODO faire une classe pour la tête du serpent qui se souvient de la position précédente après déplacement
public class snakeHead {
    private ImageView imageSnakeHead;
    private int directionX;
    private int directionY;
    private float previousPosX;
    private float previousPosY;
    private float previousRotationAngle;


    public snakeHead(ImageView imageSnakeHead) {
        this.imageSnakeHead = imageSnakeHead;
    }

    public void setDirectionX(int newDirectionX) {
        this.directionX=newDirectionX;
    }

    public void setDirectionY(int newDirectionY) {
        this.directionY=newDirectionY;
    }

    public int getDirectionX() {
        return this.directionX;
    }

    public int getDirectionY() {
        return this.directionY;
    }

    public void moveSnake() {
        previousPosX = imageSnakeHead.getX();
        previousPosY = imageSnakeHead.getY();

        imageSnakeHead.setX(imageSnakeHead.getX() + directionX);
        imageSnakeHead.setY(imageSnakeHead.getY() + directionY);
    }

    public void rotateSnake(int rotationAngle) {
        previousRotationAngle = imageSnakeHead.getRotation();
        this.imageSnakeHead.setRotation(rotationAngle);
    }

    public float getPreviousPosX() {
        return this.previousPosX;
    }

    public float getPreviousPosY() {
        return this.previousPosY;
    }

    public float getRotationAngle() {
        return this.previousRotationAngle;
    }

    public float getPosX() {
        return this.imageSnakeHead.getX();
    }

    public float getPosY() {
        return this.imageSnakeHead.getY();
    }
}
