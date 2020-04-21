package fr.rphstudio.procmaze.factory;

import fr.rphstudio.ecs.component.physic.Physic2D;
import fr.rphstudio.ecs.component.render.RenderAnimations;
import fr.rphstudio.ecs.core.ECSWorld;
import fr.rphstudio.ecs.core.Entity;
import fr.rphstudio.procmaze.launcher.Common;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

public class FactoryWall
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
        RenderAnimations render01 = new RenderAnimations("RENDER", Common.RENDER_LAYER_WALLS);
        Animation        anm      = new Animation();
        Image            img      = new Image( "./sprites/wall2.png" ).getScaledCopy(ratio);
        anm.addFrame(img, 80);
        render01.addAnimation(anm, "DOOR", (int)(64*ratio), (int)(64*ratio));
        // set position using ratio
        Vector2f renderPos01 = new Vector2f(initPos01.x*ratio*Common.NB_PIXELS_PER_METER, initPos01.y*ratio*Common.NB_PIXELS_PER_METER);
        render01.setPosition(renderPos01);
        // Add component to entity
        entity01.addComponent(render01);
        
        //----------------------
        // Component PHY2D
        Physic2D phy01 = new Physic2D("PHYSIC_WALL");
        float wallSize = 128.0f/Common.NB_PIXELS_PER_METER;
        phy01.addSquareBody("PHY_WALL", initPos01, wallSize,wallSize,true,1000.0f,1.0f);
        // Add component to entity
        entity01.addComponent(phy01);

        
        //----------------------
        // Return entity
        //----------------------
        return entity01;
    }
}
