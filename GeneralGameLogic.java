// Shiran Golbar, 313196974
// Lev Levin, 342480456
package game.level.logic;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.opengl.util.awt.TextRenderer;
import events.Event;
import events.IObserver;
import game.GLSingletons;
import game.KeyboardManager;
import game.level.gameObjects.*;
import game.level.gameWorld.GameWorld;
import game.level.gameWorld.IGameWorld;
import game.level.gameWorld.collisions.CollisionManager;
import game.level.gameWorld.collisions.ICollidable;
import math.Axis;
import math.Vertex;
import utils.Timer;

import java.awt.*;
import java.util.*;
import java.util.List;
/**
 * This class manages a logic of coronavirus game level.
 */
public class GeneralGameLogic implements IGameLogic, IObserver {
    private final float BIKE_SPEED = 0.6f; //player's speed using bike.
    private final float DEFAULT_SPEED = 0.3f; //player's regular speed.
    private Player player; // player instance.
    private Map<Dollar, Integer> dollarAmountMap = new HashMap<>(); //contains values for each dollar in the level.

    private int bikeCost;
    private int playerMoney;
    private int foodCounter;
    private int playerHealth;
    private int damageCost; // how many lives player losses in the case of collision with other man.
    private int foodNeeded; // how many food needed to be able to enter home.
    private int foodCost; // home much costs each food instance.

    private Dollar visibleDollar = null; // whether some dollar is visible to player right now.
    private Food visibleFood = null; // whether some food is visible to player right now.
    private Bike visibleBike = null;
    private boolean bikeBought = false; //whether the player has bought the bike.
    private IGameWorld gameWorld = null; // physical world where the game takes place.
    private Bike playersBike = null; //instance of bike in the case player is on it(otherwise, null).
    private Timer timer = new Timer(); // timer for the current level.
    private NotEnterableBuilding home; // reference to the home of the player.
    private boolean isHomeVisible = false;//is home visible to the player.
    private boolean isWinner = false;
    private List<Human>  humanList = new ArrayList<>();
    public GeneralGameLogic addHuman(Human h) {
        humanList.add(h);
        h.setMoveSpeedAxisX(0.15f);
        h.setMoveSpeedAxisZ(0.2f);
        return  this;
    }
    public GeneralGameLogic addHumans(Collection<Human> humans) {
        humanList.addAll(humans);
        for (Human h: humans) {
            h.setMoveSpeedAxisX(0.15f);
            h.setMoveSpeedAxisZ(0.2f);
        }
        return this;
    }

    public GeneralGameLogic setFoodNeeded(int f) {
        foodNeeded = f;
        return this;
    }
    public GeneralGameLogic setBikeCost(int cost) {
        bikeCost = cost;
        return  this;
    }
    public GeneralGameLogic setDamageCost(int cost) {
        damageCost = cost;
        return this;
    }

    /**
     * Set hom many time the player would have to finish the game(to buy food and return home).
     * @param hours
     * @param minutes
     * @param seconds
     * @return
     */
    public GeneralGameLogic setTime(int hours, int minutes, int seconds) {
        timer.setStart(hours,minutes,seconds);
        return this;
    }
    public GeneralGameLogic setFoodCost(int cost) {
        foodCost = cost;
        return this;
    }

    /**
     * Set dollars that are located in the physical game world and their values.
     * @param dollarAmountMap mapping from dollar to its value.
     * @return
     */
    public GeneralGameLogic DollarAmountMap(Map<Dollar,Integer> dollarAmountMap) {
        this.dollarAmountMap = dollarAmountMap;
        return this;
    }

    /**
     * Creates GeneralGameLogic instance. Settable parameters that are not passed in the constructor
     * will be set to default values.
     * @param player - game player.
     * @param dollarToAmount - mapping from dollars to its values.
     * @param world - physical world where the game takes place.
     * @param home - reference to the home where player need to return.
     */
    public GeneralGameLogic(Player player, Map<Dollar,Integer> dollarToAmount,
                            GameWorld world,NotEnterableBuilding home) {
        this.player = player;
        player.setSpeedMove(DEFAULT_SPEED);
        this.dollarAmountMap = dollarToAmount;
        // default configurations
        playerMoney = 0;
        foodCounter = 0;
        playerHealth = 100;
        foodNeeded = 3;
        this.gameWorld = world;
        KeyboardManager.getSingleton().attachObserver(this);
        timer.setStart(0,4,30);
        this.home = home;
    }
    @Override
    public void init() {
        this.foodCounter = 0;
        this.playerHealth = 100;
        timer.start();
    }
    public void setGameWorld(IGameWorld gameWorld) {
        this.gameWorld = gameWorld;
    }


