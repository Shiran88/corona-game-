package game.level.factory;

import game.level.gameObjects.*;
import game.level.gameWorld.IGameWorld;
import game.level.gameWorld.TexturesManager;
import math.Vertex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameWorldFactory {
    TexturesManager texturesManager;
    public GameWorldFactory() {
    }
    public List<NotEnterableBuilding> createOldBuildings(String objPath, String texturePath,List<Float> surroundingPoints) {
        List<NotEnterableBuilding> buildings = new ArrayList<NotEnterableBuilding>();
        for(int i = 0; i < 2; i++) {
            NotEnterableBuilding b = new NotEnterableBuilding(new Vertex(30 + i* 40,1.7,75),
                    objPath, surroundingPoints);
            b.setRotationAngleY(90f);
            b.setScaleFactor(0.7f,0.7f,0.7f);
            buildings.add(b);
        }
        for(int i = 0; i < 2; i++) {
            NotEnterableBuilding b = new NotEnterableBuilding(new Vertex(30 + i* 40,1.7,15),
                    objPath, surroundingPoints);
            buildings.add(b);
            b.setRotationAngleY(90f);
            b.setScaleFactor(0.7f,0.7f,0.7f);
            buildings.add(b);
        }
        for(int i = 0; i < 2; i++) {
            NotEnterableBuilding b = new NotEnterableBuilding(new Vertex(-30 - i* 40,1.7,15),
                    objPath, surroundingPoints);
            buildings.add(b);
            b.setRotationAngleY(90f);
            b.setScaleFactor(0.7f,0.7f,0.7f);
        }

        for(int i = 0; i < 2; i++) {
            NotEnterableBuilding b = new NotEnterableBuilding(new Vertex(-30 - i* 40,1.7,-15),
                    objPath,surroundingPoints);
            buildings.add(b);
            b.setRotationAngleY(90f);
            b.setScaleFactor(0.7f,0.7f,0.7f);
        }

        for(int i = 0; i < 2; i++) {
            NotEnterableBuilding b = new NotEnterableBuilding(new Vertex(30 + i* 40,1.7,-15),
                    objPath,surroundingPoints);
            buildings.add(b);
            b.setRotationAngleY(90f);
            b.setScaleFactor(0.7f,0.7f,0.7f);
        }
        buildings.forEach(building->texturesManager.bind(texturePath,building));
        return buildings;

    }




    public List<Fence> createFences(String objPath, String texturePath) {
        Fence fenceSouth = new Fence(220,150,new Vertex(0,70,150),Fence.Position.EastWest);
        Fence fenceNorth = new Fence(220,150, new Vertex(0,70,-150),Fence.Position.EastWest);
        Fence fenceEast = new Fence(300,150, new Vertex(-110,70,0),Fence.Position.SouthNorth);
        Fence fenceWest = new Fence(300, 150, new Vertex(110,70,0), Fence.Position.SouthNorth);
        List<Fence> fences = new ArrayList<>(Arrays.asList(fenceSouth, fenceNorth, fenceEast, fenceWest));
        for (Fence f: fences) {
            texturesManager.bind(texturePath,f);
        }
        return fences;
    }
    public void setScalingFor(NotEnterableBuilding b ) {
        b.setScaleFactor(0.7f,0.7f,0.7f);
    }
    public void rotateToHorizontalPosition(NotEnterableBuilding b) {
        b.setRotationAngleY( 90f);
    }

    public List<Grass> createGrasses(String texturePath) {
        Grass grass0_L = new Grass(100,100,new Vertex(-60,0,-100));
        Grass grass0_R = new Grass(100,100,new Vertex(60,0,-100));
        Grass grass1_L = new Grass(100,60,new Vertex(-60,0,0));
        Grass grass1_R = new Grass(100,60,new Vertex(60,0,0));
        Grass grass2_L = new Grass(100,100,new Vertex(-60,0,100));
        Grass grass2_R = new Grass(100,100,new Vertex(60,0,100));
        Grass[] grasses = {grass0_L, grass0_R, grass1_L, grass1_R, grass2_L, grass2_R};
        for (Grass g: grasses) {
            texturesManager.bind(texturePath,g);
        }
        return Arrays.asList(grasses);
    }
    public List<Road> createHorizontalRoads(String texturePath) {
        Road[] roadsHor = new Road[15];
        for (int i = 0; i < roadsHor.length; ++i) {
            roadsHor[i] = new Road(20,20, new Vertex(0,0,-140 + i*20));
            texturesManager.bind(texturePath,roadsHor[i]);
        }
        return Arrays.asList(roadsHor);
    }

    public List<Road> createVerticalRoads(String texturePath) {
        Road[] roadsVer = new Road[20];
        for (int i=0, j=0; j < roadsVer.length; i++,j+=4) {
            roadsVer[j] = new Road(20,20, new Vertex(-100+i*20,0,-40));
            roadsVer[j+1] = new Road(20, 20, new Vertex(100-i*20,0,-40));
            texturesManager.bind("resources/road-ver.jpg",roadsVer[j]);
            texturesManager.bind("resources/road-ver.jpg",roadsVer[j+1]);

            roadsVer[j+2] = new Road(20,20, new Vertex(-100+i*20,0,40));
            roadsVer[j+3] = new Road(20, 20, new Vertex(100-i*20,0,40));
            texturesManager.bind("resources/road-ver.jpg",roadsVer[j+2]);
            texturesManager.bind("resources/road-ver.jpg",roadsVer[j+3]);
        }
        return Arrays.asList(roadsVer);
    }

    public List<Dollar> createCoins(String coinObjPath,String coinTexturePath) {
        List<Dollar> coins = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            //Coin c = new Coin(0,0,new Vertex(0, 3,100 + i*10),coinObjPath);
            Dollar c = new Dollar(new Vertex(70, 3,80 + 20*i),coinObjPath);
            coins.add(c);

        }
        for (int i = 0; i < 3; i++) {
            //Coin c = new Coin(0,0,new Vertex(0, 3,100 + i*10),coinObjPath);
            Dollar c = new Dollar(new Vertex(-70, 3,80 + 20*i),coinObjPath);
            coins.add(c);

        }
        coins.addAll(Arrays.asList(new Dollar(new Vertex(-65,3,-3),coinObjPath),
                                    new Dollar(new Vertex(-70,3,-3),coinObjPath),
                                    new Dollar(new Vertex(-35,3,-3),coinObjPath)));
        for (Dollar c:coins) {
            c.setScaleFactor(0.25f,0.25f,0.25f);
            c.setRotationAngle(30,30,30);
            texturesManager.bind(coinTexturePath,c);
        }
        return coins;
    }

    public void setTexturesManager(TexturesManager texturesManager) {
        this.texturesManager = texturesManager;
    }

    private TexturesManager getTexturesManager() {
        return texturesManager;
    }


    public IGameWorld createDefaultWorld(int worldNumber) {
        switch (worldNumber) {
            case 1:
            default:
                throw new IllegalArgumentException("no such world number");
        }
    }
}
