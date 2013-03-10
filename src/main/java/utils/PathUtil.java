package utils;

import java.util.ResourceBundle;

/**
 * Created by IntelliJ IDEA.
 * User: Basil
 * Date: 11-7-28
 * Time: 下午4:24
 */
public class PathUtil
{
    /**
     * 取到配置文件路径
     * 
     * @param name 属性名
     * @return 路径
     */
    public static String getPath(String name)
    {
        ResourceBundle localResource = ResourceBundle.getBundle("config");
        return localResource.getString(name);
    }
}
