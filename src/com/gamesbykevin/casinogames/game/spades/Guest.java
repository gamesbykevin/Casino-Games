package com.gamesbykevin.casinogames.game.spades;

import com.gamesbykevin.framework.input.Mouse;

import com.gamesbykevin.casinogames.engine.Engine;
import com.gamesbykevin.casinogames.deck.Card;
import com.gamesbykevin.casinogames.deck.Card.*;
import com.gamesbykevin.casinogames.deck.Hand;
import com.gamesbykevin.casinogames.player.*;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

public final class Guest extends Player implements IPlayer
{
    //the speed at which the cards move while placing
    private static final int PLACE_PIXEL_SPEED_X = 3;
    private static final int PLACE_PIXEL_SPEED_Y = 3;
    
    //the index of our selected card
    private int index = -1;
    
    //bet how many wins you will have
    private int bet = 0;
    
    //keep track of score
    private int score = 0;
    
    //count the number of victories
    private int win = 0;
    
    private enum Logic
    {
        HighestWin,
        HighestLose,
        LowestWin,
        LowestLose
    }
    
    public Guest()
    {
        super();
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
    }
    
    public int getBet()
    {
        return this.bet;
    }
    
    public void addWin()
    {
        this.win++;
    }
    
    /**
     * Get the number of wins for this player
     * @return # of wins
     */
    public int getWin()
    {
        return this.win;
    }
    
    /**
     * Reset the bet and current number of wins
     */
    public void reset()
    {
        this.win = 0;
        this.bet = 0;
    }
    
    @Override
    public void update(final Engine engine)
    {
        //if the player is human check for mouse input
        if (isHuman())
        {
            if (hasCardSelected())
            {
                //if user has let go of mouse
                if (engine.getMouse().isMouseReleased())
                {
                    //the active card that is not at its destination
                    final Card card = getHand().getActiveCard();

                    //get the place where the cards can be placed
                    final Hand destination = engine.getManager().getCardGame().getCardDestinations().get(0);

                    //if the active card equals the place card area
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
                                //reset card selection and mouse events
                                resetSelection(engine.getMouse());
                                
                                //don't continue right now
                                return;
                            }
                        }
                        
                        //place card accordingly
                        placeCard(destination, card);
                        
                        //no longer turn so change
                        engine.getManager().getCardGame().changeTurnIndex();
                    }
                    
                    //reset card selection and mouse events
                    resetSelection(engine.getMouse());
                    
                    //don't continue right now
                    return;
                }

