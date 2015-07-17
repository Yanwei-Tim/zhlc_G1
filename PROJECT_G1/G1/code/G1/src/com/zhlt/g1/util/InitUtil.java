/**
 *
 */
package com.zhlt.g1.util;

/**
 * @email 313044509@qq.com
 * @author kenneth 初始配置
 */
public class InitUtil {

    /**
     * 本地 server Socket端口号 9999
     */
    public static final int PORT = 9999;
    /**
     * 服务器 端口  7776
     */
    public static final  int PORTSERVER=7776;
    /**
     * 服务器端ip地址
     */
  //  public static final String SERVERIP="121.41.16.21";
   public static final String SERVERIP="192.168.1.130";
    public static final String NAME="862466020645081";
    public static final String KEY="123";
    public static final byte X = 0x0A;

    /**
     * 多久请求一次服务器 30s
     */
    public static final  int   SERVERTIME=1000*300;
    /**
     * log开关
     */
    public static final boolean DEBUG = true;
    /**
     * wifi名称
     */
    public static final String WIFIPNAME = "G1";
    /**
     * wifi密码
     */
    public static final String WIFIPWD = "11111111";


    /**
     * 上传图片照片地址
     */
    public final  static String HTTPPATH="http://192.168.1.130:8080/hjyPC/";
}
