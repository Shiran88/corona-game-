package game.level.factory;

import game.level.ILevel;
import game.level.Level;
import game.level.gameObjects.*;
import game.level.gameWorld.GameWorld;
import game.level.gameWorld.Light;
import game.level.gameWorld.TexturesManager;
import game.level.gameWorld.collisions.CollisionManager;
import game.level.logic.GeneralGameLogic;
import math.Vertex;

import javax.media.opengl.GL2;
import java.util.*;
import java.util.List;

/**
 * Responsible for generating Levels.
 */
public class LevelFactory {
    private GameLogicFactory logicFactory;
    private GameWorldFactory worldFactory;
    private TexturesManager texturesManager;

    private final List<Float> buildingSurroundingPointsHor = Arrays.asList(-13f,18f,-13f,2f);
    private final List<Float> buildingSurroundingPointsVer = Arrays.asList( -2f,13.5f,-14f,18f);
    final String grassTexturePath = "resources/grass3.jpg";
    final String roadHorTexturePath = "resources/road-hor.jpg";
    final String roadVerTexturePath = "resources/road-ver.jpg";
    final String skyTexturePath = "resources/sky.jpg";
    final String bigOldHouseObjPath = "resources/house/house.obj";
    final String bigOldHouseTexturePath = "resources/house/house.png";

    final String fenceObjPath= "resources/fence.obj";
    final String fenceTexturePath = "resources/fence.jpg";

    final String coinObjPath = "resources/dollar/dollar.obj";
    final String coinTexturePath = "resources/dollar/dollar.jpg";

    final String bikeObjPath = "resources/bike/11717_bicycle_v2_L1.obj";
    final String bikeTexturePath = "resources/bike/bicycle_bitmap_v2.jpg";

    final String appleObjectPath = "resources/apple/apple.obj";
    final String appleTexturePath = "resources/apple/apple.png";

    final String humanObjectPath = "resources/human/human.obj";
    final String humanTexturePath = "resources/human/human.jpg";

    final String myHouseTexture = "resources/myhouse/myhouse.png";
    public LevelFactory() {
        logicFactory = new GameLogicFactory();
        worldFactory = new GameWorldFactory();
        texturesManager = new TexturesManager();
        worldFactory.texturesManager = texturesManager;
    }
    /**
     * Creates level.
     * @param levelNumber a number of level to create.
     * @return created level.
     */
    public ILevel createLevel(int levelNumber) {
        switch (levelNumber) {
            case 1:
                return createFirstLevel();
            case 2:
                return createSecondLevel();
            default:
                throw new IllegalArgumentException("no such level number");
        }
    }

    private Collection<Light> createLightsFirstLevel() {
        Collection<Light> lights = new ArrayList<>();
        Light l1Amb = new Light(GL2.GL_LIGHT0, new Vertex(0,150,0),
                new float[]{0.65f,0.65f,0.65f,1f},
                new float[]{0f,0f,0f,1f});
        Light l1Dif = new Light(GL2.GL_LIGHT0 + 1,new Vertex(30,6,120),
                new float[]{0f,0f,0f,1f},
                new float[]{0.75f,0.5f,0.3f,1f});

//        Light l2Dif = new Light(GL2.GL_LIGHT0 + 2,new float[]{30f,5f,120},
//                new float[]{0f,0f,0f,1f},
//                new float[]{0.7f,0.7f,0.7f,1f});
//        lights.add(l1Dif);
//        lights.add(l2Dif);
        lights.add(l1Amb);
        lights.add(l1Dif);
        return lights;
    }

