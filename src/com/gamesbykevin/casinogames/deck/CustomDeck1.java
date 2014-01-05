package com.gamesbykevin.casinogames.deck;

import com.gamesbykevin.casinogames.deck.Card.Suit;
import com.gamesbykevin.casinogames.deck.Card.Value;

public final class CustomDeck1 extends Deck
{
    //the original dimensions of the cards on the image
    private static final int SOURCE_WIDTH = 120;
    private static final int SOURCE_HEIGHT = 160;
    
    /**
     * Constructor for a new deck.
     * @param type Which deck will be used
     */
    public CustomDeck1()
    {
        super(SOURCE_WIDTH, SOURCE_HEIGHT);
        
        //map card coordinates
        map();
    }
    
    /**
     * Map out coordinates to cards here
     */
    @Override
    protected void map()
    {
        //hidden coordinates are at 6 different possible locations, pick 1 random
        super.addMapping(null, null, (int)(Math.random() * 6), 4);
        
        super.addMapping(Suit.Clubs, Value.Ace, 0, 3);
        super.addMapping(Suit.Clubs, Value.Two, 1, 3);
        super.addMapping(Suit.Clubs, Value.Three, 2, 3);
        super.addMapping(Suit.Clubs, Value.Four, 3, 3);
        super.addMapping(Suit.Clubs, Value.Five, 4, 3);
        super.addMapping(Suit.Clubs, Value.Six, 5, 3);
        super.addMapping(Suit.Clubs, Value.Seven, 6, 3);
        super.addMapping(Suit.Clubs, Value.Eight, 7, 3);
        super.addMapping(Suit.Clubs, Value.Nine, 8, 3);
        super.addMapping(Suit.Clubs, Value.Ten, 9, 3);
        super.addMapping(Suit.Clubs, Value.Jack, 10, 3);
        super.addMapping(Suit.Clubs, Value.Queen, 11, 3);
        super.addMapping(Suit.Clubs, Value.King, 12, 3);

        super.addMapping(Suit.Diamonds, Value.Ace, 0, 1);
        super.addMapping(Suit.Diamonds, Value.Two, 1, 1);
        super.addMapping(Suit.Diamonds, Value.Three, 2, 1);
        super.addMapping(Suit.Diamonds, Value.Four, 3, 1);
        super.addMapping(Suit.Diamonds, Value.Five, 4, 1);
        super.addMapping(Suit.Diamonds, Value.Six, 5, 1);
        super.addMapping(Suit.Diamonds, Value.Seven, 6, 1);
        super.addMapping(Suit.Diamonds, Value.Eight, 7, 1);
        super.addMapping(Suit.Diamonds, Value.Nine, 8, 1);
        super.addMapping(Suit.Diamonds, Value.Ten, 9, 1);
        super.addMapping(Suit.Diamonds, Value.Jack, 10, 1);
        super.addMapping(Suit.Diamonds, Value.Queen, 11, 1);
        super.addMapping(Suit.Diamonds, Value.King, 12, 1);

        super.addMapping(Suit.Hearts, Value.Ace, 0, 2);
        super.addMapping(Suit.Hearts, Value.Two, 1, 2);
        super.addMapping(Suit.Hearts, Value.Three, 2, 2);
        super.addMapping(Suit.Hearts, Value.Four, 3, 2);
        super.addMapping(Suit.Hearts, Value.Five, 4, 2);
        super.addMapping(Suit.Hearts, Value.Six, 5, 2);
        super.addMapping(Suit.Hearts, Value.Seven, 6, 2);
        super.addMapping(Suit.Hearts, Value.Eight, 7, 2);
        super.addMapping(Suit.Hearts, Value.Nine, 8, 2);
        super.addMapping(Suit.Hearts, Value.Ten, 9, 2);
        super.addMapping(Suit.Hearts, Value.Jack, 10, 2);
        super.addMapping(Suit.Hearts, Value.Queen, 11, 2);
        super.addMapping(Suit.Hearts, Value.King, 12, 2);

        super.addMapping(Suit.Spades, Value.Ace, 0, 0);
        super.addMapping(Suit.Spades, Value.Two, 1, 0);
        super.addMapping(Suit.Spades, Value.Three, 2, 0);
        super.addMapping(Suit.Spades, Value.Four, 3, 0);
        super.addMapping(Suit.Spades, Value.Five, 4, 0);
        super.addMapping(Suit.Spades, Value.Six, 5, 0);
        super.addMapping(Suit.Spades, Value.Seven, 6, 0);
        super.addMapping(Suit.Spades, Value.Eight, 7, 0);
        super.addMapping(Suit.Spades, Value.Nine, 8, 0);
        super.addMapping(Suit.Spades, Value.Ten, 9, 0);
        super.addMapping(Suit.Spades, Value.Jack, 10, 0);
        super.addMapping(Suit.Spades, Value.Queen, 11, 0);
        super.addMapping(Suit.Spades, Value.King, 12, 0);
        
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