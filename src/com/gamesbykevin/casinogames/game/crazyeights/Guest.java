package com.gamesbykevin.casinogames.game.crazyeights;

import com.gamesbykevin.casinogames.deck.*;
import com.gamesbykevin.casinogames.engine.Engine;
import com.gamesbykevin.casinogames.game.crazyeights.CrazyEights.Destinations;
import com.gamesbykevin.casinogames.player.IPlayer;
import com.gamesbykevin.casinogames.player.Player;
import com.gamesbykevin.casinogames.resources.GameAudio;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

public class Guest extends Player implements IPlayer
{
    //the speed at which the cards move while placing
    protected static final int PLACE_PIXEL_SPEED_X = 3;
    protected static final int PLACE_PIXEL_SPEED_Y = 3;
    
    //is a card moving flag
    private boolean moveCard = false;
    
    //did we place a wildcard
    private boolean placedWildcard = false;
    
    //status message
    private String message = "";
    
    public Guest(final String name)
    {
        super(name);
        
        super.setPixelSpeed(PLACE_PIXEL_SPEED_X, PLACE_PIXEL_SPEED_Y);
    }
    
    private void setMessage(final String message)
    {
        this.message = message;
    }
    
    private String getMessage()
    {
        return this.message;
    }
    
    @Override
    public void dispose()
    {
        super.dispose();
    }
    
