import javax.swing.JComponent;
import java.awt.image.BufferedImage;
import java.awt.Graphics;

public class JImageDisplay extends JComponent {
    private BufferedImage image;

    /**
     * Constructor of our display
     * @param imWid - width of the display
     * @param imHei - height of the display
     */
    JImageDisplay(int imWid,int imHei)
    {
        image = new BufferedImage(imWid,imHei,BufferedImage.TYPE_INT_RGB);
        super.setPreferredSize(new java.awt.Dimension(imWid,imHei));
    }

    /**
     *
     * @param g - Drawing the display on screen
     */
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(image,0,0,image.getWidth(),image.getHeight(),null);
    }

    /**
     * Set the image to black color
     */
    public void clearImage()
    {
        //Initialize the array of 0s
        int arr_len;
        if (image.getHeight() > image.getWidth()) arr_len = image.getHeight();
        else                                      arr_len = image.getWidth();
        int[] rgb = new int[arr_len];
        //Set the whole image to black color
        image.setRGB(0,0,image.getWidth()-1,image.getHeight()-1,rgb,0,0);
    }

    /**
     * Draws a pixel with a given color to coordinates x,y
     * @param x - x position of pixel
     * @param y - y position of pixel
     * @param rgbColor - color of pixel in RGB color set
     */
    public void drawPixel(int x,int y, int rgbColor)
    {
        image.setRGB(x,y,rgbColor);
    }

    public BufferedImage getImage() {
        return image;
    }
}
