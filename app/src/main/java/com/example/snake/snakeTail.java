package com.example.snake;

import android.widget.ImageView;

public class snakeTail {
    private ImageView imagesnakeTail;

    /**
     * La queue du serpent. Elle va simplement suivre le dernier segment et copier ses anciens angles de rotation
     * @param imagesnakeTail L'image de la queue du serpent.
     */
    public snakeTail(ImageView imagesnakeTail) {
        this.imagesnakeTail = imagesnakeTail;
    }

    /**
     * Déplace la queue en fonction d'une position donnée et la tourne.
     * La position donnée correspond à l'ancienne position du dernier segment.
     * @param posX l'ancienne position X
     * @param posY l'ancienne position Y
     * @param rotationAngle l'ancien angle de rotation
     */
    public void followPreviousSegment(float posX, float posY, float rotationAngle) {
        imagesnakeTail.setX(posX);
        imagesnakeTail.setY(posY);
        imagesnakeTail.setRotation(rotationAngle);
    }

    /**
     * Obtient la position x actuelle de la queue
     * @return la position x
     */
    public float getPosX() {
        return this.imagesnakeTail.getX();
    }

    /**
     * Obtient la position y actuelle de la queue
     * @return la position y
     */
    public float getPosY() {
        return this.imagesnakeTail.getY();
    }

    public float getRotation() {
        return this.imagesnakeTail.getRotation();
    }
}
