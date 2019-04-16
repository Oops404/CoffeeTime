package sound;

import javazoom.jl.player.Player;

import java.io.ByteArrayInputStream;

/**
 * @Author CheneyJin
 * @Time 2018-09-27-13:07
 * @Email cheneyjin@outlook.com
 */
public class SoundPlayer extends Thread {
    //重写run方法
    @Override
    public void run() {
        super.run();
        byte[] music = Tone.getInstance("mp3/tone.mp3").getMusic();
        playSound(music);
    }


    static void playSound(byte[] music) {
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
