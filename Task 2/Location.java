/**
 * This class represents a specific location in a 2D map.  Coordinates are
 * integer values.
 **/
public class Location
{
    /** X coordinate of this location. **/
    public int xCoord;

    /** Y coordinate of this location. * */
    public int yCoord;


    /** Creates a new location with the specified integer coordinates. **/
    public Location(int x, int y)
    {
        xCoord = x;
        yCoord = y;
    }

    @Override
    public boolean equals(Object loc)
    {
        Location loc_cast = ((Location) loc);
        return xCoord == loc_cast.xCoord && yCoord == loc_cast.yCoord;
    }

    @Override
    public int hashCode()
    {
        int hash = 1;
        int PRIME = 31;
        hash += PRIME*hash+xCoord;
        hash += PRIME*hash+yCoord;
        return hash;
    }

    /** Creates a new location with coordinates (0, 0). **/
    public Location()
    {
        this(0, 0);
    }
}