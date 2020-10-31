package game.level.gameObjects.testing;

import com.jogamp.newt.Window;
import com.jogamp.newt.event.KeyAdapter;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.awt.AWTKeyAdapter;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.texture.Texture;
import game.level.gameObjects.GameObject;
import game.level.gameObjects.GameObjectFromFile;
import game.level.gameObjects.NotEnterableBuilding;
import game.level.gameWorld.CoordinateSystem;
import game.level.gameWorld.TexturesManager;
import math.Axis;
import math.Vertex;

import javax.media.opengl.*;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.GLU;
import java.awt.*;
import java.util.Collections;

public class Test1  extends KeyAdapter implements GLEventListener {
    static String texturePath = "";

    private float xrot = 1;        // X Rotation ( NEW )
    private float yrot;        // Y Rotation ( NEW )
    private float zrot;        // Z Rotation ( NEW )
    private CoordinateSystem coordsSystem = new CoordinateSystem(0,4,0);
    private Texture texture;
    private GameObject obj;
    static private TexturesManager texturesManager;
    static GLU glu = new GLU();
    static GLCanvas canvas = new GLCanvas();
    static Frame frame = new Frame("Jogl 3D Shape/Rotation");

    static Animator animator = new Animator(canvas);
    public Test1(GameObject gameObject) {
        obj = gameObject;
    }
    public void display(GLAutoDrawable gLDrawable) {
        final GL2 gl = gLDrawable.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();
        try {
            Vertex lookat = coordsSystem.getOrigin().subtract(coordsSystem.getZAxis());
            glu.gluLookAt(coordsSystem.getOrigin().getX(), coordsSystem.getOrigin().getY(),
                    coordsSystem.getOrigin().getZ(), lookat.getX(),
                    lookat.getY(), lookat.getZ(),
                    coordsSystem.getYAxis().getX(), coordsSystem.getYAxis().getY(), coordsSystem.getYAxis().getZ());
        } catch (Exception ex) {
            return;
        }

        obj.update(gLDrawable);




    }

    public void displayChanged(GLAutoDrawable gLDrawable,
                               boolean modeChanged, boolean deviceChanged) {
    }

    public void init(GLAutoDrawable drawable) {
        texturesManager.loadTextures(Collections.singletonList("resources/bi/textures/Box_D.jpg"));
        texturesManager.bind("resources/notEnterableBuilding/textures/Box_D.jpg", this.obj);
        GL2 gl = drawable.getGL().getGL2();

        // Smooth Shading
        gl.glShadeModel(GL2.GL_SMOOTH);

        // Black background
        gl.glClearColor(0f, 0f, 0f, 0.5f);

        // Depth Buffer clearing
        gl.glClearDepth(1f);

        // Depth Testing
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glDepthFunc(GL2.GL_LEQUAL);

        // Use best perspective calculations
        gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);
        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
//        gl.glShadeModel(GL2.GL_SMOOTH);              // Enable Smooth Shading
//        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);    // Black Background
//        gl.glClearDepth(1.0f);                      // Depth Buffer Setup
//        gl.glEnable(GL2.GL_DEPTH_TEST);              // Enables Depth Testing
//        gl.glDepthFunc(GL2.GL_LEQUAL);               // The Type Of Depth Testing To Do
//        // Really Nice Perspective Calculations
//        gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);
//        gl.glEnable(GL2.GL_TEXTURE_2D);
//        try {
//            String filename="resources/ground.jpg"; // the FileName to open
//            texture=TextureIO.newTexture(new File( filename ),true);
//        } catch (IOException e) {
//            e.printStackTrace();
//            throw new RuntimeException(e);
//        }
//        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR);
//        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
        if (drawable instanceof com.jogamp.newt.Window) {
            com.jogamp.newt.Window window = (Window) drawable;
            window.addKeyListener(this);
        } else if (GLProfile.isAWTAvailable() && drawable instanceof java.awt.Component) {
            java.awt.Component comp = (java.awt.Component) drawable;
            new AWTKeyAdapter(this, drawable).addTo(comp);
        }
        obj.init(drawable);

    }

    public void reshape(GLAutoDrawable drawable, int x,
                        int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();
        if(height <= 0) {
            height = 1;
        }
        float h = (float)width / (float)height;
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(50.0f, h, 1.0, 1000.0);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            exit();
        }
        double coef = 1;
        double coefMove = 0.1;
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            this.coordsSystem.rotate(-coef, Axis.X);
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            this.coordsSystem.rotate(coef,Axis.X);
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            this.coordsSystem.rotate(coef,Axis.Y);
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            this.coordsSystem.rotate(-coef, Axis.Y);
        }
        coef = coefMove;
        if (e.getKeyCode() == KeyEvent.VK_W) {
            this.coordsSystem.move(Axis.Z,-coef);
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            this.coordsSystem.move(Axis.Z, coef);
        }
        if (e.getKeyCode() == KeyEvent.VK_A) {
            this.coordsSystem.move(Axis.X, -coef);
        }
        if (e.getKeyCode() == KeyEvent.VK_D) {
            this.coordsSystem.move(Axis.X, coef);
        }
        if (e.getKeyCode() == KeyEvent.VK_Q) {
            this.coordsSystem.move(Axis.Y, coef);
        }
        if (e.getKeyCode() == KeyEvent.VK_E) {
            this.coordsSystem.move(Axis.Y, -coef);
        }

    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {

    }

    public static void exit(){
        animator.stop();
        frame.dispose();
        System.exit(0);
    }

    public static void main(String[] args) {
        GameObjectFromFile g = new GameObjectFromFile(new Vertex(0,0,15),"resources/trantula/trantula.obj");
      //  NotEnterableBuilding building = new NotEnterableBuilding(25,25, new Vertex(0,0,-10),
      //          "resources/notEnterableBuilding/residential-buildings.obj");
       texturesManager = new TexturesManager();



        //building.setTexturesManager(texturesManager);
        Test1 test1 = new Test1(g);
        canvas.addGLEventListener(test1);
        frame.add(canvas);
        frame.setSize(800, 600);
//		frame.setUndecorated(true);
//		frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                // Run this on another thread than the AWT event queue to
                // make sure the call to Animator.stop() completes before
                // exiting
                new Thread(new Runnable() {
                    public void run() {
                        animator.stop();
                        System.exit(0);
                    }
                }).start();
            }
        });
        frame.setVisible(true);
        animator.start();
        canvas.requestFocus();
    }

    @Override
    public void dispose(GLAutoDrawable arg0) {
        // TODO Auto-generated method stub

    }
}
