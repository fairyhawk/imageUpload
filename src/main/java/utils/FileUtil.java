package utils;

import net.sf.json.JSONObject;
import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by IntelliJ IDEA.
 * User: Basil
 * Date: 11-6-18
 * Time: 上午11:30
 * 文件操作工具类
 */
public class FileUtil
{
    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);
    public static String kindeditorUpload(String savePath, String callBackPath, String importUrl, HttpServletRequest request, HttpServletResponse response, String cusid) throws IOException
    {
        if (savePath == null || savePath.equals(""))
        {
            savePath = "temp/";
        }
        // 小组模块中kindeditor编辑器，上传时，所使用到的中间存储目录
        String kindeditorSavePath = savePath;
        String newFileName = "", newName = "";
        StringBuilder str = new StringBuilder();

        // Struts2 请求 包装过滤器
        MultiPartRequestWrapper wrapper = (MultiPartRequestWrapper) request;
        // 获得上传的文件名
        String fileName = wrapper.getFileNames("imgFile")[0];

//		Log.debug("文件名" + fileName);
        // 获得未见过滤器
        File file = wrapper.getFiles("imgFile")[0];

        response.setCharacterEncoding("UTF-8");
        //文件大小校验 10M = 10485760
        //struts.multipart.maxSize
        if (file.length() > 10485760)
        {
            str.append("<html><head><title>Insert Image</title><meta http-equiv='content-type' content='text/html; charset=utf-8'/></head><body>");
            str.append("<script type='text/javascript'>");

            str.append("alert('文件过大!')");


            str.append("</script>");
            str.append("</body></html>");
        }
        if (str.length() == 0)
        {
            // ----------- 重新构建上传文件名----------------------
            final Lock lock = new ReentrantLock();
            lock.lock();
            try
            {
                // 加锁为防止文件名重复
                newName = System.currentTimeMillis() + fileName.substring(fileName.lastIndexOf("."), fileName.length());
            }
            finally
            {
                lock.unlock();
            }
            // ------------ 锁结束 -------------

            File isD = new File(kindeditorSavePath);
            // 校验如果目录不存在，则创建目录
            if (!isD.isDirectory())
            {
                isD.mkdirs();
            }
            // 获取文件输出流
            FileOutputStream fos = new FileOutputStream(kindeditorSavePath + "/" + newName);
            // 设置 KE 中的图片文件地址
/*            newFileName = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                + request.getContextPath() + "/" + kindeditorSavePath + "/" + newName;*/
            if (cusid == null || cusid.equals(""))
            {
                newFileName = request.getScheme() + "://" + importUrl + newName;
            }
            else
            {
                newFileName = request.getScheme() + "://" + importUrl + cusid + "/" + newName;
            }
            byte[] buffer = new byte[1024];

            // 获取内存中当前文件输入流
            InputStream in = new FileInputStream(file);
            try
            {
                int num;
                while ((num = in.read(buffer)) > 0)
                {
                    fos.write(buffer, 0, num);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace(System.err);
            }
            finally
            {
                in.close();
                fos.close();
            }
            // 发送给 KE
        }
        JSONObject obj = new JSONObject();
        obj.put("error", 0);
        obj.put("url", newFileName);

        str.append("<html><head>\n" +
            "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
            "<title>PIC UPLOAD</title>\n" +
            "<script type=\"text/javascript\">\n" +
            "    var upload_callback = function(){\n" +
            "        var iframe_proxy = document.createElement('iframe');\n" +
            "        iframe_proxy.style.display = 'none';\n" +
            "        iframe_proxy.src = '" + callBackPath + "/kindeditor/plugins/image/call_back.html#'+encodeURIComponent('" + obj.toString() + "');\n" +
            "        document.body.appendChild(iframe_proxy);\n" +
            "    };\n" +
            "</script>\n" +
            "</head>\n" +
            "<body onload=\"upload_callback();\">\n" +
            "\n" +
            "</body></html>");
        logger.debug(str.toString());
        return str.toString();
    }


}
