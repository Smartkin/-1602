import java.util.List;
import java.util.ArrayList;
/**
 * This class stores the basic state necessary for the A* algorithm to compute a
 * path across a map.  This state includes a collection of "open waypoints" and
 * another collection of "closed waypoints."  In addition, this class provides
 * the basic operations that the A* pathfinding algorithm needs to perform its
 * processing.
 **/
public class AStarState
{
    /** This is a reference to the map that the A* algorithm is navigating. **/
    private Map2D map;

    /**
     * Use <code>open_waypoints and closed_waypoints</code> as linked lists
     */
    private List<Waypoint> open_waypoints;
    private List<Waypoint> closed_waypoints;


    /**
     * Initialize a new state object for the A* pathfinding algorithm to use.
     **/
    public AStarState(Map2D map)
    {
        if (map == null)
        throw new NullPointerException("map cannot be null");

        this.map = map;
        open_waypoints = new ArrayList<Waypoint>();
        closed_waypoints = new ArrayList<Waypoint>();
    }

    /** Returns the map that the A* pathfinder is navigating. **/
    public Map2D getMap()
    {
        return map;
    }

    /**
     * This method scans through all open waypoints, and returns the waypoint
     * with the minimum total cost.  If there are no open waypoints, this method
     * returns <code>null</code>.
     **/
    public Waypoint getMinOpenWaypoint()
    {
        Waypoint min_point = open_waypoints.get(0);
        //Start iterating through the list
        for (int i=1;i<open_waypoints.size();i++)
        {
            Waypoint point = open_waypoints.get(i);
            if (min_point.getTotalCost() > point.getTotalCost()) min_point = point;
            point = point.getPrevious();
        }
        return min_point;
    }

    /**
     * This method adds a waypoint to (or potentially updates a waypoint already
     * in) the "open waypoints" collection.  If there is not already an open
     * waypoint at the new waypoint's location then the new waypoint is simply
     * added to the collection.  However, if there is already a waypoint at the
     * new waypoint's location, the new waypoint replaces the old one <em>only
     * if</em> the new waypoint's "previous cost" value is less than the current
     * waypoint's "previous cost" value.
     **/
    public boolean addOpenWaypoint(Waypoint newWP)
    {
        for (int i=0;i<open_waypoints.size();i++)
        {
            Waypoint point = open_waypoints.get(i);
            Location loc = point.getLocation();
            //Compare locations to see if received waypoint is in the list
            if (loc.equals(newWP.getLocation()))
            {
                //Next check if its cost is less than the existing one
                if (newWP.getPreviousCost() < point.getPreviousCost())
                {
                    open_waypoints.remove(point);
                    open_waypoints.add(newWP);
                    //Return true that the point was added
                    return true;
                }
                else
                {
                    //Return false if the point was not satisfying the requirments
                    return false;
                }
            }
        }
        //Add the point to the list if it was not found in the list
        open_waypoints.add(newWP);
        return true;
    }


    /** Returns the current number of open waypoints. **/
    public int numOpenWaypoints()
    {
        return open_waypoints.size();
    }


    /**
     * This method moves the waypoint at the specified location from the
     * open list to the closed list.
     **/
    public void closeWaypoint(Location loc)
    {
        for (int i=0;i<open_waypoints.size();i++)
        {
            Waypoint point = open_waypoints.get(i);
            //If needed point found start replacing
            if (loc.equals(point.getLocation()))
            {
                closed_waypoints.add(point);
                open_waypoints.remove(point);
                break;
            }
        }
    }

    /**
     * Returns true if the collection of closed waypoints contains a waypoint
     * for the specified location.
     **/
    public boolean isLocationClosed(Location loc)
    {
        for (int i=0;i<closed_waypoints.size();i++)
        {
            Waypoint point = closed_waypoints.get(i);
            if (loc.equals(point.getLocation()))
                return true;
        }
        return false;
    }
}