    @Override
    public void update(final Engine engine)
    {
        //if the player is human check for mouse input
        if (isHuman())
        {
            super.update(engine);
            
            //if user has let go of mouse
            if (engine.getMouse().isMouseReleased() && !moveCard)
            {
                if (hasCardSelected())
                {
                    //check this destination
                    final Hand destination = engine.getManager().getCardGame().getCardDestination(Destinations.CardPlace);
                    
                    //if the user previously placed a wildcard
                    if (placedWildcard)
                    {
                        //the active card that is not at its destination
                        final Card card = getHand().get(getCardSelectedIndex());
                        
                        //set the suit accordint to the card selected
                        destination.getLast().setSuit(card.getSuit());
                        
                        //play sound effect
                        engine.getResources().playGameAudio(GameAudio.Keys.ButtonPress);
                            
                        //set new message
                        setMessage("Wild new suit is: " + card.getSuit().toString());
                        
                        //turn flag off
                        placedWildcard = false;
                        
                        //move is complete now switch turns
                        engine.getManager().getCardGame().changeTurn();
                        
                        //reset card selection and mouse events
                        resetSelection(engine.getMouse());
                        
                        //no need to continue now
                        return;
                    }
                    
                    //if the card was placed inside the destination
                    if (destination.getRectangle().contains(engine.getMouse().getLocation()))
                    {
                        //the active card that is not at its destination
                        final Card card = getHand().getActiveCard();
                        
                        //now determine whether the card can be placed at the destination
                        final Card destinationCard = destination.getLast();
                        
                        //if the face card or suit equal the card in the destination, or is the wild card
                        if (card.equalsValue(destinationCard.getValue()) || card.equalsSuit(destinationCard.getSuit()) || card.equalsValue(CrazyEights.WILDCARD))
                        {
                            //set the correct location
                            card.setLocation(destination.getCenter().x - (card.getWidth() / 2), destination.getCenter().y - (card.getHeight() / 2));

                            //remove card from hand
                            getHand().remove(card);

                            //add card to destination
                            destination.add(card);
                            
                            //reset all destinations of the cards
                            getHand().resetDestinations(CrazyEights.CARD_WIDTH);

                            //play sound effect
                            engine.getResources().playGameAudio(GameAudio.Keys.PlaceCard);
                            
                            if (card.equalsValue(CrazyEights.WILDCARD))
                            {
                                //flag that we placed wildcard
                                placedWildcard = true;
                                
                                //reset card selection and mouse events
                                resetSelection(engine.getMouse());
                                
                                //notify human to set new suit
                                setMessage("Pick 1 of your cards to set new Suit.");
                                
                                //create new image to show new message
                                resetStatusImage();
                            }
                            else
                            {
                                //remove message
                                setMessage("");
                                
                                //move is complete now switch turns
                                engine.getManager().getCardGame().changeTurn();
                            }
                        }
                        else
                        {
                            //reset card selection and mouse events
                            resetSelection(engine.getMouse());
                            
                            //play sound effect
                            engine.getResources().playGameAudio(GameAudio.Keys.InvalidMove);
                        }
                    }
                    else
                    {
                        //play sound effect
                        engine.getResources().playGameAudio(GameAudio.Keys.InvalidMove);
                    }
                }
                else
                {
                    //check if the user has chosen to draw a card
                    final Hand destination = engine.getManager().getCardGame().getCardDestination(Destinations.DeckSource);
                    
                    //if the card was placed inside the destination
                    if (destination.getRectangle().contains(engine.getMouse().getLocation()))
                    {
                        //get the top card
                        final Card card = engine.getManager().getCardGame().getDeck().getHand().getLast();
                        
                        card.setState(Card.State.Display);
                        
                        //determine the position of the card
                        final int size = getHand().getSize();
                        
                        //set the new destination
                        card.setDestination(getHand().getDestination(size, CrazyEights.CARD_WIDTH));
                        
                        //remove it from the deck
                        engine.getManager().getCardGame().getDeck().getHand().remove(card);
                        
                        //play sound effect
                        engine.getResources().playGameAudio(GameAudio.Keys.ButtonPress);
                        
                        //reset all destinations of the cards before we add our new card
                        getHand().resetDestinations(CrazyEights.CARD_WIDTH);
                        
                        //add card to our hand
                        addHand(card);
                        
                        //we are drawing a card
                        moveCard = true;
                    }
                }
                
                //reset card selection and mouse events
                resetSelection(engine.getMouse());
            }
            
            //if we have a moving card
            if (moveCard)
            {
                //are we done with movement
                if (!getHand().hasActiveCard())
                {
                    //flag false
                    moveCard = false;

                    //move is complete now switch turns
                    engine.getManager().getCardGame().changeTurn();
                }
            }
        }
        else
        {
            //make sure we haven't picked a card yet
            if (!getHand().hasActiveCard())
            {
                //reset message seperate from reset image
                setMessage("");
                
                //make new image
                resetStatusImage();
                
                //check this destination
                final Hand destination = engine.getManager().getCardGame().getCardDestination(Destinations.CardPlace);

                //source of deck
                final Hand deckSource = engine.getManager().getCardGame().getDeck().getHand();
                
                //now determine whether the card can be placed at the destination
                final Card card = getBestPlayableCard(destination.getLast());
                
                //if card was not found we need to draw a card then switch turn
                if (card == null)
                {
                    //play sound effect
                    engine.getResources().playGameAudio(GameAudio.Keys.ButtonPress);
                    
                    //get card from top
                    final Card tmp = deckSource.getLast();
                    
                    //determine the position where the card will go
                    final int size = getHand().getSize();
                    
                    //set the new destination
                    tmp.setDestination(getHand().getDestination(size, CrazyEights.CARD_WIDTH));
                    
                    //remove card from deck
                    deckSource.remove(tmp);
                    
                    //reset all destinations of the cards before we add the new card
                    getHand().resetDestinations(CrazyEights.CARD_WIDTH);
                    
                    //add to current hand
                    addHand(tmp);
                }
                else
                {
                    //it is ok to reveal the card now
                    card.setState(Card.State.Display);
                    
                    //when all cards are placed they will be placed the same way
                    card.setDisplay(Hand.CardDisplay.Horizontal);
                    
                    //set the new location
                    card.setDestination(destination.getCenter().x - (card.getWidth() / 2), destination.getCenter().y - (card.getHeight() / 2));
                    
                    //flag that we are moving
                    moveCard = true;

                    //is the card the wildcard
                    placedWildcard = card.equalsValue(CrazyEights.WILDCARD);
                }
            }
            else
            {
                //this is the active card
                final Card card = getHand().getActiveCard();
                
                //we have an active card so move it
                getHand().moveActiveCard(PLACE_PIXEL_SPEED_X, PLACE_PIXEL_SPEED_Y);
                
                //if we had an active card but no longer do we can now place it
                if (!getHand().hasActiveCard())
                {
                    //play sound effect
                    engine.getResources().playGameAudio(GameAudio.Keys.PlaceCard);
                    
                    if (moveCard)
                    {
                        moveCard = false;
                        
                        //if we placed a wildcard
                        if (placedWildcard)
                        {
                            Card.Suit tmp = null;
                            
                            //count to beat
                            int count = 0;
                            
                            //check each suit to find which one is the most common
                            for (Card.Suit suit : Card.Suit.values())
                            {
                                if (getSuitCount(suit) > count)
                                {
                                    //set the new count to beat
                                    count = getSuitCount(suit);
                                    
                                    //the new suit to play
                                    tmp = suit;
                                }
                            }
                            
                            //set message
                            setMessage("Wild new suit is: " + tmp.toString());
                            
                            //set the appropriate suit
                            card.setSuit(tmp);
                            
                            //turn flag off for now
                            placedWildcard = false;
                        }
                        
                        //place card accordingly
                        placeCard(engine.getManager().getCardGame().getCardDestination(CrazyEights.Destinations.CardPlace), card);
                    }
                    
                    //reset all destinations of the cards
                    getHand().resetDestinations(CrazyEights.CARD_WIDTH);
                        
                    //no longer turn so change turn
                    engine.getManager().getCardGame().changeTurn();
                }
            }
        }
    }
    
