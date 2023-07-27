package com.ssoftwares.gunsimulator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES10;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

public class TextureCube {
    private FloatBuffer vertexBuffer; // Buffer for vertex-array
    private FloatBuffer texBuffer;    // Buffer for texture-coords-array (NEW)
    private FloatBuffer targetBuffer;
    private float[] vertices = { // Vertices for a face
            -1.0f, -1.0f, 2.0f,  // 0. left-bottom-front
            1.0f, -1.0f, 2.0f,  // 1. right-bottom-front
            -1.0f,  1.0f, 2.0f,  // 2. left-top-front
            1.0f,  1.0f, 2.0f   // 3. right-top-front
    };

    float[] texCoords = { // Texture coords for the above face (NEW)
            0.0f, 1.0f,  // A. left-bottom (NEW)
            1.0f, 1.0f,  // B. right-bottom (NEW)
            0.0f, 0.0f,  // C. left-top (NEW)
            1.0f, 0.0f   // D. right-top (NEW)
    };
    int[] textureIDs = new int[3];  // Array for 3 texture-IDs (NEW)
    private float[] targetVertices = { // Vertices for a face
            -0.1f, -0.1f, 1f,  // 0. left-bottom-front
            0.1f, -0.1f, 1f,  // 1. right-bottom-front
            -0.1f,  0.1f, 1f,  // 2. left-top-front
            0.1f,  0.1f, 1f   // 3. right-top-front
    };
    // Constructor - Set up the buffers
    public TextureCube() {
        // Setup vertex-array buffer. Vertices in float. An float has 4 bytes
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder()); // Use native byte order
        vertexBuffer = vbb.asFloatBuffer(); // Convert from byte to float
        vertexBuffer.put(vertices);         // Copy data into buffer
        vertexBuffer.position(0);           // Rewind

        ByteBuffer nbb = ByteBuffer.allocateDirect(targetVertices.length * 4);
        nbb.order(ByteOrder.nativeOrder()); // Use native byte order
        targetBuffer = nbb.asFloatBuffer(); // Convert from byte to float
        targetBuffer.put(vertices);         // Copy data into buffer
        targetBuffer.position(0);           // Rewind

        // Setup texture-coords-array buffer, in float. An float has 4 bytes (NEW)
        ByteBuffer tbb = ByteBuffer.allocateDirect(texCoords.length * 4);
        tbb.order(ByteOrder.nativeOrder());
        texBuffer = tbb.asFloatBuffer();
        texBuffer.put(texCoords);
        texBuffer.position(0);
    }

    // Draw the shape
    public void draw(GL10 gl, boolean isTarget, float angleX, float angleY) {
        gl.glFrontFace(GL10.GL_CCW);    // Front face in counter-clockwise orientation
//        gl.glEnable(GL10.GL_CULL_FACE); // Enable cull face
//        gl.glCullFace(GL10.GL_BACK);    // Cull the back face (don't display)
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_ONE , GL10.GL_ONE);

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);  // Enable texture-coords-array (NEW)
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texBuffer); // Define texture-coords buffer (NEW)


        //target
//        if (isTarget) {
//            gl.glPushMatrix();
//            gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIDs[1]);
//            gl.glPushMatrix();
//            gl.glTranslatef(0.0f, 0.0f, 1.0f);
//            gl.glRotatef(0, 1.0f, 0.0f, 0.0f); // Rotate (NEW)
//            gl.glRotatef(180, 0.0f, 1.0f, 0.0f); // Rotate (NEW)
//            gl.glScalef(0.2f, 0.2f, 1);
//            gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
//            gl.glPopMatrix();
//        }
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIDs[0]);

        // front
        gl.glPushMatrix();
        gl.glTranslatef(0.0f, 0.0f, 1.0f);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
        gl.glPopMatrix();


        // left
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIDs[0]);
        gl.glPushMatrix();
        gl.glRotatef(270.0f, 0.0f, 1.0f, 0.0f);
        gl.glTranslatef(0.0f, 0.0f, 1.0f);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
        gl.glPopMatrix();

        // back
        gl.glPushMatrix();
        gl.glRotatef(180.0f, 0.0f, 1.0f, 0.0f);
        gl.glTranslatef(0.0f, 0.0f, 1.0f);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
        gl.glPopMatrix();

        // right
        gl.glPushMatrix();
        gl.glRotatef(90.0f, 0.0f, 1.0f, 0.0f);
        gl.glTranslatef(0.0f, 0.0f, 1.0f);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
        gl.glPopMatrix();

        // top
        gl.glPushMatrix();
        gl.glRotatef(270.0f, 1.0f, 0.0f, 0.0f);
        gl.glTranslatef(0.0f, 0.0f, 1.0f);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
        gl.glPopMatrix();
//
        // bottom
        gl.glPushMatrix();
        gl.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
        gl.glTranslatef(0.0f, 0.0f, 1.0f);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
        gl.glPopMatrix();

        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);  // Disable texture-coords-array (NEW)
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisable(GL10.GL_CULL_FACE);


//        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
//        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, targetBuffer);
//        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);  // Enable texture-coords-array (NEW)
//        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texBuffer); // Define texture-coords buffer (NEW)
//        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIDs[1]);
////
//        gl.glTranslatef(-0.0f, -0.0f, 1.0f);
//        gl.glScalef(0.2f , 0.2f , 1);
//        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP , 0 , 4);
//
//        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);  // Disable texture-coords-array (NEW)
//        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
    }

    // Load an image into GL texture
    public void loadTexture(GL10 gl, Context context) {
        InputStream istream = context.getResources().openRawResource(R.drawable.crate);
        Bitmap bitmap;
        try {
            bitmap = BitmapFactory.decodeStream(istream);
        } finally {
            try {
                istream.close();
            } catch(IOException e) { }
        }
        InputStream targetStream = context.getResources().openRawResource(R.drawable.target);
        Bitmap targetBitmap;
        try {
            targetBitmap = BitmapFactory.decodeStream(targetStream);
        } finally {
            try {
                istream.close();
            } catch(IOException e) { }
        }
        gl.glGenTextures(3, textureIDs, 0);  // Generate texture-ID array for 3 textures (NEW)

        // Create Nearest Filtered Texture and bind it to texture 0 (NEW)
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIDs[0]);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

        // Create Linear Filtered Texture and bind it to texture 1 (NEW)
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIDs[1]);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, targetBitmap, 0);

        // Create mipmapped textures and bind it to texture 2 (NEW)
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIDs[2]);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR_MIPMAP_NEAREST);
        if(gl instanceof GL11) {
            gl.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_GENERATE_MIPMAP, GL11.GL_TRUE);
        }
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();
    }
}