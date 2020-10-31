package game.level.factory;

import game.level.gameObjects.Dollar;
import game.level.gameObjects.Player;
import game.level.logic.GeneralGameLogic;
import game.level.logic.IGameLogic;

import java.util.Map;

public class GameLogicFactory {
    private Player player;
    private Map<Dollar, Integer> dollarMap;
    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setDollarMap(Map<Dollar, Integer> dollarMap) {
        this.dollarMap = dollarMap;
    }

    IGameLogic createDefaultLogic1() {
        return new GeneralGameLogic(player,dollarMap,null,null);
    }
    public IGameLogic createDefaultLogic(int logicNumber) {
        switch (logicNumber) {
            case 1:
                return createDefaultLogic1();
            default:
                throw new IllegalArgumentException("no such logic number");
        }
    }
}