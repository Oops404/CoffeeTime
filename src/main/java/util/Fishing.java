package util;

import action.ActionButton1;
import action.ActionMouseL;
import action.GameAction;
import com.sun.jna.platform.win32.WinDef;
import javafx.scene.control.ListView;
import mouse.MouseCorrectRobot;
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

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static java.lang.Thread.sleep;

public class Fishing {

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

    private GameAction gameAction;
    private MouseCorrectRobot robot;
    private Properties pros = new Properties();

    private boolean close = false;
    private MouseHook mouseHook;

    public class Properties {
        public static final String LH = "lh";
        public static final String LS = "ls";
        public static final String LV = "lv";
        public static final String HH = "hh";
        public static final String HS = "hs";
        public static final String HV = "hv";
        public static final String GET_FISH_ALIGN_X = "getFishAlignX";
        public static final String GET_FISH_ALIGN_Y = "getFishAlignY";
        public static final String MASK_DEBUG = "maskDebug";
        public static final String CATCH_DEBUG = "catchDebug";
        public static final String LOAD_BAR_DEBUG = "loadBarDebug";
        public static final String SPRAY_AREA_DEBUG = "sprayAreaDebug";
        public static final String LOAD_BAR_X = "loadBarX";
        public static final String LOAD_BAR_Y = "loadBarY";
        public static final String SPRAY_STEP = "sprayStep";
        public static final String TARGET_THRESHOLD = "targetThreshold";
        public static final String TARGET_ALIGN_X = "targetAlignX";
        public static final String TARGET_ALIGN_Y = "targetAlignY";
        public static final String TARGET_SIZE = "targetSize";

        public int lh, ls, lv, hh, hs, hv;
        public int getFishAlignX, getFishAlignY;
        public boolean maskDebug, catchDebug, loadBarDebug, sprayAreaDebug;
        public int loadBarX, loadBarY, sprayStep, targetThreshold, targetAlignX, targetAlignY, targetSize;

        @Override
        public String toString() {
            return "Properties{" +
                    "lh=" + lh +
                    ", ls=" + ls +
                    ", lv=" + lv +
                    ", hh=" + hh +
                    ", hs=" + hs +
                    ", hv=" + hv +
                    ", getFishAlignX=" + getFishAlignX +
                    ", getFishAlignY=" + getFishAlignY +
                    ", maskDebug=" + maskDebug +
                    ", catchDebug=" + catchDebug +
                    ", loadBarDebug=" + loadBarDebug +
                    ", sprayAreaDebug=" + sprayAreaDebug +
                    ", loadBarX=" + loadBarX +
                    ", loadBarY=" + loadBarY +
                    ", sprayStep=" + sprayStep +
                    ", targetThreshold=" + targetThreshold +
                    ", targetAlignX=" + targetAlignX +
                    ", targetAlignY=" + targetAlignY +
                    ", targetSize=" + targetSize +
                    '}';
        }
    }

    public Properties getProperties() {
        return pros;
    }

    public Fishing() {
        initController();
    }

    public void auto() {
        close = false;
        mouseControl();
        Monitor monitor = new Monitor();
        monitor.start();
    }

    public void close() {
        this.close = true;
        mouseHook.stopWindowsHookEx();
    }

    public void loadProperties(ConfigUtil configUtil) {
        pros.loadBarX = configUtil.getValue(Properties.LOAD_BAR_X, 860);
        pros.loadBarY = configUtil.getValue(Properties.LOAD_BAR_Y, 875);
        pros.sprayStep = configUtil.getValue(Properties.SPRAY_STEP, 921);
        pros.targetThreshold = configUtil.getValue(Properties.TARGET_THRESHOLD, 20);

        pros.targetAlignX = configUtil.getValue(Properties.TARGET_ALIGN_X, 0);
        pros.targetAlignY = configUtil.getValue(Properties.TARGET_ALIGN_Y, 0);
        pros.targetSize = configUtil.getValue(Properties.TARGET_SIZE, 55);

        pros.getFishAlignX = configUtil.getValue(Properties.GET_FISH_ALIGN_X, 5);
        pros.getFishAlignY = configUtil.getValue(Properties.GET_FISH_ALIGN_Y, 0);

        pros.lh = configUtil.getValue(Properties.LH, 35);
        pros.ls = configUtil.getValue(Properties.LS, 110);
        pros.lv = configUtil.getValue(Properties.LV, 110);
        lower_red = new Scalar(pros.lh, pros.ls, pros.lv);

        pros.hh = configUtil.getValue(Properties.HH, 119);
        pros.hs = configUtil.getValue(Properties.HS, 255);
        pros.hv = configUtil.getValue(Properties.HV, 255);
        upper_red = new Scalar(pros.hh, pros.hs, pros.hv);

        pros.maskDebug = configUtil.getValueBool(Properties.MASK_DEBUG, true);
        pros.catchDebug = configUtil.getValueBool(Properties.CATCH_DEBUG, true);
        pros.loadBarDebug = configUtil.getValueBool(Properties.LOAD_BAR_DEBUG, true);
        pros.sprayAreaDebug = configUtil.getValueBool(Properties.SPRAY_AREA_DEBUG, true);
    }

