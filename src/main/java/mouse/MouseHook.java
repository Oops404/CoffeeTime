package mouse;

import com.sun.jna.Platform;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HMODULE;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.platform.win32.WinUser.HHOOK;
import com.sun.jna.platform.win32.WinUser.MSG;

/**
 * 鼠标钩子
 */
public class MouseHook {

    private User32 lib;
    private MouseHookListener mouseHook;
    private HMODULE hMod;
    private boolean isWindows;
    private HHOOK hHook;

    public MouseHook() {
        isWindows = Platform.isWindows();
        if (isWindows) {
            lib = User32.INSTANCE;
            hMod = Kernel32.INSTANCE.GetModuleHandle(null);
        }

    }

    //添加钩子监听
    public void addMouseHookListener(MouseHookListener mouseHook) {
        this.mouseHook = mouseHook;
        this.mouseHook.lib = lib;
    }

    public void startWindowsHookEx() {
        if (isWindows) {
            hHook = lib.SetWindowsHookEx(WinUser.WH_MOUSE_LL, mouseHook, hMod, 0);
        }
    }

    public void stopWindowsHookEx() {
        if (isWindows) {
            lib.UnhookWindowsHookEx(hHook);
            System.out.println("hHook is closed.");
        }
    }
}
