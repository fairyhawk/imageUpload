package utils;

import ch.qos.logback.core.util.Loader;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.jsp.PageContext;


/**
 * Properties 文件的操作
 *
 * @author baisheng
 */
public class PropertiesUtil
{
   private static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);

    private String fileName = "";
    private static String filePath;
    private Properties p;

    /**
     * guessPropFile:
     *
     * @param propFile:要寻找的属性文件名
     * @return InputStream
     */
    public static InputStream guessPropFile(String... propFile)
    {
        String fileName = Config.CONFIG_FILE;
        if (propFile.length > 0)
        {
            fileName = propFile[0];
        }
        try
        {
            // 得到类的类装载器
            ClassLoader loader = Loader.getTCL();

            // 先从当前类所处路径的根目录中寻找属性文件
            InputStream in = loader.getResourceAsStream(fileName);

            // System.out.println(loader.getResource(""));
            if (in != null)
            {
                filePath = loader.getResource("").getPath();
                logger.debug("class " + filePath);
                return in;
            }

            // 没有找到，就从该类所处的包目录中查找属性文件
            Package pack = loader.getClass().getPackage();
            if (pack != null)
            {
                String packName = pack.getName();
                StringBuffer path = new StringBuffer();
                if (!packName.contains(".")) path.append(packName).append("/");
                else
                {
                    int start = 0, end = 0;
                    end = packName.indexOf(".");
                    while (end != -1)
                    {
                        path.append(packName.substring(start, end)).append("/");
                        start = end + 1;
                        end = packName.indexOf(".", start);
                    }
                    path.append(packName.substring(start)).append("/");
                }
                in = loader.getResourceAsStream(path + Arrays.toString(propFile));
                if (in != null)
                {
                    filePath = path.toString();
                    logger.debug("file path " + filePath);
                    return in;
                }
            }

            // 如果没有找到，再从当前系统的用户目录中进行查找
            File f = null;
            String curDir = System.getProperty("user.dir");
            f = new File(curDir, fileName);
            if (f.exists())
            {
                filePath = curDir;
                return new FileInputStream(f);
            }

            // 如果还是没有找到，则从系统所有的类路径中查找
            String classpath = System.getProperty("java.class.path");

            String[] cps = classpath.split(System.getProperty("path.separator"));

            for (String cp : cps)
            {
                f = new File(cp, fileName);
                if (f.exists())
                {
                    System.out.println("test");
                    filePath = cp;
                    break;
                }
                f = null;
            }
            if (f != null)
            {
                return new FileInputStream(f);
            }

            return null;
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }

    }

    public InputStream getInputStream(String fileName) throws FileNotFoundException
    {
        return new FileInputStream(new File(fileName));
    }

    /**
     * 根据InputStream 构造
     *
     * @param in inputStream
     */
    public PropertiesUtil(InputStream in)
    {
        try
        {
            p = new Properties();
            p.load(in);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 根据传进的文件名载入文件
     *
     * @param fileName String
     */
    public PropertiesUtil(String fileName)
    {
        this.fileName = fileName;

        try
        {
            InputStream in = getInputStream(fileName);
            p = new Properties();

            // 载入文件
            p.load(in);
            in.close();
        }
        catch (FileNotFoundException e)
        {
            // log.error(e.toString());
            e.printStackTrace();
        }
        catch (Exception e)
        {
            // log.error("读取文件错误: " + e.toString());
            e.printStackTrace();
        }

    }

    public PropertiesUtil()
    {

    }

    /**
     * 根据文件名构造
     *
     * @param fileName 文件名
     * @return PropertiesUtil
     */
    public static PropertiesUtil getPropertiesUtil(String fileName)
    {
        return new PropertiesUtil(fileName);
    }

    /**
     * 根据类装载器与文件名构造
     *
     * @param propName 文件名
     * @return PropertiesUtil
     */
    public static PropertiesUtil getPropertiesUtil(String... propName)
    {
        return new PropertiesUtil(PropertiesUtil.guessPropFile(propName));
    }

    /**
     * 配置文件一律为config.propertities，并且统一放在web应用的config目录下。
     *
     * @param servlet Servlet
     * @return PropertiesUtil
     */
/*    public static PropertiesUtil getPropertiesUtil(HttpServlet servlet)
    {
        return PropertiesUtil.getPropertiesUtil(getConfigFile(servlet.getServletContext(), Config.CONFIG_FILE));
    }*/

    /**
     * 配置文件一律为config.propertities，并且统一放在web应用的config目录下。
     *
     * @param context 上下文对象
     * @return String
     */
/*    public static PropertiesUtil getPropertiesUtil(ServletContext context)
    {
        return PropertiesUtil.getPropertiesUtil(getConfigFile(context, Config.CONFIG_FILE));
    }*/

    /**
     * 在servlet中使用,直接用this作为参数,HttpServlet类型 根据配置文件名从当前web应用的根目录下找出配置文件
     *
     * @param context
     * @param configFileName String配置文件名字
     * @param configPath
     * @return String
     */
/*    public static String getConfigFile(ServletContext context, String configFileName, String... configPath)
    {
        //TODO:处理 configPath null 问题
        String config = null;
        if (configPath != null)
        {
            config = Config.CONFIG_PATH;
        }

        String configFile;

        configFile = context.getRealPath(config + configFileName);

        if (configFile == null || configFile.equals(""))
        {
            configFile = config + configFileName;
        }
        return configFile;

    }*/

    /**
     * jsp中用pageContext作参数
     *
     * @param hs             PageContext
     * @param configFileName String 配置文件名字
     * @return String
     */
/*    public static String getConfigFile(PageContext hs, String configFileName)
    {
        String configFile;

        ServletContext sc = hs.getServletContext();

        configFile = sc.getRealPath(Config.CONFIG_PATH + configFileName);

        if (configFile == null || configFile.equals(""))
        {
            configFile = Config.CONFIG_PATH + configFileName;
        }
        return configFile;
    }*/

    /**
     * 列出所有的配置文件内容
     */
    public void list()
    {
        p.list(System.out);
    }

    /**
     * 指定配置项名称，返回配置值
     *
     * @param itemName String
     * @return String
     */
    public String getValue(String itemName)
    {
        return p.getProperty(itemName);
    }

    /**
     * 指定配置项名称和默认值，返回配置值
     *
     * @param itemName     String
     * @param defaultValue String
     * @return String
     */
    public String getValue(String itemName, String defaultValue)
    {
        return p.getProperty(itemName, defaultValue);
    }

    /**
     * 设置配置项名称及其值
     *
     * @param itemName String
     * @param value    String
     */
    public void setValue(String itemName, String value)
    {
        p.setProperty(itemName, value);
    }

    /**
     * 保存配置文件，指定文件名和抬头描述
     *
     * @param fileName    String
     * @param description String
     * @throws Exception 异常
     */
    public void saveFile(String fileName, String description) throws Exception
    {
        File f = new File(fileName);
        FileOutputStream out = null;
        try
        {
            out = new FileOutputStream(f);
            p.store(out, description);// 保存文件
            out.close();

        }
        catch (IOException ex)
        {
            logger.error("Can't save file: " + fileName);
        }
        finally
        {
            if (out != null)
            {
                out.close();
            }
        }
    }

    /**
     * 保存配置文件，指定文件名
     *
     * @param fileName String
     * @throws Exception 文件异常
     */

    public void saveFile(String fileName) throws Exception
    {

        saveFile(fileName, "config.config");
    }

    /**
     * 保存配置文件，采用原文件名
     *
     * @throws Exception 文件异常
     */
    public void saveFile() throws Exception
    {
        if (filePath.length() == 0) throw new Exception("需指定保存的路径");
        saveFile(filePath + Config.CONFIG_FILE);
    }

    /**
     * 保存配置文件，采用原文件名
     *
     * @throws Exception
     */
    public void saveFileUtil() throws Exception
    {
        if (fileName.length() == 0) throw new Exception("需指定保存的路径");
        saveFile(fileName);
    }

    /**
     * 删除一个属性
     *
     * @param value String
     */

    public void deleteValue(String value)
    {
        p.remove(value);
    }

    /**
     * main method for test
     *
     * @param args String[]
     */

    public static void main(String[] args)
    {
//        System.out.println( PropertiesUtil.getPropertiesUtil().getValue("static.root"));
/*        Entity entity = new Entity();
        String name = (String) PropertyUtils.getProperty(entity, "name");  */
    }
}