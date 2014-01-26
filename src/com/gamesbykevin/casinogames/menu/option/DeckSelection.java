package com.gamesbykevin.casinogames.menu.option;

import com.gamesbykevin.framework.menu.Option;
import com.gamesbykevin.framework.resources.Audio;

import com.gamesbykevin.casinogames.resources.GameImage.Keys;

public class DeckSelection extends Option
{
    private static final String TITLE = "Deck Selection: ";
    
    public DeckSelection(final Audio audio)
    {
        super(TITLE);
        
        super.add(Keys.Deck1.toString(), audio);
        super.add(Keys.Deck2.toString(), audio);
        super.add(Keys.Deck3.toString(), audio);
        super.add(Keys.Deck4.toString(), audio);
        super.add(Keys.Deck5.toString(), audio);
        super.add(Keys.Deck6.toString(), audio);
        super.add("Random", audio);
        
        //default to random
        super.setIndex(6);
    }}
