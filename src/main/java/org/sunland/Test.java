package org.sunland;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: Basil
 * Date: 11-6-20
 * Time: 上午11:01
 * 测试类
 */
public class Test
{
    public static void main(String[] args)
    {
        Pattern p = Pattern.compile("([a-z]*:(//[^/?#]+)?)?",Pattern.CASE_INSENSITIVE);
        String url = "http://localhost:8080/kindeditor/plugins/image/image.html?id=content1&ver=3.5.5%20%282011-05-22%29";
        Matcher matcher = p.matcher(url);
        matcher.find();
        System.out.println(matcher.group());
    }
}
