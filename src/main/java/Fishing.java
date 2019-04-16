import action.ActionButton1;
import action.ActionMouseL;
import action.GameAction;
import com.sun.jna.platform.win32.WinDef;
import mouse.MouseHook;
import mouse.MouseHookListener;
import mouse.MouseHookStruct;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import sound.SoundPlayer;
import util.ConfigUtil;
import util.ImageViewer;
import util.MouseCorrectRobot;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import static java.lang.Thread.sleep;

public class Fishing extends JFrame {

    private ConfigUtil configUtil;
    private Scalar lower_red;
    private Scalar upper_red;
    private ActionButton1 button1 = new ActionButton1(300);
    private ActionMouseL mouseL = new ActionMouseL(60);
    private Random random = new Random(System.currentTimeMillis());
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private int screenWidth = (int) screenSize.getWidth();
    private int screenHeight = (int) screenSize.getHeight();
    private boolean start = false;
    private int screenCutX = (int) (screenWidth * 0.3);
    private int screenCutY = (int) (screenHeight * 0.2);
    private int lh, ls, lv, hh, hs, hv;
    private boolean maskDebug, catchDebug, loadBarDebug, sprayAreaDebug;
    private int loadBarX, loadBarY, sprayStep, targetThreshold, targetAlignX, targetAlignY, targetSize;
    private GameAction gameAction;
    private MouseCorrectRobot robot;

    public Fishing() throws HeadlessException {
        int windowWidth = this.getWidth();
        int windowHeight = this.getHeight();
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        this.setLocation((int) (screenWidth / 2.2 - windowWidth / 2.2),
                (int) (screenHeight / 2.2 - windowHeight / 2.2));
        this.setName("嘿嘿嘿");
        setSize(200, 100);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        this.setContentPane(contentPane);
        JButton guide = new JButton("说明");
        guide.setFont(new Font("黑体", Font.PLAIN, 17));
        contentPane.add(guide);
        try {
            configUtil = new ConfigUtil();
        } catch (Exception e) {
            e.printStackTrace();
            guide.setText("读取配置失败");
        }
        initData();
        try {
            initController();
        } catch (AWTException e) {
            e.printStackTrace();
            guide.setText("自动控制失败");
        }
        guide.addActionListener(e -> {
            Mat guideJpg = null;
            try {
                guideJpg = Imgcodecs.imread(ConfigUtil.root() + "\\libs\\guide");
                ImageViewer imageViewer = new ImageViewer(guideJpg, "说明");
                imageViewer.imShow();
            } finally {
                if (guideJpg != null) {
                    guideJpg.release();
                }
            }
        });
    }

    private void initData() {
        loadBarX = configUtil.getValue("loadBarX", 860);
        loadBarY = configUtil.getValue("loadBarY", 875);
        sprayStep = configUtil.getValue("sprayStep", 921);
        targetThreshold = configUtil.getValue("targetThreshold", 20);

        targetAlignX = configUtil.getValue("targetAlignX", 0);
        targetAlignY = configUtil.getValue("targetAlignY", 0);
        targetSize = configUtil.getValue("targetSize", 55);

        lh = configUtil.getValue("lh", 35);
        ls = configUtil.getValue("ls", 110);
        lv = configUtil.getValue("lv", 110);
        lower_red = new Scalar(lh, ls, lv);

        hh = configUtil.getValue("hh", 119);
        hs = configUtil.getValue("hs", 255);
        hv = configUtil.getValue("hv", 255);
        upper_red = new Scalar(hh, hs, hv);

        maskDebug = configUtil.getValueBool("maskDebug", true);
        catchDebug = configUtil.getValueBool("catchDebug", true);
        loadBarDebug = configUtil.getValueBool("loadBarDebug", true);
        sprayAreaDebug = configUtil.getValueBool("sprayAreaDebug", true);
    }

    private void initController() throws AWTException {
        robot = new MouseCorrectRobot();
        gameAction = new GameAction(robot);
    }

    public static void main(String[] args) {
        System.out.println(new File("").getAbsolutePath());
        Fishing fishing = new Fishing();
        fishing.auto();
    }

    private void auto() {
        Monitor monitor = new Monitor();
        monitor.start();
        mouseControl();
    }

