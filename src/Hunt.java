import javax.swing.*;
import java.awt.*;
import java.util.EnumMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public class Hunt extends JPanel
{
    public enum Speed
    {
        VERY_SLOW, SLOW, NORMAL, FAST, VERY_FAST;
    }

    private static final int SIZE_X = 10;
    private static final int SIZE_Y = 10;
    private Speed mySpeed;
    private EnumMap<Speed, Integer> speedMap;
    private ScheduledFuture<?> schedulerHandler;
    private Vector2D myPosition;
    private Hunter hunterInstance;
    private static GameLogic gameLogic;

    public Hunt(Vector2D startingPosition, Speed speed, Hunter hunterInstance, GameLogic gameLogicParam)
    {
        gameLogic = gameLogicParam;
        myPosition = startingPosition;
        mySpeed = speed;
        this.hunterInstance = hunterInstance;
        setSize(getPreferredSize());
        setLocation(myPosition.x, myPosition.y);

        speedMap = new EnumMap<Speed, Integer>(Speed.class);
        speedMap.put(Speed.VERY_SLOW, 5);
        speedMap.put(Speed.SLOW, 10);
        speedMap.put(Speed.NORMAL, 20);
        speedMap.put(Speed.FAST, 40);
        speedMap.put(Speed.VERY_FAST, 80);
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
        g.setColor(Color.red);
        g2.drawRect(0, 0, SIZE_X, SIZE_Y);
        g.fillRect(0, 0, SIZE_X, SIZE_Y);
    }

    private final ScheduledExecutorService moveScheduler = Executors.newScheduledThreadPool(1);
    private final ScheduledExecutorService collisionCheckScheduler = Executors.newScheduledThreadPool(1);

    final Runnable collisionChecker = new Runnable()
    {
        public void run()
        {
            checkCollision(hunterInstance);
        }
    };

    final Runnable mover = new Runnable()
    {
        public void run()
        {
            move();
            repaint();
        }
    };

    private void checkCollision(Hunter hunter)
    {
        if (myPosition.x < hunter.getPosition().x + hunter.getPreferredSize().width &&
                myPosition.x + getPreferredSize().width > hunter.getPosition().x &&
                myPosition.y < hunter.getPosition().y + hunter.getPreferredSize().height &&
                myPosition.y + getPreferredSize().height > hunter.getPosition().y)
        {
            gameLogic.removeHunt(this, true);
            schedulerHandler.cancel(true);
            moveScheduler.shutdown();
        }
    }

    public void moveEverySecond()
    {
        schedulerHandler = moveScheduler.scheduleAtFixedRate(mover, 0, 1, TimeUnit.SECONDS);
    }

    public void checkCollisionEveryFrame()
    {
        schedulerHandler = collisionCheckScheduler.scheduleAtFixedRate(collisionChecker, 0, 1, TimeUnit.MILLISECONDS);
    }

    public void move()
    {
        int newY = myPosition.y + speedMap.get(mySpeed);
        setSize(getPreferredSize());
        myPosition.y = newY;
        setLocation(getX(), newY);

        if (newY >= GameFrame.SIZE_Y-45)
        {
            gameLogic.removeHunt(this, false);
            schedulerHandler.cancel(true);
            moveScheduler.shutdown();
        }
    }
}


