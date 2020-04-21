/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.rphstudio.procmaze.script;

import fr.rphstudio.ecs.component.common.Position;
import fr.rphstudio.ecs.component.control.KeyboardHandler;
import fr.rphstudio.ecs.component.physic.Physic2D;
import fr.rphstudio.ecs.core.utils.CoreUtils;
import fr.rphstudio.ecs.core.interf.IComponent;
import fr.rphstudio.ecs.core.interf.IScript;

/**
 *
 * @author GRIGNON FAMILY
 */
public class MovePhyWithKeys implements IComponent, IScript
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
    private KeyboardHandler  key = null;
    
    
    //================================================
    // CONSTRUCTOR
    //================================================
    public MovePhyWithKeys(Position pos01, Physic2D phy01, KeyboardHandler key01)
    {
        this(pos01, phy01, key01, "ScriptMovePhysicToPosition");
    }
    public MovePhyWithKeys(Position pos01, Physic2D phy01, KeyboardHandler key01, String nam)
    {
        // Store name
        this.name = nam;
        // Get unique ID
        this.id = CoreUtils.getNewID();
        // Common process
        this.pos = pos01;
        this.phy = phy01;
        this.key = key01;
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
        if(this.pos != null && this.phy != null && this.key != null)
        {
            float force = 0.50f;
            // Apply force to physic
            if(this.key.isKeyActive("UP"))
            {
                this.phy.setForce(0, -force);
            }
            if(this.key.isKeyActive("DOWN"))
            {
                this.phy.setForce(0, force);
            }
            if(this.key.isKeyActive("LEFT"))
            {
                this.phy.setForce(-force,0);
            }
            if(this.key.isKeyActive("RIGHT"))
            {
                this.phy.setForce(force,0);
            }
            // Update position from physics
            this.pos.setXPosition( (float)this.phy.getXPosition() );
            this.pos.setYPosition( (float)this.phy.getYPosition() );
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
