/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.rphstudio.procmaze.component;

import fr.rphstudio.ecs.core.utils.CoreUtils;
import fr.rphstudio.ecs.core.interf.IComponent;
import fr.rphstudio.procmaze.launcher.Common;


/**
 *
 * @author GRIGNON FAMILY
 */
public class SpaceDoor implements IComponent
{
    //================================================
    // PRIVATE PROPERTIES
    //================================================
    private final long     id;
    private final String   name;
    
    private long           startTime;
    private float          openingSize;
    private boolean        hasToBeClosed;
    private boolean        isHorizontal;
    private boolean        isOpeningPositive;
    
    
    //================================================
    // CONSTRUCTOR
    //================================================
    public SpaceDoor(float openSize, boolean isH, boolean isPos)
    {
        this(openSize, isH, isPos, "Class_SpaceDoor");
    }    
    public SpaceDoor(float openSize, boolean isH, boolean isPos, String nam)
    {
        // Get unique ID
        this.id = CoreUtils.getNewID();
        // Store name
        this.name = nam;
        // By default the door is locked
        this.startTime  = 0;
        this.hasToBeClosed = true;
        // horizontal/vertical and opening Positive/negative
        this.isHorizontal      = isH;
        this.isOpeningPositive = isPos;
        // Opening size (max real distance between full-open and full-closed positions)
        this.openingSize = openSize;
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
    
    
    //================================================
    // SETTERS
    //================================================
    public void open()
    {
        // Set start time 
        this.startTime = System.currentTimeMillis();
        // Set the way of moving (opening or closing)
        this.hasToBeClosed = false;
    }
    public void close()
    {
        // Set start time 
        this.startTime = System.currentTimeMillis();
        // Set the way of moving (opening or closing)
        this.hasToBeClosed = true;
    }
    public void toggle()
    {
        // Set start time 
        this.startTime = System.currentTimeMillis();
        // change way of moving
        this.hasToBeClosed = !this.hasToBeClosed;
    }
    
    
    //================================================
    // GETTERS
    //================================================
    public float getOpeningDistance()
    {
        // get current moving duration
        long duration;
        float distance;
        duration = System.currentTimeMillis()-this.startTime;
        duration = Math.min(Common.DOOR_OPENING_TIME_MS,Math.max(0,duration));
        // Get opposite value if door is closing
        if(this.hasToBeClosed)
        {
            duration = Common.DOOR_OPENING_TIME_MS - duration; 
        }
        // normalize distance
        distance = duration/((float)Common.DOOR_OPENING_TIME_MS);
        // positive/negative
        if(!this.isOpeningPositive)
        {
            distance = -distance;
        }
        // return result
        return this.openingSize * distance;
    }
    
    public boolean isHorizontal()
    {
        return this.isHorizontal;
    }
    
    public boolean isOpen()
    {
        return !this.hasToBeClosed;
    }
    
    
    //================================================
    // END OF CLASS
    //================================================
}
