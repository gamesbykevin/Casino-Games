package com.gamesbykevin.casinogames.game.spades;

import com.gamesbykevin.framework.input.Mouse;

import com.gamesbykevin.casinogames.engine.Engine;
import com.gamesbykevin.casinogames.deck.Card;
import com.gamesbykevin.casinogames.deck.Card.*;
import com.gamesbykevin.casinogames.deck.Hand;
import com.gamesbykevin.casinogames.player.*;
import com.gamesbykevin.casinogames.resources.GameAudio;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public final class Guest extends Player implements IPlayer
{
    //the speed at which the cards move while placing
    protected static final int PLACE_PIXEL_SPEED_X = 3;
    protected static final int PLACE_PIXEL_SPEED_Y = 3;
    
    //the index of our selected card
    private int index = -1;
    
    //bet how many wins you will have
    private int bet = -1;
    
    //keep track of score
    private int score = 0;
    
    //count the number of victories
    private int win = 0;
    
    //image for display stats
    private BufferedImage displayImage;
    
    //our graphics object
    private Graphics2D displayImageGraphics;
    
    //do we write to image
    private boolean resetImage = true;
    
    private enum Logic
    {
        HighestWin,
        LowestWin,
        
        //this logic will be for NIL bid
        HighestLose,
    }
    
    public enum Position
    {
        East, West, North, South
    }
    
    //the position where our player is located
    private final Position position;
    
    public Guest(final String name, final Position position)
    {
        super(name);
        
        //set the player's position
        this.position = position;
    }
    
    @Override
    public void dispose()
    {
        super.dispose();
        
        if (displayImage != null)
            displayImage.flush();
        
        displayImage = null;
        
        if (displayImageGraphics != null)
            displayImageGraphics.dispose();
        
        displayImageGraphics = null;
    }
    
    /**
     * reset the bet and books won
     */
    public void reset()
    {
        setBet(-1);
        setWin(0);
    }
    
    /**
     * Add parameter to total score
     * @param score 
     */
    public void addScore(final int score)
    {
        this.score += score;
    }
    
    public int getScore()
    {
        return this.score;
    }
    
    public void setBet(final int bet)
    {
        this.bet = bet;
        
        //we need a new display image
        resetImage();
    }
    
    public int getBet()
    {
        return this.bet;
    }
    
    /**
     * Has the player bet NIL
     * @return True if the bet = 0, false otherwise
     */
    public boolean hasNilBid()
    {
        return (getBet() == 0);
    }
    
    public boolean hasBet()
    {
        return (getBet() >= 0);
    }
    
    public void addWin()
    {
        this.win++;
        
        //we need a new display image
        resetImage();
    }
    
    private void setWin(final int win)
    {
        this.win = win;
    }
    
    /**
     * Get the number of wins for this player
     * @return # of wins
     */
    public int getWin()
    {
        return this.win;
    }
    
    @Override
    public void update(final Engine engine)
    {
        //if the player is human check for mouse input
        if (isHuman())
        {
            super.update(engine);
            
            if (hasCardSelected())
            {
                //if user has let go of mouse
                if (engine.getMouse().isMouseReleased())
                {
                    //the active card that is not at its destination
                    final Card card = getHand().getActiveCard();

                    //if there is no active card return
                    if (card == null)
                        return;
                    
                    //get the place where the cards can be placed
                    final Hand destination = engine.getManager().getCardGame().getCardDestinations().get(0);

                    //if the active card is within the place card area
                    if (destination.getRectangle().contains(card.getCenter()))
                    {
                        //NOW WE NEED TO CHECK TO SEE IF THE CARD PLACED IS VALID
                        if (destination.getSize() > 0)
                        {
                            //get the first card placed to make sure we are performing a valid move
                            final Card initial = destination.getCards().get(0);

                            //do we have the suit of the initial card placed
                            final boolean hasSuit = (getHand().get(initial.getSuit()) != null);
                            
                            //if we have the suit but the card is not the suit this move is illegal
                            if (hasSuit && !card.equalsSuit(initial.getSuit()))
                            {
                                //play sound effect
                                engine.getResources().playGameAudio(GameAudio.Keys.InvalidMove);
                                
                                //reset card selection and mouse events
                                resetSelection(engine.getMouse());
                                
                                //don't continue right now
                                return;
                            }
                        }
                        else
                        {
                            //spades can only be first card if no other suits are available
                            final boolean hasOther = (getHand().get(Suit.Clubs) != null) || (getHand().get(Suit.Diamonds) != null) || (getHand().get(Suit.Hearts) != null);
                            
                            //if there are other cards and we have selected spades this is an invalid move
                            if (hasOther && card.equalsSuit(Suit.Spades))
                            {
                                //play sound effect
                                engine.getResources().playGameAudio(GameAudio.Keys.InvalidMove);
                        
                                //reset card selection and mouse events
                                resetSelection(engine.getMouse());
                                
                                //don't continue right now
                                return;
                            }
                        }
                        
                        //play sound effect
                        engine.getResources().playGameAudio(GameAudio.Keys.PlaceCard);
                        
                        //place card accordingly
                        placeCard(destination, card);
                        
                        //no longer turn so change turn
                        engine.getManager().getCardGame().changeTurn();
                    }
                    else
                    {
                        //play sound effect
                        engine.getResources().playGameAudio(GameAudio.Keys.InvalidMove);
                    }
                    
                    //reset card selection and mouse events
                    resetSelection(engine.getMouse());
                }
            }
        }
        else
        {
            //make sure we haven't picked a card yet
            if (!getHand().hasActiveCard())
            {
                //get the card destination where the cards are placed
                final Hand destination = engine.getManager().getCardGame().getCardDestinations().get(0);
                
                Card selection = null;
                
                //determine how many more wins are needed to reach our required bet
                final int required = getBet() - getWin();
                
                //the first card that was added will determine the suit needed to play
                final Card start = (destination.getSize() > 0) ? destination.get(0) : null;
                    
                //the suit we need to play from the first card placed
                final Suit startSuit = (start != null) ? start.getSuit() : null;
                
                //get the winning card in the list
                final Card winner = Spades.getWinner(destination.getCards());
                
                if (hasNilBid())
                {
                    //if we bid nil and lost, might as well try to win some to take away from opponent
                    if (getWin() > 0)
                    {
                        selection = getBestPlayableCard(startSuit, winner, Logic.HighestWin);
                    }
                    else
                    {
                        selection = getBestPlayableCard(startSuit, winner, Logic.HighestLose);
                    }
                }
                else
                {
                    //see if there any cards have been placed
                    if (startSuit != null)
                    {
                        //if the size is 1 short of the # of players then this is the last card to be placed
                        if (destination.getSize() == engine.getManager().getCardGame().getPlayers().size() - 1)
                        {
                            //win using the lowest ranked card
                            selection = getBestPlayableCard(startSuit, winner, Logic.LowestWin);
                        }
                        else
                        {
                            //if the # required is less than the # of cards in our hand
                            if (required < getHand().getSize())
                            {
                                //we still have some slack room so get the lowest card possible of winning
                                selection = getBestPlayableCard(startSuit, winner, Logic.LowestWin);
                            }
                            else
                            {
                                //we don't have any slack and must win
                                selection = getBestPlayableCard(startSuit, winner, Logic.HighestWin);
                            }
                        }
                    }
                    else
                    {
                        //if the # required is at least the same # as the number of cards we have
                        if (required >= getHand().getSize())
                        {
                            selection = getBestPlayableCard(null, null, Logic.HighestWin);
                        }
                        else
                        {
                            selection = getBestPlayableCard(null, null, Logic.LowestWin);
                        }
                    }
                }
                
                //the area where the card can be placed
                Rectangle tmp = destination.getRectangle();
                
                //random position where card will be placed
                final int x, y;
                
                //the random position will determine where the random location is
                switch(position)
                {
                    case North:
                        x = tmp.x + (tmp.width  / 4) + engine.getRandom().nextInt(tmp.width / 2);
                        y = tmp.y + engine.getRandom().nextInt(tmp.height / 4);
                        break;
                              
                    case South:
                        x = tmp.x + (tmp.width  / 4) + engine.getRandom().nextInt(tmp.width / 2);
                        y = tmp.y + (tmp.height / 4) + engine.getRandom().nextInt(tmp.height / 2);
                        break;
                        
                    case East:
                        x = tmp.x + (tmp.width  / 2) + engine.getRandom().nextInt(tmp.width / 4);
                        y = tmp.y + engine.getRandom().nextInt(tmp.height/2);
                        break;
                        
                    case West:
                    default:
                        x = tmp.x + engine.getRandom().nextInt(tmp.width / 4);
                        y = tmp.y + engine.getRandom().nextInt(tmp.height / 2);
                        break;
                }
                
                //change the destination of this card and offset the dimensions
                selection.setDestination(x, y);
                
                //we can also display the card now as well
                selection.setState(Card.State.Display);
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
                    
                    //place card accordingly
                    placeCard(engine.getManager().getCardGame().getCardDestinations().get(0), card);

                    //no longer turn so change turn
                    engine.getManager().getCardGame().changeTurn();
                }
            }
        }
    }
    
    /**
     * Determine how many wins we can get with the given hand
     */
    public void calculateBet()
    {
        //how many do we think we can win
        int estimatedWins = 0;
        
        for (Card card : getHand().getCards())
        {
            //spades we will determine differently
            if (card.equalsSuit(Suit.Spades))
            {
                //if a ten or higher assume that we can win
                if (card.getValue().getRank() > 7)
                    estimatedWins++;
            }
            else
            {
                //if higher than jack assume that we can win
                if (card.getValue().getRank() > 9)
                    estimatedWins++;
            }
        }
        
        //set our calculated bet
        setBet(estimatedWins);
    }
    
    /**
     * Check the player's hand to see if there are cards that are not spades
     * @return true if at least 1 card is not a spade, false otherwise
     */
    private boolean hasOtherSuits()
    {
        for (Suit suit : Suit.values())
        {
            if (suit == Suit.Spades)
                continue;
            
            if (getHand().get(suit) != null)
                return true;
        }
        
        //we did not find any other suits than spades
        return false;
    }
    
    /**
     * Get the card we want to play
     * @param startSuit The suit of the initial card played
     * @param winner If there is another card that beats the initial card played
     * @param logic The strategy to execute
     * @return Card to play for this player, null should only be returned if 0 cards exist in the hand
     */
    private Card getBestPlayableCard(final Suit startSuit, final Card winner, final Logic logic)
    {
        //this will contain the card we want to play
        Card tmp = null;
        
        //make sure we have an opponent
        final boolean hasOpponent = (startSuit != null);
        
        //check if we have spades in our hand
        final boolean hasSpades = (getHand().get(Suit.Spades) != null);
        
        //check if we have a card of the same suit as our opponent
        final boolean hasSuit = hasOpponent && (getHand().get(startSuit) != null);
        
        //check every card in our hand
        for (Card card : getHand().getCards())
        {
            //we don't have an opponent so we are the first to place a card
            if (!hasOpponent)
            {
                //we can only play spades first if there are no other suits
                if (hasOtherSuits())
                {
                    //we can't play spades now
                    if (card.equalsSuit(Suit.Spades))
                        continue;
                    
                    //if no card has been selected yet, do so now
                    if (tmp == null)
                        tmp = card;
                    
                    switch (logic)
                    {
                        case HighestLose:
                            
                            //if the new card has a lower rank than previous
                            if (card.getValue().getRank() < tmp.getValue().getRank())
                                tmp = card;
                            
                            break;
                            
                        default:
                            
                            //if the new card has a higher rank than previous
                            if (card.getValue().getRank() > tmp.getValue().getRank())
                                tmp = card;
                            break;
                    }
                    
                    continue;
                }
                
                //if no card has been selected yet, do so now
                if (tmp == null)
                    tmp = card;

                switch (logic)
                {
                    case HighestLose:

                        //if the new card has a lower rank than previous
                        if (card.getValue().getRank() < tmp.getValue().getRank())
                            tmp = card;

                        break;

                    default:

                        //if the new card has a higher rank than previous
                        if (card.getValue().getRank() > tmp.getValue().getRank())
                            tmp = card;
                        break;
                }
                
                //check the next card in our hand
                continue;
            }
            
            //if we do not have the suit of the initial card placed
            if (!hasSuit)
            {
                //we do not have any spades in our hand
                if (!hasSpades)
                {
                    //pick a card by default
                    if (tmp == null)
                        tmp = card;

                    switch(logic)
                    {
                        case HighestLose:
                            
                            //pick high ranking card since there is no way to win
                            if (card.getValue().getRank() > tmp.getValue().getRank())
                                tmp = card;
                            break;
                            
                        default:
                            
                            //pick low ranking card since there is no other way we can win
                            if (card.getValue().getRank() < tmp.getValue().getRank())
                                tmp = card;
                            break;
                    }
                    
                    //check the next card in our hand
                    continue;
                }

                //pick a card by default
                if (tmp == null)
                    tmp = card;

                //check if the current card can beat the winner
                if (Spades.hasWin(card, winner))
                {
                    if (Spades.hasWin(tmp, winner))
                    {
                        switch(logic)
                        {
                            case LowestWin:

                                //if the rank is lower we have new choice
                                if (card.getValue().getRank() < tmp.getValue().getRank())
                                    tmp = card;

                                break;

                            case HighestWin:

                                //if the rank is higher we have new choice
                                if (card.getValue().getRank() > tmp.getValue().getRank())
                                    tmp = card;

                                break;
                                
                            //we want to lose so do nothing here
                            case HighestLose:
                                break;
                        }
                    }
                    else
                    {
                        switch(logic)
                        {
                            //we want to lost so do nothing here
                            case HighestLose:
                                break;

                            default:

                                //the previous card was not a winner so our current card will be
                                tmp = card;
                                break;
                        }
                    }
                }
                else
                {
                    //since we haven't found a winner choose low rank card
                    if (!Spades.hasWin(tmp, winner))
                    {
                        switch(logic)
                        {
                            //do nothing here since we already have losing card
                            case HighestLose:
                                
                                //does the current card win
                                if (!Spades.hasWin(card, winner))
                                {
                                    //if the previous and current card lose pick highest rank
                                    if (card.getValue().getRank() > tmp.getValue().getRank())
                                        tmp = card;
                                }
                                break;
                                
                            default:
                                if (card.getValue().getRank() < tmp.getValue().getRank())
                                    tmp = card;
                                break;
                        }
                    }
                    else
                    {
                        //does the current card lose
                        if (!Spades.hasWin(card, winner))
                        {
                            switch(logic)
                            {
                                case HighestLose:

                                    //we want a losing card
                                    tmp = card;
                                    break;

                                default:
                                    break;
                            }
                        }
                    }
                }

                //skip to next card
                continue;
            }

            //since we have the suit any card of a different suit can't be played
            if (!card.equalsSuit(startSuit))
                continue;

            //if we have not selected a card pick default card
            if (tmp == null)
                tmp = card;

            //if the current card can beat the winner
            if (Spades.hasWin(card, winner))
            {
                //have we already found a winner yet
                if (Spades.hasWin(tmp, winner))
                {
                    switch(logic)
                    {
                        case LowestWin:

                            //if the rank is lower we have new choice
                            if (card.getValue().getRank() < tmp.getValue().getRank())
                                tmp = card;

                            break;

                        case HighestWin:

                            //if the rank is higher we have new choice
                            if (card.getValue().getRank() > tmp.getValue().getRank())
                                tmp = card;

                            break;
                            
                        //we want to lose so do nothing here
                        case HighestLose:
                            break;
                    }
                }
                else
                {
                    switch(logic)
                    {
                        //we want to lose so do nothing here
                        case HighestLose:
                            break;
                            
                        default:
                            
                            //the previous card was not a winner so our current card will be
                            tmp = card;
                            break;
                    }
                }
            }
            else
            {
                //if the previous card can't win we haven't found a winner yet
                if (!Spades.hasWin(tmp, winner))
                {
                    switch(logic)
                    {
                        case HighestLose:

                            //does the current card win
                            if (!Spades.hasWin(card, winner))
                            {
                                //if the previous and current card lose pick highest rank
                                if (card.getValue().getRank() > tmp.getValue().getRank())
                                    tmp = card;
                            }
                            break;

                        default:
                            if (card.getValue().getRank() < tmp.getValue().getRank())
                                tmp = card;
                            break;
                    }
                }
                else
                {
                    //does the current card lose
                    if (!Spades.hasWin(card, winner))
                    {
                        switch(logic)
                        {
                            case HighestLose:

                                //we want a losing card
                                tmp = card;
                                break;

                            default:
                                break;
                        }
                    }
                }
            }
        }
        
        return tmp;
    }
    
    /**
     * Remove the display image
     */
    private void resetImage()
    {
        if (this.displayImage != null)
        {
            //release image resources
            this.displayImage.flush();
        }
        
        //write to image again
        this.resetImage = true;
    }
    
    @Override
    public void render(final Graphics graphics, final Image image)
    {
        //draw the player's hand
        getHand().render(graphics, image);
        
        //draw dislay info
        drawDisplay(graphics);
    }
    
    private void drawDisplay(final Graphics graphics)
    {
        //create new display image
        if (displayImage == null)
        {
            this.displayImage = new BufferedImage(getImage().getWidth(null), getImage().getHeight(null), BufferedImage.TYPE_INT_ARGB);
        }
        
        //do we write to image again
        if (resetImage)
        {
            resetImage = false;
            
            //create our graphics object to write to the image
            displayImageGraphics = this.displayImage.createGraphics();

            //draw display background
            displayImageGraphics.drawImage(getImage(), 0, 0, null);
            
            //set font color
            displayImageGraphics.setColor(Color.BLACK);

            //also need to draw win count, and bet amount along with player name
            switch(getHand().getDisplay())
            {
                case Vertical:

                    //set graphics object to have transformation
                    displayImageGraphics.rotate(Math.toRadians(90), 0, 0);

                    //draw info
                    drawInfo(displayImageGraphics, "Books = ", + 85,  - 35);
                    drawInfo(displayImageGraphics, "Bet = ", + 185, - 35);
                    drawInfo(displayImageGraphics, getName(), + 85, - 15);

                    if (hasBet())
                        drawInfo(displayImageGraphics, "" + getBet(), + 245, - 35);

                    if (hasTurn() && hasBet())
                        drawInfo(displayImageGraphics, "(Playing)", + 185, - 15);

                    if (getWin() < getBet())
                        displayImageGraphics.setColor(Color.RED);
                    else
                        displayImageGraphics.setColor(Color.GREEN);

                    drawInfo(displayImageGraphics, "" + getWin(), + 145,  - 35);
                    break;

                case Horizontal:
                case None:
                default:

                    //draw status
                    drawInfo(displayImageGraphics, "Books = ", + 85, + 30);
                    drawInfo(displayImageGraphics, "Bet = ", + 185, + 30);
                    drawInfo(displayImageGraphics, getName(), + 85,  + 50);

                    if (hasBet())
                        drawInfo(displayImageGraphics, "" + getBet(), + 245, + 30);

                    if (hasTurn() && hasBet())
                        drawInfo(displayImageGraphics, "(Playing)", + 185, + 50);

                    if (getWin() < getBet())
                        displayImageGraphics.setColor(Color.RED);
                    else
                        displayImageGraphics.setColor(Color.GREEN);

                    drawInfo(displayImageGraphics, "" + getWin(), + 145,  + 30);

                    break;
            }
        }
        
        super.draw(graphics, displayImage);
    }
    
    private void drawInfo(final Graphics2D g2d, final String info, final double x, final double y)
    {
        g2d.drawString(info, (int)x, (int)y);
    }
}