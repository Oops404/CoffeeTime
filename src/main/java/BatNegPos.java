import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

/**
 * @Author CheneyJin
 * @Time 2018-11-16-11:56
 * @Email cheneyjin@outlook.com
 */
public class BatNegPos {
    public static void main(String[] args) throws FileNotFoundException {
//        String posPath = "C:\\Users\\Octave\\Desktop\\opencv\\x64\\vc15\\bin\\pos\\";
//        File posFile = new File(posPath + "pos.txt");
//        FileOutputStream pfos = new FileOutputStream(posFile);
//        PrintWriter ppw = new PrintWriter(pfos, true);
//        File[] pFiles = new File(posPath).listFiles();
//        if (pFiles != null) {
//            for (File file : pFiles) {
//                ppw.println("pos/" + file.getName() + " 1 0 0 40 40");
//            }
//        }


        String path = "C:\\Users\\Octave\\Desktop\\opencv\\x64\\vc15\\bin\\neg\\";
        File negFile = new File(path + "neg.txt");
        FileOutputStream nfos = new FileOutputStream(negFile);
        PrintWriter npw = new PrintWriter(nfos, true);
        File[] nFiles = new File(path).listFiles();
        if (nFiles != null) {
            for (File file : nFiles) {
                npw.println("neg/" + file.getName());

            }
        }
    }
}
