package fr.rphstudio.procmaze.factory;

import fr.rphstudio.ecs.component.common.Position;
import fr.rphstudio.ecs.component.control.KeyboardHandler;
import fr.rphstudio.ecs.component.physic.Physic2D;
import fr.rphstudio.ecs.component.render.RenderAnimations;
import fr.rphstudio.ecs.core.ECSWorld;
import fr.rphstudio.ecs.core.Entity;
import fr.rphstudio.procmaze.launcher.Common;
import fr.rphstudio.procmaze.script.MovePhyWithKeys;
import fr.rphstudio.procmaze.script.SetRenderToPosition;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

public class FactoryPlayer
{   
    public static Entity create(ECSWorld world, Vector2f initPos01, float ratio, int playerNum) throws SlickException
    {
        Color playerColor = Color.white;
        
        //----------------------
        // Create Entity
        Entity entity01 = new Entity(world,"PLAYER"+playerNum);

        //----------------------
        // Component KeyboardHandler
        KeyboardHandler keyboard01 = new KeyboardHandler();
        if(playerNum == 1)
        {
            keyboard01.addActionKey("UP"   , Input.KEY_UP   );
            keyboard01.addActionKey("DOWN" , Input.KEY_DOWN );
            keyboard01.addActionKey("LEFT" , Input.KEY_LEFT );
            keyboard01.addActionKey("RIGHT", Input.KEY_RIGHT);
            // Prepare color for render
            playerColor = new Color(255,0,0);
        }
        else if(playerNum == 2)
        {
            keyboard01.addActionKey("UP"   , Input.KEY_Z );
            keyboard01.addActionKey("DOWN" , Input.KEY_S );
            keyboard01.addActionKey("LEFT" , Input.KEY_Q );
            keyboard01.addActionKey("RIGHT", Input.KEY_D );    
            // Prepare color for render
            playerColor = new Color(0,255,0);
        }
        else
        {
            throw new Error("Bad player number !!");
        }
        // Add component to entity
        entity01.addComponent(keyboard01);
        
        //----------------------
        // Component RENDER
        RenderAnimations render01 = new RenderAnimations("RENDER", Common.RENDER_LAYER_PLAYERS);
        Animation anm = new Animation();
        Image     img = new Image( "./sprites/players.png" ).getScaledCopy(ratio);
        anm.addFrame(img, 80);
        render01.addAnimation(anm, "PLAYER", (int)(34*ratio), (int)(34*ratio));
        render01.setColor(playerColor);
        // Add component to entity
        entity01.addComponent(render01);
        
        //----------------------
        // Component POSITION
        Position pos01 = new Position("POSITION");
        pos01.setPosition( initPos01 );
        // Add component to entity
        entity01.addComponent(pos01);
        
        //----------------------
        // Component PHY2D
        Physic2D phy01 = new Physic2D("PHYSIC_PLAYER");
        float playerSize = 68.0f/Common.NB_PIXELS_PER_METER;
        phy01.addCircleBody("PHY_PLAYER", initPos01, playerSize/2, false, 5.0f, 0.995f);
        // Add component to entity
        entity01.addComponent(phy01);

        //----------------------
        // Script RENDER to POSITION
        SetRenderToPosition scrRenToPos01 = new SetRenderToPosition(pos01, render01, 0, 0, ratio);
        // Add component to entity
        entity01.addComponent(scrRenToPos01);
        
        //----------------------
        // Script Update physic and pos according to Keyboard
        MovePhyWithKeys move01 = new MovePhyWithKeys(pos01, phy01, keyboard01);
        // Add component to entity
        entity01.addComponent(move01);
        
        
        // return created entity
        return entity01;
    }
}


