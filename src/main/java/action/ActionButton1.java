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
public class ActionButton1 extends AbstractEvent {

    public ActionButton1(long cdTime) {
        super(cdTime);
    }

    @Override
    public void act(Robot robot) {
        robot.keyPress(KeyEvent.VK_1);
        try {
            sleep(random.nextInt(40) + 10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        robot.keyRelease(KeyEvent.VK_1);
    }
}
