// Shiran Golbar, 313196974
// Lev Levin, 342480456
package game;

import com.jogamp.newt.Window;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.awt.AWTKeyAdapter;
import com.jogamp.opengl.util.awt.TextRenderer;
import events.*;
import events.Event;
import game.level.ILevel;
import game.level.factory.LevelFactory;
import utils.AudioPlayer;
import utils.Drawable2D;
import utils.TimedMessage;
import utils.Timer;

import javax.media.opengl.*;
import javax.media.opengl.glu.GLU;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class that represents a coronavirus game.
 * Manages a general state of the game.
 */
public class CoronavirusGame implements GLEventListener, ILevelObserver, IObserver,Game  /* Game,ILevelObserver*/ {
    final String backgroundMusic = "resources/music/m1.wav";
    // the level that is currently running.
    ILevel currentLevel;
    // a sequential number of a current level.
    int currentLevelNumber;
    private boolean isGameFinished;
    private boolean isLevelFinished = false;
    private EventArgs finishedGameArgs = null;
    private boolean isKeyboard = false;
    private LevelFactory factory;
    private int scrWidth = GLSingletons.getGLCanvas().getWidth();
    private int scrHeight = GLSingletons.getGLCanvas().getHeight();
    private List<TimedMessage> timedMessages = new ArrayList<>();
    private String betweenLevelsMessage = "";
    private Timer musicTimer = new Timer();
    private Map<Integer,Drawable2D> levelToHelpScreen = new HashMap<>(){{
       put(1,new Drawable2D("resources/message/f1-level-1.jpg" ));
       put(2,new Drawable2D("resources/message/f1-level-2.jpg"));
    }};

    private Map<Integer, Drawable2D> levelToStoryScreen = new HashMap<>() {{
        put(1, new Drawable2D("resources/message/f3-level-1.jpg"));
        put(2, new Drawable2D("resources/message/f3-level-2.jpg"));
    }};
    private boolean isHelpScreenOn = false;
    private boolean isStoryScreenOn = false;
    public CoronavirusGame(LevelFactory factory) {
//        levels = new ArrayList<ILevel>(2);
        this.factory = factory; // factory that responsible for creating levels.
        isGameFinished = false;
        finishedGameArgs = null;
        KeyboardManager.getSingleton().attachObserver(this);
        musicTimer.setStart(0,1,20);
    }

    /**
     * This function loads  and initializes a current level so that it can be run.
     * @param glDrawable
     */
    private void loadLevel(GLAutoDrawable glDrawable) {
        currentLevel = factory.createLevel(currentLevelNumber);
        isLevelFinished = false;
        currentLevel.init(glDrawable);
        currentLevel.attachObserver(this);
        AudioPlayer.instance.play(backgroundMusic);

//        timedMessages.add(new TimedMessage("LEVEL " + currentLevelNumber, 0,0,5)
//                .setSize(50).setColor(Color.MAGENTA).setPosition(scrWidth/2 - 100, scrHeight/2));
    }

    // the mapping that helps to track what keys was pressed but hasn't released yet.
    private Map<Short,Boolean> releasedKeys= new HashMap<>();

    @Override
    public void getNotified(Event event) {
        if (event.getType() == EventType.KeyPressed) {
            short lastCode = KeyboardManager.getSingleton().getLastKeyEvent().getKeyCode();
            if (releasedKeys.containsKey(lastCode) && !releasedKeys.get(lastCode)) {
                return;
            }
            switch (lastCode) {

                case KeyEvent.VK_F2:

                    releasedKeys.put(KeyEvent.VK_F2, false);
                    currentLevelNumber++;
                    isLevelFinished = true;
                    betweenLevelsMessage = "Moving to the next level...\nIt will take a few seconds...";
                    if (currentLevelNumber > factory.getNumOfLevels()) {
                        currentLevelNumber = 1;

                    }
                    break;
                case KeyEvent.VK_F1:
                    releasedKeys.put(KeyEvent.VK_F1,false);
                    isHelpScreenOn = !isHelpScreenOn;
                    break;
                case KeyEvent.VK_F3:
                    releasedKeys.put(KeyEvent.VK_F3,false);
                    isStoryScreenOn = !isStoryScreenOn;
                    break;
            }
        } else if (event.getType() == EventType.KeyReleased) {
            releasedKeys.put(KeyboardManager.getSingleton().getLastKeyEvent().getKeyCode(),true);

        } else if (event.getType() == EventType.LevelWin && currentLevelNumber < factory.getNumOfLevels()) {

            isLevelFinished = true;

            currentLevelNumber++;

        } else if(event.getType() == EventType.LevelLoss) {
            isLevelFinished = true;



        } else if (event.getType() == EventType.LevelWin && currentLevelNumber >= factory.getNumOfLevels()) {
            this.isGameFinished = true;
            this.finishedGameArgs = event.getArgs();

        }
    }

