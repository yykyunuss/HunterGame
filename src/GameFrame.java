import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame
{
    public static final int SIZE_X = 500 + 18;  // size of left and right bars of window has been excluded.
    public static final int SIZE_Y = 500 + 45;  // size of title bar of window has been excluded.
    private JPanel mainPanel;

    public GameFrame() throws HeadlessException
    {
        setTitle("Hunter Game");
        setPreferredSize(getPreferredSize());
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainPanel = new JPanel();
        mainPanel.setBackground(Color.GRAY);
        mainPanel.setVisible(true);
        mainPanel.setLayout(null);
        mainPanel.setPreferredSize(new Dimension(SIZE_X, SIZE_Y));
        setContentPane(mainPanel);

        new GameLogic(this);
    }

    @Override
    public Dimension getPreferredSize()
    {
        return new Dimension(SIZE_X, SIZE_Y);
    }

    public JPanel getMainPanel()
    {
        return mainPanel;
    }

}
