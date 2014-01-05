package com.gamesbykevin.casinogames.deck;

import com.gamesbykevin.casinogames.deck.Card.Suit;
import com.gamesbykevin.casinogames.deck.Card.Value;

public final class CustomDeck6 extends Deck
{
    //the original dimensions of the cards on the image
    private static final int SOURCE_WIDTH = 300;
    private static final int SOURCE_HEIGHT = 420;
    
    /**
     * Constructor for a new deck.
     * @param type Which deck will be used
     */
    public CustomDeck6()
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
        //add hidden animation here because all cards in the deck will have the same 
        super.addMapping(null, null, 2, 4);
        
        super.addMapping(Suit.Clubs, Value.Ace, 0, 0);
        super.addMapping(Suit.Clubs, Value.Two, 4, 3);
        super.addMapping(Suit.Clubs, Value.Three, 0, 3);
        super.addMapping(Suit.Clubs, Value.Four, 9, 2);
        super.addMapping(Suit.Clubs, Value.Five, 4, 2);
        super.addMapping(Suit.Clubs, Value.Six, 0, 2);
        super.addMapping(Suit.Clubs, Value.Seven, 8, 1);
        super.addMapping(Suit.Clubs, Value.Eight, 4, 1);
        super.addMapping(Suit.Clubs, Value.Nine, 0, 1);
        super.addMapping(Suit.Clubs, Value.Ten, 8, 0);
        super.addMapping(Suit.Clubs, Value.Jack, 4, 0);
        super.addMapping(Suit.Clubs, Value.Queen, 1, 4);
        super.addMapping(Suit.Clubs, Value.King, 5, 3);

        super.addMapping(Suit.Diamonds, Value.Ace, 7, 2);
        super.addMapping(Suit.Diamonds, Value.Two, 8, 3);
        super.addMapping(Suit.Diamonds, Value.Three, 3, 3);
        super.addMapping(Suit.Diamonds, Value.Four, 12, 2);
        super.addMapping(Suit.Diamonds, Value.Five, 8, 2);
        super.addMapping(Suit.Diamonds, Value.Six, 3, 2);
        super.addMapping(Suit.Diamonds, Value.Seven, 12, 1);
        super.addMapping(Suit.Diamonds, Value.Eight, 7, 1);
        super.addMapping(Suit.Diamonds, Value.Nine, 3, 1);
        super.addMapping(Suit.Diamonds, Value.Ten, 12, 0);
        super.addMapping(Suit.Diamonds, Value.Jack, 7, 0);
        super.addMapping(Suit.Diamonds, Value.Queen, 3, 0);
        super.addMapping(Suit.Diamonds, Value.King, 0, 4);

        super.addMapping(Suit.Hearts, Value.Ace, 9, 1);
        super.addMapping(Suit.Hearts, Value.Two, 7, 3);
        super.addMapping(Suit.Hearts, Value.Three, 2, 3);
        super.addMapping(Suit.Hearts, Value.Four, 11, 2);
        super.addMapping(Suit.Hearts, Value.Five, 6, 2);
        super.addMapping(Suit.Hearts, Value.Six, 2, 2);
        super.addMapping(Suit.Hearts, Value.Seven, 11, 1);
        super.addMapping(Suit.Hearts, Value.Eight, 6, 1);
        super.addMapping(Suit.Hearts, Value.Nine, 2, 1);
        super.addMapping(Suit.Hearts, Value.Ten, 10, 0);
        super.addMapping(Suit.Hearts, Value.Jack, 6, 0);
        super.addMapping(Suit.Hearts, Value.Queen, 2, 0);
        super.addMapping(Suit.Hearts, Value.King, 12, 3);

        super.addMapping(Suit.Spades, Value.Ace, 11, 0);
        super.addMapping(Suit.Spades, Value.Two, 6, 3);
        super.addMapping(Suit.Spades, Value.Three, 1, 3);
        super.addMapping(Suit.Spades, Value.Four, 10, 2);
        super.addMapping(Suit.Spades, Value.Five, 5, 2);
        super.addMapping(Suit.Spades, Value.Six, 1, 2);
        super.addMapping(Suit.Spades, Value.Seven, 10, 1);
        super.addMapping(Suit.Spades, Value.Eight, 5, 1);
        super.addMapping(Suit.Spades, Value.Nine, 1, 1);
        super.addMapping(Suit.Spades, Value.Ten, 9, 0);
        super.addMapping(Suit.Spades, Value.Jack, 5, 0);
        super.addMapping(Suit.Spades, Value.Queen, 1, 0);
        super.addMapping(Suit.Spades, Value.King, 11, 3);
        
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