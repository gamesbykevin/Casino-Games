package com.gamesbykevin.casinogames.game.spades;

import com.gamesbykevin.casinogames.deck.Card;
import com.gamesbykevin.casinogames.game.CardGame;
import com.gamesbykevin.casinogames.game.ICardGame;
import com.gamesbykevin.casinogames.game.spades.Guest.Position;
import com.gamesbykevin.casinogames.game.spades.overlay.BetOverlay;
import com.gamesbykevin.casinogames.game.spades.overlay.GameOverOverlay;
import com.gamesbykevin.casinogames.game.spades.overlay.ScoreOverlay;
import com.gamesbykevin.casinogames.deck.Hand;
import com.gamesbykevin.casinogames.deck.Hand.CardDisplay;
import com.gamesbykevin.casinogames.engine.Engine;
import com.gamesbykevin.casinogames.resources.GameAudio;
import com.gamesbykevin.casinogames.resources.GameImage;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class Spades extends CardGame implements ICardGame
{
    //we only need the width because we calculate the height by the ratio
    private static final int CARD_WIDTH = 125;
    
    //this is the score to win
    protected static final int SCORE_FINISH = 250;
    
    //the speed at which the cards move while dealing
    private static final int DEAL_PIXEL_SPEED_X = 50;
    private static final int DEAL_PIXEL_SPEED_Y = 50;
    
    //where the deck will be located
    private static final Point DECK_LOCATION = new Point(350, 195);
    
    //each player is dealt x cards
    private static final int HAND_LIMIT = 13;
    
    //the overlay for our betting
    private BetOverlay betOverlay;
    
    //the overlay for displaying the score
    private ScoreOverlay scoreOverlay;
    
    private GameOverOverlay gameOverOverlay;
    
    //steps to follow in this game
    private enum Steps
    {
        //deal cards to players
        Deal,
        
        //players now place their bet
        PlaceBet,
        
        //we play 1 hand
        Play,
        
        //check who won the hand
        CheckWin,
        
        //fly the cards towards the winner
        AnimateWin,
        
        //check if all cards have been played and then compare wins to bet
        CheckFinish,
        
        //display score and overall of each player
        DisplayScore,
        
        //check if the game has ended yet
        CheckGameOver,
        
        //here the game is officially over
        GameOver
    }
    
    public Spades(final Engine engine) throws Exception
    {
        super(engine);
        
        //play sound effect repeatively
        engine.getResources().playGameAudio(GameAudio.Keys.PlaceCard, true);
        
        //set the current step
        super.setStep(Steps.Deal);
        
        //create area for players to place their cards
        final Hand destination = new Hand();
        
        //set area where cards can be placed
        destination.setLocation(300, 175);
        destination.setDimensions(200, 150);
        
        //set background image for destination
        destination.setImage(engine.getResources().getGameImage(GameImage.Keys.CardPlaceHolder));
        
        //create bet overlay and set image/location/dimensions
        betOverlay = new BetOverlay();
        betOverlay.setImage(engine.getResources().getGameImage(GameImage.Keys.SpadesBetOverlay));
        betOverlay.setLocation(0, 0);
        betOverlay.setDimensions(engine.getMain().getScreen());
        
        //create score overlay and set image/location/dimensions
        scoreOverlay = new ScoreOverlay();
        scoreOverlay.setImage(engine.getResources().getGameImage(GameImage.Keys.SpadesScoreOverlay));
        scoreOverlay.setLocation(0, 0);
        scoreOverlay.setDimensions(engine.getMain().getScreen());
        
        //create game over screen
        gameOverOverlay = new GameOverOverlay();
        gameOverOverlay.setImage(engine.getResources().getGameImage(GameImage.Keys.SpadesGameOverOverlay));
        gameOverOverlay.setLocation(0, 0);
        gameOverOverlay.setDimensions(engine.getMain().getScreen());
        
        //add all background image possibilities so we can pick a random one
        final List<GameImage.Keys> keys = new ArrayList<>();
        keys.add(GameImage.Keys.Background1);
        keys.add(GameImage.Keys.Background2);
        keys.add(GameImage.Keys.Background3);
        keys.add(GameImage.Keys.Background4);
        keys.add(GameImage.Keys.Background5);
        keys.add(GameImage.Keys.Background6);
        
        //set the background image
        super.setImage(engine.getResources().getGameImage(keys.get(engine.getRandom().nextInt(keys.size()))));
        super.setDimensions(super.getImage().getWidth(null), super.getImage().getHeight(null));
        
        //add place for players to put their cards when playing
        super.getCardDestinations().add(destination);
        
        //add our 4 players
        super.getPlayers().add(new Guest("Human",    Position.South));
        super.getPlayers().add(new Guest("Player 2", Position.West));
        super.getPlayers().add(new Guest("Player 3", Position.North));
        super.getPlayers().add(new Guest("Player 4", Position.East));
        
        //create card deck
        reset(engine.getRandom());
        
        //player 1, south and this will be our human
        Guest player1 = (Guest)super.getPlayer(0);
        player1.getHand().setLocation(135, 365);
        player1.getHand().setDisplay(CardDisplay.Horizontal);
        player1.setHuman(true);
        player1.setImage(engine.getResources().getGameImage(GameImage.Keys.HorizontalDisplay));
        player1.setLocation(250, 435);
        player1.setDimensions(298, 70);
        
        //player 2, west
        Guest player2 = (Guest)super.getPlayer(1);
        player2.getHand().setLocation(5, 0);
        player2.getHand().setDisplay(CardDisplay.Vertical);
        player2.setImage(engine.getResources().getGameImage(GameImage.Keys.VerticalDisplay));
        player2.setLocation(5, 55);
        player2.setDimensions(57, 297);
        
        //player 3, north
        Guest player3 = (Guest)super.getPlayer(2);
        player3.getHand().setLocation(135, 10);
        player3.getHand().setDisplay(CardDisplay.Horizontal);
        player3.setImage(engine.getResources().getGameImage(GameImage.Keys.HorizontalDisplay));
        player3.setLocation(250, 0);
        player3.setDimensions(298, 70);
        
        //player 4, east
        Guest player4 = (Guest)super.getPlayer(3);
        player4.getHand().setLocation(675, 0);
        player4.getHand().setDisplay(CardDisplay.Vertical);
        player4.setImage(engine.getResources().getGameImage(GameImage.Keys.VerticalDisplay));
        player4.setLocation(740, 55);
        player4.setDimensions(57, 297);
        
        //reset all turns
        super.resetTurns();
        
        //for now the first player will have the first turn
        super.setTurn(player1.getId());
    }
    
    /**
     * Create/Shuffle new deck.<br>
     * Do not reset the players just the card deck.
     * @param random Object used to make random decisions
     * @throws Exception 
     */
    private void reset(final Random random) throws Exception
    {
        //create deck
        createDeck();
        
        //shuffle it twice
        shuffleDeck(random);
        shuffleDeck(random);
        
        //set the dimensions for display purposes
        setCardSize();
    }
    
    @Override
    public void dispose()
    {
        super.dispose();
        
        betOverlay.dispose();
        betOverlay = null;
        
        scoreOverlay.dispose();
        scoreOverlay = null;
        
        gameOverOverlay.dispose();
        gameOverOverlay = null;
    }
    
    @Override
    public void assignRank() throws Exception
    {
        for (Card card : getDeck().getHand().getCards())
        {
            switch (card.getSuit())
            {
                case Hearts:
                case Diamonds:
                case Clubs:
                    card.getSuit().setRank(1);
                    break;
                    
                //since we are playing spades the spades suit has the highest rank
                case Spades:
                    card.getSuit().setRank(2);
                    break;
                    
                default:
                    throw new Exception("Suit not found");
            }
            
            switch (card.getValue())
            {
                case Two:
                    card.getValue().setRank(0);
                    break;
                        
                case Three:
                    card.getValue().setRank(1);
                    break;
                        
                case Four:
                    card.getValue().setRank(2);
                    break;
                        
                case Five:
                    card.getValue().setRank(3);
                    break;
                        
                case Six:
                    card.getValue().setRank(4);
                    break;
                        
                case Seven:
                    card.getValue().setRank(5);
                    break;
                        
                case Eight:
                    card.getValue().setRank(6);
                    break;
                        
                case Nine:
                    card.getValue().setRank(7);
                    break;
                        
                case Ten:
                    card.getValue().setRank(8);
                    break;
                        
                case Jack:
                    card.getValue().setRank(9);
                    break;
                        
                case Queen:
                    card.getValue().setRank(10);
                    break;
                        
                case King:
                    card.getValue().setRank(11);
                    break;
                    
                case Ace:
                    card.getValue().setRank(12);
                    break;
                    
                default:
                    throw new Exception("Value not found");
            }
        }
    }
    
    @Override
    public void createDeck() throws Exception
    {
        //set the location of the deck
        super.getDeck().setLocation(DECK_LOCATION);
        
        //create deck
        for (Card.Suit suit : Card.Suit.values())
        {
            for (Card.Value value : Card.Value.values())
            {
                //add card to deck
                super.getDeck().add(suit, value);
            }
        }
        
        //now that deck is created assign rank to each card
        assignRank();
    }
    
    @Override
    public void shuffleDeck(final Random random)
    {
        //shuffle the deck
        super.getDeck().getHand().shuffle(random);
    }
    
    @Override
    public void setCardSize()
    {
        //set the size of the cards when displayed
        super.setCardDimensions(CARD_WIDTH);
        //super.setCardDimensions(super.getDeck().getSourceWidth());
    }
    
    @Override
    public void deal(final Engine engine)
    {
        //get the current player we are dealing to
        Guest player = (Guest)getPlayer();
        
        //reset the wins and betting for this player
        player.reset();
        
        //if the player has an active card that has not yet reached the destination
        if (player.getHand().hasActiveCard())
        {
            player.getHand().moveActiveCard(DEAL_PIXEL_SPEED_X, DEAL_PIXEL_SPEED_Y);
            
            //if the card has now been placed
            if (!player.getHand().hasActiveCard())
            {
                //switch to next players turn
                changeTurn();
            }
        }
        else
        {
            //this player has already been dealt the required amount of cards
            if (player.getHand().hasLimit(HAND_LIMIT))
            {
                changeTurn();
            }
            else
            {
                //this player has not reached the limit yet so grab a card from the deck
                final Card card = getDeck().getHand().get(engine.getRandom());
                
                //mark the card as hidden
                card.setState(Card.State.Hidden);
                
                //if our player is human we will display the cards
                if (player.isHuman())
                    card.setState(Card.State.Display);

                //set the card to be placed where the player is
                card.setDestination(player.getHand().getDestination(CARD_WIDTH));
                
                //add card to player hand
                player.addHand(card);

                //remove card from deck
                getDeck().getHand().remove(card);
            }
        }
        
        //check if we are finished dealing cards to every player
        for (Object obj : getPlayers())
        {
            Guest tmp = (Guest)obj;
            
            //if the player reached the limit we are not finished dealing yet
            if (!tmp.getHand().hasLimit(HAND_LIMIT) || tmp.getHand().hasActiveCard())
                return;
        }
        
        //stop sound effect
        engine.getResources().stopGameAudio(GameAudio.Keys.PlaceCard);
        
        //for now the first player will have the first turn
        super.setTurn(0);
        
        //dealing is finished so we need to set the next step
        super.setStep(Steps.PlaceBet);
    }
    
    /**
     * Determine if all cards have been played
     * @return True if all players have played all cards, false otherwise
     */
    private boolean hasPlayedAllCards()
    {
        for (Object obj : getPlayers())
        {
            Guest player = ((Guest)obj);

            //if cards still exist we are not done
            if (player.getHand().getSize() > 0)
                return false;
        }
        
        return true;
    }
    
    /**
     * Calculate the score 
     * @param mouse Use mouse event to determine when next step is to start
     */
    private void calculateScore(final Engine engine)
    {
        //determine here if all the cards have been used so we can calculate score
        boolean finished = hasPlayedAllCards();

        //if we are finished
        if (finished)
        {
            //if we haven't added stats for the players yet
            if (!scoreOverlay.hasPlayerStats())
            {
                //play show score sound
                engine.getResources().playGameAudio(GameAudio.Keys.ShowScoreSound);
                
                //calculate each player's score
                for (Object obj : getPlayers())
                {
                    Guest player = ((Guest)obj);

                    final int scoreAdd;

                    //if the player bid NIL the logic will be different for the score
                    if (player.getBet() == 0)
                    {
                        if (player.getWin() == 0)
                            scoreAdd = 100;
                        else
                            scoreAdd = -100;
                    }
                    else
                    {
                        if (player.getWin() >= player.getBet())
                        {
                            //multiply 10 by the bet amount, also any extra wins count as 1 point each
                            scoreAdd = (player.getBet() * 10) + (player.getWin() - player.getBet());
                        }
                        else
                        {
                            //the bet times 10 will be the amount deducted from score
                            scoreAdd = (-player.getBet() * 10);
                        }
                    }

                    //add stats for player
                    scoreOverlay.addPlayerStat(player.getName(), player.getScore(), player.getBet(), player.getWin(), scoreAdd, player.getScore() + scoreAdd);

                    //add our result
                    player.addScore(scoreAdd);
                }
            }
            else
            {
                //mouse was hit so go to next step
                if (engine.getMouse().isMousePressed())
                {
                    //play button sound
                    engine.getResources().playGameAudio(GameAudio.Keys.ButtonPress);
                    
                    super.setStep(Steps.CheckGameOver);
                }
            }
        }
        else
        {
            //reset all stats
            scoreOverlay.reset();
            
            //we aren't finished
            super.setStep(Steps.Play);
        }
    }
    
    /**
     * Here we will calculate the score and determine if the game ended
     */
    public void checkGameOver(final Engine engine) throws Exception
    {
        //did someone win
        boolean win = false;
        
        //did a human win
        boolean humanWin = false;
        
        //have all the cards been removed from all players
        boolean finished = hasPlayedAllCards();
        
        //the winning score
        int score = 0;
        
        //player id of the winner
        long id;
        
        //now lets check if any players have won
        for (Object obj : getPlayers())
        {
            //the current player we are checking
            Guest player = (Guest)obj;
            
            //if a player has reached the final score the game is over
            if (player.getScore() >= SCORE_FINISH)
            {
                //someone reached the winning score
                win = true;
                
                //if multiple reach the winning score we want the highest score
                if (player.getScore() > score)
                {
                    //did human win
                    humanWin = player.isHuman();
                    
                    //set the new score to beat
                    score = player.getScore();
                    
                    //identify the player that won
                    id = player.getId();
                    
                    //set the winners name
                    gameOverOverlay.setWinnerName(player.getName());
                }
            }
        }
        
        //if nobody won and all players cards are gone, create a new deck and continue
        if (!win && finished)
        {
            //play sound effect repeatively
            engine.getResources().playGameAudio(GameAudio.Keys.PlaceCard, true);
            
            //we will need to deal again
            super.setStep(Steps.Deal);
            
            //create new deck
            reset(engine.getRandom());
        }
        else
        {
            if (!win)
            {
                super.setStep(Steps.Play);
            }
            else
            {
                //play the appropriate sound
                if (humanWin)
                    engine.getResources().playGameAudio(GameAudio.Keys.WinGame);
                else
                    engine.getResources().playGameAudio(GameAudio.Keys.LoseGame);
                
                //the game is over set final step
                super.setStep(Steps.GameOver);
            }
        }
    }
    
    /**
     * Check the list of cards to determine the winner by spades standards
     * @param cards List of cards to check
     * @return Winning card, if no cards exist null is returned
     */
    public static Card getWinner(final List<Card> cards)
    {
        //no cards exist so return null
        if (cards.isEmpty())
            return null;
        
        //determine who is the winner, starting with the initial card that was placed
        Card winner = cards.get(0);
        
        //check each card to determine the winner
        for (Card card : cards)
        {
            //check if the current card can beat the winner
            if (hasWin(card, winner))
                winner = card;
        }

        return winner;
    }
    
    /**
     * Check if the challenger can beat the opponent
     * @param challenger The card we want to test
     * @param opponent The card we want to beat
     * @return true if the challenger can beat the opponent, false otherwise
     */
    public static boolean hasWin(final Card challenger, final Card opponent)
    {
        //if the cards are the same suit and the challengers card has a greater rank
        if (challenger.equalsSuit(opponent.getSuit()) && challenger.getValue().getRank() > opponent.getValue().getRank())
            return true;
        
        //if the chalengers suit outranks the opponent
        if (challenger.getSuit().getRank() > opponent.getSuit().getRank())
            return true;
        
        //we do not have a win
        return false;
    }
    
    /**
     * Here we will check to see who won the hand
     */
    private void checkWin()
    {
        //get the destination with all of the players cards
        final Hand destination = super.getCardDestinations().get(0);
        
        //determine who is the winner
        Card winner = getWinner(destination.getCards());
        
        //now that we have the winning card determine the player that won
        for (Object obj : getPlayers())
        {
            Guest player = ((Guest)obj);
            
            //if this is the player add a win, and only 1 player can win at a time
            if (player.getId() == winner.getPlayerId())
            {
                //add win
                player.addWin();
                
                //now we need to simulate all of the cards to go to the winner
                for (Card card : destination.getCards())
                {
                    //set the destination to the location of one of the remaining cards in the players hand
                    card.setDestination(player.getCenter());
                }
                
                //set the next step
                super.setStep(Steps.AnimateWin);
                
                //reset all turns
                super.resetTurns();
                
                //the player that wins needs to go first
                super.setTurn(player.getId());
                
                //no need to continue for now
                break;
            }
        }
    }
    
    private void placeBets(final Engine engine)
    {
        for (Object obj : getPlayers())
        {
            Guest player = ((Guest)obj);
            
            if (player.hasBet())
                continue;
            
            if (player.isHuman())
            {
                if (engine.getMouse().hasMouseMoved())
                {
                    betOverlay.setKey(engine.getMouse().getLocation());
                    engine.getMouse().reset();
                }
                
                if (engine.getMouse().isMouseReleased())
                {
                    //reset mouse events
                    engine.getMouse().reset();
                    
                    //determine which key was pressed
                    BetOverlay.Keys key = (BetOverlay.Keys)betOverlay.get(engine.getMouse().getLocation());
                    
                    if (key != null)
                    {
                        //play sound effect
                        engine.getResources().playGameAudio(GameAudio.Keys.ButtonPress);
                        
                        switch(key)
                        {
                            case NIL:
                                player.setBet(0);
                                break;
                                
                            case ONE:
                                player.setBet(1);
                                break;
                                
                            case TWO:
                                player.setBet(2);
                                break;
                                
                            case THREE:
                                player.setBet(3);
                                break;
                                
                            case FOUR:
                                player.setBet(4);
                                break;
                                
                            case FIVE:
                                player.setBet(5);
                                break;
                                
                            case SIX:
                                player.setBet(6);
                                break;
                                
                            case SEVEN:
                                player.setBet(7);
                                break;
                                
                            case EIGHT:
                                player.setBet(8);
                                break;
                                
                            case NINE:
                                player.setBet(9);
                                break;
                                
                            case TEN:
                                player.setBet(10);
                                break;
                                
                            case ELEVEN:
                                player.setBet(11);
                                break;
                                
                            case TWELVE:
                                player.setBet(12);
                                break;
                                
                            case THIRTEEN:
                                player.setBet(13);
                                break;
                        }
                        
                        //reset the bet for next time in the overlay specifically
                        betOverlay.reset();
                    }
                }
            }
            else
            {
                player.calculateBet();
            }
            
            return;
        }
        
        //set the next step now that we are done betting
        super.setStep(Steps.Play);
    }
    
    /**
     * This method simply makes all the existing cards move to the winning player
     */
    private void animateWin()
    {
        //get the destination with all of the players cards
        final Hand destination = super.getCardDestinations().get(0);
        
        //if we have any cards not at the destination yet
        if (destination.hasActiveCard())
        {
            //move each card towards destination at same time
            destination.moveActiveCards(Guest.PLACE_PIXEL_SPEED_X, Guest.PLACE_PIXEL_SPEED_Y);
        }
        else
        {
            //no more cards are active clear list
            destination.getCards().clear();
            
            //change step to play
            super.setStep(Steps.DisplayScore);
        }
    }
    
    @Override
    public void update(final Engine engine) throws Exception
    {
        switch((Steps)getStep())
        {
            case Deal:
                
                //deal cards out to players
                deal(engine);
                break;
                
            case PlaceBet:
                
                //players need to place their bets
                placeBets(engine);
                break;
                
            case Play:
                
                //update the current player that has a turn
                ((Guest)getPlayer()).update(engine);
                
                //if each player has placed a card
                if (getCardDestinations().get(0).getSize() == getPlayers().size())
                    super.setStep(Steps.CheckWin);
                
                break;
                
            case CheckWin:
                
                //check for game over
                checkWin();
                
                break;
                
            case AnimateWin:
                animateWin();
                break;
                
            case DisplayScore:
                
                //calculate score
                calculateScore(engine);
                break;
                
            case CheckGameOver:
                
                //check if game has ended
                checkGameOver(engine);
                break;
        }
    }
    
    @Override
    public void render(final Graphics graphics)
    {
        //draw the background
        super.draw(graphics);
        
        //draw deck and player cards
        super.render(graphics);
        
        //if it is the turn to bet draw overlay
        if (getStep() == Steps.PlaceBet)
            betOverlay.render(graphics);
        
        //if it is the turn to score draw overlay
        if (getStep() == Steps.DisplayScore && scoreOverlay.hasPlayerStats())
            scoreOverlay.render(graphics);
        
        //if we are checking for game over and have a winner name
        if (getStep() == Steps.GameOver && gameOverOverlay.hasWinnerName())
            gameOverOverlay.render(graphics);
    }
}