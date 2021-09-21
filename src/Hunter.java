import javax.swing.*;
import java.awt.*;

public class Hunter extends JComponent
{
    private static final int SIZE_X = 50;
    private static final int SIZE_Y = 50;
    private Vector2D myPosition;
    private static final int speedInBothAxex = 10;

    public Hunter()
    {
        myPosition = new Vector2D((GameFrame.SIZE_X-18)/2 - SIZE_X/2,
                (GameFrame.SIZE_Y-45)/2 - SIZE_Y/2);
        setSize(getPreferredSize());
        setLocation(myPosition.x, myPosition.y);
    }

    @Override
    public Dimension getPreferredSize()
    {
        return new Dimension(SIZE_X, SIZE_Y);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g.setColor(Color.blue);
        g2.drawRect(0, 0, SIZE_X, SIZE_Y);
        g.fillRect(0, 0, SIZE_X, SIZE_Y);
    }

    public Vector2D getPosition()
    {
        return myPosition;
    }
    void moveWithInput(int xAxisMovement, int yAxisMovement)
    {
        int newX = myPosition.x + xAxisMovement * speedInBothAxex;
        int newY = myPosition.y + yAxisMovement * speedInBothAxex;
        myPosition.x = newX;
        myPosition.y = newY;
        setLocation(newX, newY);
        repaint();
    }
}
