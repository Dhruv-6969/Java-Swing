import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class Frames{

    public static void main(String[] args) {

        //JFrame is a GUI window to add components to

        JFrame frame = new JFrame(); //Creates a new frame
        frame.setTitle("JFrame Title"); //sets title of frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //exit out of the application
        frame.setResizable(false); //prevents the frame from resizing
        frame.setSize(420,420); //Sets the x and y dimensions of a frame
        frame.setVisible(true); //Makes Frame visible

        ImageIcon image = new ImageIcon("pikachu.png"); // create an image icon
        frame.setIconImage(image.getImage()); //change icon of frame
        frame.getContentPane().setBackground(new Color(0,0,0)); //sets background color of the frame
    }
}