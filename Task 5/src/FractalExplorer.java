import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.io.IOException;

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
    private int remaining_rows;
    private JImageDisplay display;
    private JComboBox<FractalGenerator> box = new JComboBox<>();
    private JButton bt = new JButton("Reset Fractal");
    private JButton bt2 = new JButton("Save Fractal");
    private FractalGenerator generator;
    private JFrame frame = new JFrame("Fractal Explorer");
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

        //Add our fractal display
        display.addMouseListener(new mouse_listener());
        frame.add(display,BorderLayout.CENTER);

        //Buttons panel
        JPanel bt_panel = new JPanel();

        //Add the reset button

        bt.setActionCommand("Reset");
        bt.addActionListener(new act_listener());
        bt_panel.add(bt,BorderLayout.CENTER);

        //Add the save button

        bt2.setActionCommand("Save");
        bt2.addActionListener(new act_listener());
        bt_panel.add(bt2,BorderLayout.CENTER);

        frame.add(bt_panel,BorderLayout.SOUTH);

        //Add combobox with label
        JPanel box_panel = new JPanel();
        JLabel box_label = new JLabel("Fractal:");
        box.addItem(new Mandelbrot());
        box.addItem(new Tricorn());
        box.addItem(new BurningShip());
        box.addActionListener(new act_listener());
        box_panel.add(box_label,BorderLayout.CENTER);
        box_panel.add(box,BorderLayout.CENTER);
        frame.add(box_panel,BorderLayout.NORTH);

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
        enableUI(false);
        remaining_rows = dispSize;
        for (int i=0;i<dispSize;i++)
        {
            FractalWorker worker = new FractalWorker(i);
            worker.execute();
        }
        //Display what we drew on screen
        //display.repaint();
    }

    private void enableUI(boolean val){
        box.setEnabled(val);
        bt.setEnabled(val);
        bt2.setEnabled(val);
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
            //Getting the action command
            String cmd = e.getActionCommand();

            if (e.getSource() == box) { //Check for source being combo_box
                generator = (FractalGenerator) box.getSelectedItem();
                generator.getInitialRange(plane_range);
                display.clearImage();
                drawFractal();
            }
            else if (cmd.equals("Reset")) { //Check for source being reset button
                generator.getInitialRange(plane_range);
                display.clearImage();
                drawFractal();
            }
            else if (cmd.equals("Save")) { //Check for source being save button
                JFileChooser chooser = new JFileChooser();
                FileFilter filter = new FileNameExtensionFilter("PNG Files","png");
                chooser.setFileFilter(filter);
                chooser.setAcceptAllFileFilterUsed(false);
                if (chooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) { //Attempt to save file if user chose to save
                    try { //Exception handling if anything went wrong during writing
                        ImageIO.write(display.getImage(),"png",chooser.getSelectedFile());
                    }
                    catch (IOException ex) {
                        JOptionPane.showMessageDialog(frame,ex.getMessage(),"Couldn't save the image",JOptionPane.ERROR_MESSAGE);
                    }
                }
                else {
                    return;
                }

            }
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
            if (remaining_rows != 0) return;

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

    /**
     * Allows for multi-threading drawing of fractals
     */
    private class FractalWorker extends SwingWorker<Object,Object> {
        private int y_coord;
        private int[] rgb_values;

        /**
         * Constructs the worker
         * @param y - row to draw on
         */
        FractalWorker(int y){
            y_coord = y;
        }

        @Override
        protected Object doInBackground() {
            rgb_values = new int[dispSize];
            for (int i=0;i<dispSize;i++)
            {
                //Map display coordinates to fractal coordinates
                double xCoord = FractalGenerator.getCoord(plane_range.x,plane_range.x + plane_range.width,dispSize,i);
                double yCoord = FractalGenerator.getCoord(plane_range.y,plane_range.y + plane_range.height,dispSize,y_coord);

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
                //Save the pixel color into an array
                rgb_values[i] = px_color;
            }
            return null;
        }

        @Override
        protected void done() {
            super.done();
            //Draw the row of pixels
            for(int i=0;i<dispSize;i++){
                display.drawPixel(i,y_coord,rgb_values[i]);
            }
            //Repaint the screen
            display.repaint(0,0,y_coord,dispSize,1);

            if (--remaining_rows == 0){
                enableUI(true);
            }
        }
    }
}
