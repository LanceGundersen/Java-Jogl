import javax.media.opengl.*;
import javax.media.opengl.awt.GLJPanel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * File JavaJogl.java
 * Creates 3D OpenGL to display six different shapes and transformations.
 *
 * @author Lance Gundersen
 * @version 1.2
 * @since 2019-02-10
 */
public class JavaJogl extends JPanel implements GLEventListener {

    private int frameNumber;

    /**
     * Default Constructor.
     */
    private JavaJogl() {
        GLCapabilities caps = new GLCapabilities(null);

        final GLJPanel display = new GLJPanel(caps);
        display.setPreferredSize(new Dimension(640, 480));
        display.addGLEventListener(this);
        setLayout(new BorderLayout());
        add(display, BorderLayout.CENTER);

        Timer animationTimer = new Timer(20, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                frameNumber++;
                display.repaint();
            }
        });
        animationTimer.start();
    }

    /**
     * This is the main method.
     *
     * @param args Unused.
     */
    public static void main(String[] args) {
        JFrame window = new JFrame("CMSC 405 Project 2");
        JavaJogl panel = new JavaJogl();
        window.setContentPane(panel);
        window.pack();
        window.setResizable(false);
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        window.setLocation(
                (screen.width - window.getWidth()) / 2,
                (screen.height - window.getHeight()) / 2);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
    }

    /**
     * Takes an object from Shapes class, then draws, and colors, said Object into the scene.
     *
     * @param gl2 Graphics interface
     * @param obj Object from Shapes class
     * @param x   Translation along the X axis
     * @param z   Translation along the Z axis
     */
    private void drawObj(GL2 gl2, Shapes obj, double x, double z) {
        gl2.glPushMatrix();
        gl2.glTranslated(x, (double) 0, z);

        for (int i = 0; i < obj.faces.length; i++) {
            gl2.glPushMatrix();
            double r = obj.rgb[i][0];
            double g = obj.rgb[i][1];
            double b = obj.rgb[i][2];

            gl2.glColor3d(r, g, b);
            gl2.glBegin(GL2.GL_TRIANGLE_FAN);
            for (int j = 0; j < obj.faces[i].length; j++) {
                int v = obj.faces[i][j];
                gl2.glVertex3dv(obj.vertices[v], 0);
            }
            gl2.glEnd();

            gl2.glColor3d(0, 0, 0);
            gl2.glBegin(GL2.GL_LINE_LOOP);
            for (int j = 0; j < obj.faces[i].length; j++) {
                int v = obj.faces[i][j];
                gl2.glVertex3dv(obj.vertices[v], 0);
            }
            gl2.glEnd();
            gl2.glPopMatrix();
        }
        gl2.glPopMatrix();
    }

    public void init(GLAutoDrawable drawable) {
        GL2 gl2 = drawable.getGL().getGL2();

        gl2.glEnable(GL2.GL_DEPTH_TEST);
        gl2.glLineWidth(2);
        gl2.glShadeModel(GL2.GL_SMOOTH);
        gl2.glEnable(GL2.GL_COLOR_MATERIAL);

        gl2.glMatrixMode(GL2.GL_PROJECTION);
        gl2.glLoadIdentity();
        gl2.glOrtho(-10, 10, -10, 10, -5, 5); //sets up the orthographic projection with the near and far perspective to 5.
        gl2.glMatrixMode(GL2.GL_MODELVIEW);

        gl2.glClearDepth(1.0f);
        gl2.glDepthFunc(GL2.GL_LEQUAL);
        gl2.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);

        gl2.glEnable(GL2.GL_BLEND);
        gl2.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);

    }

    public void display(GLAutoDrawable drawable) {
        GL2 gl2 = drawable.getGL().getGL2();
        gl2.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

        gl2.glLoadIdentity();

        // Hole Shape
        gl2.glPushMatrix();
        gl2.glLineWidth(2);
        gl2.glTranslated(-6.0, 5.0, 0);
        gl2.glRotated(90, 1.5, 2, -3);
        gl2.glScaled(1, 1, 1);
        gl2.glRotated(frameNumber * 0.7, 0, 0, 1);
        drawObj(gl2, Shapes.holeShape, 0, -1.5);
        gl2.glPopMatrix();

        // Prism Shape
        gl2.glPushMatrix();
        gl2.glLineWidth(1);
        gl2.glTranslated(0, 5.0, 0);
        gl2.glRotated(30, 1.5, -2, 3);
        gl2.glScaled(1, 1, 1);
        gl2.glRotated(-frameNumber * 0.7, 1, 0, 0);
        drawObj(gl2, Shapes.prismShape, 0, 1.5);
        gl2.glPopMatrix();

        // Len Shape
        gl2.glPushMatrix();
        gl2.glTranslated(2 * Math.cos(.015 * frameNumber) + 0, 2 * Math.sin(.015 * frameNumber) + 0, 0.0);
        gl2.glTranslated(6.0, 5.0, 0);
        gl2.glRotated(45, 1.5, -2, 3);
        gl2.glScaled(.5, .5, .5);
        gl2.glRotated(-frameNumber * 0.7, 0, 1, 0);
        drawObj(gl2, Shapes.lenShape, 2, 1.5);
        gl2.glPopMatrix();

        // Cone Shape
        gl2.glPushMatrix();
        gl2.glLineWidth(2);
        gl2.glTranslated(-12 + 24 * (frameNumber % 500) / 500.0, 0, 0);
        gl2.glScaled(1, 1, 1);
        gl2.glRotated(-frameNumber * 0.7, 0, 1, 0);
        drawObj(gl2, Shapes.coneShape, -2, -1.5);
        gl2.glPopMatrix();

        // Gem Shape
        gl2.glPushMatrix();
        gl2.glLineWidth(1);
        gl2.glTranslated(-6.0, -5.0, 0);
        gl2.glRotated(20, 1.5, -2, 3);
        gl2.glScaled(.99 * (frameNumber % 250) / 250, .99 * (frameNumber % 250) / 250, .99 * (frameNumber % 250) / 250);
        gl2.glRotated(-frameNumber * 0.7, 1, 0, 0);
        drawObj(gl2, Shapes.gemShape, 2, -1.5);
        gl2.glPopMatrix();

        // V Shape
        gl2.glPushMatrix();
        gl2.glLineWidth(2);
        gl2.glTranslated(6.0, -5.0, 0);
        gl2.glRotated(35, 1.5, -2, 3);
        gl2.glScaled(1, 1, 1);
        gl2.glRotated(-frameNumber * 0.7, 0, 1, 0);
        drawObj(gl2, Shapes.vShape, -2, 1.5);
        gl2.glPopMatrix();
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) { }

    public void dispose(GLAutoDrawable arg0) { }
}