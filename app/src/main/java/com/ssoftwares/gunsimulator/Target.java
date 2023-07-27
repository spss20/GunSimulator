package com.ssoftwares.gunsimulator;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Target {
    private FloatBuffer vertexBuffer; // Buffer for vertex-array

    private float[] vertices = { // Vertices for a face
            -0.1f, -0.1f, 1f,  // 0. left-bottom-front
            0.1f, -0.1f, 1f,  // 1. right-bottom-front
            -0.1f,  0.1f, 1f,  // 2. left-top-front
            0.1f,  0.1f, 1f   // 3. right-top-front
    };

    public Target(){
        // Setup vertex-array buffer. Vertices in float. An float has 4 bytes
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder()); // Use native byte order
        vertexBuffer = vbb.asFloatBuffer(); // Convert from byte to float
        vertexBuffer.put(vertices);         // Copy data into buffer
        vertexBuffer.position(0);           // Rewind
    }

    public void draw(GL10 gl) {
        gl.glFrontFace(GL10.GL_CCW);    // Front face in counter-clockwise orientation
//        gl.glEnable(GL10.GL_CULL_FACE); // Enable cull face
//        gl.glCullFace(GL10.GL_BACK);    // Cull the back face (don't display)

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
//        gl.glTranslatef(0.0f, 0.0f, 1.0f);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);

        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
//        gl.glDisable(GL10.GL_CULL_FACE);
    }

}
