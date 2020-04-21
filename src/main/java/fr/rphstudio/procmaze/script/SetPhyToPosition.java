/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.rphstudio.procmaze.script;

import fr.rphstudio.ecs.component.common.Position;
import fr.rphstudio.ecs.component.physic.Physic2D;
import fr.rphstudio.ecs.core.utils.CoreUtils;
import fr.rphstudio.ecs.core.interf.IComponent;
import fr.rphstudio.ecs.core.interf.IScript;

/**
 *
 * @author GRIGNON FAMILY
 */
public class SetPhyToPosition implements IComponent, IScript
{
    //================================================
    // PRIVATE PROPERTIES
    //================================================
    //----------------------------
    // Personal properties
    //----------------------------
    private final long       id;
    private final String     name;
    
    private Position         pos = null;
    private Physic2D         phy = null;
    private float            dX  = 0;
    private float            dY  = 0;
    
    
    //================================================
    // CONSTRUCTOR
    //================================================
    public SetPhyToPosition(Position pos01, Physic2D phy01, float dx, float dy)
    {
        this(pos01, phy01, dx, dy, "ScriptMovePhysicToPosition");
    }
    public SetPhyToPosition(Position pos01, Physic2D phy01, float dx, float dy, String nam)
    {
        // Store name
        this.name = nam;
        // Get unique ID
        this.id = CoreUtils.getNewID();
        // Common process
        this.pos = pos01;
        this.phy = phy01;
        this.dX  = dx;
        this.dY  = dy;
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
        if(this.pos != null && this.phy != null)
        {
            this.phy.setXPosition( this.pos.getXPosition() + this.dX );
            this.phy.setYPosition( this.pos.getYPosition() + this.dY );
        }
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
