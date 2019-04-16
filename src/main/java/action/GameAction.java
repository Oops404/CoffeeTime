package action;

import java.awt.*;

/**
 * @Author CheneyJin
 * @Time 2018-09-29-14:54
 * @Email cheneyjin@outlook.com
 */
public class GameAction {

    private Robot robot;

    public GameAction(Robot robot) {
        this.robot = robot;
    }

    public final void start(Event event) {
        //noinspection StatementWithEmptyBody
        if (event.inCD()) {
        } else {
            event.act(robot);
            event.startCD();
        }
    }
}
