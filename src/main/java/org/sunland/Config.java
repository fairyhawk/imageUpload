package org.sunland;

/**
 * 系统的全局常量配置类
 * 
 * @author Baisheng E-mail:baisheng@gmail.com
 * @version 2009-2-27 下午02:41:49
 */
public interface Config {
	/** 配置文件名 */
	public static String CONFIG_FILE = "config.properties";
	/** 配置文件路径 */
	public static String CONFIG_PATH = "/config/";
	/** Socket 主机 */
	public static String SOCKET_HOST = "socket.host";
	/** UAC 服务端口号 */
	public static String SOCKET_LISTEN = "socket.listen";
	/** Socket 服务端口号 */
	public static String SOCKET_PORT = "socket.port";
	/** mta 主机 */
	public static String MTA_HOST = "mta.host";
	/** mta 起始端口 */
	public static String MTA_START_PORT = "mta.start";
	/** mta 终止端口 */
	public static String MTA_END_PORT = "mta.end";
	/** openfire 服务端口号 */
	public static String OPENFIRE_PORT = "openfire.serverport";
	/** Socket 开启的线程数量 */
	public static String SOCKET_THREAD = "socket.thread";
	/** 正确的状态标识 */
	public static String STATUS_OK = "200";
	/** 错误的状态标识 */
	public static String STATUS_WARN = "400";
	/** openfire Socket 主机 */
	public static String OPENFIRE_HOST = "openfire.host";
	/** openfire Socket 服务端口号 */
	public static String OPENFIRE_LISTEN = "openfire.listen";
	/** ud Socket 主机 */
	public static String UD_HOST = "ud.host";
	/** ud Socket 服务端口号 */
	public static String UD_LISTEN = "ud.listen";
	/** UD命令 */
	public static byte[] SOCKET_SENDUD = { 85, 68 };

}
