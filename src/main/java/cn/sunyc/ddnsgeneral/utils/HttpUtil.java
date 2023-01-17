package cn.sunyc.ddnsgeneral.utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URL;
import java.util.Map;

/**
 * @author 孙玉朝
 */
@SuppressWarnings("all")
public class HttpUtil {
    /**
     * 请求类型： GET
     */
    public final static String GET = "GET";
    /**
     * 请求类型： POST
     */
    public final static String POST = "POST";

    /**
     * 模拟Http Post请求
     *
     * @param urlStr 请求路径
     * @return 响应内容
     */
    public static String post(String urlStr) throws Exception {
        return post(urlStr, null, null);
    }

    /**
     * 模拟Http Post请求
     *
     * @param urlStr   请求路径
     * @param paramMap 请求参数
     * @return 响应内容
     */
    public static String post(String urlStr, Map<String, String> paramMap) throws Exception {
        return post(urlStr, paramMap, null);
    }

    /**
     * 模拟Http Post请求
     *
     * @param urlStr    请求路径
     * @param paramMap  请求参数
     * @param headerMap 请求头
     * @return 响应内容
     */
    public static String post(String urlStr, Map<String, String> paramMap, Map<String, String> headerMap) throws Exception {
        HttpURLConnection conn = null;
        PrintWriter writer = null;
        try {
            // 创建URL对象
            //URL url = new URL(urlStr);
            URL url = new URL(new URI(urlStr).toASCIIString());
            // 获取请求参数
            String param = getParamString(paramMap);
            // 获取URL连接
            conn = (HttpURLConnection) url.openConnection();
            // 设置通用请求属性
            setHttpUrlConnection(conn, POST);
            // 设置请求头信息
            setHttpUrlConnectionHeader(conn, headerMap);
            // 建立实际的连接
            conn.connect();
            // 将请求参数写入请求字符流中
            writer = new PrintWriter(conn.getOutputStream());
            writer.print(param);
            writer.flush();
            // 读取响应的内容
            return readResponseContent(conn.getInputStream());
        } finally {
            if (null != conn)
                conn.disconnect();
            if (null != writer)
                writer.close();
        }
    }


    /**
     * 模拟Http Post请求
     *
     * @param urlStr   请求路径
     * @param paramMap 请求参数
     * @return 响应内容
     */
    public static HttpURLConnection post_stream(String urlStr, Map<String, String> paramMap) throws Exception {
        HttpURLConnection conn = null;
        PrintWriter writer = null;
        try {
            // 创建URL对象
            URL url = new URL(urlStr);
            // 获取请求参数
            String param = getParamString(paramMap);
            // 获取URL连接
            conn = (HttpURLConnection) url.openConnection();
            // 设置通用请求属性
            setHttpUrlConnection(conn, POST);
            // 建立实际的连接
            conn.connect();
            // 将请求参数写入请求字符流中
            writer = new PrintWriter(conn.getOutputStream());
            writer.print(param);
            writer.flush();
            // 读取响应的内容
            return conn;
        } finally {
			/*if (null != conn)
				conn.disconnect();*/
            if (null != writer)
                writer.close();
        }
    }


    /**
     * 模拟Http Get请求
     *
     * @param urlStr   请求路径
     * @param paramMap 请求参数
     * @return 响应内容
     */
    public static String get(String urlStr, Map<String, String> paramMap, Map<String, String> headerMap) throws Exception {
        urlStr = urlStr + "?" + getParamString(paramMap);
        HttpURLConnection conn = null;
        try {
            // 创建URL对象
            URL url = new URL(urlStr);
            // 获取URL连接
            conn = (HttpURLConnection) url.openConnection();
            // 设置通用的请求属性
            setHttpUrlConnection(conn, GET);
            // 设置请求头信息
            setHttpUrlConnectionHeader(conn, headerMap);
            // 建立实际的连接
            conn.connect();
            // 获取响应的内容
            return readResponseContent(conn.getInputStream());
        } finally {
            if (null != conn)
                conn.disconnect();
        }
    }

    /**
     * 模拟Http Get请求
     *
     * @param urlStr   请求路径
     * @param paramMap 请求参数
     * @return 响应内容
     */
    public static String get(String urlStr, Map<String, String> paramMap) throws Exception {
        urlStr = urlStr + "?" + getParamString(paramMap);
        HttpURLConnection conn = null;
        try {
            // 创建URL对象
            URL url = new URL(urlStr);
            // 获取URL连接
            conn = (HttpURLConnection) url.openConnection();
            // 设置通用的请求属性
            setHttpUrlConnection(conn, GET);
            // 建立实际的连接
            conn.connect();
            // 获取响应的内容
            return readResponseContent(conn.getInputStream());
        } finally {
            if (null != conn)
                conn.disconnect();
        }
    }

    /**
     * 读取响应字节流并将之转为字符串
     *
     * @param in 要读取的字节流
     * @return 响应的字符串
     */
    private static String readResponseContent(InputStream in) throws IOException {
        Reader reader = null;
        StringBuilder content = new StringBuilder();
        try {
            reader = new InputStreamReader(in);
            char[] buffer = new char[1024];
            int head;
            while ((head = reader.read(buffer)) > 0) {
                content.append(new String(buffer, 0, head));
            }
            return content.toString();
        } finally {
            if (null != in)
                in.close();
            if (null != reader)
                reader.close();
        }
    }

    /**
     * 设置Http连接属性
     *
     * @param conn http连接
     */
    private static void setHttpUrlConnection(HttpURLConnection conn, String requestMethod) throws ProtocolException {
        conn.setRequestMethod(requestMethod);
        conn.setRequestProperty("accept", "*/*");
        conn.setRequestProperty("Accept-Language", "zh-CN");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)");
        conn.setRequestProperty("Proxy-Connection", "Keep-Alive");
        if (POST.equals(requestMethod)) {
            conn.setDoOutput(true);
            conn.setDoInput(true);
        }
    }


    /**
     * 设置Http请求头信息
     *
     * @param conn http连接
     */
    private static void setHttpUrlConnectionHeader(HttpURLConnection conn, Map<String, String> headerMap) throws ProtocolException {
        if (null == headerMap || headerMap.isEmpty()) {
            return;
        }
        for (String key : headerMap.keySet()) {
            conn.setRequestProperty(key, headerMap.get(key));
        }
    }

    /**
     * 将参数转为路径字符串
     *
     * @param paramMap 参数
     * @return 字符串
     */
    private static String getParamString(Map<String, String> paramMap) {
        if (null == paramMap || paramMap.isEmpty()) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (String key : paramMap.keySet()) {
            builder.append("&").append(key).append("=").append(paramMap.get(key));
        }
        return builder.deleteCharAt(0).toString();
    }

    /**
     * 将html代码转换成txt格式
     *
     * @param html       html代码
     * @param filteEnter 是否过滤回车、换行、制表符
     * @param br2Enter   是否把br转换成换行
     * @return 文本
     */
    public static String html2txt(String html, boolean filteEnter, boolean br2Enter) {
        //把br转换成换行
        if (br2Enter) {
            html = html.replaceAll("<br>", "\r\n");
            html = html.replaceAll("</br>", "\r\n");
        }

        //剔出<html>的标签
        String txtcontent = html.replaceAll("</?[^>]+>", "");


        //去除字符串中的空格,回车,换行符,制表符、
        if (filteEnter) {
            txtcontent = txtcontent.replaceAll("\\s*|\t|\r|\n|&nbsp;|&gt;", "");
        }
        return txtcontent;
    }


}
