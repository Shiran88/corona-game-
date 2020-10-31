package game.level.gameWorld;

import javax.media.opengl.GLAutoDrawable;

public interface IDrawable {
    void init(GLAutoDrawable glDrawable, TexturesManager texturesMannager);
    void display(GLAutoDrawable glDrawable, TexturesManager texturesMannager);
}
