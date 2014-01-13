package com.gamesbykevin.casinogames.resources;

import static com.gamesbykevin.casinogames.resources.Resources.RESOURCE_DIR;
import com.gamesbykevin.framework.resources.*;

/**
 * All game images
 * @author GOD
 */
public class GameImage extends ImageManager
{
    //location of resources
    private static final String DIRECTORY = "images/game/{0}.png";
    
    //description for progress bar
    private static final String DESCRIPTION = "Loading Game Image Resources";
    
    public enum Keys
    {
        Deck1, Deck2, Deck3, Deck4, Deck5, 
        Deck6, Deck7, Deck8,
        
        Background1, Background2, Background3, 
        Background4, Background5, Background6, 
        
        CardPlaceHolder
    }
    
    public GameImage() throws Exception
    {
        super(RESOURCE_DIR + DIRECTORY, Keys.values());
        
        //the description that will be displayed for the progress bar
        super.setDescription(DESCRIPTION);
    }
}