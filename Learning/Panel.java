import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Panel{

    public static void main(String[] args){

        //JPanel = a GUI component that functions a container to hold other components

        JPanel redpanel = new JPanel();
        redpanel.setBackground(new Color(255, 0, 0));
        redpanel.setBounds(0, 0, 250, 250);

        JPanel bluepanel = new JPanel();
        bluepanel.setBackground(new Color(0, 0, 255));
        bluepanel.setBounds(250, 0, 250, 250);

        JPanel greenpanel = new JPanel();
        greenpanel.setBackground(new Color(0, 255, 0));
        greenpanel.setBounds(0, 250, 500, 250);

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setSize(750,750);
        frame.setVisible(true);
        frame.add(redpanel); //connects panel with the frame
        frame.add(bluepanel);
        frame.add(greenpanel);
    }
}