package org.sunland.action;

import static utils.web.Struts2Utils.getParameter;
import static utils.web.Struts2Utils.getRequest;
import static utils.web.Struts2Utils.getResponse;
import static utils.web.Struts2Utils.renderHtml;

import java.awt.Rectangle;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;
import org.owasp.esapi.ESAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import utils.ChangeImage;
import utils.FileUtil;
import utils.ImageHelper;
import utils.OSUtil;
import utils.PathUtil;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 
 * @ClassName UploadAction
 * @package org.sunland.action
 * @description 文件上传服务类
 * @author liuqinggang
 * @Create Date: 2013-3-1 下午04:13:16
 * 
 */
public class UploadAction extends ActionSupport {
    /**
     * 
     */
    private static final long serialVersionUID = 1035172567157056332L;
    private static Logger logger = LoggerFactory.getLogger(UploadAction.class);

    // 小组ID
    private String disid;
    // 用户ID
    private String cusid;
    // 目录
    private String folder;

    /**
     * 用户头像
     */
    public void userFace() throws IOException {
        logger.debug("user face uploading...");
        HttpServletRequest request = getRequest();
        MultiPartRequestWrapper wrapper = (MultiPartRequestWrapper) request;

        File[] files = wrapper.getFiles("fileupload");
        String[] fileNames = wrapper.getFileNames("fileupload");
        String fileName = fileNames[0];

        String savePath = PathUtil.getPath("file.cus.temp");
        if (files != null && files.length > 0) {
            getResponse().setCharacterEncoding("utf-8");
            final Lock lock = new ReentrantLock();
            String newName = null;
            lock.lock();
            try {
                // 加锁为防止文件名重复
                newName = System.currentTimeMillis()
                        + fileName
                                .substring(fileName.lastIndexOf("."), fileName.length());
            } finally {
                lock.unlock();
            }

            File isD = new File(savePath);

            if (!isD.exists()) {
                synchronized (UploadAction.class) {
                    isD.mkdirs();
                }
            }
            if (!isD.isDirectory()) {
                logger.error(isD.getName() + " must be a directory!");
            }
            // 获取文件输出流
            FileOutputStream fos = null;
            InputStream in = null;
            try {
                fos = new FileOutputStream(savePath + "/" + newName);
                byte[] buffer = new byte[1024];

                // 获取内存中当前文件输入流
                in = new FileInputStream(files[0]);

                int num;
                while ((num = in.read(buffer)) > 0) {
                    fos.write(buffer, 0, num);
                }
                in.close();

                fos.close();
            } catch (Exception e) {
                logger.error(e.getMessage());
            } finally {
                if (in != null) {
                    in.close();
                }
                if (fos != null) {
                    fos.close();
                }
            }
            logger.debug("import.cus.temp_url" + PathUtil.getPath("import.cus.temp"));
            renderHtml(request.getScheme() + "://" + PathUtil.getPath("import.cus.temp")
                    + newName);

        }
    }