    private ILevel createFirstLevel() {

        //--------------------------------------------------------------------------
        texturesManager.loadTextures(Arrays.asList(grassTexturePath,
                roadHorTexturePath, roadVerTexturePath,skyTexturePath,
                fenceTexturePath,bigOldHouseTexturePath,coinTexturePath,
                bikeTexturePath, appleTexturePath, humanTexturePath, myHouseTexture));

//        List<Grass> grasses = worldFactory.createGrasses(grassTexturePath);
        List<Grass> grasses = createHighQualityGrass(220,300,new Vertex(0,0,0),20,20);
        List<Road> roadsHor = worldFactory.createHorizontalRoads(roadHorTexturePath);
        List<Road> roadsVer = worldFactory.createVerticalRoads(roadVerTexturePath);

        Sky sky = new Sky(220 + 250,300 + 250,new Vertex(0,200,0));
        texturesManager.bind(skyTexturePath,sky);


        Fence fenceEast = new Fence(300,150, new Vertex(-110,70,0),Fence.Position.SouthNorth);
        Fence fenceWest = new Fence(300, 150, new Vertex(110,70,0), Fence.Position.SouthNorth);
        List<Fence> fences = new ArrayList<>(Arrays.asList());
        fences.addAll(createHighQualityFence(220,150,new Vertex(0,70,150),Fence.Position.EastWest,20,20));
        fences.addAll(createHighQualityFence(220,150,new Vertex(0,70,-150),Fence.Position.EastWest,20,20));
        fences.addAll(createHighQualityFence(300,150, new Vertex(-110,70,0),Fence.Position.SouthNorth,20,20));
        fences.addAll(createHighQualityFence(300, 150, new Vertex(110,70,0), Fence.Position.SouthNorth,20,20));
        //wall
        fences.addAll(createHighQualityFence(55,40,new Vertex(-17,20,123.5),Fence.Position.SouthNorth,10,10));
        fences.addAll(createHighQualityFence(55,40,new Vertex(-40,20,123.5),Fence.Position.SouthNorth,10,10));
        fences.addAll(createHighQualityFence(23,40,new Vertex(-28.5,20,96),Fence.Position.EastWest,10,10));
        //fences.addAll(createHighQualityFence())
        List<NotEnterableBuilding> buildings= worldFactory.createOldBuildings(bigOldHouseObjPath,
                bigOldHouseTexturePath,buildingSurroundingPointsHor);


        List<Dollar> coins = worldFactory.createCoins(coinObjPath,coinTexturePath);
        Bike bike = new Bike(new Vertex(-3,0.1,130),bikeObjPath);
        //bike.setRotationAngle(270f,0f,270f);
        bike.setScaleFactor(0.1f,0.1f,0.1f);

        texturesManager.bind(bikeTexturePath, bike);
        List<Food> food = Arrays.asList(
                    new Food(new Vertex(-80,3,-130),appleObjectPath),
                    new Food(new Vertex(-90,3,-130),appleObjectPath),
                    new Food(new Vertex(-100,3,-130),appleObjectPath),
                    new Food(new Vertex(80,3,-100),appleObjectPath),
                    new Food(new Vertex(90,3,-100),appleObjectPath),
                    new Food(new Vertex(100,3,-100),appleObjectPath));
        for (Food f:food) {
            texturesManager.bind(appleTexturePath,f);
        }
        NotEnterableBuilding home = new NotEnterableBuilding(new Vertex(30,1.7,120), bigOldHouseObjPath,
                buildingSurroundingPointsHor);
        worldFactory.setScalingFor(home);
        worldFactory.rotateToHorizontalPosition(home);
        texturesManager.bind(bigOldHouseTexturePath, home);

        PointingArrow triangle = new PointingArrow(new Vertex(30,45,115));
        texturesManager.bind(myHouseTexture, triangle);

        Player player = new Player(new Vertex(5,4,145));
//        Player player = new Player(new Vertex(0f,70f,0f));
        List<Human> humans = Arrays.asList(
                new Human(new Vertex(0,0,0),humanObjectPath),
                new Human(new Vertex(5,0,-30),humanObjectPath),
                new Human(new Vertex(0,0,-70),humanObjectPath),
                new Human(new Vertex(-10,0,-100),humanObjectPath),
                new Human(new Vertex(-5,0,30),humanObjectPath),
                new Human(new Vertex(5,0,50),humanObjectPath));
        for (Human h: humans) {
            h.setScaleFactor(0.025f,0.025f,0.025f);
            texturesManager.bind(humanTexturePath,h);
        }

        List<GameObject> gameObjects = new ArrayList<>();
        gameObjects.addAll(grasses);
        gameObjects.addAll(roadsHor);
        gameObjects.addAll(roadsVer);
        gameObjects.add(sky);
        gameObjects.addAll(fences);
        gameObjects.add(bike);
        gameObjects.addAll(buildings);
        gameObjects.addAll(coins);
        gameObjects.addAll(food);
        gameObjects.add(player);
        gameObjects.add(home);
        gameObjects.add(triangle);
        gameObjects.addAll(humans);
        CollisionManager collisionManager = new CollisionManager();
        GameWorld gameWorld = new GameWorld(gameObjects, player, texturesManager,collisionManager).addLights(createLightsFirstLevel());
        Map<Dollar, Integer> dollarToAmount = new HashMap<>();
        for (Dollar d: coins) {
            dollarToAmount.put(d,50);
        }
        GeneralGameLogic logic = new GeneralGameLogic(player,dollarToAmount,gameWorld,home);
        logic.addHumans(humans);
        logic.setTime(0,5,0).setBikeCost(150).setFoodCost(50).setFoodNeeded(5).setDamageCost(10);
        for (GameObject g : gameObjects) {
            g.setLogic(logic);
        }
        return new Level(logic,gameWorld);
    }




