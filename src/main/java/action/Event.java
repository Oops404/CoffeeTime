package action;

import java.awt.*;
import java.util.Random;

import static java.lang.Thread.sleep;

/**
 * @Author CheneyJin
 * @Time 2018-09-29-16:04
 * @Email cheneyjin@outlook.com
 */
public abstract class Event {

    Random random = new Random(System.currentTimeMillis());

    private long cdTime;

    private CD cd = new CD();

    public Event(long cdTime) {
        this.cdTime = cdTime;
    }

    abstract void act(Robot robot);

    private final class CD implements Runnable {
        private boolean live = false;

        @Override
        public void run() {
            try {
                live = true;
                sleep(cdTime + random.nextInt(10));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                live = false;
            }

        }

        boolean isLive() {
            return live;
        }
    }

    protected boolean inCD() {
        return cd.isLive();
    }

    protected void startCD() {
        if (!cd.isLive()) {
            Thread cdThread = new Thread(cd);
            cdThread.start();
        }
    }

    public long getCdTime() {
        return cdTime;
    }
}
