import action.*;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;
import mouse.MouseHook;
import mouse.MouseHookListener;
import mouse.MouseHookStruct;
import sound.SoundPlayer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

import static java.lang.Thread.sleep;

/**
 * RGB
 * int red = (pixel & 0xff0000) >> 16;
 * int green = (pixel & 0xff00) >> 8;
 * int blue = (pixel & 0xff);
 * <p>
 * 将钩子信息传递到当前钩子链中的下一个子程，一个钩子程序可以调用这个函数之前或之后处理钩子信息
 * hhk：当前钩子的句柄
 * nCode ：钩子代码; 就是给下一个钩子要交待的，钩传递给当前Hook过程的代码。下一个钩子程序使用此代码，以确定如何处理钩的信息。
 * wParam：要传递的参数; 由钩子类型决定是什么参数，此参数的含义取决于当前的钩链与钩的类型。
 * lParam：Param的值传递给当前Hook过程。此参数的含义取决于当前的钩链与钩的类型。
 */
public class Diablo3Crusader {

    private ActionButton1 button1 = new ActionButton1(300);
    private ActionButton2 button2 = new ActionButton2(300);
    private ActionButton3 button3 = new ActionButton3(300);
    private ActionButton4 button4 = new ActionButton4(300);

    private ActionMouseL mouseL = new ActionMouseL(60);
    private ActionMouseR mouseR = new ActionMouseR(60);
    private ActionMouseWD mouseWD = new ActionMouseWD(60);
    private ActionMouseWU mouseWU = new ActionMouseWU(60);
    private ActionCompelMove compelMove = new ActionCompelMove(60);

    private Random random = new Random(System.currentTimeMillis());

    private Wait defenseWait = new Wait(650);
    private Wait saveWait = new Wait(650);

    private boolean defenseType = true;
    private volatile boolean start = false;

    private Robot robot;

    {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    private GameAction gameAction = new GameAction(robot);

    public static void main(String[] args) {
        Diablo3Crusader ct = new Diablo3Crusader();
        ct.auto();
    }

    private boolean findD3HWND() {
        WinDef.HWND hwnd = User32.INSTANCE.FindWindow(null, "暗黑破壞神III");
        boolean showed = User32.INSTANCE.ShowWindow(hwnd, WinUser.SW_RESTORE);
        if (showed) {
            System.out.println("show diablo3 window.");
        }
        return start = (hwnd != null);
    }

    private void auto() {
        Thread monitorThread = new Thread(() -> {
            try {
                System.out.println("keeping life&power thread start.");
                //noinspection InfiniteLoopStatement
                while (true) {
                    if (start) {
                        capture(robot);
                    }
                    sleep(1);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        monitorThread.start();

        Thread mechanicalClickThread = new Thread(() -> {
            try {
                System.out.println("mechanical click thread start.");
                //noinspection InfiniteLoopStatement
                while (true) {
                    if (start) {
                        gameAction.start(button3);
                        gameAction.start(button2);
                        gameAction.start(mouseR);
                    }
                    sleep(1);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        mechanicalClickThread.start();
        Thread moveThread = new Thread(() -> {
            try {
                System.out.println("move thread start.");
                //noinspection InfiniteLoopStatement
                while (true) {
                    if (start) {
                        gameAction.start(compelMove);
                        gameAction.start(mouseL);
                    }
                    sleep(1);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        moveThread.start();
        MouseHook mouseHook = new MouseHook();
        try {
            mouseHook.addMouseHookListener(new MouseHookListener() {

                @Override
                public WinDef.LRESULT callback(int nCode, WinDef.WPARAM wParam, MouseHookStruct lParam)
                        throws InterruptedException {
                    if (nCode >= 0) {
                        switch (wParam.intValue()) {
                            case 519:
                                start = false;
                                new SoundPlayer().start();
                                System.out.println("action finish.");
                                break;
                            case 523:
                                //start = true;
                                if (findD3HWND()) {
                                    new SoundPlayer().start();
                                    System.out.println("action start.");
                                } else {
                                    System.out.println("open the game.");
                                }
                                break;
//                            default:
//                                System.out.println(wParam.intValue());
//                                break;
                        }
                    }
                    sleep(1);
                    return lib.CallNextHookEx(hHook, nCode, wParam, lParam.getPointer());
                }
            });
            mouseHook.startWindowsHookEx();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mouseHook.stopWindowsHookEx();
        }
    }


    private int ansAkalate(BufferedImage img) {
        int height = img.getHeight();
        int minX = img.getMinX();
        if (((img.getRGB(minX + 1, height / 2) & 0xff00) >> 8) > 100) {
            return 1;
        }
        return 0;
    }

    private void capture(Robot robot) {
        BufferedImage life = robot.createScreenCapture(new Rectangle(520, 935, 60, 115));
        BufferedImage power = robot.createScreenCapture(new Rectangle(1345, 910, 60, 145));
        BufferedImage akalate = robot.createScreenCapture(new Rectangle(970, 991, 50, 10));
        int lifeCount = ansPowerAndLife(life, 105, 175, false);
        int powerCount = ansPowerAndLife(power, 106, 246, true);
        int akalateCount = ansAkalate(akalate);
        if (powerCount == 0 || lifeCount == 0) {
            return;
        }
        if (akalateCount == 0) {
            if (defenseWait.isLive()) {
                System.out.println("Defense CD.");
            } else {
                gameAction.start(mouseWD);
                new Thread(defenseWait).start();
            }
        }
        if (15 < lifeCount && lifeCount < 3200) {
            if (saveWait.isLive()) {
                // 无视定时器，救命先
                if (16 < lifeCount && lifeCount < 1600) {
                    saveLife();
                }
            } else {
                saveLife();
                new Thread(saveWait).start();
            }
        }
        if (25 < powerCount && powerCount < 2800) {
            gameAction.start(button4);
        }
    }

    private void saveLife() {
        if (defenseType) {
            gameAction.start(mouseWD);
            defenseType = false;
        } else {
            gameAction.start(mouseWU);
            defenseType = true;
        }
    }

    //判断红色的最大值与最小值之间来看生命值是否安全
    private int ansPowerAndLife(BufferedImage img, int rMin, int rMax, boolean isPower) {
        int width = img.getWidth();
        int height = img.getHeight();
        int minX = img.getMinX();
        int minY = img.getMinY();
        if (isPower) {
            if (((img.getRGB(width - 1, minY) & 0xff0000) >> 16) <= 25) {
                return 0;
            }
        }
        int count = 0;
        for (int x = minX; x < width; x++) {
            for (int y = minY; y < height; y++) {
                int pixel = img.getRGB(x, y);
                int red = (pixel & 0xff0000) >> 16;
                if (rMin <= red && red <= rMax) {
                    count++;
                }
            }
        }
        return count;
    }

    private class Wait implements Runnable {
        private boolean live = false;
        private int second;

        Wait(int second) {
            this.second = second;
        }

        @Override
        public void run() {
            live = true;
            try {
                second = second + random.nextInt(10);
                sleep(second);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            live = false;
        }

        boolean isLive() {
            return live;
        }
    }
}