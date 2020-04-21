/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.rphstudio.procmaze.launcher;

import fr.rphstudio.ecs.core.utils.ControllerButtons;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

public class State01Start extends BasicGameState
{   
    //------------------------------------------------
    // PUBLIC CONSTANTS
    //------------------------------------------------
    public static final int ID = 100;

    
    //------------------------------------------------
    // PRIVATE PROPERTIES
    //------------------------------------------------
    private StateBasedGame gameObject;
    private GameContainer  container;
    private String         version;
    
    private Image          backGround;
    private FontManager    font;
    
    
    //------------------------------------------------
    // PRIVATE METHODS
    //------------------------------------------------
    // Get current program version string from file
    private void getVersion()
    {
        // Get display version
        BufferedReader br = null;
        try
        {
            this.version = "";
            br = new BufferedReader(new FileReader("info/version.txt"));
            String line;
            line = br.readLine();
            while(line != null)
            {
                this.version = this.version + line + "\n";
                line = br.readLine();
            }
            if (br != null)
            {
                br.close();
            }
        }
        catch (IOException e)
        {
            throw new Error(e);
        }
        finally
        {
            try
            {
                if (br != null)
                {
                    br.close();
                }
            }
            catch (IOException ex)
            {
                throw new Error(ex);
            }
        }
    }
    // Quit game
    private void quitGame()
    {
        this.container.exit();
    }
    // Go to next menu 
    private void goToGame(int nbPlayers)
    {
        // Get Menu state
        GameState gs = null;
        // Init Menu state
        try
        {
            gs = new State03Game(nbPlayers);
            this.gameObject.addState(gs);
            gs.init(this.container, this.gameObject);
        }
        catch(Exception e)
        {
            throw new Error(e);
        };
        // enter Menu state
        this.gameObject.enterState( State03Game.ID, new FadeOutTransition(Common.COLOR_FADE_IN, Common.TIME_FADE_IN), new FadeInTransition(Common.COLOR_FADE_OUT, Common.TIME_FADE_OUT) );
    } 
    
    
    //------------------------------------------------
    // CONSTRUCTOR
    //------------------------------------------------
    public State01Start()
    {
        this.font = new FontManager("./sprites/font.png", 40, 40 );
    }
    
    
    //------------------------------------------------
    // INIT METHOD
    //------------------------------------------------
    public void init(GameContainer container, StateBasedGame sbGame) throws SlickException
    {
        // Init fields
        this.container  = container;
        this.gameObject = sbGame;
        
        // Get version string
        this.getVersion();

        // Load background image
        this.backGround  = new Image("sprites/backgrounds/splashScreen.png");
    }

        
    //------------------------------------------------
    // RENDER METHOD
    //------------------------------------------------
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException
    {
        Color YELLOW = new Color(255,255,0,192);
        Color ORANGE = new Color(255,192,128,210);
        int refX = 0;
        int refY = 0;
        
        // Fit Screen
        MainLauncher.fitScreen(container, g);
        
        // Render Start screen background
        g.setColor(Color.white);
        g.drawImage(this.backGround, 0, 0);
        
        // display title
        refX = 415-40;
        refY = 320-5;
        g.setColor(new Color(255,255,255,32));
        g.fillRect(refX-15, refY, 490, 100);
        g.setColor(YELLOW);
        g.drawRect(refX-15, refY, 490, 100);
        this.font.displayString(g, "PROC-MAZE", refX, refY, YELLOW, 2.1f );
        this.font.displayString(g, "a procedural game for 1-2 players", refX, refY+70, YELLOW, 0.56f );

        g.setColor(new Color(255,255,255,32));
        g.fillRect(refX+250, refY+165, 475, 135);
        g.setColor(YELLOW);
        g.drawRect(refX+250, refY+165, 475, 135);
        this.font.displayString(g, "PRESS '1' for 1 player",  refX+260, refY+180-15, YELLOW, 0.8f );
        this.font.displayString(g, "PRESS '2' for 2 players", refX+260, refY+210-15, YELLOW, 0.8f );
        this.font.displayString(g, "PRESS 'ESC' to quit", refX+260, refY+270-15, YELLOW, 0.56f );
        this.font.displayString(g, "during game press 'ESC' to stop", refX+260, refY+292-15, YELLOW, 0.56f );
        
        // Display player 1 controls
        refX = 900;
        refY = 720;        
        g.setColor(new Color(255,255,255,32));
        g.fillRect(refX-5, refY-5, 445, 150+10);
        g.setColor(ORANGE);
        g.drawRect(refX-5, refY-5, 445, 150+10);
        this.font.displayString(g, "<Player 1 keys>"   , refX, refY, ORANGE, 0.8f );
        this.font.displayString(g, " UP    : 'up arrow'"   , refX, refY+30, ORANGE, 0.8f );
        this.font.displayString(g, " DOWN  : 'down arrow'" , refX, refY+60, ORANGE, 0.8f );
        this.font.displayString(g, " LEFT  : 'left arrow'" , refX, refY+90, ORANGE, 0.8f );
        this.font.displayString(g, " RIGHT : 'right arrow'", refX, refY+120, ORANGE, 0.8f );
    
        // Display player 2 controls
        refX = 900+440+25;
        refY = 720;        
        g.setColor(new Color(255,255,255,32));
        g.fillRect(refX-5, refY-5, 305, 150+10);
        g.setColor(ORANGE);
        g.drawRect(refX-5, refY-5, 305, 150+10);
        this.font.displayString(g, "<Player 2 keys>"   , refX, refY, ORANGE, 0.8f );
        this.font.displayString(g, " UP    : 'Z'"   , refX, refY+30, ORANGE, 0.8f );
        this.font.displayString(g, " DOWN  : 'S'" , refX, refY+60, ORANGE, 0.8f );
        this.font.displayString(g, " LEFT  : 'Q'" , refX, refY+90, ORANGE, 0.8f );
        this.font.displayString(g, " RIGHT : 'D'", refX, refY+120, ORANGE, 0.8f );
        
        // Render version number
        g.setColor(Color.yellow);
        g.drawString(this.version, 15, 1080-30);
    }

    
    //------------------------------------------------
    // UPDATE METHOD
    //------------------------------------------------
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException
    {   

    }
    
    
    //------------------------------------------------
    // KEYBOARD METHODS
    //------------------------------------------------
    @Override
    public void keyPressed(int key, char c)
    {
        switch(key)
        {
            // Quit game by pressing escape
            case Input.KEY_ESCAPE:
                this.quitGame();
                break;
            // go to game
            case Input.KEY_1:
            case Input.KEY_NUMPAD1:
                this.goToGame(1);
                break;
            case Input.KEY_2:
            case Input.KEY_NUMPAD2:
                this.goToGame(2);
                break;
            // all other keys have no effect
            default :     
                break;        
        }
    }
    
    
    //------------------------------------------------
    // STATE ID METHOD
    //------------------------------------------------
    @Override
    public int getID()
    {
          return this.ID;
    }
    
    
    //------------------------------------------------
    // END OF STATE
    //------------------------------------------------
}