                //user dragged mouse so move card with it
                if (engine.getMouse().isMouseDragged())
                {
                    final int x = (int)(engine.getMouse().getLocation().x - (getHand().get(getCardSelectedIndex()).getWidth() / 2));
                    final int y = (int)(engine.getMouse().getLocation().y - (getHand().get(getCardSelectedIndex()).getHeight() / 2));

                    getHand().get(getCardSelectedIndex()).setLocation(x, y);
                }
            }
            else
            {
                //if we have an active card move it
                if (getHand().hasActiveCard())
                {
                    getHand().moveActiveCard(PLACE_PIXEL_SPEED_X, PLACE_PIXEL_SPEED_Y);
                }
                else
                {
                    //has the mouse been pressed
                    if (engine.getMouse().isMousePressed())
                    {
                        //set the selected card based on the mouse location
                        setCardSelected(super.getHand().getIndex(engine.getMouse().getLocation()));
                    }
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
                
                //see if there are cards already placed
                if (destination.getSize() > 0)
                {
                    //the first card that was added will determine the card that we can play
                    final Card start = destination.get(0);
                    
                    //if we have already reached our projected amount attempt to forfeit
                    if (getBet() >= getWin())
                    {
                        //determine the highest ranking card that will lose
                        selection = getPlayableCard(start, Logic.HighestLose);
                    }
                    else
                    {
                        //if the destination size is 1 less than the # of players then this is the last card
                        if (destination.getSize() == engine.getManager().getCardGame().getPlayers().size() - 1)
                        {
                            //if we are 1 shy of our goal
                            if (getBet() - 1 == getWin())
                            {
                                //use the highest possible ranking card to win
                                selection = getPlayableCard(start, Logic.HighestWin);
                            }
                            else
                            {
                                //since we have more to win choose the lowest ranked card that will win
                                selection = getPlayableCard(start, Logic.LowestWin);
                            }
                        }
                        else
                        {
                            //there are more players that still need to go so need to determine which card to play
                            
                            //JUST DO THIS FOR NOW
                            selection = getPlayableCard(start, Logic.LowestLose);
                        }
                    }
                }
                else
                {
                    //we are the first to place a card we can only start with spades if that is all we have left
                    
                    //JUST DO THIS FOR NOW
                    //selection = getPlayableCard(null, Logic.LowestLose);
                }
                
                //the area where the card can be placed
                Rectangle tmp = destination.getRectangle();
                
                //pick a random location at the destination
                final int x = (int)((tmp.x + engine.getRandom().nextInt(tmp.width))  - (selection.getWidth()  / 2));
                final int y = (int)((tmp.y + engine.getRandom().nextInt(tmp.height)) - (selection.getHeight() / 2));
                
                //change the destination of this card and offset the dimensions
                selection.setDestination(x, y);
                
                //we can also display the card now as well
                selection.getSpriteSheet().setCurrent(Card.State.Display);
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
                    //place card accordingly
                    placeCard(engine.getManager().getCardGame().getCardDestinations().get(0), card);

                    //no longer turn so change
                    engine.getManager().getCardGame().changeTurnIndex();
                }
            }
        }
    }
    
    /**
     * Add card to the destination parameter and remove card from our hand
     * @param destination The destination
     * @param card The card we want to place
     */
    private void placeCard(final Hand destination, final Card card)
    {
        //mark the location of the card as the destination
        card.setDestination(card.getPoint());

        //add card to destination
        destination.add(card);

        //remove card from the players hand
        getHand().remove(card);
    }
    
    /**
     * Reset the mouse events and card selection
     * @param mouse Object used for mouse input
     */
    private void resetSelection(final Mouse mouse)
    {
        //we are no longer selecting a card
        resetCardSelected();

        //reset mouse events
        mouse.reset();
    }
    
    /**
     * Get the card we want to play
     * @param opponent Our opponents card
     * @param logic The strategy to execute
     * @return Card to play for this player
     */
    private Card getPlayableCard(final Card opponent, final Logic logic)
    {
        //this will contain the card we want to play
        Card tmp = null;
        
        //check if we have a card of the same suit
        final boolean hasSuit = (getHand().get(opponent.getSuit()) != null);
        
        for (Card card : getHand().getCards())
        {
            //if we have a card of the opponent's suit we must play it
            if (hasSuit)
            {
                //can only use a card that is part of the suit
                if (!card.equalsSuit(opponent.getSuit()))
                    continue;
                
                //if we have not selected a card yet do so now
                if (tmp == null)
                    tmp = card;
                
                switch(logic)
                {
                    case LowestWin:
                        
                        //first lets see if the card can beat the opponent
                        if (card.getValue().getRank() > opponent.getValue().getRank())
                        {
                            //second lets see if the card rank is lower than our temporary card
                            if (card.getValue().getRank() < tmp.getValue().getRank())
                                tmp = card;
                        }
                        break;
                        
                    case HighestWin:
                        
                        //first lets see if the card can beat the opponent
                        if (card.getValue().getRank() > opponent.getValue().getRank())
                        {
                            //second lets see if the card rank is higher than our temporary card
                            if (card.getValue().getRank() > tmp.getValue().getRank())
                                tmp = card;
                        }
                        break;
                        
                    case HighestLose:
                        
                        //first lets see if the card rank is lower than opponent
                        if (card.getValue().getRank() < opponent.getValue().getRank())
                        {
                            //second lets see if the card rank is higher than our temporary card
                            if (card.getValue().getRank() > tmp.getValue().getRank())
                                tmp = card;
                        }
                        break;
                        
                    case LowestLose:
                        
                        //first lets see if the card rank is lower than opponent
                        if (card.getValue().getRank() < opponent.getValue().getRank())
                        {
                            //second lets see if the card rank is lower than our temporary card
                            if (card.getValue().getRank() < tmp.getValue().getRank())
                                tmp = card;
                        }
                        break;
                }
            }
            else
            {
                //if no card has been selected, do so now
                if (tmp == null)
                    tmp = card;
                
                switch(logic)
                {
                    case LowestWin:
                        
                        //since we don't have matching suit the only way to win is by selecting spades
                        if (card.getSuit() != Suit.Spades)
                            continue;
                        
                        //if card's suit rank is > temporary card this is our new selection
                        if (card.getSuit().getRank() > tmp.getSuit().getRank())
                            tmp = card;
                        
                        //if the rank is lower we have new choice
                        if (card.getValue().getRank() < tmp.getValue().getRank())
                            tmp = card;
                        
                        break;
                        
                    case HighestWin:
                        
                        //since we don't have matching suit the only way to win is by selecting spades
                        if (card.getSuit() != Suit.Spades)
                            continue;
                        
                        //if card's suit rank is > temporary card this is our new selection
                        if (card.getSuit().getRank() > tmp.getSuit().getRank())
                            tmp = card;
                        
                        //if the rank is higher we have new choice
                        if (card.getValue().getRank() > tmp.getValue().getRank())
                            tmp = card;
                        
                        break;
                        
                    case HighestLose:
                        
                        //since we don't have matching suit the only way we can lose is by skipping spades
                        if (card.getSuit() == Suit.Spades)
                            continue;
                        
                        //if card's suit rank is < temporary card this is our new selection
                        if (card.getSuit().getRank() < tmp.getSuit().getRank())
                            tmp = card;
                        
                        //if the rank is higher we have new choice
                        if (card.getValue().getRank() > tmp.getValue().getRank())
                            tmp = card;
                        
                        break;
                        
                    case LowestLose:
                        
                        //since we don't have matching suit the only way we can lose is by skipping spades
                        if (card.getSuit() == Suit.Spades)
                            continue;
                        
                        //if card's suit rank is < temporary card this is our new selection
                        if (card.getSuit().getRank() < tmp.getSuit().getRank())
                            tmp = card;
                        
                        //if the rank is lower we have new choice
                        if (card.getValue().getRank() < tmp.getValue().getRank())
                            tmp = card;
                        
                        break;
                }
            }
        }
        
        return tmp;
    }
    
    private int getCardSelectedIndex()
    {
        return this.index;
    }
    
    private void resetCardSelected()
    {
        setCardSelected(-1);
    }
    
    private void setCardSelected(final int index)
    {
        this.index = index;
    }
    
    /**
     * Does this player have a card selected
     * @return 
     */
    public boolean hasCardSelected()
    {
        return (index >= 0);
    }
    
    @Override
    public void render(final Graphics graphics, final Image image)
    {
        //draw the player's hand
        getHand().render(graphics, image);
    }
}