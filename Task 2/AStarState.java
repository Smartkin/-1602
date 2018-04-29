import java.util.HashMap;
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
     * Use <code>open_waypoints and closed_waypoints</code> as hash maps
     */
    private HashMap<Location,Waypoint> open_waypoints;
    /**
     * Use <code>open_waypoints and closed_waypoints</code> as hash maps
     */
    private HashMap<Location,Waypoint> closed_waypoints;


    /**
     * Initialize a new state object for the A* pathfinding algorithm to use.
     **/
    public AStarState(Map2D map)
    {
        if (map == null)
            throw new NullPointerException("map cannot be null");

        this.map = map;

        open_waypoints = new HashMap<>();
        closed_waypoints = new HashMap<>();
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
        Waypoint[] points = new Waypoint[]{};
        points = open_waypoints.values().toArray(points);
        Waypoint min_point = points[0];
        //Start iterating through the list
        for (int i=1;i<points.length;i++)
        {
            Waypoint point = points[i];
            if (min_point.getTotalCost() > point.getTotalCost()) min_point = point;
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
        //Compare locations to see if received waypoint is in the list
        if (open_waypoints.containsKey(newWP.getLocation())) {
            //Next check if its cost is less than the existing one
            if (newWP.getPreviousCost() < open_waypoints.get(newWP.getLocation()).getPreviousCost()) {
                open_waypoints.replace(newWP.getLocation(), newWP);
                //Return true that the point was added
                return true;
            } else {
                //Return false if the point was not satisfying the requirments
                return false;
            }
        }
        //Add the point to the list if it was not found in the list
        open_waypoints.put(newWP.getLocation(),newWP);
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
        //Iterate through the list
        Waypoint point = open_waypoints.get(loc);
        closed_waypoints.put(point.getLocation(),point);
        open_waypoints.remove(point.getLocation());
    }

    /**
     * Returns true if the collection of closed waypoints contains a waypoint
     * for the specified location.
     **/
    public boolean isLocationClosed(Location loc)
    {
        Waypoint point = closed_waypoints.get(loc);
        return point != null;
    }
}
