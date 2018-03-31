import java.awt.geom.Rectangle2D;

public class BurningShip extends FractalGenerator {

    /**Maximum amount of iterations until concluding that the point was set**/
    public static final int MAX_ITER = 2000;

    /**
     * Resets the given range to 1.0 scale of the fractal
     * @param range - fractal range to modify
     */
    @Override
    public void getInitialRange(Rectangle2D.Double range)
    {
        range.x = -2;
        range.y = -2.5;
        range.width = 4;
        range.height = 4;
    }

    /**
     *
     * @param x - x coordinate to check
     * @param y - y coordinate to check
     * @return returns number of iterations that was required to reach the x,y otherwise returns -1 reaching the limit
     */
    @Override
    public int numIterations(double x, double y)
    {
        double re = 0;
        double im = 0;
        for(int i=1;i<MAX_ITER;i++)
        {
            //Using the simple sum of 2 numbers squared formula
            //(a+b)^2 = a^2 + 2*a*b + b^2
            //(re-im)^2 = re^2 - 2*re*im + im^2
            double nextRe = re*re - im*im + x;
            double nextIm = Math.abs(2*re*im) + y;

            // If we reached the Mandelbrot's condition return the amount of iterations
            if ((im*im+re*re) > 4)
                return i;

            //Reassigning the real and imaginary parts for next iteration
            re = nextRe;
            im = nextIm;
        }
        return -1;
    }

    @Override
    public String toString() {
        return "Burning Ship";
    }
}

