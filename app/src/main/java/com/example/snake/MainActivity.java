package com.example.snake;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
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
    private ImageView imageApple;

    // déclaration des variables
    private int screenHeight;
    private int screenWidth;
    int directionX;
    int directionY;

    int score = 0;


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
        imageApple = findViewById(R.id.imageApple);

        // définit le type de capteur
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        gravity = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);

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

            // Met à jour la TextView avec les valeurs du capteur de gravité, raccourci à un chiffre après la virgule
            gravityValues.setText("X: " + ((int)(x*10))/10f + "\nY: " + ((int)(y*10))/10f + "\nZ: " + ((int)(z*10))/10f + "\nscore : " + score);

            // Met à jour la TextView avec les valeurs du capteur de gravité, affichés en % (il y a des pourcents négatifs, donc jsp)
            // gravityValues.setText("X: " + ((int)(x*10)) + "%\nY: " + ((int)(y*10)) + "%\nZ: " + ((int)(z*10)) + "%");

            // Méthode qui met à jour la position de la bulle
            changeSnakeDirection(x, y);
            checkSnakeAppleCollision();

            imageSnake.setX(imageSnake.getX() + directionX);
            imageSnake.setY(imageSnake.getY() + directionY);
        }
    }



    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Ne fais rien pour l'instant
    }

    /**
     * Met à jour la position de la bulle en fonction de la rotation du téléphone
     * @param x la position x retournée par le "capteur de gravité"
     * @param y la position y retournée par le "capteur de gravité"
     */
    private void changeSnakeDirection(float x, float y) {

        /*
        // Calculez les nouvelles coordonnées en ajoutant les valeurs du gravity
        float newX = (x * (screenWidth/2f/10f) + screenWidth/2f - imageSnake.getWidth()/2f);
        float newY = (y * (screenHeight/2f/10f) + screenHeight/2f - imageSnake.getHeight()/2f);

        // Limitez les coordonnées à l'intérieur de l'écran
        newX = Math.max(0, Math.min(newX, screenWidth - imageSnake.getWidth()));
        newY = Math.max(0, Math.min(newY, screenHeight - imageSnake.getHeight()));

        // Définissez les nouvelles coordonnées pour le TextView
        imageSnake.setX(newX);
        imageSnake.setY(newY);
        */

        if(y > 3 && directionX != -3) {
            imageSnake.setRotation(90);
            directionY = 0;
            directionX = 3;
            directionValues.setText("droite" + "\nX:" + directionX + "\nY:" + directionY + "\nwormX " + imageSnake.getX() + "\nwormY " + imageSnake.getY());
        } if (y < -3 && directionX != 3) {
            imageSnake.setRotation(-90);
            directionY = 0;
            directionX = -3;
            directionValues.setText("gauche" + "\nX:" + directionX + "\nY:" + directionY + "\nwormX " + imageSnake.getX() + "\nwormY " + imageSnake.getY());
        } if (x > 3 && directionY != -3) {
            imageSnake.setRotation(180);
            directionY = 3;
            directionX = 0;
            directionValues.setText("bas" + "\nX:" + directionX + "\nY:" + directionY + "\nwormX " + imageSnake.getX() + "\nwormY " + imageSnake.getY());
        } if (x < -3 && directionY != 3) {
            imageSnake.setRotation(0);
            directionY = -3;
            directionX = 0;
            directionValues.setText("haut" + "\nX:" + directionX + "\nY:" + directionY + "\nwormX " + imageSnake.getX() + "\nwormY " + imageSnake.getY());
        } if(x >= -3 && x <= 3 && y >= -3 && y <= 3) {
            directionValues.setText("tout droit" + "\nwormX " + imageSnake.getX() + "\nwormY " + imageSnake.getY());
        }

        if(imageSnake.getY() > screenHeight - imageSnake.getHeight() || imageSnake.getY() < 0 || imageSnake.getX() < 0 || imageSnake.getX() > screenWidth - imageSnake.getWidth()) {
            directionValues.setText("t mor");
            directionY = 0;
            directionX = 0;
            this.finish();
        }
    }

    /**
     * Génère une pomme à un endroit aléatoire sur l'écran
     */
    private void moveApple() {
        int MAX_X = screenWidth - 100;
        int MAX_Y = screenHeight - 100;
        int MIN_X = 50;
        int MIN_Y = 50;

        int appleX = (int) ((Math.random() * (MAX_X - MIN_X)) + MIN_X);
        int appleY = (int) ((Math.random() * (MAX_Y - MIN_Y)) + MIN_Y);


        appleValues.setText("apple X :" + appleX + "\napple Y :" + appleY);

        imageApple.setX(appleX);
        imageApple.setY(appleY);
    }

    private void checkSnakeAppleCollision() {
        Rect rc1 = new Rect(imageSnake.getLeft(), imageSnake.getTop(), imageSnake.getRight(), imageSnake.getBottom());
        Rect rc2 = new Rect(imageApple.getLeft(), imageApple.getTop(), imageApple.getRight(), imageApple.getBottom());
        if (Rect.intersects(rc1, rc2)) {
            score+=1;
            // moveApple();
        }
    }
}