    /**
     * Get the best playable card found.
     * @param matching Card we need to match
     * @return Card to play, if none found null is returned
     */
    private Card getBestPlayableCard(final Card matching)
    {
        //count how many matching suits we have
        final int suitCount = this.getSuitCount(matching.getSuit());
        
        //count how many matching face values we have
        final int valueCount = this.getValueCount(matching.getValue());
        
        //count how many wild cards we have
        final int wildCount = this.getValueCount(CrazyEights.WILDCARD);
        
        //we don't have any matching face value, suit or wild card
        if (suitCount == 0 && valueCount == 0 && wildCount == 0)
            return null;
        
        //our valid playable card
        Card playableCard = null;

        //best count
        int count = 0;
        
        //check every card in the hand
        for (Card card : getHand().getCards())
        {
            //if we match the suit, face value, or wild card
            if (card.equalsValue(matching.getValue()) || card.equalsSuit(matching.getSuit()) || card.equalsValue(CrazyEights.WILDCARD))
            {
                //check the suit count
                if (getSuitCount(card.getSuit()) > count)
                {
                    //set our new count to beat
                    count = getSuitCount(card.getSuit());

                    //our best card found
                    playableCard = card;
                }

                //check the value count
                if (getValueCount(card.getValue()) > count)
                {
                    //set our new count to beat
                    count = getValueCount(card.getValue());

                    //our best card found
                    playableCard = card;
                }

                break;
            }
        }
        
        //return our result
        return playableCard;
    }
    
    /**
     * Count how many cards of the specified suit exist
     * @param suit
     * @return count of cards matching the suit
     */
    private int getSuitCount(final Card.Suit suit)
    {
        int count = 0;
        
        for (Card card : getHand().getCards())
        {
            if (card.equalsSuit(suit))
                count++;
        }
        
        return count;
    }
    
    /**
     * Count how many cards of the specified value exist
     * @param value
     * @return count of cards matching the face value
     */
    private int getValueCount(final Card.Value value)
    {
        int count = 0;
        
        for (Card card : getHand().getCards())
        {
            if (card.equalsValue(value))
                count++;
        }
        
        return count;
    }
    
    /**
     * Determine what the players final score is
     */
    public void calculateScore()
    {
        for (Card card : getHand().getCards())
        {
            //the points of every card is stored in the face value rank
            setScore(getScore() + card.getValue().getRank());
        }
    }
    
    @Override
    public void render(final Graphics graphics, final Image image)
    {
        //draw the player's hand
        getHand().render(graphics, image);
        
        //draw dislay info
        drawDisplay(graphics);
    }
    
    /**
     * Here we will draw the player's name and if it is currently their turn
     * @param graphics Object used to write to image
     */
    private void drawDisplay(final Graphics graphics)
    {
        //create new display image
        if (getStatusImage() == null)
            createStatusImage(getImage().getWidth(null), getImage().getHeight(null));
        
        //do we write to image again
        if (super.hasResetStatusImageFlag())
        {
            super.switchResetStatusImageFlag();

            //get our graphics object to write to
            Graphics2D tmp = getStatusImageGraphics();
            
            //draw display background
            tmp.drawImage(getImage(), 0, 0, null);
            
            //set font color
            tmp.setColor(Color.BLACK);

            //also need to draw win count, and bet amount along with player name
            switch(getHand().getDisplay())
            {
                case Vertical:

                    //set graphics object to have transformation
                    tmp.rotate(Math.toRadians(90), 0, 0);

                    //draw info
                    drawInfo(tmp, getName(), + 75, - 15);

                    //draw message
                    drawInfo(tmp, getMessage(), + 75, - 35);
                    
                    if (hasTurn())
                        drawInfo(tmp, "(Playing)", + 175, - 15);

                    break;

                case Horizontal:
                case None:
                default:

                    //draw status
                    drawInfo(tmp, getName(), + 75,  + 50);

                    //draw message
                    drawInfo(tmp, getMessage(), + 75, + 35);
                    
                    if (hasTurn())
                        drawInfo(tmp, "(Playing)", + 175, + 50);

                    break;
            }
        }
        
        super.draw(graphics, getStatusImage());
    }
}