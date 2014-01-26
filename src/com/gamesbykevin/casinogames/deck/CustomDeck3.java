package com.gamesbykevin.casinogames.deck;

import com.gamesbykevin.casinogames.deck.Card.*;

public final class CustomDeck3 extends Deck
{
    //the original dimensions of the cards on the image
    private static final int SOURCE_WIDTH = 67;
    private static final int SOURCE_HEIGHT = 95;
    
    //each card has x pixels between it
    private static final int PIXEL_SPACING = 1;
    
    /**
     * Constructor for a new deck.
     * @param type Which deck will be used
     */
    public CustomDeck3()
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
        super.addMapping(null, null, 8, 4);
        
        super.addMapping(Suit.Clubs, Value.Ace, 0, 0);
        super.addMapping(Suit.Clubs, Value.Two, 1, 1);
        super.addMapping(Suit.Clubs, Value.Three, 0, 1);
        super.addMapping(Suit.Clubs, Value.Four, 10, 0);
        super.addMapping(Suit.Clubs, Value.Five, 9, 0);
        super.addMapping(Suit.Clubs, Value.Six, 8, 0);
        super.addMapping(Suit.Clubs, Value.Seven, 7, 0);
        super.addMapping(Suit.Clubs, Value.Eight, 6, 0);
        super.addMapping(Suit.Clubs, Value.Nine, 5, 0);
        super.addMapping(Suit.Clubs, Value.Ten, 4, 0);
        super.addMapping(Suit.Clubs, Value.Jack, 3, 0);
        super.addMapping(Suit.Clubs, Value.Queen, 2, 0);
        super.addMapping(Suit.Clubs, Value.King, 1, 0);

        super.addMapping(Suit.Diamonds, Value.Ace, 4, 2);
        super.addMapping(Suit.Diamonds, Value.Two, 5, 3);
        super.addMapping(Suit.Diamonds, Value.Three, 4, 3);
        super.addMapping(Suit.Diamonds, Value.Four, 3, 3);
        super.addMapping(Suit.Diamonds, Value.Five, 2, 3);
        super.addMapping(Suit.Diamonds, Value.Six, 1, 3);
        super.addMapping(Suit.Diamonds, Value.Seven, 0, 3);
        super.addMapping(Suit.Diamonds, Value.Eight, 10, 2);
        super.addMapping(Suit.Diamonds, Value.Nine, 9, 2);
        super.addMapping(Suit.Diamonds, Value.Ten, 8, 2);
        super.addMapping(Suit.Diamonds, Value.Jack, 7, 2);
        super.addMapping(Suit.Diamonds, Value.Queen, 6, 2);
        super.addMapping(Suit.Diamonds, Value.King, 5, 2);

        super.addMapping(Suit.Hearts, Value.Ace, 6, 3);
        super.addMapping(Suit.Hearts, Value.Two, 7, 4);
        super.addMapping(Suit.Hearts, Value.Three, 6, 4);
        super.addMapping(Suit.Hearts, Value.Four, 5, 4);
        super.addMapping(Suit.Hearts, Value.Five, 4, 4);
        super.addMapping(Suit.Hearts, Value.Six, 3, 4);
        super.addMapping(Suit.Hearts, Value.Seven, 2, 4);
        super.addMapping(Suit.Hearts, Value.Eight, 1, 4);
        super.addMapping(Suit.Hearts, Value.Nine, 0, 4);
        super.addMapping(Suit.Hearts, Value.Ten, 10, 3);
        super.addMapping(Suit.Hearts, Value.Jack, 9, 3);
        super.addMapping(Suit.Hearts, Value.Queen, 8, 3);
        super.addMapping(Suit.Hearts, Value.King, 7, 3);

        super.addMapping(Suit.Spades, Value.Ace, 2, 1);
        super.addMapping(Suit.Spades, Value.Two, 3, 2);
        super.addMapping(Suit.Spades, Value.Three, 2, 2);
        super.addMapping(Suit.Spades, Value.Four, 1, 2);
        super.addMapping(Suit.Spades, Value.Five, 0, 2);
        super.addMapping(Suit.Spades, Value.Six, 10, 1);
        super.addMapping(Suit.Spades, Value.Seven, 9, 1);
        super.addMapping(Suit.Spades, Value.Eight, 8, 1);
        super.addMapping(Suit.Spades, Value.Nine, 7, 1);
        super.addMapping(Suit.Spades, Value.Ten, 6, 1);
        super.addMapping(Suit.Spades, Value.Jack, 5, 1);
        super.addMapping(Suit.Spades, Value.Queen, 4, 1);
        super.addMapping(Suit.Spades, Value.King, 3, 1);
        
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