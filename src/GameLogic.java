import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;
import java.util.List;
import java.util.Timer;

public class GameLogic implements KeyListener
{

    public static final float SPAWN_RATE_IN_SECONDS = 0.5f;
    private static final List<Hunt.Speed> SPEED_VALUES = Arrays.asList(Hunt.Speed.values());
    private ArrayList<Hunt> hunts;
    private Hunter hunter;
    private Random randomGenerator;
    private GameFrame gameFrame;
    private static int points;
    Timer timer;

    public GameLogic(GameFrame gameFrame)
    {
        hunter = new Hunter();
        randomGenerator = new Random();
        this.gameFrame = gameFrame;
        hunts = new ArrayList<>();
        timer = new Timer();
        points = 0;

        gameFrame.getMainPanel().requestFocus();
        gameFrame.getMainPanel().addKeyListener(this);

        gameFrame.getMainPanel().add(hunter);
        timer.schedule(new Spawner(), 0, (int)(SPAWN_RATE_IN_SECONDS * 1000));
    }

    public void removeHunt(Hunt huntToRemove, boolean isEaten)
    {
        gameFrame.getMainPanel().remove(huntToRemove);
        gameFrame.revalidate();
        gameFrame.repaint();

        if (isEaten)
        {
            points+=2;
        }
        else
        {
            points-=1;
        }

        if (points >= 20)
        {
            finishGame(true);
            timer.cancel();
        }
        else if (points<= -100)
        {
            finishGame(false);
            timer.cancel();
        }
    }

    void spawnNewHunt()
    {
        int startingX =  randomGenerator.ints(0, (GameFrame.SIZE_X + 1)).findFirst().getAsInt();
        int speedIndex = randomGenerator.ints(0,4).findFirst().getAsInt();

        Hunt newHunt = new Hunt(new Vector2D(startingX, 15), SPEED_VALUES.get(speedIndex), hunter, this);
        hunts.add(newHunt);
        gameFrame.getMainPanel().add(newHunt);
        gameFrame.revalidate();
        gameFrame.repaint();
        newHunt.moveEverySecond();
        newHunt.checkCollisionEveryFrame();
    }

    void finishGame(boolean isWon)
    {
        Vector2D middlePoint = new Vector2D((GameFrame.SIZE_X-18)/2 - 100,
                (GameFrame.SIZE_Y-45)/2- 100);

        String finishMessage = isWon ? "You Won!!!!" : "You lost :(";
        JLabel finishLabel = new JLabel(finishMessage, JLabel.CENTER);
        finishLabel.setPreferredSize(new Dimension(200,200));
        finishLabel.setFont(new Font("serif", Font.BOLD, 25));
        gameFrame.getMainPanel().removeAll();
        gameFrame.getMainPanel().add(finishLabel);

        Insets insets = gameFrame.getMainPanel().getInsets();
        Dimension labelSize = finishLabel.getPreferredSize();
        finishLabel.setBounds(middlePoint.x + insets.left, middlePoint.y + insets.top,
                labelSize.width, labelSize.height);

        gameFrame.revalidate();
        gameFrame.repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT)
        {
            hunter.moveWithInput(-1,0);
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
        {
            hunter.moveWithInput(1,0);
        }
        else if (e.getKeyCode() == KeyEvent.VK_UP)
        {
            hunter.moveWithInput(0,-1);
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN)
        {
            hunter.moveWithInput(0,1);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    class Spawner extends TimerTask {

        public void run()
        {
            System.out.println(points);
            spawnNewHunt();
        }
    }
}
