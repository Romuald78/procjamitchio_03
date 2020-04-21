/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.rphstudio.procmaze.launcher;

import fr.rphstudio.ecs.component.common.Position;
import fr.rphstudio.ecs.core.ECSWorld;
import fr.rphstudio.ecs.core.Entity;
import fr.rphstudio.ecs.core.utils.ControllerButtons;

import fr.rphstudio.procmaze.factory.FactoryLevel;
import fr.rphstudio.utils.rng.Prng;
import java.util.List;
import org.newdawn.slick.Color;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;


class State03Game extends BasicGameState
{
    //------------------------------------------------
    // PUBLIC CONSTANTS
    //------------------------------------------------
    public static final int ID = 300;

    
    //------------------------------------------------
    // PRIVATE PROPERTIES
    //------------------------------------------------
    private StateBasedGame gameObject;
    private GameContainer  container;
    
    private ECSWorld       world;
    private Prng           rng;
    private int            level;
    private long           counter;
    private long           stageStartTime;
    private FontManager    font;
    private int            nbPlayers;
    private int            score1;
    private int            score2;
    
    
    //------------------------------------------------
    // PRIVATE METHODS
    //------------------------------------------------
    private static int getNbX(int level)
    {
        // Saturate level
        level = Math.max(1,level);
        level = Math.min(50,level);
        // Compute nbX from level
        int nbX = 9+(4*(level/3));
        if (level%3 == 2)
        {
            nbX += 2*((level-1)%3);
        }
        return nbX;
    }
    private static int getNbY(int level)
    {
        // Saturate level
        level = Math.max(1,level);
        level = Math.min(50,level);
        // Compute nbY from level
        int nbY = 7+(2*((level-1)/3));
        return nbY;
    }
    private void goToMainScreen()
    {
        GameState gs;
        // Remove in game controller
        gs = this.gameObject.getState(State03Game.ID);
        try
        {
            gs.leave(this.container, this.gameObject);
        }
        catch(Exception e)
        {
            throw new Error(e);
        };
        // Reset the main menu state
        gs = this.gameObject.getState(State01Start.ID);
        try
        {
            gs.init(this.container, this.gameObject);
        }
        catch(Exception e)
        {
            throw new Error(e);
        };
        // enter main menu
        this.gameObject.enterState( State01Start.ID, new FadeOutTransition(Common.COLOR_FADE_IN, Common.TIME_FADE_IN), new FadeInTransition(Common.COLOR_FADE_OUT, Common.TIME_FADE_OUT) );
    }
    private void startNewLevel()
    {
        GameState gs;
        // Remove in game controller
        gs = this.gameObject.getState(State03Game.ID);
        try
        {
            gs.init(this.container, this.gameObject);
        }
        catch(Exception e)
        {
            throw new Error(e);
        };
    }


    //------------------------------------------------
    // CONSTRUCTOR
    //------------------------------------------------
    public State03Game(int nbPlay)
    {
        this.level          = 0;
        this.counter        = 0;
        this.stageStartTime = 0;
        this.font           = new FontManager("./sprites/font.png", 40, 40 );
        this.nbPlayers      = nbPlay == 2 ? 2 : 1;
        this.score1         = 0;
        this.score2         = 0;
    }
    
    
    //------------------------------------------------
    // INIT METHOD
    //------------------------------------------------
    @Override
    public void init(GameContainer container, StateBasedGame sbGame) throws SlickException
    {
        // Init fields
        this.container  = container;
        this.gameObject = sbGame;

        container.setClearEachFrame(true);
        
        // Instanciate World
        this.world = new ECSWorld("World1");
        // init PRNG
        this.rng = new Prng();
        this.rng.setSeed(123456789);
        // Go to next level
        if(this.level < 50)
        {
            this.level++;
        }
        // Start timer
        this.stageStartTime = System.currentTimeMillis();
        
        
        //=================================================================
        // CREATE ENTITIES
        //=================================================================

        // Create level
        Entity level01 = FactoryLevel.create(this.world, this.getNbX(this.level), this.getNbY(this.level), this.rng, this.nbPlayers);
        this.world.addEntity(level01);
        

    }
    
