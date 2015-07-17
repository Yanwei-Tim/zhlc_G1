/**
 * 
 */
package com.zhlt.g1.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.util.Date;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.zhlt.g1.camera.bean.CameraPhoto;

/**
 * @email 313044509@qq.com
 * @author kenneth
 * 
 */
public class SocketUtil {
	static Logger log = Log4jUtil.getLogger("com.zhlt.g1.SocketUtil");

	

	/**
	 * write string 2 a outputstream
	 * 
	 * @param str
	 *            to write string
	 * @param in
	 *            stream
	 * @throws IOException
	 */
	public static void writeStr2Stream(String str,Socket socket)  {
		try {
			if (socket != null) {
				ObjectOutputStream oo = new ObjectOutputStream(socket
						.getOutputStream());
				oo.writeObject(str);
				oo.flush();
			} else {
				if (InitUtil.DEBUG)
					log.info("Socket为空!");
			}
		} catch (IOException ex) {
			
			ex.printStackTrace();
		}finally{
			if(socket!=null){
				try {
					socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				socket=null;
			}
			
		}
	}
    /**
     * wifi 直连 发送
     * @param str
     * @param socket
     */
	public static void writeStr2StreamVideo(byte[] str,Socket  socket) {
		try {
			if (socket != null) {
				DataOutputStream outdata = new DataOutputStream(socket
						.getOutputStream());
				outdata.writeInt(str.length);
				outdata.write(str);
				outdata.flush();
				
				
			} else {
				if (InitUtil.DEBUG)
					log.info("Socket为空!");
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}finally{
			if(socket!=null){
				try {
					socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
				socket=null;
				}
			}
		}
	}
	 /**
     *与 service  连接
     * @param str
     * @param socket
     */
	public static void writeStr2StreamVideoService(byte[] str,Socket  socket,String imei) {
		try {
			if (socket != null) {
				DataOutputStream outdata = new DataOutputStream(socket
						.getOutputStream());
				outdata.writeInt(str.length);
				outdata.write(str);
				outdata.flush();
				//socket.shutdownOutput();
				/*JSONObject  obj=new JSONObject();
				obj.put("code", Codes.CODE1012);
				obj.put("imei", imei);
				byte[]  bs=obj.toString().trim().getBytes();
				outdata.writeInt(bs.length);
				outdata.write(bs);
				outdata.flush();*/
			} else {
				if (InitUtil.DEBUG)
					log.info("Socket为空!");
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		} 
	}
	public static void writeStr2Streamphoto(CameraPhoto cp,Socket soct) throws IOException {
		try {
			if (soct != null) {
				DataOutputStream dos = new DataOutputStream(soct.getOutputStream());    
	            FileInputStream fis = new FileInputStream(cp.getPath());    
	            int size = fis.available();  
	            byte[] data = new byte[size];    
	            fis.read(data);    
	            dos.writeInt(size);    
	            dos.write(data);    
	              
	            dos.flush();    
	            dos.close();    
	            fis.close();    
			
				
			} else {
				if (InitUtil.DEBUG)
					log.info("Socket为空!");
			}
		} catch (IOException ex) {
			
			ex.printStackTrace();
		}finally{
			if(soct!=null){
				try {
					soct.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
					soct=null;
				}
			}
		}
	}
	/**
	 * read string from a inputstream
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static byte[] readStrFromStream(Socket socket)  {

		// read 1024 bytes per time
		if (socket != null) {
			byte[] chars = null;

			try {
				InputStream in = socket.getInputStream();
				int len = in.available();

				if (len > 0) {
					chars = new byte[len];
					in.read(chars);
				}

			} catch (IOException e) {
				
				e.printStackTrace();
			}finally{
				if(socket!=null){
					try {
						socket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}finally{
						socket=null;
					}
				}
			}
			
			// System.out.println("end reading string from stream");
			return chars;
		} else {
			if (InitUtil.DEBUG)
				log.info("Socket为空!");
			return null;
		}
	}

	/**
	 * read string from a inputstream
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static byte[] readStrFromStreambyte(Socket  sock) {

		// read 1024 bytes per time
		
			byte[] chars = null;
        log.info("111111111111111111");
			try {
			   	sock.setSoTimeout(1000*30);
                log.info("222222");
				InputStream in =sock.getInputStream();
                log.info("g1close");
				DataInputStream  dis=new DataInputStream(in);
				int size = dis.readInt();

				if (size > 0) {
					chars = new byte[size];
					int lens=0;
					while(lens<size){
						lens += dis.read(chars, lens, size-lens);
					}
					
				}

			} catch (IOException e) {
                if (InitUtil.DEBUG)log.error("读取超时："+e.getStackTrace());
				//e.printStackTrace();
			}
			// System.out.println("end reading string from stream");
			return chars;
		
	}

	public static String getNowTime() {
		return new Date().toString();
	}

	
}
