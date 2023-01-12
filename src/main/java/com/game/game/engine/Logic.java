package com.game.game.engine;

import com.game.game.engine.graph.Render;
import com.game.game.engine.scene.Scene;

public interface Logic {

    void cleanup();

    void init(Window window, Scene scene, Render render);

    void input(Window window, Scene scene, long diffTimeMillis);

    void update(Window window, Scene scene, long diffTimeMillis);
}