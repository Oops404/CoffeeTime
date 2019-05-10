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
public class ActionButton3 extends AbstractEvent {

    public ActionButton3(long cdTime) {
        super(cdTime);
    }
    @Override
    public void act(Robot robot) {
        robot.keyPress(KeyEvent.VK_3);
        try {
            sleep(random.nextInt(30) + 17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        robot.keyRelease(KeyEvent.VK_3);
    }
}
