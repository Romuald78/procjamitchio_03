package fr.rphstudio.procmaze.factory;

import fr.rphstudio.ecs.component.common.Position;
import fr.rphstudio.ecs.component.physic.Physic2D;
import fr.rphstudio.ecs.component.render.RenderAnimations;
import fr.rphstudio.ecs.core.ECSWorld;
import fr.rphstudio.ecs.core.Entity;
import fr.rphstudio.procmaze.component.SpaceDoor;
import fr.rphstudio.procmaze.launcher.Common;
import fr.rphstudio.procmaze.script.OpenCloseDoor;
import fr.rphstudio.procmaze.script.SetPhyToPosition;
import fr.rphstudio.procmaze.script.SetRenderToPosition;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

public class FactoryDoor
{
    public static Entity create(ECSWorld world, Vector2f initPos01, boolean isHorizontal, boolean isMovePositive, float moveSize, float ratio) throws SlickException
    {
        //===========================================================
        // ENTITY
        //===========================================================

        //----------------------
        // Create Entity
        Entity entity01 = new Entity(world);
        
        //----------------------
        // Component SPACE DOOR
        SpaceDoor sd01 = new SpaceDoor(moveSize, isHorizontal, isMovePositive);
        // Add component to entity
        entity01.addComponent(sd01);
        
        //----------------------
        // Component POSITION
        Position pos01 = new Position();
        pos01.setPosition( initPos01 );
        // Add component to entity
        entity01.addComponent(pos01);
        
        //----------------------
        // Component RENDER
        RenderAnimations render01 = new RenderAnimations("RENDER", Common.RENDER_LAYER_DOORS);
        Animation anm = new Animation();
        Image     img = new Image( "./sprites/doors2.png" ).getScaledCopy(ratio);
        if (isHorizontal)
        {
            render01.setAngle(-90);
        }
        if(!isMovePositive)
        {
            img = img.getFlippedCopy(false,true);
        }
        anm.addFrame(img, 80);
        render01.addAnimation(anm, "DOOR", 0, 0);
        // Add component to entity
        entity01.addComponent(render01);
        
        //----------------------
        // Component PHY2D
        Physic2D phy01 = new Physic2D("PHYSIC_DOOR");
        float doorSize = 128.0f/Common.NB_PIXELS_PER_METER;
        phy01.addSquareBody("PHY_DOOR", new Vector2f(0,0),doorSize,doorSize,true,1000.0f,1.0f);
        phy01.setXPosition(initPos01.x + doorSize/2);
        phy01.setYPosition(initPos01.y + doorSize/2);
        // Add component to entity
        entity01.addComponent(phy01);
        
        //----------------------
        // Script RENDER to POSITION
        SetRenderToPosition scrRenToPos01 = new SetRenderToPosition(pos01,render01,0,0,ratio);
        // Add component to entity
        entity01.addComponent(scrRenToPos01);
        
        //----------------------
        // Script Phy to Position
        SetPhyToPosition scrPhyToPos01 = new SetPhyToPosition(pos01, phy01, doorSize/2, doorSize/2);
        // Add component to entity
        entity01.addComponent(scrPhyToPos01);

        //----------------------
        // Script OPEN CLOSE DOOR
        OpenCloseDoor scrOpenClose01 = new OpenCloseDoor(pos01, initPos01, sd01);
        // Add component to entity
        entity01.addComponent(scrOpenClose01);
        
        //----------------------
        // Return entity
        //----------------------
        return entity01;
    }
}
