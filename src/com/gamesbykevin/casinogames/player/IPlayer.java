package com.gamesbykevin.casinogames.player;

import com.gamesbykevin.framework.resources.Disposable;

import com.gamesbykevin.casinogames.engine.Engine;

import java.awt.Graphics;
import java.awt.Image;

public interface IPlayer extends Disposable
{
    public void render(final Graphics graphics, final Image image);
    
    public void update(final Engine engine);
}