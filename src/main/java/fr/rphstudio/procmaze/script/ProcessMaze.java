/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.rphstudio.procmaze.script;

import fr.rphstudio.ecs.core.utils.CoreUtils;
import fr.rphstudio.ecs.core.interf.IComponent;
import fr.rphstudio.ecs.core.interf.IScript;
import fr.rphstudio.procmaze.component.SpaceLevel;
import fr.rphstudio.procmaze.launcher.Common;
import fr.rphstudio.procmaze.launcher.MazeGenerator;
import fr.rphstudio.utils.rng.Prng;

/**
 *
 * @author GRIGNON FAMILY
 */
public class ProcessMaze implements IComponent, IScript
{
    
    //================================================
    // PRIVATE PROPERTIES
    //================================================
    //----------------------------
    // Personal properties
    //----------------------------
    private final long       id;
    private final String     name;
    
    private SpaceLevel       spaceLevel;
    private Prng             rng;
    private long             startTime;         
    private MazeGenerator    maze;        
    
    
    //================================================
    // CONSTRUCTOR
    //================================================
    public ProcessMaze(SpaceLevel sl, Prng rand)
    {
        // Store name
        this.name = "ScriptOpenCloseDoor";
        // Get unique ID
        this.id = CoreUtils.getNewID();
        // Common process
        this.spaceLevel = sl;
        this.rng        = rand;
        // Create maze generator
        this.maze = new MazeGenerator(this.spaceLevel.getWidth(), this.spaceLevel.getHeight(), this.rng);
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
        long curTime = System.currentTimeMillis();
        if( curTime > this.startTime+Common.MAZE_GENERATION_PERIOD )
        {
            // Store the current time for next period
            this.startTime = curTime;
            // generate new maze
            this.maze.init();
            this.maze.generate();
            // Check the change of state for each door
            for(int x=0;x<this.spaceLevel.getWidth();x++)
            {
                for(int y=0;y<this.spaceLevel.getHeight();y++)
                {
                    if(this.maze.isDoorOpen(x,y) )
                    {
                        this.spaceLevel.openDoors(x,y);
                    }
                    else
                    {
                        this.spaceLevel.closeDoors(x,y);
                    }
                }
            }
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
