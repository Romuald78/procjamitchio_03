/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.rphstudio.procmaze.component;

import fr.rphstudio.ecs.core.utils.CoreUtils;
import fr.rphstudio.ecs.core.interf.IComponent;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author GRIGNON FAMILY
 */
public class SpaceLevel implements IComponent
{
    //================================================
    // PRIVATE CONSTANT
    //================================================
    

    //================================================
    // PRIVATE PROPERTIES
    //================================================
    private final long     id;
    private final String   name;
    
    private List<SpaceDoor>  grid[][];
    
    
    //================================================
    // CONSTRUCTOR
    //================================================
    public SpaceLevel(int w, int h)
    {
        this(w, h, "Class_SpaceLevel");
    }    
    public SpaceLevel(int w, int h, String nam)
    {
        // Get unique ID
        this.id = CoreUtils.getNewID();
        // Store name
        this.name = nam;
        // Create grid
        this.grid = new ArrayList[w][h];
        for(int x=0;x<w;x++)
        {
            for(int y=0;y<h;y++)
            {
                this.grid[x][y] = new ArrayList<SpaceDoor>();
            }
        }
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
    public void addDoor(int x, int y, SpaceDoor sd)
    {
        this.grid[x][y].add(sd);
    }
    
    public void openDoors(int x, int y)
    {
        if(!this.isOpenDoor(x, y))
        {
            for(int i=0;i<this.grid[x][y].size();i++)
            {
                this.grid[x][y].get(i).open();
            }
        }
    }
    
    public void closeDoors(int x, int y)
    {
        if(this.isOpenDoor(x, y))
        {
            for(int i=0;i<this.grid[x][y].size();i++)
            {
                this.grid[x][y].get(i).close();
            }
        }
    }
    
    
    //================================================
    // GETTERS
    //================================================
    public int getWidth()
    {
        return this.grid.length;
    }
    
    public int getHeight()
    {
        return this.grid[0].length;
    }
    
    public boolean isOpenDoor(int x, int y)
    {
        boolean res = true;
        if(this.grid[x][y].size() != 0)
        {
            res = this.grid[x][y].get(0).isOpen();
        }
        return res;
    }
    
    
    //================================================
    // END OF CLASS
    //================================================
}
