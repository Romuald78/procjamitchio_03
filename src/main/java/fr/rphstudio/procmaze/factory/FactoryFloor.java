package fr.rphstudio.procmaze.factory;

import fr.rphstudio.ecs.component.render.RenderAnimations;
import fr.rphstudio.ecs.core.ECSWorld;
import fr.rphstudio.ecs.core.Entity;
import fr.rphstudio.procmaze.launcher.Common;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

public class FactoryFloor
{
    public static Entity create(ECSWorld world, Vector2f initPos01, float ratio) throws SlickException
    {
        //===========================================================
        // ENTITY
        //===========================================================

        //----------------------
        // Create Entity
        Entity entity01 = new Entity(world);

        //----------------------
        // Component RENDER
        RenderAnimations render01 = new RenderAnimations("RENDER", Common.RENDER_LAYER_FLOOR);
        Animation        anm      = new Animation();
        Image            img      = new Image( "./sprites/floor.png" ).getScaledCopy(ratio);
        anm.addFrame(img, 80);
        render01.addAnimation(anm, "DOOR", 0, 0);
        // set position using ratio
        Vector2f renderPos01 = new Vector2f(initPos01.x*ratio*Common.NB_PIXELS_PER_METER, initPos01.y*ratio*Common.NB_PIXELS_PER_METER);
        render01.setPosition(renderPos01);
        // Add component to entity
        entity01.addComponent(render01);
           
        //----------------------
        // Return entity
        //----------------------
        return entity01;
    }
}
