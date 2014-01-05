package com.gamesbykevin.casinogames.deck;

import com.gamesbykevin.casinogames.deck.Card.*;

public final class CustomDeck7 extends Deck
{
    //the original dimensions of the cards on the image
    private static final int SOURCE_WIDTH = 110;
    private static final int SOURCE_HEIGHT = 146;
    
    //each card has x pixels between it
    private static final int PIXEL_SPACING = 2;
    
    /**
     * Constructor for a new deck.
     * @param type Which deck will be used
     */
    public CustomDeck7()
    {
        super(SOURCE_WIDTH, SOURCE_HEIGHT);
        
        super.setPixelSpacing(PIXEL_SPACING);
        
        //map card coordinates
        map();
    }
    
    /**
     * Map out coordinates to cards here
     */
    @Override
    protected void map()
    {
        //add hidden animation here because all cards in the deck will have the same 
        super.addMapping(null, null, 7, 5);
        
        super.addMapping(Suit.Clubs, Value.Ace, 3, 4);
        super.addMapping(Suit.Clubs, Value.Two, 4, 4);
        super.addMapping(Suit.Clubs, Value.Three, 5, 4);
        super.addMapping(Suit.Clubs, Value.Four, 6, 4);
        super.addMapping(Suit.Clubs, Value.Five, 7, 4);
        super.addMapping(Suit.Clubs, Value.Six, 8, 4);
        super.addMapping(Suit.Clubs, Value.Seven, 0, 5);
        super.addMapping(Suit.Clubs, Value.Eight, 1, 5);
        super.addMapping(Suit.Clubs, Value.Nine, 2, 5);
        super.addMapping(Suit.Clubs, Value.Ten, 3, 5);
        super.addMapping(Suit.Clubs, Value.Jack, 4, 5);
        super.addMapping(Suit.Clubs, Value.Queen, 5, 5);
        super.addMapping(Suit.Clubs, Value.King, 6, 5);

        super.addMapping(Suit.Diamonds, Value.Ace, 4, 1);
        super.addMapping(Suit.Diamonds, Value.Two, 5, 1);
        super.addMapping(Suit.Diamonds, Value.Three, 6, 1);
        super.addMapping(Suit.Diamonds, Value.Four, 7, 1);
        super.addMapping(Suit.Diamonds, Value.Five, 8, 1);
        super.addMapping(Suit.Diamonds, Value.Six, 0, 2);
        super.addMapping(Suit.Diamonds, Value.Seven, 1, 2);
        super.addMapping(Suit.Diamonds, Value.Eight, 2, 2);
        super.addMapping(Suit.Diamonds, Value.Nine, 3, 2);
        super.addMapping(Suit.Diamonds, Value.Ten, 4, 2);
        super.addMapping(Suit.Diamonds, Value.Jack, 5, 2);
        super.addMapping(Suit.Diamonds, Value.Queen, 6, 2);
        super.addMapping(Suit.Diamonds, Value.King, 7, 2);

        super.addMapping(Suit.Hearts, Value.Ace, 0, 0);
        super.addMapping(Suit.Hearts, Value.Two, 1, 0);
        super.addMapping(Suit.Hearts, Value.Three, 2, 0);
        super.addMapping(Suit.Hearts, Value.Four, 3, 0);
        super.addMapping(Suit.Hearts, Value.Five, 4, 0);
        super.addMapping(Suit.Hearts, Value.Six, 5, 0);
        super.addMapping(Suit.Hearts, Value.Seven, 6, 0);
        super.addMapping(Suit.Hearts, Value.Eight, 7, 0);
        super.addMapping(Suit.Hearts, Value.Nine, 8, 0);
        super.addMapping(Suit.Hearts, Value.Ten, 0, 1);
        super.addMapping(Suit.Hearts, Value.Jack, 1, 1);
        super.addMapping(Suit.Hearts, Value.Queen, 2, 1);
        super.addMapping(Suit.Hearts, Value.King, 3, 1);

        super.addMapping(Suit.Spades, Value.Ace, 8, 2);
        super.addMapping(Suit.Spades, Value.Two, 0, 3);
        super.addMapping(Suit.Spades, Value.Three, 1, 3);
        super.addMapping(Suit.Spades, Value.Four, 2, 3);
        super.addMapping(Suit.Spades, Value.Five, 3, 3);
        super.addMapping(Suit.Spades, Value.Six, 4, 3);
        super.addMapping(Suit.Spades, Value.Seven, 5, 3);
        super.addMapping(Suit.Spades, Value.Eight, 6, 3);
        super.addMapping(Suit.Spades, Value.Nine, 7, 3);
        super.addMapping(Suit.Spades, Value.Ten, 8, 3);
        super.addMapping(Suit.Spades, Value.Jack, 0, 4);
        super.addMapping(Suit.Spades, Value.Queen, 1, 4);
        super.addMapping(Suit.Spades, Value.King, 2, 4);
        
        try
        {
            verifyMapping();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}