    @Override
    public void leave(GameContainer container, StateBasedGame sbGame) throws SlickException
    {
        // reinit level value
        this.level = 0;
    }
    
    
    //------------------------------------------------
    // RENDER METHOD
    //------------------------------------------------
    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException
    {
        // Fit Screen
        MainLauncher.fitScreen(container, g);
        
        // Call world render method
        this.world.render(container, game, g);
        
        Color YELLOW = new Color(255,255,0,192);
        
        g.setColor(new Color(0,0,0,128));
        g.fillRect(895, 1080-35, 150, 30);
        this.font.displayString(g, "LEVEL="+this.level+"/50", 900, 1080-30, YELLOW, 0.56f );
        g.setColor(new Color(0,0,0,128));
        g.fillRect(1920-405, 5, 1000, 30);
        this.font.displayString(g, "LEVEL TIMER = " + ((System.currentTimeMillis()-this.stageStartTime)/100)/10.0f + " sec.", 1920-400, 10, YELLOW, 0.56f );
        g.setColor(new Color(0,0,0,128));
        g.fillRect(1920-405, 1080-35, 1000, 30);
        this.font.displayString(g, "GLOBAL TIMER = " + ((System.currentTimeMillis()-this.stageStartTime+this.counter)/100)/10.0f + " sec.", 1920-400, 1080-30, YELLOW, 0.56f );
        
        g.setColor(new Color(0,0,0,128));
        g.fillRect(5, 1080-35, 170, 30);
        this.font.displayString(g, "SCORE1 = " + this.score1 , 15, 1080-30, Color.red, 0.56f );
        if(this.nbPlayers > 1)
        {
            g.setColor(new Color(0,0,0,128));
            g.fillRect(5, 5, 170, 30);
            this.font.displayString(g, "SCORE2 = " + this.score2 , 15, 10      , Color.green, 0.56f );
        }
    }

    
    //------------------------------------------------
    // UPDATE METHOD
    //------------------------------------------------
    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException
    {
        boolean isWinner1 = false;
        boolean isWinner2 = false;
        
        container.getGraphics().clear();
        // Call all script components and execute them
        this.world.update(container, game, delta);
    
        List<Entity> playerList;
        Vector2f pos;
        
        
        playerList = this.world.getEntity("PLAYER1");
        if(playerList.size()!=1)
        {
            throw new Error("Bad Player 1 entity !");
        }
        // Get the position from this entity and check it is on the destination position
        pos = ((Position)(playerList.get(0).getComponent("POSITION").get(0))).getPosition();
        System.out.println(pos.x+"-"+pos.y);
        if( pos.x >= 2*State03Game.getNbX(this.level)-3.5f )
        {
            if( (pos.y >= State03Game.getNbY(this.level)-0.5f ) && (pos.y <= State03Game.getNbY(this.level)+0.5f ) )
            {
                isWinner1 = true;
                this.score1++;
            }
        }
        if(this.nbPlayers > 1)
        {
            playerList = this.world.getEntity("PLAYER2");
            if(playerList.size()!=1)
            {
                throw new Error("Bad Player 2 entity !");
            }
            // Get the position from this entity and check it is on the destination position
            pos = ((Position)(playerList.get(0).getComponent("POSITION").get(0))).getPosition();
            if( pos.x >= 2*State03Game.getNbX(this.level)-3.5f )
            {
                if( (pos.y >= State03Game.getNbY(this.level)-0.5f ) && (pos.y <= State03Game.getNbY(this.level)+0.5f ) )
                {
                    isWinner2 = true;
                    this.score2++;
                }
            }
        }
        
        if(isWinner1 || isWinner2)
        {
            if(this.level >= 50)
            {
                this.goToMainScreen();
            }
            else
            {
                this.counter += System.currentTimeMillis() - this.stageStartTime;
                this.init(this.container, this.gameObject);
            }
        }
    }
    
    
    //------------------------------------------------
    // KEYBOARD METHODS
    //------------------------------------------------
    @Override
    public void keyPressed(int key, char c)
    {   
        // Call World Key Method
        this.world.keyPressed(key);
        
        // Check for application exit
        switch(key)
        {
            case Input.KEY_ESCAPE:
                // Go to pause screen
                this.goToMainScreen();
                break;
            case Input.KEY_SPACE:
                // reinit current game state
                this.startNewLevel();
            default:
                break;
        }
    }    
    @Override
    public void keyReleased(int key, char c)
    {
        // Call World Key Method
        this.world.keyReleased(key);
    }
    
        
    //------------------------------------------------
    // CONTROLLER METHODS
    //------------------------------------------------
    @Override
    public void controllerButtonPressed(int controller, int button)
    {
        // Call World PAD Button method
        this.world.controllerButtonPressed(controller, button);
        // Go to pause menu if requested
        if(button == ControllerButtons.BUTTON_BACK)
        {
            this.goToMainScreen();
        }
    }
    @Override
    public void controllerButtonReleased(int controller, int button)
    {   
        // Call World PAD Button method
        this.world.controllerButtonReleased(controller, button);
    }
    @Override
    public void controllerLeftPressed(int controller)
    {
        // Call World PAD Direction method
        this.world.controllerDirectionPressed(controller, ECSWorld.PAD_DIR_LEFT);
    }
    @Override
    public void controllerLeftReleased(int controller)
    {
        // Call World PAD Direction method
        this.world.controllerDirectionReleased(controller, ECSWorld.PAD_DIR_LEFT);
    }    
    @Override
    public void controllerRightPressed(int controller)
    {
        // Call World PAD Direction method
        this.world.controllerDirectionPressed(controller, ECSWorld.PAD_DIR_RIGHT);
    }
    @Override
    public void controllerRightReleased(int controller)
    {
        // Call World PAD Direction method
        this.world.controllerDirectionReleased(controller, ECSWorld.PAD_DIR_RIGHT);
    }    
    @Override
    public void controllerUpPressed(int controller)
    {
        // Call World PAD Direction method
        this.world.controllerDirectionPressed(controller, ECSWorld.PAD_DIR_UP);
    }
    @Override
    public void controllerUpReleased(int controller)
    {
        // Call World PAD Direction method
        this.world.controllerDirectionReleased(controller, ECSWorld.PAD_DIR_UP);
    }    
    @Override
    public void controllerDownPressed(int controller)
    {
        // Call World PAD Direction method
        this.world.controllerDirectionPressed(controller, ECSWorld.PAD_DIR_DOWN);
    }
    @Override
    public void controllerDownReleased(int controller)
    {
        // Call World PAD Direction method
        this.world.controllerDirectionReleased(controller, ECSWorld.PAD_DIR_DOWN);
    }
    
    
    //------------------------------------------------
    // STATE ID METHOD
    //------------------------------------------------
    @Override
    public int getID()
    {
          return State03Game.ID;
    }
    
    
    //------------------------------------------------
    // END OF STATE
    //------------------------------------------------
}