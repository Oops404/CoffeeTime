package sound;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * @Author CheneyJin
 * @Time 2018-09-27-13:44
 * @Email cheneyjin@outlook.com
 */
public class Tone {

    private byte[] musicBytes;
    private static Tone instance;

    private Tone(String musicPath) {
        try (InputStream musicStream = this.getClass()
                .getClassLoader().getResourceAsStream(musicPath)) { //"mp3/tone.mp3"
            musicBytes = input2byte(musicStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Tone getInstance(String musicPath) {
        if (instance == null) {
            synchronized (Tone.class) {
                if (instance == null) {
                    instance = new Tone(musicPath);
                }
            }
        }
        return instance;
    }

    public byte[] getMusic() {
        return musicBytes.clone();
    }

    private byte[] input2byte(InputStream inStream) {
        try (ByteArrayOutputStream swapStream = new ByteArrayOutputStream()) {
            byte[] buff = new byte[1024];
            int rc;
            while ((rc = inStream.read(buff, 0, 100)) > 0) {
                swapStream.write(buff, 0, rc);
            }
            return swapStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