    /**
     * This function moves from a previous level to the current level(which is identified with currentLevelNumber).
     * @param glDrawable
     */
    private void passToTheNextLevel(GLAutoDrawable glDrawable) {
        if (!betweenLevelsMessage.equals("")) {


            final GL2 gl = glDrawable.getGL().getGL2();
            gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
            gl.glLoadIdentity();

            TextRenderer renderer = new TextRenderer(new Font("Forte", Font.BOLD, 65));
            int width = GLSingletons.getGLCanvas().getWidth();
            int height = GLSingletons.getGLCanvas().getHeight();
            renderer.beginRendering(width, height);
            renderer.setColor(Color.GREEN);

            renderer.draw(betweenLevelsMessage, 200, height / 2);
            renderer.setColor(Color.WHITE);
            renderer.endRendering();
        }
            loadLevel(glDrawable);
            betweenLevelsMessage = "";


    }

















    @Override
    public void init(GLAutoDrawable glDrawable) {
        currentLevel = factory.createLevel(1);
        currentLevelNumber = 1;
        currentLevel.init(glDrawable);
        currentLevel.attachObserver(this);
        levelToHelpScreen.forEach((k,s)->s.init(glDrawable.getGL().getGL2()));
        levelToStoryScreen.forEach((k,s)->s.init(glDrawable.getGL().getGL2()));
        AudioPlayer.instance.play(backgroundMusic);
        musicTimer.start();
        final GL2 gl = glDrawable.getGL().getGL2();
        gl.glShadeModel(GL2.GL_SMOOTH);              // Enable Smooth Shading
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);    // Black Background
        gl.glClearDepth(1.0f);                      // Depth Buffer Setup
        gl.glEnable(GL2.GL_DEPTH_TEST);              // Enables Depth Testing
        gl.glDepthFunc(GL2.GL_LEQUAL);               // The Type Of Depth Testing To Do
        // Really Nice Perspective Calculations
        gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);

        //enabling textures...
        gl.glEnable(GL2.GL_TEXTURE_2D);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
        //finished enabling textures

        // Enabling lighting
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glLightModeli(GL2.GL_LIGHT_MODEL_TWO_SIDE, 1);




        // Keyboard
        if (!isKeyboard) {
            isKeyboard = true;
            if (glDrawable instanceof Window) {
                Window window = (Window) glDrawable;
                window.addKeyListener(KeyboardManager.getSingleton());
            } else if (GLProfile.isAWTAvailable() && glDrawable instanceof Component) {
                Component component = (Component) glDrawable;
                new AWTKeyAdapter(KeyboardManager.getSingleton(), glDrawable).addTo(component);
            }
        }
//        for (ILevel l: levels) {
//            l.init(glDrawable);
//            l.attachObserver(this);
//        }
    }
























    @Override
    public void display(GLAutoDrawable gLDrawable) {
        try {
            if (musicTimer.isFinished()) {
                AudioPlayer.instance.play(backgroundMusic);
                musicTimer.start();
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        final GL2 gl = gLDrawable.getGL().getGL2();
        if (isGameFinished) {
            displayGameFinished(gLDrawable, this.finishedGameArgs);
        } else if (isLevelFinished) {
            passToTheNextLevel(gLDrawable);
        } else if(isHelpScreenOn) {
            levelToHelpScreen.get(currentLevelNumber).display(gl);
        } else if (isStoryScreenOn) {
            levelToStoryScreen.get(currentLevelNumber).display(gl);
        }
        else {
            currentLevel.display(gLDrawable);
        }
    }



    public void displayChanged(GLAutoDrawable gLDrawable,
                               boolean modeChanged, boolean deviceChanged) {
    }

    private void displayGameFinished(GLAutoDrawable glDrawable, EventArgs finishedGameArgs) {
        final GL2 gl =  glDrawable.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();

        TextRenderer renderer = new TextRenderer(new Font("Forte", Font.BOLD, 65));
        int width = GLSingletons.getGLCanvas().getWidth();
        int height = GLSingletons.getGLCanvas().getHeight();
        renderer.beginRendering(width, height);
        renderer.setColor(Color.BLUE);

        String message = "GAME OVER! YOU WON! :)!";
        renderer.draw(message, 200, height / 2);
        renderer.draw("Congratulations!",150, height/2 - 120);
        renderer.setColor(Color.WHITE);
        renderer.endRendering();
    }


    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {

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
        (new GLU()).gluPerspective(50.0f, h, 1.0, 1000.0);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }


    public static void exit(){
        // animator.stop();
     //   frame.dispose();
        System.exit(0);
    }


}


