package sound;

import javazoom.jl.player.Player;

import java.io.ByteArrayInputStream;

/**
 * @Author CheneyJin
 * @Time 2018-09-27-13:07
 * @Email cheneyjin@outlook.com
 */
public class SoundPlayer extends Thread {
    private byte[] source;
    private boolean speak = true;

    public SoundPlayer(byte[] source, boolean speak) {
        this.source = source;
        this.speak = speak;
    }

    public SoundPlayer(byte[] source) {
        this.source = source;
    }

    //重写run方法
    @Override
    public void run() {
        super.run();
        if (speak) {
            playSound(source);
        }
    }


    private static void playSound(byte[] music) {
        try (ByteArrayInputStream bai = new ByteArrayInputStream(music)) {
            Player player = new Player(bai);
            player.play();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            music = null;
        }
    }
}