    private void initController() {
        mouseHook = new MouseHook();
        try {
            robot = new MouseCorrectRobot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
        gameAction = new GameAction(robot);
    }

    private void mouseControl() {
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
                    return lib.CallNextHookEx(null, nCode, wParam, lParam.getPointer());
                }
            });
            mouseHook.startWindowsHookEx();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void resetMousePoint() throws InterruptedException {
        robot.mouseMove(100 + random.nextInt(400), 100 + random.nextInt(400));
        randomSleep(4000, 500);
    }

    private void usingFishingSkill() {
        gameAction.start(button1);
    }

    private class Monitor extends Thread {
        @Override
        public void run() {
            try {
                //noinspection InfiniteLoopStatement
                while (true) {
                    if (start && !close) {
                        usingFishingSkill();
                        resetMousePoint();
                        Target target = detectFishingLine(robot);
                        if (!target.isEmpty()) {
                            fishing(target);
                        } else {
                            System.out.println("can not find fishing line.");
                        }
                        System.out.println("--- step finish ---");
                    }
                    sleep(1);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println("Monitor is closed.");
            }
        }
    }

    private void getFish(Target target) throws InterruptedException {
        robot.mouseMove(screenCutX + target.x, screenCutY + target.y);
        randomSleep(300, 200);
        gameAction.start(mouseL);
        randomSleep(1000, 300);
    }

    private void fishing(Target target) throws InterruptedException {
        ArrayList<Long> frames = new ArrayList<>();
        while (true) {
            if (!detectLoadingBar()) {
                System.out.println("can not find loading bar.");
                randomSleep(1, 99);
                return;
            }
            if (detectSpray(frames, target)) {
                getFish(target);
                break;
            }
            sleep(1);
        }
    }

    private boolean detectLoadingBar() {
        BufferedImage fishingLoadingBar = robot.createScreenCapture(new Rectangle(
                pros.loadBarX, pros.loadBarY, 2, 2));
        try (CTMat loadingBar = new CTMat(bufferedImage2Mat(fishingLoadingBar), "loadingBar")) {
            double[] pixel = loadingBar.getMat().get(0, 0);
            if (pros.loadBarDebug) {
                Imgcodecs.imwrite(ConfigUtil.root() + "/loadBar.jpg", loadingBar.getMat());
            }
            return pixel.length != 0 && (pixel[1] - (pixel[0] + pixel[2]) > 110);
        } catch (Exception e) {
            System.out.println("detect loading bar error.");
            e.printStackTrace();
        }
        return false;
    }

    private boolean detectSpray(ArrayList<Long> frames, Target target) {
        BufferedImage targetScape = robot.createScreenCapture(
                new Rectangle(screenCutX + target.x + pros.targetAlignX, screenCutY + target.y + pros.targetAlignY,
                        pros.targetSize, pros.targetSize));
        try (CTMat sprayArea = new CTMat(bufferedImage2Mat(targetScape), "sprayArea");
             CTMat graySprayArea = new CTMat(new Mat(), "graySprayArea")) {
            if (pros.sprayAreaDebug) {
                Imgcodecs.imwrite(ConfigUtil.root() + "/sprayArea.jpg", sprayArea.getMat());
            }
            Imgproc.cvtColor(sprayArea.getMat(), graySprayArea.getMat(), Imgproc.COLOR_BGR2GRAY);
            int width = graySprayArea.getMat().cols();
            int height = graySprayArea.getMat().rows();
            long sum = 0;
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    sum += graySprayArea.getMat().get(y, x)[0];
                }
            }
            frames.add(sum);
            int framesSize = frames.size();
            if (framesSize >= 5
                    && frames.get(framesSize - 1) > frames.get(framesSize - 2) + pros.sprayStep
                    && frames.get(framesSize - 2) > frames.get(framesSize - 3) + pros.sprayStep
                    && frames.get(framesSize - 3) > frames.get(framesSize - 4) + pros.sprayStep
                    && frames.get(framesSize - 4) > frames.get(framesSize - 5) + pros.sprayStep
            ) {
                if (pros.catchDebug) {
                    Imgcodecs.imwrite(ConfigUtil.root() + "/catch.jpg", sprayArea.getMat());
                }
                return true;
            }
        } catch (Exception e) {
            System.out.println("detect spray action error.");
            e.printStackTrace();
        }
        return false;
    }

    private Target detectFishingLine(Robot robot) {
        BufferedImage waterScape = robot.createScreenCapture(
                new Rectangle(screenCutX, screenCutY, (int) (screenWidth * 0.35), (int) (screenHeight * 0.35))
        );
        try (CTMat scape = new CTMat(bufferedImage2Mat(waterScape), "scape");
             CTMat mask = new CTMat(new Mat(), "mask")) {
            Imgproc.cvtColor(scape.getMat(), scape.getMat(), Imgproc.COLOR_RGB2HSV);
            Core.inRange(scape.getMat(), lower_red, upper_red, mask.getMat());
            if (pros.maskDebug) {
                Imgcodecs.imwrite(ConfigUtil.root() + "/mask.jpg", mask.getMat());
            }
            return selectFishingLinePoint(mask.getMat());
        } catch (Exception e) {
            System.out.println("detect util.Fishing Line error.");
            e.printStackTrace();
        }
        return new Target(0, 0);
    }

    private Target selectFishingLinePoint(Mat mask) {
        int x = 0, y = 0, count = 0;
        for (int r = 0; r < mask.rows(); r++) {
            for (int c = 0; c < mask.cols(); c++) {
                double[] pixels = mask.get(r, c);
                if (pixels[0] == 255) {
                    if (count == pros.targetThreshold) {
                        x = c + pros.getFishAlignX;
                        y = r + pros.getFishAlignY;
                        break;
                    }
                    count++;
                }
            }
        }
        return new Target(x, y);
    }

    private Mat bufferedImage2Mat(BufferedImage image) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            ImageIO.write(image, "jpg", byteArrayOutputStream);
            byteArrayOutputStream.flush();
            return Imgcodecs.imdecode(
                    new MatOfByte(byteArrayOutputStream.toByteArray()), Imgcodecs.CV_LOAD_IMAGE_UNCHANGED);
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

        boolean isEmpty() {
            return this.x == 0 && this.y == 0;
        }
    }

    private void randomSleep(int sleepTime, int randomArea) throws InterruptedException {
        if (sleepTime > 0 && randomArea >= 0) {
            sleep(sleepTime + random.nextInt(randomArea));
        }
    }

    private void logOnUI(ListView<String> listView, String value) {
        listView.getItems().add(0, value);
    }
}
//robot.MoveMouseControlled(screenCutX + target.x, screenCutY + target.y);
//Point point = java.awt.MouseInfo.getPointerInfo().getLocation();


