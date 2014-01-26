package com.yizhilu.os.image.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yizhilu.os.core.util.FileUtil;
import com.yizhilu.os.core.util.PropertyUtil;

/**
 * 上传图片用
 * 
 * @ClassName com.supergenius.sns.image.controller.UploadImageContoller
 * @description
 * @author : qinggang.liu bis@foxmail.com
 * @Create Date : 2014-1-11 下午2:42:03
 */
@Controller
public class UploadImageContoller {

	// 读取配置文件类
	public static PropertyUtil propertyUtil = PropertyUtil
			.getInstance("project");

	private static Logger logger = Logger.getLogger(UploadImageContoller.class);

	public static Gson gson = new GsonBuilder().setDateFormat(
			"yyyy-MM-dd HH:mm:ss").create();
	public static JsonParser jsonParser = new JsonParser();

	/**
	 * kindeditor4.x编辑器中上传图片,返回ke中需要的url和error值 注意：同域中本方法直接返回json格式字符即可
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/imgk4", method = RequestMethod.POST)
	public String img(HttpServletRequest request, HttpServletResponse response) {
		String referer = request.getHeader("referer");
		Pattern p = Pattern.compile("([a-z]*:(//[^/?#]+)?)?",
				Pattern.CASE_INSENSITIVE);
		Matcher mathcer = p.matcher(referer);
		logger.info("referer:" + referer);
		if (mathcer.find()) {
			String callBackPath = mathcer.group();// 请求来源
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			MultipartFile imgFile = multipartRequest.getFile("imgFile");
			String[] paths = FileUtil.getSavePathByRequest(request);

			JsonObject json = FileUtil.saveImage(imgFile, paths);

			// 编辑器中需要返回完整的路径
			json.addProperty("url", propertyUtil.getProperty("import.root")
					+ json.get("url").getAsString());
			// 同域时直接返回json即可无需redirect
			String url = "redirect:" + callBackPath
					+ "/kindeditor/plugins/image/redirect.html#"
					+ json.toString();
			return url;
		}
		logger.info("img ok");
		return null;
	}

	/**
	 * kindeditor4.x使用redirect.html kindeditor3.5中使用call_back.html 单个图片按钮时
	 * 上传方法集合，根据参数匹配属性文件 模块提供,返回的是图片的全路径 base:项目 param：模块 cusid.用户id
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/gok4", method = RequestMethod.POST)
	public String go(HttpServletRequest request, HttpServletResponse response) {
		String referer = request.getHeader("referer");
		Pattern p = Pattern.compile("([a-z]*:(//[^/?#]+)?)?",
				Pattern.CASE_INSENSITIVE);
		Matcher mathcer = p.matcher(referer);

		if (mathcer.find()) {
			String callBackPath = mathcer.group();// 请求来源
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			MultipartFile imgFile = multipartRequest.getFile("fileupload");
			String[] paths = FileUtil.getSavePathByRequest(request);
			JsonObject json = FileUtil.saveImage(imgFile, paths);
			// 同域时直接返回json即可无需redirect
			String url = "redirect:" + callBackPath
					+ "/kindeditor/plugins/image/redirect.html#"
					+ json.get("url").getAsString();
			logger.info("++++upload img return:" + url);
			return url;
		} else {
			return null;
		}
	}

	/**
	 * kindeditor3.5.x编辑器中上传图片,返回ke中需要的url和error值 注意：同域中本方法直接返回json格式字符即可
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/imgk3", method = RequestMethod.POST)
	@ResponseBody
	public String imgk3(HttpServletRequest request, HttpServletResponse response) {
		String referer = request.getHeader("referer");
		Pattern p = Pattern.compile("([a-z]*:(//[^/?#]+)?)?",
				Pattern.CASE_INSENSITIVE);
		Matcher mathcer = p.matcher(referer);
		logger.info("referer:" + referer);
		if (mathcer.find()) {
			StringBuffer buffer = new StringBuffer();
			String callBackPath = mathcer.group();// 请求来源
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			MultipartFile imgFile = multipartRequest.getFile("imgFile");
			String[] paths = FileUtil.getSavePathByRequest(request);
			JsonObject json = FileUtil.saveImage(imgFile, paths);
			// 编辑器中需要返回完整的路径
			json.addProperty("url", propertyUtil.getProperty("import.root")
					+ json.get("url").getAsString());
			if (!("0").equals(json.get("error").toString())) {
				buffer.append("<html><head><title>Insert Image</title><meta http-equiv='content-type' content='text/html; charset=utf-8'/></head><body>");
				buffer.append("<script type='text/javascript'>");
				buffer.append("alert('").append(json.get("message"))
						.append("!')");
				buffer.append("</script>");
				buffer.append("</body></html>");
			} else {
				// 同域时直接返回json即可无需call_back
				buffer.append("<html><head>\n"
						+ "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n"
						+ "<title>PIC UPLOAD</title>\n"
						+ "<script type=\"text/javascript\">\n"
						+ "    var upload_callback = function(){\n"
						+ "        var iframe_proxy = document.createElement('iframe');\n"
						+ "        iframe_proxy.style.display = 'none';\n"
						+ "        iframe_proxy.src = '"
						+ callBackPath
						+ "/kindeditor/plugins/image/call_back.html#'+encodeURIComponent('"
						+ json.getAsString() + "');\n"
						+ "        document.body.appendChild(iframe_proxy);\n"
						+ "    };\n" + "</script>\n" + "</head>\n"
						+ "<body onload=\"upload_callback();\">\n" + "\n"
						+ "</body></html>");
			}
			return buffer.toString();
		}
		logger.info("img ok");
		return null;
	}

	/**
	 * 点击头像上传face()。上传到临时目录，可以定期清除此目录
	 * 
	 * 点击保存时saveface()，读取临时目录下的文件，按照页面传来的坐标截取图片
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/face", method = RequestMethod.POST)
	@ResponseBody
	public String face(HttpServletRequest request, HttpServletResponse response) {
		String referer = request.getHeader("referer");
		Pattern p = Pattern.compile("([a-z]*:(//[^/?#]+)?)?",
				Pattern.CASE_INSENSITIVE);
		Matcher mathcer = p.matcher(referer);

		if (mathcer.find()) {
			String callBackPath = mathcer.group();// 请求来源
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			MultipartFile imgFile = multipartRequest.getFile("fileupload");
			String[] paths = FileUtil.getTempSavePathByRequest(request);
			JsonObject json = FileUtil.saveImage(imgFile, paths);
			// 同域时直接返回json即可无需redirect
			String url = "redirect:" + callBackPath
					+ "/kindeditor/plugins/image/redirect.html#"
					+ json.get("url").getAsString();
			logger.info("++++upload img return:" + url);
			return url;
		} else {
			return null;
		}
	}

	/**
	 * 
	 * 保存头像，读取临时目录下的文件，按照页面传来的坐标截取图片
	 * 
	 * @param request
	 * @param response
	 */

	@RequestMapping(value = "/saveface", method = RequestMethod.POST)
	@ResponseBody
	public String saveface(HttpServletRequest request,
			HttpServletResponse response) {
		String referer = request.getHeader("referer");
		Pattern p = Pattern.compile("([a-z]*:(//[^/?#]+)?)?",
				Pattern.CASE_INSENSITIVE);
		Matcher mathcer = p.matcher(referer);

		if (mathcer.find()) {
			String callBackPath = mathcer.group();// 请求来源
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			MultipartFile imgFile = multipartRequest.getFile("fileupload");
			String[] paths = FileUtil.getTempSavePathByRequest(request);
			JsonObject json = FileUtil.saveImage(imgFile, paths);
			// 同域时直接返回json即可无需redirect
			String url = "redirect:" + callBackPath
					+ "/kindeditor/plugins/image/redirect.html#"
					+ json.get("url").getAsString();
			logger.info("++++upload img return:" + url);
			return url;
		} else {
			return null;
		}
	}

}