    private ILevel createSecondLevel() {

        worldFactory.texturesManager = texturesManager;
        final float WEST_EAST_LEN_GRASS = 55;
        final float SOUTH_NORTH_LEN_GRASS = 50;

        //--------------------------------------------------------------------------
        texturesManager.loadTextures(Arrays.asList(grassTexturePath,
                roadHorTexturePath, roadVerTexturePath, skyTexturePath,
                fenceTexturePath, bigOldHouseTexturePath, coinTexturePath,
                bikeTexturePath, appleTexturePath, humanTexturePath, myHouseTexture));


        List<Grass> grasses = new ArrayList<>();
        //grasses first room
        grasses.addAll(createHighQualityGrass(220,300,new Vertex(0,0,-175),20,20));
        //grasses coridor
        grasses.addAll(createHighQualityGrass(80,50,new Vertex(0,0,0),20,20));
        //grasses second room
        grasses.addAll(createHighQualityGrass(220,150,new Vertex(0,0,100),20,20));

        Sky sky = new Sky(220,500,new Vertex(0,100,-75));
        texturesManager.bind(skyTexturePath,sky);

        List<Fence> fences = new ArrayList<>();

        //North wall.
        fences.addAll(createHighQualityFence(220,100,new Vertex(0,50,-325), Fence.Position.EastWest,20f,20f));

        //fences middle
        fences.addAll(createHighQualityFence(70,100,new Vertex(-75,50,-25),Fence.Position.EastWest,20f,20f));
        fences.addAll(createHighQualityFence(70,100,new Vertex(75,50,-25),Fence.Position.EastWest,20f,20f));
        fences.addAll(createHighQualityFence(70,100,new Vertex(-75,50,25),Fence.Position.EastWest,20f,20f));
        fences.addAll(createHighQualityFence(70,100,new Vertex(75,50,25),Fence.Position.EastWest,20f,20f));
        fences.addAll(createHighQualityFence(50,100,new Vertex(-40,50,0),Fence.Position.SouthNorth,20f,20f));
        fences.addAll(createHighQualityFence(50,100,new Vertex(40,50,0),Fence.Position.SouthNorth,20f,20f));

        //fences south
        fences.addAll(createHighQualityFence(220,100, new Vertex(0,50,175), Fence.Position.EastWest,20f,20f));

        //side fences East
        fences.addAll(createHighQualityFence(300,100,new Vertex(-110,50,-175), Fence.Position.SouthNorth,20f,20f));
        fences.addAll(createHighQualityFence(150,100,new Vertex(-110,50,100), Fence.Position.SouthNorth,20f,20f));

        //side fences West
        fences.addAll(createHighQualityFence(300,100,new Vertex(110,50,-175), Fence.Position.SouthNorth,20f,20f));
        fences.addAll(createHighQualityFence(150,100,new Vertex(110,50,100), Fence.Position.SouthNorth,20f,20f));






            List<Road> roads = new ArrayList<>();

            for (int i = 0; i < 24; i++) {
                Road rHor = new Road(20, 20, new Vertex(0, 0.01, 165 - i * 20));
                texturesManager.bind(roadHorTexturePath, rHor);
                roads.add(rHor);
            }
            for (int i = 0; i < 4; i++) {
                Road rVer = new Road(20, 20, new Vertex(0 - i * 20, 0.01, -295));
                texturesManager.bind(roadVerTexturePath, rVer);
                roads.add(rVer);
            }
            //East North buildings
            List<NotEnterableBuilding> buildings = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                NotEnterableBuilding b = createNotEnterableBuilding(-75, -70 - i * 50, buildingSurroundingPointsVer);
                worldFactory.setScalingFor(b);
                buildings.add(b);
            }
            // East South buildings
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    NotEnterableBuilding b = new NotEnterableBuilding(new Vertex(-80 + i * 50, 1.7, 50 + j * 40),
                            bigOldHouseObjPath, buildingSurroundingPointsHor);
                    texturesManager.bind(bigOldHouseTexturePath, b);
                    worldFactory.setScalingFor(b);
                    worldFactory.rotateToHorizontalPosition(b);
                    buildings.add(b);
                }
            }

            // West North buildings
            NotEnterableBuilding b1 = createNotEnterableBuilding(40, -275, buildingSurroundingPointsVer);
            worldFactory.setScalingFor(b1);
            NotEnterableBuilding b2 = createNotEnterableBuilding(80, -275, buildingSurroundingPointsVer);
            worldFactory.setScalingFor(b2);
            NotEnterableBuilding b3 = createNotEnterableBuilding(60, -235, buildingSurroundingPointsHor);
            worldFactory.setScalingFor(b3);
            worldFactory.rotateToHorizontalPosition(b3);
            // West North buildings
            NotEnterableBuilding b4 = createNotEnterableBuilding(40, -195, buildingSurroundingPointsVer);
            worldFactory.setScalingFor(b4);
            NotEnterableBuilding b5 = createNotEnterableBuilding(80, -195, buildingSurroundingPointsVer);
            worldFactory.setScalingFor(b5);
            buildings.addAll(Arrays.asList(b1, b2, b3, b4, b5));

            NotEnterableBuilding home = createNotEnterableBuilding(60, 100, buildingSurroundingPointsHor);
            worldFactory.setScalingFor(home);
            worldFactory.rotateToHorizontalPosition(home);
            buildings.add(home);

        Bike bike = new Bike(new Vertex(-0,0.1,-310),bikeObjPath);
        //bike.setRotationAngle(270f,0f,270f);
        bike.setScaleFactor(0.1f,0.1f,0.1f);

        texturesManager.bind(bikeTexturePath, bike);

        List<Dollar> dollars = Arrays.asList(
                new Dollar(new Vertex(-77,3,-190),coinObjPath),
                new Dollar(new Vertex(-68,3,-190),coinObjPath),
                new Dollar(new Vertex(-65,3,-88),coinObjPath),
                new Dollar(new Vertex(-75,3,-88),coinObjPath),
                new Dollar(new Vertex(96,3,41),coinObjPath),
                new Dollar(new Vertex(82,3,41),coinObjPath),
                new Dollar(new Vertex(64,3,41),coinObjPath),
                new Dollar(new Vertex(89,3,-314),coinObjPath),
                new Dollar(new Vertex(64,3,-316),coinObjPath),
                new Dollar(new Vertex(58,3,-316),coinObjPath)
        );
        for (Dollar d: dollars) {
            texturesManager.bind(coinTexturePath,d);
            d.setScaleFactor(0.25f,0.25f,0.25f);
            d.setRotationAngle(30,30,30);
        }

        List<Food> foods  = Arrays.asList(
                new Food(new Vertex(86,3,-240),appleObjectPath),
                new Food(new Vertex(86,3,-226),appleObjectPath),
                new Food(new Vertex(68,3,-224),appleObjectPath),
                new Food(new Vertex(98,3,-63),appleObjectPath),
                new Food(new Vertex(87,3,-50),appleObjectPath),
                new Food(new Vertex(87,3,-60),appleObjectPath),
                new Food(new Vertex(-94,3,-34),appleObjectPath),
                new Food(new Vertex(-54,3,69),appleObjectPath),
                new Food(new Vertex(-70,3,69),appleObjectPath)
        );
        foods.forEach(f->texturesManager.bind(appleTexturePath,f));

        List<Human> humans = new ArrayList<>(Arrays.asList(
                new Human(new Vertex(40, 0, 30), humanObjectPath),
                new Human(new Vertex(5,0,-30),humanObjectPath),
                new Human(new Vertex(-5,0,-75),humanObjectPath),
                new Human(new Vertex(30,0,-150),humanObjectPath),
                new Human(new Vertex(-40,0,60),humanObjectPath),
                new Human(new Vertex(0,0,-200),humanObjectPath),
                new Human(new Vertex(10,0,250),humanObjectPath),
                new Human(new Vertex(-10,0,230),humanObjectPath),
                new Human(new Vertex(5,0,0),humanObjectPath),
                new Human(new Vertex(-5,0,-10),humanObjectPath)
                ));
        for (Human h: humans) {
            texturesManager.bind(humanTexturePath,h);
            h.setScaleFactor(0.025f,0.025f,0.025f);


        }




