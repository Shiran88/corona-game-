package game.level.gameObjects;
public class GameObjectUpdateInfo {
    private GameObjectType type;
    public GameObjectUpdateInfo(GameObjectType t) {
        type = t;
    }
    public GameObjectType getObjectType() {
        return this.type;
    }

}
