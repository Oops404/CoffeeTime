package util;

import org.opencv.core.Mat;

/**
 * @Author CheneyJin
 * @Time 2019-04-16-11:18
 * @Email cheneyjin@outlook.com
 * 直接 extends Mat implements AutoCloseable，在强制类型转换时会出现
 * java.lang.ClassCastException: org.opencv.core.Mat cannot be cast to util.CTMat
 */
public class CTMat implements AutoCloseable {
    private Mat mat;
    private String name;

    public CTMat(Mat mat, String name) throws Exception {
        if (mat != null) {
            this.mat = mat;
        } else {
            throw new Exception("mat is null");
        }
        this.name = name;
    }

    public Mat getMat() {
        return this.mat;
    }

    @Override
    public void close() throws Exception {
        if (mat != null) {
            mat.release();
        }
    }
}
