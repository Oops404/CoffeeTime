package mouse;

import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.LRESULT;
import com.sun.jna.platform.win32.WinDef.WPARAM;
import com.sun.jna.platform.win32.WinUser.HHOOK;
import com.sun.jna.platform.win32.WinUser.HOOKPROC;

/**
 * @author CheneyJin
 * 定义鼠标钩子，及事件监听回调
 */
public abstract class AbstractMouseHookListener implements HOOKPROC {
    /**
     * window应用程序接口
     */
    protected User32 lib = null;

    /**
     * 返回这个值链中的下一个钩子程序，返回值的含义取决于钩型
     * @param nCode nCode
     * @param wParam wParam
     * @param lParam lParam
     * @return LRESULT
     * @throws InterruptedException InterruptedException
     */
    public abstract LRESULT callback(int nCode, WPARAM wParam, MouseHookStruct lParam) throws InterruptedException;
}