    @Override
    public void update() {
        if (playerHealth <= 0) {
            isWinner = false;
        }
    }

    @Override
    public boolean isGameFinished() {
        try {
           return timer.isFinished() || isWinner || playerHealth <= 0 ;
        } catch (IllegalAccessException ignored) {
            return false;
        }
    }

    @Override
    public boolean isWinner(Player player) {
        return isWinner;
    }

    @Override
    public void getNotifiedAboutCollision(ICollidable c1, ICollidable c2) {
        if (c1 == player) {
            switch (c2.getCollidableInfo().getGameObjectType()) {
                case MONEY:
                    if (c2 instanceof Dollar && dollarAmountMap.containsKey(c2)) {
                        visibleDollar = (Dollar)c2;
                    }
                    break;
                case FOOD:
                    if (c2 instanceof Food ) {
                        visibleFood = (Food)c2;
                    }
                    break;
                case BIKE:
                    if (c2 instanceof Bike) {
                        visibleBike = (Bike)c2;
                    }
                    break;
                case NOT_ENTERABLE_BUILDING:
                    if(c2 instanceof NotEnterableBuilding && c2 == home) {
                        isHomeVisible = true;
                    }
            }
        }
        else if (c1.getCollidableInfo().getGameObjectType() == GameObjectType.HUMAN && c2 == player){
            playerHealth -= damageCost;
        }
    }

    /**
     * Change the logic state as response to pressing enter.
     */
    private void reactOnEnterPressedEvent() {
        //if the dollar is visible to player.
        if (visibleDollar != null && playersBike == null) {
            this.playerMoney += dollarAmountMap.get(visibleDollar);
            gameWorld.deleteFromWorld(visibleDollar);
            visibleDollar = null;
            //if the food is visible to the player and it has enough money to buy it.
        } else if (visibleFood != null && this.playerMoney >= foodCost && playersBike == null ) {
                this.foodCounter++;
                this.playerMoney -= foodCost;
                gameWorld.deleteFromWorld(visibleFood);
                visibleFood = null;
        // if the bike is visible to the player and it has enough money to buy it
        // or bike was bough already.
        } else if (visibleBike != null && (this.playerMoney >=bikeCost || bikeBought)) {
            StuckToCameraObject frontalObject = new StuckToCameraObject(new Vertex(0,-2,-1.5),
                    visibleBike.getDisplayListID());
            frontalObject.setRotationAngle(-80f,0f,90f);
            frontalObject.setScaleFactor(0.05f,0.05f,0.05f);
            frontalObject.setTexture(visibleBike.getTexture());
            player.setFrontalObject(frontalObject);
            player.setSpeedMove(BIKE_SPEED);
           playersBike = visibleBike;
           this.gameWorld.deleteFromWorld(visibleBike);
            visibleBike = null;
            // if bike was not bought before, player pays bike price.
            if (!bikeBought) {
                this.playerMoney -= bikeCost;
                bikeBought = true;
            }
            //If the player reached home with food within allowed time - it wins.
        } else if (isHomeVisible && this.foodCounter >= foodNeeded && playersBike == null) {
            isWinner = true;
        }
    }

    /**
     * Change the logic state as response to pressing T.
     */
    private void reactOnTpressedEvent() {
        if (playersBike != null) {
            player.setFrontalObject(null);
            try {
                float radius = (float) playersBike.getCollidableInfo().getRadius();
          //      Vertex pos = player.getCoordsSystem().getOrigin().subtract(player.getCoordsSystem().getLookAt());
                Vertex pos = player.getCoordsSystem().getLookAt();
                playersBike.setPosition(new Vertex(pos.getX(),playersBike.getCoordsSystem().getOrigin().getY(),pos.getZ() - radius));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                System.out.println("CANNOT PUT BIKE ON RADIUS DISTANCE BECAUSE IT HAS NO RADIUS." +
                        " PUTTING ON DEFAULT DISTANCE.");
                playersBike.setPosition(player.getCoordsSystem().getLookAt().subtract(player.getCoordsSystem().getOrigin()));
            }
            gameWorld.addToWorld(playersBike);
            player.setSpeedMove(DEFAULT_SPEED);
            playersBike = null;
        }
    }

    /**
     * React on player's attempt to move.
     * @param wasMoved wasActually player moved in his attempt or because some physical world reason(collison)
     *                 he didn't move.
     */
    private void reactToPlayersTryToMove(boolean wasMoved) {
        if (!wasMoved) {
            return;
        }
        if (visibleDollar != null) {
            visibleDollar = null;
        }
        if (visibleFood != null) {
            visibleFood = null;
        }
        if (visibleBike != null) {
            visibleBike = null;
        }
        if (isHomeVisible) {
            isHomeVisible = false;
        }
    }