    private void mouseControl() {
        MouseHook mouseHook = new MouseHook();
        try {
            mouseHook.addMouseHookListener(new MouseHookListener() {

                @Override
                public WinDef.LRESULT callback(int nCode, WinDef.WPARAM wParam, MouseHookStruct lParam)
                        throws InterruptedException {
                    if (nCode >= 0) {
                        switch (wParam.intValue()) {
                            case 519:
                                start = !start;
                                new SoundPlayer().start();
                                break;
                            default:
                                break;
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

    private class Monitor extends Thread {
        @Override
        public void run() {
            try {
                System.out.println("start fishing");
                //noinspection InfiniteLoopStatement
                while (true) {
                    if (start) {
                        randomSleep(1, 445);
                        gameAction.start(button1);
                        robot.mouseMove(100 + random.nextInt(400),
                                100 + random.nextInt(400));
                        randomSleep(4000, 500);
                        Target target = catchTarget(robot);
                        if (target.x != 0 && target.y != 0) {
                            fishing(robot, target);
                        }
                        System.out.println("~~~~~~~~~~~~~~~~~");
                    }
                    sleep(1);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void fishing(MouseCorrectRobot robot, Target target) {
        ArrayList<Long> frames = new ArrayList<>();
        while (true) {
            Mat fishingLoadBarMat = null;
            Mat sprayAreaMat = null;
            Mat grayTargetMat = null;
            try {
                BufferedImage fishingLoadBar = robot.createScreenCapture(new Rectangle(
                        loadBarX, loadBarY, 2, 2));//860, 875
                fishingLoadBarMat = bufferedImage2Mat(fishingLoadBar);
                double[] pixel = new double[0];
                if (fishingLoadBarMat != null) {
                    pixel = fishingLoadBarMat.get(0, 0);
                    if (loadBarDebug) {
                        Imgcodecs.imwrite(ConfigUtil.root() + "/loadBar.jpg", fishingLoadBarMat);
                    }
                } else {
                    System.out.println("loading bar mat is null");
                }
                if (pixel.length != 0 && (pixel[1] - (pixel[0] + pixel[2]) > 110)) {
                    BufferedImage targetScape = robot.createScreenCapture(new Rectangle(
                            screenCutX + target.x + targetAlignX, screenCutY + target.y + targetAlignY,
                            targetSize, targetSize));
                    sprayAreaMat = bufferedImage2Mat(targetScape);
                    if (sprayAreaMat == null) {
                        System.out.println("target not found.");
                        return;
                    }
                    if (sprayAreaDebug) {
                        Imgcodecs.imwrite(ConfigUtil.root() + "/sprayArea.jpg", sprayAreaMat);
                    }
                    grayTargetMat = new Mat();
                    Imgproc.cvtColor(sprayAreaMat, grayTargetMat, Imgproc.COLOR_BGR2GRAY);
                    int width = grayTargetMat.cols();
                    int height = grayTargetMat.rows();
                    long sum = 0;
                    for (int y = 0; y < height; y++) {
                        for (int x = 0; x < width; x++) {
                            sum += grayTargetMat.get(y, x)[0];
                        }
                    }
                    frames.add(sum);
                    if (frames.size() >= 5
                            && frames.get(frames.size() - 1) > frames.get(frames.size() - 2) + sprayStep
                            && frames.get(frames.size() - 2) > frames.get(frames.size() - 3) + sprayStep
                            && frames.get(frames.size() - 3) > frames.get(frames.size() - 4) + sprayStep
                            && frames.get(frames.size() - 4) > frames.get(frames.size() - 5) + sprayStep
                    ) {
                        if (catchDebug) {
                            Imgcodecs.imwrite(ConfigUtil.root() + "/catch.jpg", sprayAreaMat);
                        }
                        robot.mouseMove(screenCutX + target.x, screenCutY + target.y);
                        randomSleep(500, 300);
                        gameAction.start(mouseL);
                        randomSleep(1000, 300);
                        return;
                    }
                } else {
                    System.out.println("can not find loading bar.");
                    randomSleep(1, 99);
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (fishingLoadBarMat != null) {
                    fishingLoadBarMat.release();
                }
                if (sprayAreaMat != null) {
                    sprayAreaMat.release();
                }
                if (grayTargetMat != null) {
                    grayTargetMat.release();
                }
            }
        }
    }

    private Target catchTarget(Robot robot) {
        BufferedImage waterScape = robot.createScreenCapture(new Rectangle(
                screenCutX, screenCutY,
                (int) (screenWidth * 0.35), (int) (screenHeight * 0.35))
        );
        Mat ori = bufferedImage2Mat(waterScape);
        if (ori == null) {
            return new Target(0, 0);
        }
        Imgproc.cvtColor(ori, ori, Imgproc.COLOR_RGB2HSV);
        Mat mask = new Mat();
        Core.inRange(ori, lower_red, upper_red, mask);
        if (maskDebug) {
            Imgcodecs.imwrite(ConfigUtil.root() + "/mask.jpg", mask);
        }
        int targetX = 0, targetY = 0, count = 0;
        for (int y = 0; y < mask.rows(); y++) {
            for (int x = 0; x < mask.cols(); x++) {
                double[] pixels = mask.get(y, x);
                if (pixels[0] == 255) {
                    if (count == targetThreshold) {
                        targetX = x + 5;
                        targetY = y;
                        break;
                    }
                    count++;
                }
            }
        }
        ori.release();
        mask.release();
        System.out.println("target: " + targetX + " , " + targetY);
        return new Target(targetX, targetY);
    }

    private Mat bufferedImage2Mat(BufferedImage image) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            ImageIO.write(image, "jpg", byteArrayOutputStream);
            byteArrayOutputStream.flush();
            return Imgcodecs.imdecode(new MatOfByte(byteArrayOutputStream.toByteArray()), Imgcodecs.CV_LOAD_IMAGE_UNCHANGED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    static {
        System.load(ConfigUtil.root() + "/libs/x64/opencv_java343.dll");
    }

    private class Target {
        int x;
        int y;

        Target(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private void randomSleep(int sleepTime, int randomArea) throws InterruptedException {
        if (sleepTime > 0 && randomArea >= 0) {
            sleep(sleepTime + random.nextInt(randomArea));
        }
    }
//            if (screenWidth <= 1366) {
////            loadBarX = 583;
////            loadBarY = 563;
//        loadBarX = 592;
//        loadBarY = 565;
//        sprayStep = 930;
//    }


//    private boolean isFriends() throws IOException {
//        Process process = Runtime.getRuntime().exec(
//                new String[]{"wmic", "cpu", "get", "ProcessorId"});
//        process.getOutputStream().close();
//        Scanner sc = new Scanner(process.getInputStream());
//        String property = sc.next();
//        String serial = sc.next();
//        String serialMD5 = MD5Util.stringMD5(serial);
//        System.out.println(serialMD5);
//        System.out.println(serialMD5.equals("568B8FA5CDFD8A2623BDA1D8AB7B7B34"));
//        return (serialMD5.equals("568B8FA5CDFD8A2623BDA1D8AB7B7B34"));
//    }
//robot.MoveMouseControlled(screenCutX + target.x, screenCutY + target.y);
//Point point = java.awt.MouseInfo.getPointerInfo().getLocation();
}

