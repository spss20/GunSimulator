package com.ssoftwares.gunsimulator;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SensorEventListener {

    private GLSurfaceView glView;   // Use GLSurfaceView
    private MyGLRenderer renderer;
    private SensorManager sensorManager;
    private Sensor rotationalVectorSensor;
    private ImageView compass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button zoomIn = findViewById(R.id.zoom_in);
        Button zoomOut = findViewById(R.id.zoom_out);
        Button lockTarget = findViewById(R.id.lock_target);
        Button resetTarget = findViewById(R.id.reset_target);
        ImageView shoot = findViewById(R.id.shoot);
        compass = findViewById(R.id.compass);
        zoomIn.setOnClickListener(this);
        zoomOut.setOnClickListener(this);
        lockTarget.setOnClickListener(this);
        resetTarget.setOnClickListener(this);
        shoot.setOnClickListener(this);


        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        rotationalVectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        glView = findViewById(R.id.gl_surface_view);
        renderer = new MyGLRenderer(this);
        glView.setRenderer(renderer);
    }

    @Override
    protected void onResume() {
        super.onResume();
        glView.onResume();
        sensorManager.registerListener(this, rotationalVectorSensor, 10000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        glView.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent evt) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_A:           // Zoom out (decrease z)
                renderer.z -= 0.2f;
                break;
            case KeyEvent.KEYCODE_Z:           // Zoom in (increase z)
                renderer.z += 0.2f;
                break;
        }
        return true;
    }

//    boolean toPrint = true;

//    float[] orientation = new float[3];
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            SensorManager.getRotationMatrixFromVector(renderer.mRotationMatrix , event.values);
//            SensorManager.getOrientation(renderer.mRotationMatrix ,orientation);
//            if (toPrint) {
//                toPrint = false;
////                float[] values = rotationVectorAction(event.values);
//                Log.v("Values: ", orientation[0] + "  " + orientation[1] + "  " + orientation[2]);
//
//                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        toPrint = true;
//                    }
//                }, 3000);
//            }
        }
    }

    private float[] rotationVectorAction(float[] values) {
        float[] result = new float[3];
        float vec[] = values;
        float[] orientation = new float[3];
        float[] rotMat = new float[9];
        SensorManager.getRotationMatrixFromVector(rotMat, vec);
        SensorManager.getOrientation(rotMat, orientation);
        result[0] = (float) orientation[0]; //Yaw
        result[1] = (float) orientation[1]; //Pitch
        result[2] = (float) orientation[2]; //Roll
        return result;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.zoom_in:
                renderer.z += 0.1f;
                break;
            case R.id.zoom_out:
                renderer.z -= 0.1f;
                break;
            case R.id.lock_target:
                renderer.isTarget = true;
                break;
            case R.id.reset_target:
                if (renderer.isTarget){
                    Toast.makeText(this, "No Target Selected", Toast.LENGTH_SHORT).show();
                    return;
                }
                renderer.resetTarget();
                break;
            case R.id.shoot:
                if (!renderer.isTarget) {
                    Toast.makeText(this, "No Target Selected", Toast.LENGTH_SHORT).show();
                    return;
                }
                float xdiff = Math.abs((renderer.angleX - renderer.targetAnglex));
                float ydiff = Math.abs((renderer.angleY - renderer.targetAngley));
                if (xdiff < 1 &&
                        ydiff < 1) {
                    Toast.makeText(this, "Yeah!!! Target Shooted", Toast.LENGTH_SHORT).show();
                    renderer.resetTarget();
                } else {
                    Toast.makeText(this, "Target Missed", Toast.LENGTH_SHORT).show();
                }
        }
    }

    private float previousX;
    private float previousY;
    private final float TOUCH_SCALE_FACTOR = 180.0f / 320.0f;

    @Override
    public boolean onTouchEvent(MotionEvent evt) {
        float currentX = evt.getX();
        float currentY = evt.getY();
        float deltaX, deltaY;
        switch (evt.getAction()) {
            case MotionEvent.ACTION_MOVE:
                // Modify rotational angles according to movement
                deltaX = currentX - previousX;
                deltaY = currentY - previousY;
                renderer.angleX += deltaY * TOUCH_SCALE_FACTOR;
                renderer.angleY += deltaX * TOUCH_SCALE_FACTOR;
        }
        // Save current x, y
        previousX = currentX;
        previousY = currentY;
        return true;
    }
}