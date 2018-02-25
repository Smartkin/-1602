import java.util.Scanner;
//Main class
public class Program {
    //Program's entry poin
    public static void main(String[] args)
    {
        boolean compute = true; //Used to see whether we compute the area of the triangle
        Point3D[] points = new Point3D[]{new Point3D(),new Point3D(),new Point3D()}; //Init an array of Point3Ds
        Scanner scanner = new Scanner(System.in); //Using scanner to read values of type double
        //Loop to allow the user enter 3 points
        for(int i=0;i<points.length;i++)
        {
            System.out.format("Enter point %d: ", i+1);
            //Using Double.parseDouble() method in order to accept int values as well
            points[i].setX(Double.parseDouble(scanner.next()));
            points[i].setY(Double.parseDouble(scanner.next()));
            points[i].setZ(Double.parseDouble(scanner.next()));
        }
        //Loop for checking whether some points are at the same position
        for(int i=0;i<points.length;i++)
        {
            for (int j=0;j<points.length;j++)
            {
                if (i != j && points[i].equals(points[j]))
                {
                    //If any points are at the same position cancel any computations, output a message and quit the loop
                    System.out.print("Two or all three points are at the same position! No area was computed\n");
                    compute = false;
                    break;
                }
            }
            if (!compute) break;
        }
        //Check whether to compute the area or not
        if (compute)
        {
            //Compute and output
            double area = computeArea(points[0], points[1], points[2]);
            System.out.printf("The triangle's area: %f\n",area);
        }
        //Close the scanner to avoid memory leaking
        scanner.close();
    }

    //Compuetes triangles area using Heron's formula
    public static double computeArea(Point3D p1,Point3D p2,Point3D p3)
    {
        double area;
        double a = p1.distanceTo(p2);
        double b = p2.distanceTo(p3);
        double c  = p3.distanceTo(p1);
        double s = (a+b+c)/2;
        area = Math.sqrt(s*(s-a)*(s-b)*(s-c));
        return area;
    }
}