package mouse;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Structure;
import com.sun.jna.platform.win32.BaseTSD.ULONG_PTR;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinUser.POINT;

/**
 * 定义鼠标钩子数据结构体
 */
public class MouseHookStruct extends Structure {

    public static class ByReference extends MouseHookStruct implements Structure.ByReference {
    }

    //点坐标
    public POINT pt;
    //窗口句柄
    public HWND hwnd;
    public int wHitTestCode;
    //扩展信息
    public ULONG_PTR dwExtraInfo;


    @Override
    protected List getFieldOrder() {
        //返回属性顺序
        return Arrays.asList("dwExtraInfo", "hwnd", "pt", "wHitTestCode");
    }
}
