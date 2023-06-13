package com.example.snake;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity implements SensorEventListener {

    // déclaration des modules
    private SensorManager sensorManager;
    private Sensor gravity;
    private TextView scoreValue;
    private TextView directionValues;
    private TextView appleValues;
    private ImageView imageSnake;
    private ImageView imageSnakeBody;
    private ImageView imageSnakeTail;
    private ImageView imageApple;

    // déclaration des variables
    private int screenHeight;
    private int screenWidth;
    private int movementValue;
    int moveCooldown = 15;

    int score = 0;

    private ConstraintLayout snakeGame;

    snakeHead snake;
    snakeBody firstSegment;

    ArrayList<snakeBody> bodySegments = new ArrayList<>();
    snakeTail tail;

    private final int OK_SCORE = 5;
    private final int NICE_SCORE = 10;
    private final int PRETTY_NICE_SCORE = 15;
    private final int GOOD_SCORE = 25;

    private final int SLOW_COOLDOWN = 15;
    private final int NORMAL_COOLDOWN = 13;
    private final int FAST_COOLDOWN = 10;
    private final int VERY_FAST_COOLDOWN = 8;
    private final int REEEALLY_FAST_COOLDOWN = 5;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // mesure la largeur et hauteur de l'écran
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;

        // déclare les vues
        scoreValue = findViewById(R.id.scoreValues);

        snakeGame = findViewById(R.id.snakeGame);

        imageSnake = findViewById(R.id.imageSnake);
        imageSnakeBody = findViewById(R.id.imageSnakeBody);
        imageSnakeTail = findViewById(R.id.imageSnakeTail);
        imageApple = findViewById(R.id.imageApple);

        // définit le type de capteur
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        gravity = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);

        // définit les images par défaut du serpent
        snake = new snakeHead(imageSnake);
        firstSegment = new snakeBody(imageSnakeBody);
        bodySegments.add(firstSegment);
        tail = new snakeTail(imageSnakeTail);
        movementValue = screenWidth/19;

        // donne une direction par défaut au serpent
        snake.setDirectionX(-movementValue);

        // déplace la pomme
        moveApple();
    }


    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, gravity, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_GRAVITY) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            // Met à jour la TextView avec les valeurs du score
            scoreValue.setText("score : " + score);

            if(moveCooldown == 0) {
                changeSnakeDirection(x, y);
                snake.moveSnake();
                checkSnakeAppleCollision();
                int i = 0;
                for (snakeBody segment : bodySegments) {
                    if(i==0) {
                        segment.followPreviousSegment(snake.getPreviousPosX(), snake.getPreviousPosY(), snake.getPreviousRotationAngle());
                    } else {
                        segment.followPreviousSegment(bodySegments.get(i-1).getPreviousPosX(), bodySegments.get(i-1).getPreviousPosY(), bodySegments.get(i-1).getPreviousRotationAngle());
                    }
                    i++;
                }
                tail.followPreviousSegment(bodySegments.get(bodySegments.size()-1).getPreviousPosX(), bodySegments.get(bodySegments.size()-1).getPreviousPosY(), bodySegments.get(bodySegments.size()-1).getPreviousRotationAngle());

                // regarder les collisions avec le corps du serpent après avoir bougé
                checkSnakeCollision();
                checkSnakeBodyCollision();

                // vitesse du serpent en fonction du score
                if(score < OK_SCORE) {
                    moveCooldown = SLOW_COOLDOWN;
                } else if (score < NICE_SCORE) {
                    moveCooldown = NORMAL_COOLDOWN;
                } else if (score < PRETTY_NICE_SCORE) {
                    moveCooldown = FAST_COOLDOWN;
                } else if (score < GOOD_SCORE){
                    moveCooldown = VERY_FAST_COOLDOWN;
                } else {
                    moveCooldown = REEEALLY_FAST_COOLDOWN;
                }
            } else {
                moveCooldown--;
            }
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Ne fais rien pour l'instant
    }

    /**
     * Met à jour la position de la bulle en fonction de la rotation du téléphone
     *
     * @param x la position x retournée par le "capteur de gravité"
     * @param y la position y retournée par le "capteur de gravité"
     */
    private void changeSnakeDirection(float x, float y) {
        if (y > 3 && snake.getDirectionX() != -movementValue) {
            snake.rotateSnake(90);
            snake.setDirectionY(0);
            snake.setDirectionX(movementValue);
        }
        else if (y < -3 && snake.getDirectionX() != movementValue) {
            snake.rotateSnake(-90);
            snake.setDirectionY(0);
            snake.setDirectionX(-movementValue);
        }
        else if (x > 3 && snake.getDirectionY() != -movementValue) {
            snake.rotateSnake(180);
            snake.setDirectionY(movementValue);
            snake.setDirectionX(0);
        }
        else if (x < -3 && snake.getDirectionY() != movementValue) {
            snake.rotateSnake(0);
            snake.setDirectionY(-movementValue);
            snake.setDirectionX(0);
        }
    }

    /**
     * Déplace la pomme à un endroit aléatoire dans la zone de jeu
     */
    private void moveApple() {
        final int MAX_X = 19;
        final int MAX_Y = 9;
        final int MIN_X = 0;
        final int MIN_Y = 0;

        int appleX;
        int appleY;

        appleX = (int) ((Math.random() * (MAX_X - MIN_X)) + MIN_X);
        appleY = (int) ((Math.random() * (MAX_Y - MIN_Y)) + MIN_Y);
        imageApple.setX(appleX * movementValue);
        imageApple.setY(appleY * movementValue);
    }

    /**
     * Vérifie si la tête du serpent mange une pomme
     */
    private void checkSnakeAppleCollision() {
        Rect headRect = new Rect((int) snake.getPosX(), (int) snake.getPosY(), (int) snake.getPosX() + movementValue, (int) snake.getPosY() + movementValue);
        Rect appleRect = new Rect((int) imageApple.getX(), (int) imageApple.getY(), (int) imageApple.getX() + imageApple.getWidth(), (int) imageApple.getY() + imageApple.getHeight());
        if (Rect.intersects(headRect, appleRect)) {
            score += 1;
            moveApple();
            eatApple();
        }

        for (snakeBody segment : bodySegments) {
            Rect bodyRect = new Rect((int) segment.getPosX(), (int) segment.getPosY(), (int) segment.getPosX() + movementValue, (int) segment.getPosY() + movementValue);
            if (Rect.intersects(headRect, bodyRect)) {
                checkSnakeAppleCollision();
            }
        }
    }

    /**
     * Vérifie si le serpent collisionne avec un mur ou avec son propre corps
     */
    private void checkSnakeBodyCollision() {
        Rect headRect = new Rect((int) snake.getPosX(), (int) snake.getPosY(), (int) snake.getPosX() + movementValue, (int) snake.getPosY() + movementValue);
        Rect tailRect = new Rect((int) tail.getPosX(), (int) tail.getPosY(), (int) tail.getPosX() + movementValue, (int) tail.getPosY() + movementValue);
        if (Rect.intersects(headRect, tailRect)) {
            killSnake();
        }

        for (snakeBody segment : bodySegments) {
            Rect bodyRect = new Rect((int) segment.getPosX(), (int) segment.getPosY(), (int) segment.getPosX() + movementValue, (int) segment.getPosY() + movementValue);
            if (Rect.intersects(headRect, bodyRect)) {
                killSnake();
            }
        }
    }

    /**
     * Vérifie si le serpent entre en collision avec le bord de l'écran
     */
    private void checkSnakeCollision() {
        if (snake.getPosY() > screenHeight - movementValue || snake.getPosY() < -20 || snake.getPosX() < -20 || snake.getPosX() > screenWidth - movementValue) {
            killSnake();
        }
    }

    /**
     * Termine l'activitée (appelèe quand le serpent touche un mur ou son corps)
     */
    private void killSnake() {
        this.finish();
    }

    /**
     * Lorsque le serpent mange une pomme, on lui ajoute un nouveau segment à la fin de ses segments actuels et avant sa queue
     */
    private void eatApple() {
        // pour avoir la taille en dp
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        final int DP = 50; // Taille en dp
        int pixels = (int) (DP * displayMetrics.density);

        // modifications des dimensions
        ImageView newSegmentImage = new ImageView(this);
        newSegmentImage.setImageResource(R.drawable.snake_body);
        newSegmentImage.setAdjustViewBounds(true);
        newSegmentImage.setMaxHeight(pixels);
        newSegmentImage.setMaxWidth(pixels);
        newSegmentImage.setScaleType(ImageView.ScaleType.FIT_CENTER);

        // on le place à la position de la queue pour ne pas qu'il soit en haut à gauche
        newSegmentImage.setX(tail.getPosX());
        newSegmentImage.setY(tail.getPosY());

        // ajout à la liste des segments
        snakeBody newSegment = new snakeBody(newSegmentImage);
        bodySegments.add(newSegment);

        // les données nécéssaires pour que la queue suive le nouveau segment et n'aille pas en haut à gauche de l'écran
        newSegment.setPreviousPosX(tail.getPosX());
        newSegment.setPreviousPosY(tail.getPosY());
        newSegment.setRotationAngle(tail.getRotation());
        newSegment.setPreviousRotationAngle(tail.getRotation());

        // ajout du nouveau segment sur la zone de jeu
        snakeGame.addView(newSegmentImage);
    }
}