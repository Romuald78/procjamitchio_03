package fr.rphstudio.procmaze.factory;

import fr.rphstudio.ecs.component.render.RenderAnimations;
import fr.rphstudio.ecs.core.ECSWorld;
import fr.rphstudio.ecs.core.Entity;
import fr.rphstudio.procmaze.component.SpaceDoor;
import fr.rphstudio.procmaze.component.SpaceLevel;
import fr.rphstudio.procmaze.launcher.Common;
import fr.rphstudio.procmaze.launcher.MainLauncher;
import fr.rphstudio.procmaze.script.ProcessMaze;
import fr.rphstudio.utils.rng.Prng;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Vector2f;

public class FactoryLevel
{   
    public static Entity create(ECSWorld world, int nbX, int nbY, Prng rng, int nbPlayers) throws SlickException
    {   
        //----------------------
        // Create Entity
        Entity entity01 = new Entity(world);

        //----------------------
        // Component SPACE Level
        SpaceLevel level01 = new SpaceLevel(nbX,nbY);
        // Add component to entity
        entity01.addComponent(level01);
        
        //----------------------
        // Fill level with SPACE DOOR components
        // init position
        Vector2f initPos01;
        Entity   door01;
        Entity   wall01;
        Entity   floor01;
        Entity   player01;
        Entity   player02;
        // Compute ratio
        float ratioX = (MainLauncher.WIDTH /(float)(nbX))/128;
        float ratioY = (MainLauncher.HEIGHT/(float)(nbY))/128;
        float ratio  = Math.min(ratioX, ratioY);
        
        // Fill space doors, floors and walls
        for(int x=0;x<nbX;x++)
        {
            for(int y=0;y<nbY;y++)
            {
                // create init position
                initPos01 = new Vector2f(x*128.0f/Common.NB_PIXELS_PER_METER, y*128.0f/Common.NB_PIXELS_PER_METER);
                if( (x==0) || (y==0) || (x==nbX-1) || (y==nbY-1) || ((x%2==y%2)&&(x%2==0)) )
                {
                    // Create WALL
                    initPos01.x += 64.0f/Common.NB_PIXELS_PER_METER;
                    initPos01.y += 64.0f/Common.NB_PIXELS_PER_METER;
                    wall01 = FactoryWall.create(world, initPos01, ratio);
                    world.addEntity(wall01);
                }
                else 
                {
                    // Create FLOOR
                    floor01 = FactoryFloor.create(world, initPos01, ratio);
                    world.addEntity(floor01);
                
                    if( (y%2) != (x%2) )
                    {
                        // CREATE DOOR
                        boolean isH = (y%2==0);
                        boolean isP = (rng.random() <0.5);
                        Vector2f initPos02 = new Vector2f(initPos01.x, initPos01.y);
                        float moveDistance = 128-4;
                        if(rng.random() < 0.5f)
                        {
                            float delta = 63/Common.NB_PIXELS_PER_METER;
                            if(!isP)
                            {
                                delta = -delta;
                            }
                            // create 2nd door
                            if(isH)
                            {
                                initPos02.x += delta;
                                initPos01.x -= delta;
                            }
                            else
                            {
                                initPos02.y += delta;
                                initPos01.y -= delta;
                            }
                            moveDistance = 64-4;
                            isP = !isP;
                            // Create 2nd door if needed
                            door01 = FactoryDoor.create(world, initPos02, isH, !isP, moveDistance/Common.NB_PIXELS_PER_METER, ratio);
                            world.addEntity(door01);
                            // Add door to space level
                            level01.addDoor(x, y, (SpaceDoor)(door01.getComponent(SpaceDoor.class).get(0)) );
                        }
                        // create 1st door
                        door01  = FactoryDoor.create(world, initPos01, isH, isP, moveDistance/Common.NB_PIXELS_PER_METER, ratio);
                        world.addEntity(door01);
                        // Add door to space level
                        level01.addDoor(x, y, (SpaceDoor)(door01.getComponent(SpaceDoor.class).get(0)) );
                    }
                }
            }
        }
        
        //----------------------
        // Create players
        // player 1
        initPos01 = new Vector2f(1.5f*128.0f/Common.NB_PIXELS_PER_METER, (nbY-1.5f)*128.0f/Common.NB_PIXELS_PER_METER);
        player01  = FactoryPlayer.create(world, initPos01, ratio, 1);
        world.addEntity(player01);
        // player 2
        if(nbPlayers > 1)
        {
            initPos01 = new Vector2f(1.5f*128.0f/Common.NB_PIXELS_PER_METER, 1.5f*128.0f/Common.NB_PIXELS_PER_METER);
            player02  = FactoryPlayer.create(world, initPos01, ratio, 2);
            world.addEntity(player02);
        }
        
        //----------------------
        // Script PROCESS MAZE
        ProcessMaze scrProcessMaze01 = new ProcessMaze(level01, rng);
        // Add component to entity
        entity01.addComponent(scrProcessMaze01);

        //----------------------
        // STAR RENDER
        RenderAnimations star01 = new RenderAnimations("STAR", Common.RENDER_LAYER_PLAYERS-1);
        Animation anm = new Animation();
        SpriteSheet ss = new SpriteSheet("./sprites/star.png", 64,64);
        for(int idx=0;idx<6;idx++)
        {
            anm.addFrame(ss.getSprite(idx,0).getScaledCopy(ratio), 100);
        }
        star01.addAnimation(anm, "STARANIM", (int)(32*ratio), (int)(32*ratio));
        star01.setPosition( (nbX-1.5f)*128.0f*ratio, nbY*64.0f*ratio);
        // Add component to entity
        entity01.addComponent(star01);

        
        
        // return created entity
        return entity01;
    }
}


/*
        LVL    nb X	nb Y
    1    1	7	9
    2    2	9	
    0    3	11	
    1    4	11	11         LVL%3==1 : Y=9+(   2 *(LVL//3)) 
    2    5	13	           LVL%3<=1 : X=7+(   4 *(LVL//3))
    0    6	15	           LVL%3==2 : X=7+((1+4)*(LVL//3))
    1    7	15	13
    2    8	17	
    0    9	19	
    1    10	19	15
    2    11	21	
    0    12	23	
    1    13	23	17
    2    14	25	
        15	27	
        16	27	19
        17	29	
        18	31	
        19	31	21
        20	33	
        21	35	
        22	35	23
        23	37	
        24	39	
        25	39	25
        26	41	
        27	43	
        28	43	27
        29	45	
        30	47	
        31	47	29
        32	49	
        33	51	

        
        */
