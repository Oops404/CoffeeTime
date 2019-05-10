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
        setSource(source);
        this.speak = speak;
    }

    public SoundPlayer(byte[] source) {
        setSource(source);
    }

    private void setSource(byte[] source) {
        this.source = (source == null ? new byte[]{0} : source.clone());
    }

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
