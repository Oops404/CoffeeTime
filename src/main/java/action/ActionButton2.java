package action;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

import static java.lang.Thread.sleep;

/**
 * @Author CheneyJin
 * @Time 2018-09-29-15:11
 * @Email cheneyjin@outlook.com
 */
public class ActionButton2 extends Event {

    public ActionButton2(long cdTime) {
        super(cdTime);
    }
    @Override
    public void act(Robot robot) {
        robot.keyPress(KeyEvent.VK_2);
        try {
            sleep(random.nextInt(30) + 16);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        robot.keyRelease(KeyEvent.VK_2);
    }
}
