package com.ssoftwares.gunsimulator;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyGLRenderer implements GLSurfaceView.Renderer {

    Context context;
    private TextureCube cube;
    private TargetCube targetCube;
    private Square square;
    float angleX = 0;   // (NEW)
    float angleY = 0;   // (NEW)
    //    float speedX = 1;   // (NEW)
//    float speedY = 0;   // (NEW)
    float z = -1.1f;    // (NEW)
    float[] mRotationMatrix = new float[16];
    int currentTextureFilter = 0;  // Texture filter (NEW)
    boolean lightingEnabled = false;   // Is lighting on? (NEW)
    boolean isTarget = false;
    private float[] lightAmbient = {0.5f, 0.5f, 0.5f, 1.0f};
    private float[] lightDiffuse = {1.0f, 1.0f, 1.0f, 1.0f};
    private float[] lightPosition = {0.0f, 0.0f, 2.0f, 1.0f};
    boolean blendingEnabled = false;  // Is blending on? (NEW)

    public MyGLRenderer(Context c) {
        context = c;
        cube = new TextureCube();
        targetCube = new TargetCube();
        square = new Square();
        mRotationMatrix[0] = 1;
        mRotationMatrix[4] = 1;
        mRotationMatrix[8] = 1;
        mRotationMatrix[12] = 1;

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);  // Set color's clear-value to black
        gl.glClearDepthf(1.0f);            // Set depth's clear-value to farthest
        gl.glEnable(GL10.GL_DEPTH_TEST);   // Enables depth-buffer for hidden surface removal
        gl.glDepthFunc(GL10.GL_LEQUAL);    // The type of depth testing to do
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);  // nice perspective view
        gl.glShadeModel(GL10.GL_SMOOTH);   // Enable smooth shading of color
        gl.glDisable(GL10.GL_DITHER);      // Disable dithering for better performance

        // You OpenGL|ES initialization code here
        // ......
        cube.loadTexture(gl, context);    // Load image into Texture (NEW)
        gl.glEnable(GL10.GL_TEXTURE_2D);  // Enable texture (NEW)

        // Setup lighting GL_LIGHT1 with ambient and diffuse lights (NEW)
        gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_AMBIENT, lightAmbient, 0);
        gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_DIFFUSE, lightDiffuse, 0);
        gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_POSITION, lightPosition, 0);
        gl.glEnable(GL10.GL_LIGHT1);   // Enable Light 1 (NEW)
        gl.glEnable(GL10.GL_LIGHT0);   // Enable the default Light 0 (NEW)

        gl.glColor4f(1.0f, 1.0f, 1.0f, 0.2f);           // Full brightness, 50% alpha (NEW)
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE); // Select blending function (NEW)
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        if (height == 0) height = 1;   // To prevent divide by zero
        float aspect = (float) width / height;

        // Set the viewport (display area) to cover the entire window
        gl.glViewport(0, 0, width, height);
        // Setup perspective projection, with aspect ratio matches viewport
        gl.glMatrixMode(GL10.GL_PROJECTION); // Select projection matrix
        gl.glLoadIdentity();

        GLU.gluPerspective(gl, 45, aspect, 0.1f, 100.f);

        gl.glMatrixMode(GL10.GL_MODELVIEW);  // Select model-view matrix
        gl.glLoadIdentity();                 // Reset

        // You OpenGL|ES display re-sizing code here
        // ......
    }

    float n;
    float targetAnglex = 0;
    float targetAngley = 0;

    boolean toPrint = true;
    @Override
    public void onDrawFrame(GL10 gl) {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        if (lightingEnabled) {
            gl.glEnable(GL10.GL_LIGHTING);
        } else {
            gl.glDisable(GL10.GL_LIGHTING);
        }
        if (blendingEnabled) {
            gl.glEnable(GL10.GL_BLEND);       // Turn blending on (NEW)
            gl.glDisable(GL10.GL_DEPTH_TEST); // Turn depth testing off (NEW)

        } else {
            gl.glDisable(GL10.GL_BLEND);      // Turn blending off (NEW)
            gl.glEnable(GL10.GL_DEPTH_TEST);  // Turn depth testing on (NEW)
        }
        // ----- Render the CubeOld -----
        gl.glLoadIdentity();                  // Reset the current model-view matrix
        gl.glTranslatef(-0.0f, -0.0f, z);   // Translate into the screen (NEW)
        if (n != z) {
            n = z;
        }
//        gl.glScalef(0.1f , 0.1f , 0);

        gl.glMultMatrixf(mRotationMatrix , 0);

//        gl.glRotatef(angleX, 1.0f, 0.0f, 0.0f); // Rotate (NEW)
//        gl.glRotatef(angleY, 0.0f, 1.0f, 0.0f); // Rotate (NEW)
//        cube.draw(gl , currentTextureFilter);
        cube.draw(gl, isTarget, angleX, angleY);

        if (isTarget) {
            gl.glLoadIdentity();
            gl.glTranslatef(-0.0f, -0.0f, z);   // Translate into the screen (NEW)
//            gl.glMultMatrixf(mRotationMatrix , 0);
            if (targetAnglex != 0) {
                gl.glRotatef((angleX - targetAnglex), 1.0f, 0.0f, 0.0f); // Rotate (NEW)
                gl.glRotatef((angleY - targetAngley), 0.0f, 1.0f, 0.0f); // Rotate (NEW)
                square.draw(gl);
            } else {
                square.draw(gl);
                targetAnglex = angleX;
                targetAngley = angleY;
            }
        }
    }

    public void resetTarget() {
        isTarget = false;
        targetAnglex = 0;
        targetAngley = 0;
    }
}
