public class Point3D {
    private double x,y,z;
    /**
     * Default constructor
     */
    Point3D()
    {
        this(0,0,0);
    }
    /**
     * Initialize constructor
     */
    Point3D(double x,double y,double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * @return the x
     */
    public double getX() {
        return x;
    }

    /**
     * @return the y
     */
    public double getY() {
        return y;
    }

    /**
     * @return the z
     */
    public double getZ() {
        return z;
    }

    /**
     * @param x the x to set
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * @param y the y to set
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * @param z the z to set
     */
    public void setZ(double z) {
        this.z = z;
    }
    /**
     * Check whether 2 given points are at the same position
     * @param p Point3D to compare with
     */
    public boolean equals(Point3D p)
    {
        return this.x == p.x && this.y == p.y && this.z == p.z;
    }
    /**
     * Compute the distance between 2 points
     * @param p Point3D to compute distance to
     */
    public double distanceTo(Point3D p)
    {
        return Math.sqrt(Math.pow(Math.abs(this.x-p.x),2)+Math.pow(Math.abs(this.y-p.y),2)+Math.pow(Math.abs(this.z-p.z),2));
    }
}