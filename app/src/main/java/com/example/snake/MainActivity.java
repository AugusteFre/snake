package com.example.snake;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements SensorEventListener {

    // déclaration des modules
    private SensorManager sensorManager;
    private Sensor gravity;
    private TextView gravityValues;
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

    snakeHead snake;
    snakeBody firstSegment;


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
        gravityValues = findViewById(R.id.gravityValues);
        directionValues = findViewById(R.id.directionValue);
        appleValues = findViewById(R.id.appleValue);

        imageSnake = findViewById(R.id.imageSnake);
        imageSnakeBody = findViewById(R.id.imageSnakeBody);
        imageSnakeTail = findViewById(R.id.imageSnakeTail);
        imageApple = findViewById(R.id.imageApple);

        // définit le type de capteur
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        gravity = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);

        moveApple();

        snake = new snakeHead(imageSnake);
        firstSegment = new snakeBody(imageSnakeBody);
        movementValue = screenWidth/19;
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

            // Met à jour la TextView avec les valeurs du capteur de gravité, raccourci à un chiffre après la virgule
            gravityValues.setText("X: " + ((int) (x * 10)) / 10f + "\nY: " + ((int) (y * 10)) / 10f + "\nZ: " + ((int) (z * 10)) / 10f + "\nscore : " + score);

            // Met à jour la TextView avec les valeurs du capteur de gravité, affichés en % (il y a des pourcents négatifs, donc jsp)
            // gravityValues.setText("X: " + ((int)(x*10)) + "%\nY: " + ((int)(y*10)) + "%\nZ: " + ((int)(z*10)) + "%");

            // Méthode qui met à jour la position du serpent

            if(moveCooldown == 0) {
                checkSnakeAppleCollision();
                changeSnakeDirection(x, y);
                snake.moveSnake();
                firstSegment.followPreviousSegment(snake.getPreviousPosX(), snake.getPreviousPosY(), snake.getRotationAngle());
                moveCooldown = 15;
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
            snake.setDirectionX(100);
            directionValues.setText("droite" + "\nX:" + snake.getDirectionX() + "\nY:" + snake.getDirectionY() + "\nwormX " + snake.getPosX() + "\nwormY " + snake.getPosY());
        }
        else if (y < -3 && snake.getDirectionX() != movementValue) {
            snake.rotateSnake(-90);
            snake.setDirectionY(0);
            snake.setDirectionX(-movementValue);
            directionValues.setText("gauche" + "\nX:" + snake.getDirectionX() + "\nY:" + snake.getDirectionY() + "\nwormX " + snake.getPosX() + "\nwormY " + snake.getPosY());
        }
        else if (x > 3 && snake.getDirectionY() != -movementValue) {
            snake.rotateSnake(180);
            snake.setDirectionY(movementValue);
            snake.setDirectionX(0);
            directionValues.setText("bas" + "\nX:" + snake.getDirectionX() + "\nY:" + snake.getDirectionY() + "\nwormX " + snake.getPosX() + "\nwormY " + snake.getPosY());
        }
        else if (x < -3 && snake.getDirectionY() != movementValue) {
            snake.rotateSnake(0);
            snake.setDirectionY(-movementValue);
            snake.setDirectionX(0);
            directionValues.setText("haut" + "\nX:" + snake.getDirectionX() + "\nY:" + snake.getDirectionY() + "\nwormX " + snake.getPosX() + "\nwormY " + snake.getPosY());
        }
        else if (x >= -3 && x <= 3 && y >= -3 && y <= 3) {
            directionValues.setText("tout droit" + "\nwormX " + snake.getPosX() + "\nwormY " + snake.getPosY());
        }

        checkSnakeCollision();
    }

    /**
     * Déplace la pomme à un endroit aléatoire dans la zone de jeu
     */
    private void moveApple() {
        int MAX_X = screenWidth - movementValue;
        int MAX_Y = screenHeight - movementValue;
        int MIN_X = movementValue;
        int MIN_Y = movementValue;

        int appleX = (int) ((Math.random() * (MAX_X - MIN_X)) + MIN_X);
        int appleY = (int) ((Math.random() * (MAX_Y - MIN_Y)) + MIN_Y);


        appleValues.setText("apple X :" + appleX + "\napple Y :" + appleY);

        imageApple.setX(appleX);
        imageApple.setY(appleY);
    }

    /**
     * Vérifie si la tête du serpent mange une pomme
     */
    private void checkSnakeAppleCollision() {
        Rect rc1 = new Rect((int) snake.getPosX(), (int) snake.getPosY(), (int) snake.getPosX() + movementValue, (int) snake.getPosY() + movementValue);
        Rect rc2 = new Rect((int) imageApple.getX(), (int) imageApple.getY(), (int) imageApple.getX() + imageApple.getWidth(), (int) imageApple.getY() + imageApple.getHeight());
        if (Rect.intersects(rc1, rc2)) {
            score += 1;
            moveApple();
        }
    }


    /**
     * Vérifie si le serpent entre en collision avec le bord de l'écran
     */
    private void checkSnakeCollision() {
        if (snake.getPosY() > screenHeight - movementValue || snake.getPosY() < -20 || snake.getPosX() < -20 || snake.getPosX() > screenWidth - movementValue) {
            directionValues.setText("t mor");
            snake.setDirectionY(0);
            snake.setDirectionX(0);
            this.finish();
        }
    }


    //TODO en faire une pour la queue qui n'a pas besoin de se rappeler de la position précédente
}