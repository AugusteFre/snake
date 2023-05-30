package com.example.snake;

import android.widget.ImageView;

public class snakeHead {
    private ImageView imageSnakeHead;
    private int directionX = 0;
    private int directionY = 0;
    private float previousPosX = 0;
    private float previousPosY = 0;
    private float previousRotationAngle = 0;


    /**
     * La tête du serpent. Elle va s'occuper de diriger les déplacement et de donner ses anciennes positions
     * et rotations au segment suivant.
     * @param imageSnakeHead L'image de la tête du serpent.
     */
    public snakeHead(ImageView imageSnakeHead) {
        this.imageSnakeHead = imageSnakeHead;
    }

    /**
     * Définit la nouvelle direction X au serpent
     * @param newDirectionX la nouvelle direction x à prendre
     */
    public void setDirectionX(int newDirectionX) {
        this.directionX=newDirectionX;
    }

    /**
     * Définit la nouvelle direction y au serpent
     * @param newDirectionY la nouvelle direction y à prendre
     */
    public void setDirectionY(int newDirectionY) {
        this.directionY=newDirectionY;
    }

    /**
     * Obtient la direction x actuelle du serpent
     * @return la direction x
     */
    public int getDirectionX() {
        return this.directionX;
    }

    /**
     * Obtient la direction y actuelle du serpent
     * @return la direction y
     */
    public int getDirectionY() {
        return this.directionY;
    }

    /**
     * Déplace le serpent vers sa prochaine position. Enregistre l'ancienne position x et y
     */
    public void moveSnake() {
        previousPosX = imageSnakeHead.getX();
        previousPosY = imageSnakeHead.getY();

        imageSnakeHead.setX(imageSnakeHead.getX() + directionX);
        imageSnakeHead.setY(imageSnakeHead.getY() + directionY);
    }

    /**
     * Tourne le serpent selon un angle de rotation donné. Enregistre l'ancien angle de rotation
     * @param rotationAngle l'angle de rotation
     */
    public void rotateSnake(int rotationAngle) {
        previousRotationAngle = imageSnakeHead.getRotation();
        this.imageSnakeHead.setRotation(rotationAngle);
    }

    /**
     * Obtient la position X précédante, enregistrée après déplacement
     * @return la position X précédante
     */
    public float getPreviousPosX() {
        return this.previousPosX;
    }

    /**
     * Obtient la position Y précédante, enregistrée après déplacement
     * @return la position Y précédante
     */
    public float getPreviousPosY() {
        return this.previousPosY;
    }

    /**
     * Obtient l'angle de rotation précédant, enregistré après rotation
     * @return l'angle de rotation précédante
     */
    public float getPreviousRotationAngle() {
        return this.previousRotationAngle;
    }

    /**
     * Obtient la position x actuelle du serpent
     * @return la position x
     */
    public float getPosX() {
        return this.imageSnakeHead.getX();
    }

    /**
     * Obtient la position y actuelle du serpent
     * @return la position y
     */
    public float getPosY() {
        return this.imageSnakeHead.getY();
    }
}
