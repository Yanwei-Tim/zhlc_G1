/**
 * 
 */
package com.zhlt.g1app.basefunc;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.Date;

/**
 * @email 313044509@qq.com
 * @author kenneth
 * 
 */
public class SocketUtil {

	/**
	 * write string 2 a outputstream
	 * 
	 * @param str
	 *            to write string
	 * @param in
	 *            stream
	 * @throws IOException
	 */
	public static void writeStr2Stream(String str, OutputStream out)
			throws IOException {
		try {
			ObjectOutputStream oo = new ObjectOutputStream(out);
			oo.writeObject(str);
			oo.flush();
		} catch (IOException ex) {
			System.out.println(ex);
			ex.printStackTrace();
		}
	}

	public static void writeStr2StreamVideo(byte[] str, OutputStream out)
			throws IOException {
		try {
			DataOutputStream outdata = new DataOutputStream(out);
			outdata.writeInt(str.length);
			outdata.write(str);
			outdata.flush();

		} catch (IOException ex) {
			System.out.println(ex);
			ex.printStackTrace();
		}
	}

	/**
	 * read string from a inputstream
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static String readStrFromStream(InputStream in) throws IOException {
		// System.out.println(getNowTime() +
		// " : start to read string from stream");
		StringBuffer result = new StringBuffer("");

		// build buffered reader
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));

		// read 1024 bytes per time
		char[] chars = new char[2048];
		int len;

		try {
			while ((len = reader.read(chars)) != -1) {
				// if the length of array is 1M
				if (2048 == len) {
					// then append all chars of the array
					result.append(chars);
					System.out.println("readStrFromStream : "
							+ result.toString());
				}
				// if the length of array is less then 1M
				else {
					// then append the valid chars
					for (int i = 0; i < len; i++) {
						result.append(chars[i]);
						// System.out.println("readStrFromStream : " +
						// result.toString());
					}
					break;
				}
			}

		} catch (IOException e) {
			System.out.println(e);
			e.printStackTrace();
		}
		// System.out.println("end reading string from stream");
		return result.toString();
	}

	/**
	 * read string from a inputstream
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static byte[] readStrFromStreambyte(InputStream in) {

		// read 1024 bytes per time
		byte[] chars = null;

		try {
			int len = in.available();

			if (len > 0) {
				chars = new byte[len];
				in.read(chars);
			}
		} catch (IOException e) {
			System.out.println(e);
			e.printStackTrace();
		}
		// System.out.println("end reading string from stream");
		return chars;
	}

	public static byte[] readStrFromStreamdatabyte(InputStream in) {
		DataInputStream dataInput = new DataInputStream(in);
		int size = 0;
		byte[] data = null;
		try {
			size = dataInput.readInt();
			data = new byte[size];
			int len = 0;
			while (len < size) {
				len += dataInput.read(data, len, size - len);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return data;

	}

	public static String getNowTime() {
		return new Date().toString();
	}

}
