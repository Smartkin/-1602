import java.util.Scanner;

public class Program {
    public static void main(String[] args)
    {
        boolean compute = true;
        Point3D[] points = new Point3D[]{new Point3D(),new Point3D(),new Point3D()};
        Scanner scanner = new Scanner(System.in);
        for(int i=0;i<points.length;i++)
        {
            System.out.format("Enter point %d: ", i+1);
            points[i].setX(Double.parseDouble(scanner.next()));
            points[i].setY(Double.parseDouble(scanner.next()));
            points[i].setZ(Double.parseDouble(scanner.next()));
        }
        for(int i=0;i<points.length;i++)
        {
            for (int j=0;j<points.length;j++)
            {
                if (i != j && points[i].equals(points[j]))
                {
                    System.out.print("Two or all three points are at the same position! No area was computed");
                    compute = false;
                    break;
                }
            }
            if (!compute) break;
        }
        if (compute)
        {
            double area = computeArea(points[0], points[1], points[2]);
            System.out.printf("The triangle's area: %f",area);
        }
        scanner.close();
    }

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