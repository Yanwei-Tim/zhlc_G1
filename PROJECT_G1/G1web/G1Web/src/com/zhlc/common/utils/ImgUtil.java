package com.zhlc.common.utils;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * @author anquan
 * @desc 该工具类提供对图片的处理1-等比缩放 2-非等比缩放 3-指定坐标裁剪.<br>
 * @date 2014-10-30 上午11:55:34
 */
public class ImgUtil {
	// 输出打印日志
	public static boolean IS_DEBUG = false;
	/*====================== 等比缩放，固定宽高 ======================*/
	/**
	 * 等比缩放图片
	 * <li>按原图片等比缩放图片.</li>
	 * <li>缩放后的图片宽高小于固定宽高时，会有留白.</li>
	 * <li>缩放比例scale为空或scale<=0时，scale以固定宽高比例为准.</li>
	 * <li>缩放比例scale>0时，scale取最小缩放比例.</li>
	 * @param imgSrc
	 *            原图片路径
	 * @param imgDest
	 *            输出图片路径
	 * @param scale
	 *            缩放比例
	 * @param fixed_width
	 *            固定宽
	 * @param fixed_height
	 *            固定高
	 * @throws Exception
	 */
	public static boolean zoomImage(String imgSrc, String imgDest, Float scale, Integer fixed_width, Integer fixed_height) throws Exception {
		if (imgDest == null || imgDest.trim().length() == 0) {
			if (IS_DEBUG) {
				System.err.println("等比缩放图片：输出图片路径[" + imgDest + "]错误。。。");
			}
			return false;
		}
		File file = new File(imgSrc);
		if (file == null || file.exists() == false || file.isFile() == false) {
			if (IS_DEBUG) {
				System.err.println("等比缩放图片：[" + imgSrc + "]文件不存在。。。");
			}
			return false;
		}
		InputStream input = new FileInputStream(file);
		if (input == null) {
			if (IS_DEBUG) {
				System.err.println("等比缩放图片：输入流为空。。。");
			}
			return false;
		}
		Image srcImage = javax.imageio.ImageIO.read(input);
		if (srcImage == null) {
			if (IS_DEBUG) {
				System.err.println("等比缩放图片：不是有效的图片。。。");
			}
			return false;
		}
		int src_w = srcImage.getWidth(null); // 源图宽
		int src_h = srcImage.getHeight(null);// 源图高

		// ----------- 缩放比例 -----------
		// 缩放比例 为空时
		if (scale == null || scale <= 0) {
			if (fixed_height == null && fixed_width != null && fixed_width > 0) {
				// 以固定宽为准的缩放比例
				scale = fixed_width * 1.0F / src_w;
				// 输出图片-固定宽高
				fixed_width = (int) (src_w * scale);
				fixed_height = (int) (src_h * scale);
			} else if (fixed_width == null && fixed_height != null
					&& fixed_height > 0) {
				// 以固定高为准的缩放比例
				scale = fixed_height * 1.0F / src_h;
				// 输出图片-固定宽高
				fixed_width = (int) (src_w * scale);
				fixed_height = (int) (src_h * scale);
			} else if (fixed_width != null && fixed_width != null
					&& fixed_width > 0 && fixed_height != null
					&& fixed_height != null && fixed_height > 0) {
				// 固定宽高时，取最小缩放比例
				scale = Math.min(fixed_width * 1.0F / src_w, fixed_height
						* 1.0F / src_h);
			} else {
				// 宽高为空或小于等于0时，默认缩放比例=1
				scale = 1F;
				fixed_width = (int) (src_w * scale);
				fixed_height = (int) (src_h * scale);
			}
		}
		// 缩放比例 不为空时
		else {
			if (fixed_height == null && fixed_width != null && fixed_width > 0) {
				// 以固定宽为准的缩放比例
				scale = Math.min(scale, fixed_width * 1.0F / src_w);
				// 输出图片-固定高
				fixed_height = (int) (src_h * fixed_width * 1.0F / src_w);
			} else if (fixed_width == null && fixed_height != null
					&& fixed_height > 0) {
				// 以固定高为准的缩放比例
				scale = Math.min(scale, fixed_height * 1.0F / src_h);
				// 输出图片-固定宽
				fixed_width = (int) (src_w * fixed_height * 1.0F / src_h);
			} else if (fixed_width != null && fixed_width != null
					&& fixed_width > 0 && fixed_height != null
					&& fixed_height != null && fixed_height > 0) {
				// 固定宽高时，取最小缩放比例
				scale = Math.min(fixed_width * 1.0F / src_w, fixed_height
						* 1.0F / src_h);
			} else {
				if ((long) (Math.max(src_w, src_h) * scale) > Integer.MAX_VALUE) {
					// 重置最大缩放比例
					scale = Integer.MAX_VALUE * 1.0F
							/ ((long) (Math.max(src_w, src_h) * scale));
				}
				// 输出图片-固定宽高
				fixed_width = (int) (src_w * scale);
				fixed_height = (int) (src_h * scale);
			}
		}
		// ----------- 输出图片的真实宽高 -----------
		int new_w = (int) (src_w * scale);
		int new_h = (int) (src_h * scale);
		// ----------- 图片根据长宽留白 -----------
		fixed_width = fixed_width <= 0 ? 1 : fixed_width;
		fixed_height = fixed_height <= 0 ? 1 : fixed_height;
		new_w = new_w <= 0 ? 1 : new_w;
		new_h = new_h <= 0 ? 1 : new_h;

		BufferedImage destImage = new BufferedImage(fixed_width, fixed_height,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g = destImage.createGraphics();
		g.setColor(Color.white);
		g.fillRect(0, 0, fixed_width, fixed_height);
		g.drawImage(srcImage, (fixed_width - new_w) / 2,
				(fixed_height - new_h) / 2, new_w, new_h, null);
		g.dispose();
		// ----------- 输出图片 -----------
		FileOutputStream out = new FileOutputStream(imgDest);
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
		encoder.encode(destImage);
		out.close();
		if (IS_DEBUG) {
			System.out.println("等比缩放图片：原图片宽高为[" + src_w + "x" + src_h
					+ "]，输出图片的固定宽高为[" + fixed_width + "x" + fixed_height
					+ "]，输出图片的真实宽高为[" + new_w + "x" + new_h 
					+ "]，缩放比例为[" + scale + "].");
		}
		return true;
	}


	/*
	 * ====================== 非等比缩放 ======================
	 */
	/**
	 * 非等比缩放图片
	 * <li>读取文件</li>
	 * 
	 * @param imgSrc
	 *            原图片路径
	 * @param imgDest
	 *            输出图片路径
	 * @param fixed_width
	 *            固定宽(默认原图片宽)
	 * @param fixed_height
	 *            固定高(默认原图片高)
	 * @return
	 * @throws Exception
	 */
	public static boolean zoomImage(String imgSrc, String imgDest,
			Integer fixed_width, Integer fixed_height) throws Exception {
		if (imgDest == null || imgDest.trim().length() == 0) {
			if (IS_DEBUG) {
				System.err.println("非等比缩放图片：输出图片路径[" + imgDest + "]错误。。。");
			}
			return false;
		}
		File file = new File(imgSrc);
		if (file.exists() == false || file.isFile() == false) {
			if (IS_DEBUG) {
				System.err.println("非等比缩放图片：[" + imgSrc + "]文件不存在。。。");
			}
			return false;
		}
		
		InputStream input = new FileInputStream(file);
		if (input == null) {
			if (IS_DEBUG) {
				System.err.println("非等比缩放图片：输入流为空。。。");
			}
			return false;
		}
		Image srcImage = javax.imageio.ImageIO.read(input);
		if (srcImage == null) {
			if (IS_DEBUG) {
				System.err.println("非等比缩放图片：不是有效的图片。。。");
			}
			return false;
		}
		int src_w = srcImage.getWidth(null); // 源图宽
		int src_h = srcImage.getHeight(null);// 源图高

		// 固定输出图片的宽高
		if (fixed_width == null || fixed_width <= 0) {
			fixed_width = src_w;
		}
		if (fixed_height == null || fixed_height <= 0) {
			fixed_height = src_h;
		}
		// 重绘图片
		BufferedImage destImage = new BufferedImage(fixed_width, fixed_height,
				BufferedImage.TYPE_INT_RGB);
		destImage.getGraphics().drawImage(srcImage, 0, 0, fixed_width,
				fixed_height, null);
		// 输出图片
		FileOutputStream out = new FileOutputStream(imgDest);
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
		encoder.encode(destImage);
		out.close();
		if (IS_DEBUG) {
			System.out.println("非等比缩放图片：原图片宽高为[" + src_w + "x" + src_h
					+ "]，输出图片的宽高为[" + fixed_width + "x" + fixed_height +"].") ;
		}
		return true;
	}
	public static boolean zoomImage(File file, String imgDest, Integer fixed_width, Integer fixed_height) throws Exception {
		InputStream input = new FileInputStream(file);
		if (input == null) {
			if (IS_DEBUG) {
				System.err.println("非等比缩放图片：输入流为空。。。");
			}
			return false;
		}
		Image srcImage = javax.imageio.ImageIO.read(input);
		if (srcImage == null) {
			if (IS_DEBUG) {
				System.err.println("非等比缩放图片：不是有效的图片。。。");
			}
			return false;
		}
		int src_w = srcImage.getWidth(null); // 源图宽
		int src_h = srcImage.getHeight(null);// 源图高
		
		// 固定输出图片的宽高
		if (fixed_width == null || fixed_width <= 0) {
			fixed_width = src_w;
		}
		if (fixed_height == null || fixed_height <= 0) {
			fixed_height = src_h;
		}
		// 重绘图片
		BufferedImage destImage = new BufferedImage(fixed_width, fixed_height,
				BufferedImage.TYPE_INT_RGB);
		destImage.getGraphics().drawImage(srcImage, 0, 0, fixed_width,
				fixed_height, null);
		// 输出图片
		FileOutputStream out = new FileOutputStream(imgDest);
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
		encoder.encode(destImage);
		out.close();
		if (IS_DEBUG) {
			System.out.println("非等比缩放图片：原图片宽高为[" + src_w + "x" + src_h
					+ "]，输出图片的宽高为[" + fixed_width + "x" + fixed_height +"].") ;
		}
		return true;
	}
	
	/**
	 * ====================== 裁剪图片 ======================
	 *
	 * 非等比缩放图片
	 * @param srcImg
	 *            原图片路径
	 * @param destImg
	 *            输出图片路径
	 * @param left
	 *            左边距
	 * @param top
	 *            上边距
	 * @param width
	 *            截剪宽度
	 * @param height
	 *            截剪高度
	 * @return
	 * @throws IOException
	 */
	public static boolean cutImage(String srcImg, String destImg, int left,
			int top, Integer width, Integer height) throws IOException {
		if (destImg == null || destImg.trim().length() == 0) {
			if (IS_DEBUG) {
				System.err.println("图片截剪：输出图片路径[" + destImg + "]错误。。。");
			}
			return false;
		}
		File file = new File(srcImg);
		if (file == null || file.exists() == false || file.isFile() == false) {
			if (IS_DEBUG) {
				System.err.println("图片截剪：[" + srcImg + "]文件不存在。。。");
			}
			return false;
		}
		Image srcImage = javax.imageio.ImageIO.read(file);
		if (srcImage == null) {
			if (IS_DEBUG) {
				System.err.println("图片截剪：源图不是有效的图片。。。");
			}
			return false;
		}
		StringBuffer sb = null;
		boolean params_error = false;
		if (IS_DEBUG) {
			sb = new StringBuffer("图片截剪：");
		}
		int src_w = srcImage.getWidth(null); // 源图宽
		int src_h = srcImage.getHeight(null);// 源图高

		if (left < 0 || left >= src_w) {
			if (IS_DEBUG) {
				sb.append("左边距超出原图有效宽度！ ");
			}
			params_error = true;
		}
		if (top < 0 || top >= src_h) {
			if (IS_DEBUG) {
				sb.append("上边距超出原图有效高度！ ");
			}
			params_error = true;
		}
		if(width != null && width <= 0 ){
			if (IS_DEBUG) {
				sb.append("截剪宽度不能小于或等于 0 ！ ");
			}
			params_error = true;
		}
		if(height != null && height <= 0 ){
			if (IS_DEBUG) {
				sb.append("截剪高度不能小于或等于 0 ！ ");
			}
			params_error = true;
		}
		if(params_error){
			if (IS_DEBUG) {
				System.err.println(sb.toString());
			}
			return false;
		}
		
		// 目标图片宽
		if (width == null || width > src_w || width + left > src_w) {
			width = src_w - left;
		}
		// 目标图片高
		if (height == null || height > src_h || height + top > src_h) {
			height = src_h - top;
		}
		// 目标图片
		ImageFilter cropFilter = new CropImageFilter(left, top, width, height);
		Image cutImage = Toolkit.getDefaultToolkit().createImage(
				new FilteredImageSource(srcImage.getSource(), cropFilter));
		// 重绘图片
		BufferedImage tag = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		Graphics g = tag.getGraphics();
		g.drawImage(cutImage, 0, 0, null); // 绘制缩小后的图
		g.dispose();
		// 输出为文件
		FileOutputStream out = new FileOutputStream(destImg);
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
		encoder.encode(tag);
		out.close();
		if (IS_DEBUG) {
			System.out.println("图片截剪：原图片宽高为[" + src_w + " ×　" + src_h
					+ "]，输出图片的宽高为[" + width + " ×　" + height +"].") ;
		}
		return true;
	}
	
	/**
	 * 传入源文件路径得到新文件路径
	 * @param imgSrc
	 * @param param
	 * @return
	 */
	public static String getFileName(File file, String param){
		String path = file.getAbsolutePath();
		String suffix = path.substring(path.lastIndexOf("."));//图片后缀
		String preSuffix = path.substring(0,path.lastIndexOf("."));//图片前部分
		return preSuffix + param + suffix;
	}
	
	/**
	 * 以下是做测试用的实例
	 * @param args
	 */
	
	public static void main(String[] args) {
		
		String SCALE_ = "等比_";
		String NOSCALE_ = "非等比_";
		String WH_ = "固定宽高_";
		String CUT_ ="截剪_";
		
		String imgSrc = "F:/picture/1.bmp";
		Float scale = null;
		Integer fixed_width = 800;
		Integer fixed_height = 800;
		try {
			File f = new File(imgSrc);
			int lastIndex = f.getAbsolutePath().lastIndexOf(File.separator);
			String path = f.getAbsolutePath().substring(0 , lastIndex+1);//得到文件目录(方便添加自己想要的标示)
			
			//等比缩放
			zoomImage(imgSrc, path + SCALE_ + WH_ + f.getName(), scale, fixed_width, fixed_height);
			zoomImage(imgSrc, getFileName(f, "_s"), scale, fixed_width, fixed_height);
			
			//非等比缩放
			zoomImage(imgSrc, path + NOSCALE_ + WH_ + f.getName(), fixed_width, fixed_height);
			
			//裁剪图片
			int left = 0 ;
			int top = 0 ;
			Integer width = 200;
			Integer height = 100;
			cutImage(imgSrc, path+ CUT_ + f.getName(), left, top, width, height);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
