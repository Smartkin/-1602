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
    private Waypoint open_waypoints;
    private Waypoint closed_waypoints;


    /**
     * Initialize a new state object for the A* pathfinding algorithm to use.
     **/
    public AStarState(Map2D map)
    {
        if (map == null)
        throw new NullPointerException("map cannot be null");

        this.map = map;
        open_waypoints = null;
        closed_waypoints = null;
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
        //Start iterating through the list
        Waypoint point = open_waypoints;
        if (point != null)
        {
            //Set first point as minimum
            Waypoint min_point = point;
            point = point.getPrevious();
            //Loop through the rest if they exist
            while (point != null)
            {
                if (min_point.getTotalCost() > point.getTotalCost()) min_point = point;
                point = point.getPrevious();
            }
            return min_point;
        }
        return null;
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
        Waypoint point = open_waypoints;
        //Start looping through the list
        while(point != null)
        {
            Location loc = point.getLocation();
            //Compare locations to see if received waypoint is in the list
            if (loc.equals(newWP.getLocation()))
            {
                //Next check if its cost is less than the existing one
                if (newWP.getPreviousCost() < point.getPreviousCost())
                {
                    //Replace the point, done by remaking the list the whole list (switch to normal list instead?)
                    Waypoint temp = point;
                    point = open_waypoints;
                    open_waypoints = null;
                    //Start reiterating through the list
                    while(point != null)
                    {
                        //Relink the points back unless the one being replaced is encountered
                        if (point.getPreviousCost() != temp.getPreviousCost())
                            open_waypoints = new Waypoint(point.getLocation(),open_waypoints);
                    }
                    //Return true that the point was added
                    return true;
                }
                else
                {
                    //Return false if the point was not satisfying the requirments
                    return false;
                }
            }
            point = point.getPrevious();
        }
        //Add the point to the list if it was not found in the list
        open_waypoints = new Waypoint(newWP.getLocation(),open_waypoints);
        return true;
    }


    /** Returns the current number of open waypoints. **/
    public int numOpenWaypoints()
    {
        int num = 0;
        Waypoint point = open_waypoints;
        //Start iterating through the list
        while(point != null)
        {
            num++;
            point = point.getPrevious();
        }
        return num;
    }


    /**
     * This method moves the waypoint at the specified location from the
     * open list to the closed list.
     **/
    public void closeWaypoint(Location loc)
    {
        Waypoint point = open_waypoints;
        //Start iterating through the list
        while(point != null)
        {
            //If needed point found start replacing
            if (loc.equals(point.getLocation()))
            {
                //Add the point to closed_waypoints list
                closed_waypoints = new Waypoint(point.getLocation(),closed_waypoints);
                //Replace the point, done by remaking the list the whole list (switch to normal list instead?)
                point = open_waypoints;
                open_waypoints = null;
                while (point != null)
                {
                    //Relink the points back unless the removed one is encountered
                    if (!loc.equals(point.getLocation()))
                    {
                        open_waypoints = new Waypoint(point.getLocation(),open_waypoints);
                    }
                    point = point.getPrevious();
                }
                break;
            }
            point = point.getPrevious();
        }
    }

    /**
     * Returns true if the collection of closed waypoints contains a waypoint
     * for the specified location.
     **/
    public boolean isLocationClosed(Location loc)
    {
        Waypoint point = closed_waypoints;
        //Iterate through the linked list
        while(point != null)
        {
            if (loc.equals(point.getLocation()))
                return true;
            point = point.getPrevious();
        }
        return false;
    }
}