    /**
     * 考试
     */
    public void exam() {
        try {
            logger.debug("exam is uploading...");
            String str = kindeditorUpload(PathUtil.getPath("file.exam"), PathUtil
                    .getPath("import.exam"));
            renderHtml(str);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

    }

    /**
     *图书
     */
    public void bookstore() {
        try {
            logger.debug("bookstore is uploading...");
            String str = kindeditorUpload(PathUtil.getPath("file.bookstore"), PathUtil
                    .getPath("import.bookstore"));
            renderHtml(str);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

    }

    /**
     * article
     */
    public void article() {
        try {
            logger.debug("article is uploading...");
            String str = kindeditorUpload(PathUtil.getPath("file.article"), PathUtil
                    .getPath("import.article"));
            renderHtml(str);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * 小组功能相关图片
     * 
     * @throws java.io.IOException
     *             文件读写异常
     */
    @Action
    public void img() throws IOException {

        StringBuilder stb = new StringBuilder();
        if (cusid != null && !cusid.equals("")) {
            stb.append(PathUtil.getPath("file.dis")).append(cusid);
        }
        logger.debug("save_path -" + stb.toString());

        try {
            logger.debug("dis file is uploading...");
            String str = kindeditorUpload(stb.toString(), PathUtil.getPath("import.dis"));
            renderHtml(str);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * 封装 kindeditor 上传
     * 
     * @param path
     *            服务器物理路径
     * @param importPath
     *            返回虚拟路径
     * @return 路径串
     * @throws IOException
     *             读写异常
     */
    private String kindeditorUpload(String path, String importPath) throws IOException {
        String referer = getRequest().getHeader("referer");
        Pattern p = Pattern.compile("([a-z]*:(//[^/?#]+)?)?", Pattern.CASE_INSENSITIVE);
        Matcher mathcer = p.matcher(referer);
        if (mathcer.find()) {
            String callBackPath = mathcer.group();
            // System.out.println("local url" + callBackPath);
            // 用于本机测试使用
            if (callBackPath.equals("http://localhost:8080")
                    || callBackPath.equals("http://127.0.0.1:8080")) {
                callBackPath += "/sedu";
            }

            // 判断系统
            if (OSUtil.isWindowsOS()) {
            }

            return FileUtil.kindeditorUpload(path, callBackPath, importPath,
                    getRequest(), getResponse(), cusid);
        }

        return null;

    }

    /**
     * jinlong...
     */
    public void coupon() {
        logger.debug("coupon.action is coming...");
        String savePath = PathUtil.getPath("file.coupon");
        try {
            save(savePath, false);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

    }

    /**
     * 金龙.
     */
    public void shop() {
        logger.debug("shop.action is coming...");
        String savePath = PathUtil.getPath("file.shop");
        try {
            save(savePath, false);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

    }

    /**
     * 上传方法集合，根据参数匹配属性文件\
     * 
     */
    public void go() {
        // TODO: 可以将 coupon,shop 方法整合进来
        String param = getParameter("param");
        String params = PathUtil.getPath("request.params");
        logger.debug("book.action is coming..., This param is {}, This params is {}",
                param, params);

        if (param != null) {
            if (params.contains(param)) {
                String savePath = PathUtil.getPath("file." + param);
                try {
                    save(savePath, false);
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }

            } else {
                logger.error("The params is not contains {}", param);
            }
        } else {
            logger.error("There is no param....");
        }

    }

    /**
     * 文件保存
     * 
     * @param savePath
     *            保存路径
     * @return 生成路径
     * @throws Exception
     *             异常
     */
    private String save(String savePath, boolean cut) throws Exception {
        HttpServletRequest request = getRequest();
        MultiPartRequestWrapper wrapper = (MultiPartRequestWrapper) request;
        if (wrapper == null)
            return null;

        File[] files = wrapper.getFiles("fileupload");
        String[] fileNames = wrapper.getFileNames("fileupload");
        String fileName = fileNames[0];

        if (files != null && files.length > 0) {
            getResponse().setCharacterEncoding("utf-8");
            final Lock lock = new ReentrantLock();
            String newName = null;
            lock.lock();
            try {
                // 加锁为防止文件名重复
                if (fileName.substring(fileName.lastIndexOf("."), fileName.length())
                        .indexOf("docx") > -1) { // 2011版word
                    newName = fileName;
                } else if (fileName.substring(fileName.lastIndexOf("."),
                        fileName.length()).indexOf("doc") > -1) { // 2003版word
                    newName = fileName;
                } else {
                    newName = System.currentTimeMillis()
                            + fileName.substring(fileName.lastIndexOf("."), fileName
                                    .length());
                }
            } finally {
                lock.unlock();
            }

            File isD = new File(savePath);
            // 校验如果目录不存在，则创建目录
            if (!isD.isDirectory()) {
                isD.mkdirs();
            }

            if (!isD.exists()) {
                synchronized (UploadAction.class) {
                    isD.mkdirs();
                }
            }
            if (!isD.isDirectory()) {
                logger.error(isD.getName() + " must be a directory!");
            }
            // 获取文件输出流
            FileOutputStream fos = new FileOutputStream(savePath + "/" + newName);

            byte[] buffer = new byte[1024];

            // 获取内存中当前文件输入流
            InputStream in = new FileInputStream(files[0]);
            try {
                int num;
                while ((num = in.read(buffer)) > 0) {
                    fos.write(buffer, 0, num);
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
            } finally {
                in.close();
                fos.close();
            }
            if (cut) {
                ChangeImage c = new ChangeImage();
                try {
                    c.changeSize(savePath + "/" + newName, 100);
                } catch (Exception ignored) {
                    logger.error(ignored.getMessage());
                }
            }
            // render new file name
            if (fileName.substring(fileName.lastIndexOf("."), fileName.length()).indexOf(
                    "docx") > -1) { // 2011版word
                renderHtml(request.getScheme() + "://" + PathUtil.getPath("import.feed")
                        + newName);
            } else if (fileName.substring(fileName.lastIndexOf("."), fileName.length())
                    .indexOf("doc") > -1) { // 2003版word
                renderHtml(request.getScheme() + "://" + PathUtil.getPath("import.feed")
                        + newName);
            } else {
                renderHtml(newName);
            }
        }
        return null;
    }

    // TODO: 后期有 save 方法替换并需要更改小组后的路径代码

    /**
     * 文件保存
     * 
     * @param savePath
     *            保存路径
     * @param importPath
     *            导出路径
     * @return 生成路径
     * @throws Exception
     *             异常
     */
    private String saveTemp(String savePath, String importPath, boolean cut)
            throws Exception {
        HttpServletRequest request = getRequest();
        MultiPartRequestWrapper wrapper = (MultiPartRequestWrapper) request;
        if (wrapper == null)
            return null;

        File[] files = wrapper.getFiles("fileupload");
        String[] fileNames = wrapper.getFileNames("fileupload");
        String fileName = fileNames[0];

        if (files != null && files.length > 0) {
            getResponse().setCharacterEncoding("utf-8");
            final Lock lock = new ReentrantLock();
            String newName = null;
            lock.lock();
            try {
                // 加锁为防止文件名重复
                newName = System.currentTimeMillis()
                        + fileName
                                .substring(fileName.lastIndexOf("."), fileName.length());
            } finally {
                lock.unlock();
            }

            File isD = new File(savePath);
            // 校验如果目录不存在，则创建目录
            if (!isD.isDirectory()) {
                isD.mkdirs();
            }

            if (!isD.exists()) {
                synchronized (UploadAction.class) {
                    isD.mkdirs();
                }
            }
            if (!isD.isDirectory()) {
                logger.error(isD.getName() + " must be a directory!");
            }
            // 获取文件输出流
            FileOutputStream fos = new FileOutputStream(savePath + "/" + newName);

            byte[] buffer = new byte[1024];

            // 获取内存中当前文件输入流
            InputStream in = new FileInputStream(files[0]);
            try {
                int num;
                while ((num = in.read(buffer)) > 0) {
                    fos.write(buffer, 0, num);
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
            } finally {
                in.close();
                fos.close();
            }
            if (cut) {
                ChangeImage c = new ChangeImage();
                try {
                    c.changeSize(savePath + "/" + newName, 100);
                } catch (Exception ignored) {
                    logger.error(ignored.getMessage());
                }
            }
            renderHtml(request.getScheme() + "://" + importPath + newName);
        }
        return null;
    }

    /**
     * 小组头像
     */
    public void face() {
        String savePath = PathUtil.getPath("file.dis") + disid;
        try {
            saveTemp(savePath, PathUtil.getPath("import.dis") + disid + "/", true);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * 保存头像
     */
    public void updatePhoto() {

        try {
            String photoName = savePhoto(getRequest());

            Map<String, String> map = new HashMap<String, String>();
            map.put("src", photoName);
            map.put("status", "1");
            JSONObject jsonObject = JSONObject.fromObject(map);

            String callname = ESAPI.encoder().encodeForURL(getParameter("callback"));
            if (callname != null && !callname.equals("")) {
                renderHtml(callname + "(" + jsonObject + ")");

            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            // e.printStackTrace();
        }

    }

    private String savePhoto(HttpServletRequest request) {
        logger.debug("save photoing...");
        String photoPath = request.getParameter("photoPath");

        int imageWidth = Integer.parseInt(request.getParameter("txt_width"));
        int imageHeight = Integer.parseInt(request.getParameter("txt_height"));
        int cutTop = Integer.parseInt(request.getParameter("txt_top"));
        int cutLeft = Integer.parseInt(request.getParameter("txt_left"));
        // 截取长宽，前台传过来的，暂时未用，后台长宽由ImageHelper静态属性所定
        // int dropWidth = Integer.parseInt(photoPrams.get("txt_DropWidth"));
        // int dropHeight = Integer.parseInt(photoPrams.get("txt_DropHeight"));

        Rectangle rec = createPhotoCutRec(imageWidth, imageHeight, cutLeft, cutTop);

        String photoName = cusid + "-" + System.currentTimeMillis()
                + photoPath.substring(photoPath.lastIndexOf("."));
        File file = new File(PathUtil.getPath("file.cus") + photoName);
        File tempPic = new File(PathUtil.getPath("file.cus.temp") + photoPath);

        // 数字数据用int数组传入，长度为4，分别为，图片宽度，图片高度，截取位置高，截取位置左
        try {
            saveSubImage(tempPic, file, rec, new int[] { imageWidth, imageHeight,
                    cutLeft, cutTop });

            tempPic.delete();

        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        return photoName;
    }

    private Rectangle createPhotoCutRec(int imageWidth, int imageHeight, int cutLeft,
            int cutTop) {
        int recX = cutLeft > 0 ? cutLeft : 0;
        int recY = cutTop > 0 ? cutTop : 0;
        int recWidth = ImageHelper.CUS_PHOTO_WIDTH;
        int recHieght = ImageHelper.CUS_PHOTO_HEIGHT;
        if (cutLeft < 0) {
            // 注意curLeft 是负数
            if (imageWidth - cutLeft > ImageHelper.CUS_PHOTO_WIDTH) {
                recWidth = ImageHelper.CUS_PHOTO_WIDTH + cutLeft;
            } else {
                recWidth = imageWidth;
            }
        } else {
            if (imageWidth - cutLeft < ImageHelper.CUS_PHOTO_WIDTH) {
                recWidth = imageWidth - cutLeft;
            }
        }

        if (cutTop < 0) {
            // 注意curLeft 是负数
            if (imageHeight - cutTop > ImageHelper.CUS_PHOTO_HEIGHT) {
                recHieght = ImageHelper.CUS_PHOTO_HEIGHT + cutTop;
            } else {
                recHieght = imageHeight;
            }
        } else {
            if (imageHeight - cutTop < ImageHelper.CUS_PHOTO_HEIGHT) {
                recHieght = imageHeight - cutTop;
            }
        }
        return new Rectangle(recX, recY, recWidth, recHieght);
    }

    private static void saveSubImage(File srcImageFile, File descDir, Rectangle rect,
            int[] intParms) throws IOException {
        ImageHelper.cut(srcImageFile, descDir, rect, intParms);
    }

    // ~ getter/setter
    public String getDisid() {
        return disid;
    }

    public void setDisid(String disid) {
        this.disid = disid;
    }

    public String getCusid() {
        return cusid;
    }

    public void setCusid(String cusid) {
        this.cusid = cusid;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }
}
