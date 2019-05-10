package action;

import java.awt.*;
import java.awt.event.InputEvent;
import java.util.Random;

import static java.lang.Thread.sleep;

/**
 * @Author CheneyJin
 * @Time 2018-09-29-15:11
 * @Email cheneyjin@outlook.com
 */
public class ActionMouseR extends AbstractEvent {

    public ActionMouseR(long cdTime) {
        super(cdTime);
    }
    @Override
    public void act(Robot robot) {
        robot.mousePress(InputEvent.BUTTON3_MASK);
        try {
            sleep(random.nextInt(55) + 10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        robot.mouseRelease(InputEvent.BUTTON3_MASK);
    }
}
