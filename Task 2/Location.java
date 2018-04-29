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

    /**
     * Finds whether two locations are in the same position
     * @param loc - Object that can be casted to Location class
     */
    @Override
    public boolean equals(Object loc)
    {
        Location loc_cast = ((Location) loc);
        return xCoord == loc_cast.xCoord && yCoord == loc_cast.yCoord;
    }

    /**
     * Used with hashmaps to get the hash of the location
     * Calculated using the position of the location
     */
    @Override
    public int hashCode()
    {
        int hash = 1;
        final int PRIME = 31;
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