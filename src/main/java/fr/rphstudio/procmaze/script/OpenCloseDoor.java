/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.rphstudio.procmaze.script;

import fr.rphstudio.ecs.component.common.Position;
import fr.rphstudio.ecs.core.utils.CoreUtils;
import fr.rphstudio.ecs.core.interf.IComponent;
import fr.rphstudio.ecs.core.interf.IScript;
import fr.rphstudio.procmaze.component.SpaceDoor;
import org.newdawn.slick.geom.Vector2f;

/**
 *
 * @author GRIGNON FAMILY
 */
public class OpenCloseDoor implements IComponent, IScript
{
    //================================================
    // PRIVATE PROPERTIES
    //================================================
    //----------------------------
    // Personal properties
    //----------------------------
    private final long       id;
    private final String     name;
    private Position         pos       = null;
    private SpaceDoor        spaceDoor = null;
    private Vector2f         initPos   = null;
    
    
    //================================================
    // CONSTRUCTOR
    //================================================
    public OpenCloseDoor(Position p, Vector2f initP, SpaceDoor sd)
    {
        // Store name
        this.name = "ScriptOpenCloseDoor";
        // Get unique ID
        this.id = CoreUtils.getNewID();
        // Common process
        this.pos       = p;
        this.initPos   = initP;
        this.spaceDoor = sd;
    }
    
    
    //================================================
    // INTERFACE METHODS
    //================================================
    @Override
    public long getID()
    {
        return this.id;
    }
    @Override
    public String getName()
    {
        return this.name;
    }
    @Override
    public void update(int delta)
    {
        /*
        long time = System.currentTimeMillis();
        if(time % 6000 <= 20)
        {
            this.spaceDoor.open();
        }
        else if(time%6000 >= 3000 && time%6000<=3020)
        {
            this.spaceDoor.close();
        }
        //*/
        
        // In any case, set position according to spacedoor 
        float dx = 0;
        float dy = 0;
        if(this.spaceDoor.isHorizontal())
        {
            dx = this.spaceDoor.getOpeningDistance();
        }
        else
        {
            dy = this.spaceDoor.getOpeningDistance();
        }
        this.pos.setXPosition( this.initPos.x+dx );
        this.pos.setYPosition( this.initPos.y+dy );
    }
  
        
    //================================================
    // SETTERS
    //================================================
    
    
    
    //================================================
    // GETTERS
    //================================================
    
    
    
    //================================================
    // END OF CLASS
    //================================================
}
