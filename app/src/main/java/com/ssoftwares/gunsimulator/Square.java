package com.ssoftwares.gunsimulator;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Square {
    private FloatBuffer vertexBuffer; // Buffer for vertex-array
    private FloatBuffer texBuffer;    // Buffer for texture-coords-array (NEW)

    float[] vertices = {
      -0.1f , -0.1f , -2 ,
      0.1f , -0.1f , -2 ,
      -0.1f , 0.1f , -2,
      0.1f , 0.1f , -2
    };

    float[] texCoords = { // Texture coords for the above face (NEW)
            0.0f, 1.0f,  // A. left-bottom (NEW)
            1.0f, 1.0f,  // B. right-bottom (NEW)
            0.0f, 0.0f,  // C. left-top (NEW)
            1.0f, 0.0f   // D. right-top (NEW)
    };

    public Square(){
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        vertexBuffer = vbb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        ByteBuffer tbb = ByteBuffer.allocateDirect(texCoords.length * 4);
        tbb.order(ByteOrder.nativeOrder());
        texBuffer = tbb.asFloatBuffer();
        texBuffer.put(texCoords);
        texBuffer.position(0);
    }

    public void draw(GL10 gl) {
        gl.glFrontFace(GL10.GL_CCW);    // Front face in counter-clockwise orientation
//        gl.glEnable(GL10.GL_CULL_FACE); // Enable cull face
//        gl.glCullFace(GL10.GL_BACK);    // Cull the back face (don't display)

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);

        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);  // Enable texture-coords-array (NEW)
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texBuffer); // Define texture-coords buffer (NEW)
        gl.glBindTexture(GL10.GL_TEXTURE_2D, 2);

        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP , 0 , 4);

//
//        gl.glPushMatrix();
//        gl.glTranslatef(0.0f, 0.0f, 1.0f);
//        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP , 0 , 4);
//        gl.glPopMatrix();
//
//        gl.glPushMatrix();
//        gl.glRotatef(90.0f, 0.0f, 1.0f, 0.0f);
//        gl.glTranslatef(0.0f, 0.0f, -1.0f);
//        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP , 0 , 4);
//        gl.glPopMatrix();
//
//        gl.glPushMatrix();
//        gl.glRotatef(270.0f, 0.0f, 1.0f, 0.0f);
//        gl.glTranslatef(0.0f, 0.0f, -1.0f);
//        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP , 0 , 4);
//        gl.glPopMatrix();
//
//        gl.glPushMatrix();
//        gl.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
//        gl.glTranslatef(0.0f, 0.0f, -1.0f);
//        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP , 0 , 4);
//        gl.glPopMatrix();
//
//        gl.glPushMatrix();
//        gl.glRotatef(180.0f, 1.0f, 0.0f, 0.0f);
//        gl.glTranslatef(0.0f, 0.0f, -1.0f);
//        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP , 0 , 4);
//        gl.glPopMatrix();
//
//        gl.glPushMatrix();
//        gl.glRotatef(270.0f, 1.0f, 0.0f, 0.0f);
//        gl.glTranslatef(0.0f, 0.0f, -1.0f);
//        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP , 0 , 4);
//        gl.glPopMatrix();
//        gl.glPushMatrix();
//        gl.glRotatef(-360.0f, 0.0f, 1.0f, 0.0f);
//        gl.glTranslatef(0.0f, 0.0f, 1.0f);
//        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP , 0 , 4);
//        gl.glPopMatrix();
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);  // Disable texture-coords-array (NEW)
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisable(GL10.GL_CULL_FACE);
    }

}
