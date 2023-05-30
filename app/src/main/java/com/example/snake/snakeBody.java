package com.example.snake;

import android.widget.ImageView;

public class snakeBody {
    private ImageView imageSnakeBody;
    private float previousPosX = 0;
    private float previousPosY = 0;
    private float previousRotationAngle = 0;


    /**
     * Le corps du serpent. chaque segment va suivre le segment devant lui.
     * @param imageSnakeBody l'image d'un des segments.
     */
    public snakeBody(ImageView imageSnakeBody) {
        this.imageSnakeBody = imageSnakeBody;
    }

    /**
     * Déplace le segment en fonction d'une position donnée et le tourne.
     * La position donnée correspond à l'ancienne position du segment devant lui, ou de la tête, s'il sagit du premier segment
     * @param posX l'ancienne position X
     * @param posY l'ancienne position Y
     * @param rotationAngle l'ancien angle de rotation
     */
    public void followPreviousSegment(float posX, float posY, float rotationAngle) {
        previousPosX = imageSnakeBody.getX();
        previousPosY = imageSnakeBody.getY();
        previousRotationAngle = imageSnakeBody.getRotation();

        imageSnakeBody.setX(posX);
        imageSnakeBody.setY(posY);
        this.imageSnakeBody.setRotation(rotationAngle);
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
     * Obtient la position x actuelle du segment
     * @return la position x
     */
    public float getPosX() {
        return this.imageSnakeBody.getX();
    }

    /**
     * Obtient la position y actuelle du segment
     * @return la position y
     */
    public float getPosY() {
        return this.imageSnakeBody.getY();
    }

    // à voir si je garde.
    public void setPreviousPosX(float previousPosX) {
        this.previousPosX = previousPosX;
    }

    public void setPreviousPosY(float previousPosY) {
        this.previousPosY = previousPosY;
    }

}
