import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;


public class MyPanel extends JPanel {

    MyPanel() {
        this.setPreferredSize(new Dimension(500,500));
    }

    @Override
    public void paint(Graphics g) {

        Graphics2D g2D = (Graphics2D) g;

        g2D.setPaint(Color.blue);
        //g2D.setStroke(new BasicStroke(5));
        //g2D.drawLine(0, 0, 500, 500);

        //g2D.setPaint(Color.pink);
        //g2D.drawRect(0, 0, 100, 200);
        //g2D.fillRect(0, 0, 100, 200);

        g2D.setPaint(Color.orange);
        //g2D.drawOval(0, 0, 100, 100);
        //g2D.fillOval(0, 0, 100, 100);

        g2D.setPaint(Color.red);
        //g2D.drawArc(0, 0, 100, 100, 0, 180);
        g2D.fillArc(0, 0, 100, 100, 0, 180);
        g2D.setPaint(Color.white);
        g2D.fillArc(0, 0, 100, 100, 180, 180);

        int[] xPoints = {150, 250, 350};
        int[] yPoints = {300, 150, 300};
        g2D.setPaint(Color.yellow);
        g2D.fillPolygon(xPoints, yPoints, 3);
    }

}