//    public util.Fishing() throws HeadlessException {
//        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//        int screenWidth = screenSize.width;
//        int screenHeight = screenSize.height;
//        this.setLocation((int) (screenWidth / 2.2 - this.getWidth() / 2.2),
//                (int) (screenHeight / 2.2 - this.getHeight() / 2.2));
//        this.setName("嘿嘿嘿");
//        setSize(200, 100);
//        setVisible(true);
//        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        JPanel contentPane = new JPanel();
//        contentPane.setLayout(new BorderLayout());
//        this.setContentPane(contentPane);
//        JButton guide = new JButton("说明");
//        guide.setFont(new Font("黑体", Font.PLAIN, 17));
//        contentPane.add(guide);
//        try {
//            configUtil = new ConfigUtil();
//        } catch (Exception e) {
//            e.printStackTrace();
//            guide.setText("读取配置失败");
//        }
//        initData();
//        try {
//            initController();
//        } catch (AWTException e) {
//            e.printStackTrace();
//            guide.setText("自动控制失败");
//        }
//        guide.addActionListener(e -> {
//            Mat guideJpg = null;
//            try {
//                guideJpg = Imgcodecs.imread(ConfigUtil.root() + "\\libs\\guide");
//                ImageViewer imageViewer = new ImageViewer(guideJpg, "说明");
//                imageViewer.imShow();
//            } finally {
//                if (guideJpg != null) {
//                    guideJpg.release();
//                }
//            }
//        });
//    }