//        logic.addHumans(humans);
//        for (GameObject g : gameObjects) {
//            g.setLogic(logic);
//        }
//        return new Level(logic,gameWorld);

            List<GameObject> gameObjects = new ArrayList<>();
            gameObjects.addAll(grasses);
            gameObjects.add(sky);
            gameObjects.addAll(roads);
            gameObjects.addAll(buildings);
            gameObjects.addAll(fences);
            gameObjects.add(bike);
            gameObjects.addAll(foods);
            gameObjects.addAll(dollars);
            gameObjects.addAll(humans);
            CollisionManager collisionManager = new CollisionManager();

            Player player = new Player(new Vertex(-75, 4, -275));
            gameObjects.add(player);
            Light l1Amb = new Light(GL2.GL_LIGHT0, new Vertex(0, 150, 0),
                    new float[]{0.7f, 0.7f, 0.8f, 1f},
                    new float[]{0f, 0f, 0f, 1f});


            Light l1DifRoom1 = new Light(GL2.GL_LIGHT0 + 1, new Vertex(78,40,30),
                    new float[]{0f,0f,0f,1f}, new float[]{0.3f,0.5f,0.8f,1f});

            Light l1DifRoom2 = new Light(GL2.GL_LIGHT0 + 2, new Vertex(-75,45,-60),
                    new float[] {0f,0f,0f}, new float[]{0.1f,0.6f,0.4f});
            PointingArrow sign = new PointingArrow(new Vertex(60,40,95));
            sign.setScaleFactor(0.6f,0.6f,0.6f);
            texturesManager.bind(myHouseTexture,sign);
            gameObjects.add(sign);
            GameWorld world = new GameWorld(gameObjects, player, texturesManager, collisionManager);
            world.addLight(l1Amb);
            world.addLight(l1DifRoom1);
            world.addLight(l1DifRoom2);

            Map<Dollar,Integer> dollarToValue = new HashMap<>();
            for(Dollar d: dollars) {
                dollarToValue.put(d,50);
            }
             GeneralGameLogic logic = new GeneralGameLogic(player, dollarToValue, world, home).addHumans(humans);
            logic.setTime(0,7,30).setBikeCost(150).setFoodCost(50).setFoodNeeded(7).setDamageCost(20);
            for (GameObject g : gameObjects) {
                g.setLogic(logic);

            }
            return new Level(logic, world);
        }



    NotEnterableBuilding createNotEnterableBuilding(float x, float z, List<Float> surroundingPoints) {
        NotEnterableBuilding b = new NotEnterableBuilding(new Vertex(x,1.7, z),
                bigOldHouseObjPath,surroundingPoints);
        texturesManager.bind(bigOldHouseTexturePath,b);
        return b;
    }



    public int getNumOfLevels() {
        return 2;
    }
    private List<Fence> createHighQualityFence(float width, float height, Vertex origin, Fence.Position pos,float unitWidth, float unitHeight) {
        List<Fence> fences = new ArrayList<>();

        for (int w = 0; w < (int)(width/unitWidth); w++ ) {
//            float floatingPart = (height / unitHeight) - (float)(int)(height/unitHeight) ;
            float floatingReminderH = height % unitHeight;
            if(floatingReminderH !=0) {
                Vertex unitOr = null;
                switch (pos) {
                    case EastWest:
                        unitOr = new Vertex(origin.getX() - (width/2f) + (unitWidth/2f) + w*unitWidth,
                                origin.getY() + (height/2f) - (floatingReminderH/2f),
                                origin.getZ());
                        break;
                    case SouthNorth:
                        unitOr = new Vertex(origin.getX(),
                                origin.getY() + (height/2f) - (floatingReminderH/2f),
                                origin.getZ() - (width/2f) + (unitWidth/2f) + w*unitWidth);
                        break;

                }
                Fence f = new Fence(unitWidth, floatingReminderH,unitOr,pos );
                texturesManager.bind(fenceTexturePath,f);
                fences.add(f);
            }

            for (int h = 0; h < (int)(height/unitHeight); h++) {
                Vertex unitOr = null;
                switch (pos) {
                    case EastWest:
                        unitOr = new Vertex(origin.getX() - (width/2f) + (unitWidth/2f) + w*unitWidth,
                                origin.getY() - (height/2f) + (unitHeight/2f) + h*unitHeight,
                                origin.getZ());
                        break;
                    case SouthNorth:
                        unitOr = new Vertex(origin.getX(),
                                origin.getY() - (height/2f) + (unitHeight/2f) + h*unitHeight,
                                origin.getZ() - (width/2f) + (unitWidth/2f) + w*unitWidth);
                        break;
                }
                Fence f = new Fence(unitWidth, unitHeight,unitOr,pos );
                texturesManager.bind(fenceTexturePath,f);
                fences.add(f);
            }
        }


        float floatingReminderW = width % unitWidth;
        if(floatingReminderW !=0) {
            for (int h = 0; h < (int)(height/unitHeight); h++) {
                Vertex unitOr = null;
                switch (pos) {
                    case EastWest:
                        unitOr = new Vertex(origin.getX() + (width/2f) - (floatingReminderW/2f),
                                origin.getY() - (height/2f) + (unitHeight/2f) + h*unitHeight,
                                origin.getZ());
                        break;
                    case SouthNorth:
                        unitOr = new Vertex(origin.getX(),
                                origin.getY() - (height/2f) + (unitHeight/2f) + h*unitHeight,
                                origin.getZ() + (width/2f) - (floatingReminderW/2f));
                        break;
                }
                Fence f = new Fence(floatingReminderW, unitHeight,unitOr,pos );
                texturesManager.bind(fenceTexturePath,f);
                fences.add(f);
            }

        }
        float floatingReminderH = height % unitHeight;
        if (floatingReminderH!=0 && floatingReminderW !=0) {
            Vertex unitOr = null;
            switch (pos) {
                case EastWest:
                    unitOr = new Vertex(origin.getX() + (width/2f) - (floatingReminderW/2f),
                            origin.getY() + (height/2f) - (floatingReminderH/2f),
                            origin.getZ());
                    break;
                case SouthNorth:
                    unitOr = new Vertex(origin.getX(),
                            origin.getY() + (height/2f) - (floatingReminderH/2f),
                            origin.getZ() + (width/2f) - (floatingReminderW/2f));
                    break;
            }
            Fence f = new Fence(floatingReminderW, floatingReminderH,unitOr,pos );
            texturesManager.bind(fenceTexturePath,f);
            fences.add(f);
        }
        return fences;
    }

    private List<Grass> createHighQualityGrass(float eastWestLen, float southNorthLen, Vertex origin,float unitEastWestLen, float unitSouthNorthLen) {
        List<Grass> grasses = new ArrayList<>();

        for (int w = 0; w < (int)(eastWestLen/unitEastWestLen); w++ ) {
//            float floatingPart = (height / unitHeight) - (float)(int)(height/unitHeight) ;
            float floatingReminderH = southNorthLen % unitSouthNorthLen;
            if(floatingReminderH !=0) {
                Vertex unitOr = null;

                unitOr = new Vertex(origin.getX() - (eastWestLen/2f) + (unitEastWestLen/2f) + w*unitEastWestLen,
                                origin.getY(),
                        origin.getZ() + (southNorthLen/2f) - (floatingReminderH/2f));


                Grass g = new Grass(unitEastWestLen, floatingReminderH,unitOr);
                texturesManager.bind(grassTexturePath,g);
                grasses.add(g);
            }

            for (int h = 0; h < (int)(southNorthLen/unitSouthNorthLen); h++) {
                Vertex unitOr = null;

                unitOr = new Vertex(origin.getX() - (eastWestLen/2f) + (unitEastWestLen/2f) + w*unitEastWestLen,
                                origin.getY(),
                        origin.getZ() - (southNorthLen/2f) + (unitSouthNorthLen/2f) + h*unitSouthNorthLen);


                Grass g = new Grass(unitEastWestLen, unitSouthNorthLen,unitOr );
                texturesManager.bind(grassTexturePath,g);
                grasses.add(g);
            }
        }


        float floatingReminderW = eastWestLen % unitEastWestLen;
        if(floatingReminderW !=0) {
            for (int h = 0; h < (int)(southNorthLen/unitSouthNorthLen); h++) {
                Vertex unitOr = null;

                unitOr = new Vertex(origin.getX() + (eastWestLen/2f) - (floatingReminderW/2f),
                        origin.getY(),
                        origin.getZ() - (southNorthLen/2f) + (unitSouthNorthLen/2f) + h*unitSouthNorthLen);
                Grass g = new Grass(floatingReminderW, unitSouthNorthLen, unitOr);
                texturesManager.bind(grassTexturePath,g);
                grasses.add(g);
            }

        }
        float floatingReminderH = southNorthLen % unitSouthNorthLen;
        if (floatingReminderH!=0 && floatingReminderW !=0) {
            Vertex unitOr = null;
            unitOr = new Vertex(origin.getX() + (eastWestLen/2f) - (floatingReminderW/2f),
                            origin.getY(),
                    origin.getZ() + (southNorthLen/2f) - (floatingReminderH/2f));
            Grass g = new Grass(floatingReminderW, floatingReminderH,unitOr);
            texturesManager.bind(grassTexturePath,g);
            grasses.add(g);
        }
        return grasses;

    }
}


