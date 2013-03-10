package utils;

/**
 * Created by IntelliJ IDEA.
 * User: Basil
 * Date: 11-6-18
 * Time: 下午3:37
 * To change this template use File | Settings | File Templates.
 */
public class OSUtil
{

    /**
     * 判断当前操作是否Windows.
     *
     * @return true---是Windows操作系统
     */
    public static boolean isWindowsOS()
    {
        boolean isWindowsOS = false;
        String osName = System.getProperty("os.name");
        if (osName.toLowerCase().contains("windows"))
        {
            isWindowsOS = true;
        }
        return isWindowsOS;
    }


}
