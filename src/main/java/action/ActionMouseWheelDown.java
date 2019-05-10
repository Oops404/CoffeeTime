package action;

import java.awt.*;

import static java.lang.Thread.sleep;

/**
 * @Author CheneyJin
 * @Time 2018-09-29-15:11
 * @Email cheneyjin@outlook.com
 */
public class ActionMouseWheelDown extends AbstractEvent {

    public ActionMouseWheelDown(long cdTime) {
        super(cdTime);
    }

    @Override
    public void act(Robot robot) {
        robot.mouseWheel(-2);
    }
}
