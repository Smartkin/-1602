import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;

public class FractalExplorer {

    /**
     * Program's entry point
     * @param args - Array of strings (Unused)
     */
    public static void main(String[] args)
    {
        //Create the explorer, initialize GUI and draw the fractal
        FractalExplorer expl = new FractalExplorer(800);
        expl.createAndShowGUI();
        expl.drawFractal();
    }


    private int dispSize;
    private JImageDisplay display;
    private FractalGenerator generator;
    private Rectangle2D.Double plane_range = new Rectangle2D.Double(0,0,0,0);

    FractalExplorer(int disp_size)
    {
        dispSize = disp_size;
        display = new JImageDisplay(disp_size,disp_size);
        generator = new Mandelbrot();
        generator.getInitialRange(plane_range);
    }

    /**
     * GUI Initialization
     */
    public void createAndShowGUI()
    {
        //Create the main window
        JFrame frame = new JFrame("Fractal Explorer");

        //Add our fractal display
        display.addMouseListener(new mouse_listener());
        frame.add(display,BorderLayout.CENTER);

        //Add the button
        JButton bt = new JButton("Reset");
        bt.addActionListener(new act_listener());
        frame.add(bt,BorderLayout.SOUTH);

        //Set the default close action to "exit"
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Finallize the window
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
    }

    /**
     * Fractal drawing
     */
    private void drawFractal()
    {
        for (int i=0;i<dispSize;i++)
        {
            for (int j=0;j<dispSize;j++)
            {
                //Map display coordinates to fractal coordinates
                double xCoord = FractalGenerator.getCoord(plane_range.x,plane_range.x + plane_range.width,dispSize,i);
                double yCoord = FractalGenerator.getCoord(plane_range.y,plane_range.y + plane_range.height,dispSize,j);

                //Get the iterations amount
                int iters = generator.numIterations(xCoord,yCoord);

                //Default pixel color to black
                int px_color = 0;

                //Check if we are in the limit of our fractal
                if (iters != -1)
                {
                    //Calcualte the pixel color
                    float hue = 0.7f + (float) iters / 200f;
                    px_color = Color.HSBtoRGB(hue,1f,1f);
                }
                //Draw the pixel
                display.drawPixel(i,j,px_color);
            }
        }
        //Display what we drew on screen
        display.repaint();
    }

    /**
     * Inner class for listening button clicks
     */
    private class act_listener implements ActionListener {
        /**
         * Any object using this listener will reset the fractal to its 1.0 scale
         * any time an action on it is performed (ex. Button was clicked)
         * @param e - occured event
         */
        @Override
        public void actionPerformed(ActionEvent e)
        {
            generator.getInitialRange(plane_range);
            //generator.recenterAndZoomRange(plane_range,plane_range.x/2,plane_range.y/2,1.0);
            display.clearImage();
            drawFractal();
        }
    }

    /**
     * Inner class for listening mouse click events
     */
    private class mouse_listener extends MouseAdapter {

        /**
         * Any instance of this listener zooms into fractal to where the mouse was positioned on the image
         * after a mouse click was performed
         * @param e - mouse event that occured (basically mouse object)
         */
        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);
            //Get mouse's x and y position
            int mouse_x = e.getX();
            int mouse_y = e.getY();

            //Zoom scaling
            final double SCALE = 0.5;

            //Map mouse position to fractal coordinates
            double xCoord = FractalGenerator.getCoord(plane_range.x,plane_range.x+plane_range.width,dispSize,mouse_x);
            double yCoord = FractalGenerator.getCoord(plane_range.y,plane_range.y+plane_range.height,dispSize,mouse_y);

            //Finally zoom in, clear the image and draw the fractal
            generator.recenterAndZoomRange(plane_range,xCoord,yCoord,SCALE);
            display.clearImage();
            drawFractal();
        }

    }
}