    /**
     * Change logic state according to keyboard event
     * @param keyE
     */
    private void reactOnKeyboardEvent(KeyEvent keyE) {

        switch (keyE.getKeyCode()) {
            case KeyEvent.VK_ENTER:
                reactOnEnterPressedEvent();
                break;
            case KeyEvent.VK_T:
                reactOnTpressedEvent();
                break;
            case KeyEvent.VK_UP:
                player.tiltUp();
                break;

            case KeyEvent.VK_DOWN:
                player.tiltDown();
                break;

            case KeyEvent.VK_LEFT:
                player.tiltLeft();
                break;

            case KeyEvent.VK_RIGHT:
                player.tiltRight();
                break;

            case KeyEvent.VK_W:
                this.reactToPlayersTryToMove(player.moveForward());
                break;

            case KeyEvent.VK_S:
                this.reactToPlayersTryToMove(player.moveBackward());
                break;

            case KeyEvent.VK_A:
                this.reactToPlayersTryToMove(player.moveLeft());
                break;

            case KeyEvent.VK_D:
                this.reactToPlayersTryToMove(player.moveRight());
                break;
        }


    }

    /**
     * Draw message.
     * @param message message to draw.
     * @param col color.
     */
    private void drawVisibleMessage(String message,Color col) {

        int width = GLSingletons.getGLCanvas().getWidth();
        int height = GLSingletons.getGLCanvas().getHeight();


        TextRenderer renderer = new TextRenderer(new Font("Forte", Font.PLAIN, 35));

        renderer.beginRendering(width, height);
        renderer.setColor(col);


        // Show the message (if exists)
        renderer.draw(message, width / 2 - width / 9, 29);


        renderer.setColor(Color.WHITE);
        renderer.endRendering();
    }

    /**
     * Draw message for the case player near his home.
     */
    private void drawNearHomeMessage() {
        if (foodCounter < foodNeeded) {
            drawVisibleMessage("Sorry, you  cannot enter home without food...", Color.RED);
        } else {
            drawVisibleMessage("Press ENTER to enter home", Color.blue);
        }
    }

    /**
     * Draw status bar for a current logic state.
     */
    public void drawLogicInfo() {
        if (playersBike == null) {
            if (visibleDollar != null) {
                drawVisibleMessage("Press ENTER to pop money", Color.cyan);
            } else if (visibleFood != null) {
                drawVisibleMessage("Press ENTER to pop food", Color.cyan);
            } else if (visibleBike != null) {
                drawVisibleMessage("Press ENTER to get on the bike", Color.cyan);
            } else if (isHomeVisible) {
                drawNearHomeMessage();
            }
        } else {
            if (visibleDollar != null) {
                drawVisibleMessage("To pop money, get off the bike", Color.red);
            } else if (visibleFood != null) {
                drawVisibleMessage("To pop food, get off the bike", Color.red);
            } else if (isHomeVisible) {
                drawVisibleMessage("To enter home, get off the bike", Color.red);
            }
        }

        TextRenderer renderer = new TextRenderer(new Font("Forte", Font.PLAIN, 32));
        int width = GLSingletons.getGLCanvas().getWidth();
        int height = GLSingletons.getGLCanvas().getHeight();
        renderer.beginRendering(width, height);
        renderer.setColor(Color.BLUE);

        String message = "Health: " + playerHealth;
        renderer.draw(message, 10, height - 29);

        message = "Money $: " + playerMoney;
        renderer.draw(message, 250, height - 29);
        message = "Food: " + foodCounter +"/" +foodNeeded;
        renderer.draw(message, 470, height - 29);
        message = "Time Left: " + timer.getCurrentTime().toString();

//        renderer.draw("Pos:" + player.getCoordsSystem().getOrigin().getX() + player.getCoordsSystem().getOrigin().getZ(),50,50);

        renderer.draw(message, 700, height - 29);
        if (playersBike != null) {
            renderer.setColor(Color.MAGENTA);
            message = "ON BIKE: SPEED 2X";
            renderer.draw(message,width - 400, height - 29);
            renderer.draw("press  T  to get off the bike", width - 400, height - 68);
        }
        renderer.setColor(Color.WHITE);
        renderer.endRendering();

    }


    @Override
    public void getNotified(Event e) {
        switch (e.getType()) {
            case KeyPressed:
                reactOnKeyboardEvent(KeyboardManager.getSingleton().getLastKeyEvent());
                break;

        }
    }
}
