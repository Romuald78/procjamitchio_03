/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.rphstudio.procmaze.launcher;

import fr.rphstudio.utils.rng.Prng;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author GRIGNON FAMILY
 */
public class MazeGenerator {

    private class Door
    {
        public Door(int a, int b)
        {
            this.x = a;
            this.y = b;
        }
        public int x;
        public int y;
    }
    
    private static final int WALL = -1;
    private static final int DOOR_CLOSED = -2;
    private static final int DOOR_OPEN   = -3;
    
    private int  w;
    private int  h;
    private int  maze[][];
    private Prng rng;
    
    private boolean isRealDoor(Door door)
    {
        if( (this.maze[door.x-1][door.y]==this.maze[door.x+1][door.y]) && (this.maze[door.x][door.y-1]==this.maze[door.x][door.y+1]) )
        {
            return false;
        }
        return true;
    }
    
    private void merge(Door door, int v)
    {
        int x = door.x;
        int y = door.y;
        // Set cell value
        this.maze[x][y] = v;
        // merge next cells if possible
        if( (this.maze[x+1][y] != v) && (this.maze[x+1][y] > 0) )
        {
            this.merge( new Door(x+1,y), v );
        }
        if( (this.maze[x-1][y] != v) && (this.maze[x-1][y] > 0) )
        {
            this.merge( new Door(x-1,y), v );
        }
        if( (this.maze[x][y+1] != v) && (this.maze[x][y+1] > 0) )
        {
            this.merge( new Door(x,y+1), v );
        }
        if( (this.maze[x][y-1] != v) && (this.maze[x][y-1] > 0) )
        {
            this.merge( new Door(x,y-1), v );
        }
    }
    
    private void open(Door door)
    {
        int value = WALL;
        // check if this is a horizontal or vertical door
        if( door.x%2==0 && door.y%2==1 )
        {
            // Horizontal
            value = this.maze[door.x-1][door.y];
        }
        else if( door.x%2==1 && door.y%2==0 )
        {
            // Vertical
            value = this.maze[door.x][door.y-1];
        }
        else
        {
            throw new Error("Bad door opening !");
        }
        // Check if the value is correct
        if(value <= 0)
        {
            throw new Error("Bad Cell value !");
        }
        // now we can open the door and merge areas
        this.merge(door, value);
    }
    
    public MazeGenerator(int ww, int hh, Prng rand)
    {
        // Check dimensions are correct
        if( (ww%2!=1) || (hh%2!=1) )
        {
            throw new Error("Bad Maze dimensions !");
        }
        if(rand == null)
        {
            throw new Error("Bad Prng object !");
        }
        this.w   = ww;
        this.h   = hh;
        this.rng = rand;
    }

    // Init maze
    public void init()
    {
        // Init buffer
        this.maze = new int[w][h];
        // Set different values for all cells
        int count = 1;
        for(int x=0;x<this.w;x++)
        {
            for(int y=0;y<this.h;y++)
            {
                // Set negative values for borders
                if( (x==0) || (x==this.w-1) || (y==0) || (y==this.h-1) || ((x%2==0) && (y%2==0)) )
                {
                    this.maze[x][y] = WALL;
                }
                else if(x%2 != y%2)
                {
                    this.maze[x][y] = DOOR_CLOSED;
                }
                else
                { 
                    this.maze[x][y] = count;
                    count++;
                }
            }    
        }
    }

    public boolean isFinished()
    {
        // Check if the maze has been generated completely
        int value = this.maze[1][1];
        for(int x=1;x<this.w-1;x+=2)
        {
            for(int y=1;y<this.h-1;y+=2)
            {
                if(this.maze[x][y] != value)
                {
                    return false;
                }
            }    
        }
        // Maze is finished
        return true;
    }
    
    public void generate()
    {
        // prepare list for doors
        List<Door> list = new ArrayList<Door>();
        for(int x=1;x<this.w-1;x++)
        {
            for(int y=1;y<this.h-1;y++)
            {
                if( x%2 != y%2 )
                {
                    list.add(new Door(x,y));
                }
            }    
        }
        // While all the doors have been checked
        while( (list.size()>0) && (!this.isFinished()) )
        {
            // take a random door
            float r   = (float)rng.random();
            int  idx  = (int)( r*list.size() );
            Door door = list.get(idx);
            // check this door is seprating two different areas
            if( this.isRealDoor(door) )
            {
                // open the door
                this.open(door);
            }
            // in all cases, remove this door as it is not a real one (it means it is more a wall than anything else now)
            list.remove(idx);
        }
        // Set open doors
        for(int x=1;x<this.w-1;x++)
        {
            for(int y=1;y<this.h-1;y++)
            {
                if( x%2 != y%2 )
                {
                    if( this.maze[x][y] > 0 )
                    {
                        // this is an open door
                        this.maze[x][y] = DOOR_OPEN;
                    }
                }
            }    
        }
    }

    public boolean isDoorOpen(int x, int y)
    {
        boolean res = false;
        // if outside map : 
        if(x>=0 && x<this.w && y>=0 && y<this.h)
        {
            res = (this.maze[x][y] == DOOR_OPEN);
        }
        return res;
    }
    
    // display debug
    public void display()
    {
        for(int y=0;y<this.h;y++)
        {
            String s = "";
            for(int x=0;x<this.w;x++)
            {
                int v = this.maze[x][y];
                if(v==WALL)
                {
                    s += "# ";
                }
                else if(v==DOOR_CLOSED)
                {
                    s += "x ";
                }
                else if(v==DOOR_OPEN)
                {
                    s += ". ";
                }
                else
                {
                    s += "  ";
                }
            }
            System.out.println(s);
        }
    }

    public static void main(String[] args)
    {
        Prng rng = new Prng();
        rng.setSeed(123456789);
        MazeGenerator maze = new MazeGenerator(19,9,rng);
        
        maze.init();
        System.out.println(maze.isFinished());
        maze.display();
        
        maze.generate();
        System.out.println(maze.isFinished());
        maze.display();
        
    }
    
}


