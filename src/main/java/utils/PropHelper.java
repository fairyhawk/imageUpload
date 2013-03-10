package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Created by IntelliJ IDEA.
 * User: Basil
 * Date: 11-7-28
 * Time: 下午3:46
 */
public class PropHelper
{


    public static void main(String[] args)
    {
//    Locale locale = new Locale("zh", "CN");
/*    ResourceBundle localResource = ResourceBundle.getBundle("config");
          String value = localResource.getString("dis.path");*/
    System.out.println("ResourceBundle: " + PathUtil.getPath("img.dis"));
    }
}
