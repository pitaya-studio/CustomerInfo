package com.trekiz.admin.common.utils;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * 图片处理工具类
 * */
public class ImageUtils {

//	/** 图片格式：.jpg*/
//	private static final String PICTRUE_FORMATE_JPG = "jpg";
//	/** 图片格式：.png*/
//	private static final String PICTRUE_FORMATE_PNG = "png";
//	/** 图片格式：.gif*/
//	private static final String PICTRUE_FORMATE_GIF = "gif";
//	
	/**
	 * 添加图片水印（平铺）
	 * @param iconPath 水印图片路径
	 * @param file 源图片文件
	 * @param alpha 水印设置透明度，0为全透明，1为不透明
	 * @param targetPath 文件的路径（文件夹）
	 * @param newName 在targetPath文件夹下，文件的名称
	 * @return 已覆盖水印的图片文件
	 * @author yang.wang
	 * */
	public static File markImageByIcon(String iconPath, MultipartFile file, float alpha, String targetPath, String newName) {
		File srcImgFile = null;
		try {

			srcImgFile = new File(targetPath);
			if (!srcImgFile.exists()) {
				srcImgFile.mkdirs();
			}
			srcImgFile = new File(targetPath + newName);
			FileCopyUtils.copy(file.getBytes(), srcImgFile);
			
			Image srcImg = ImageIO.read(srcImgFile);
			int srcWidth = srcImg.getWidth(null);
			int srcHeight = srcImg.getHeight(null);
			
			BufferedImage buffImage = new BufferedImage(srcWidth, srcHeight, BufferedImage.TYPE_INT_BGR);
			// 获取画笔对象
			Graphics2D g = buffImage.createGraphics();
			g.drawImage(srcImg, 0, 0, srcWidth, srcHeight, null);
			
			// 获取水印图片文件 
			Image iconImg = ImageIO.read(new File(iconPath));
			int iconWidth = iconImg.getWidth(null);
			int iconHeight = iconImg.getHeight(null);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
			
			// 水印平铺
			for (int x = 0; x < srcWidth; x += iconWidth) {
				for (int y = 0; y < srcHeight; y += iconHeight) {
					g.drawImage(iconImg, x, y, iconWidth, iconHeight, null);
				}
			}
			g.dispose();
			String[] split = newName.split("\\.");
			String ext = split[split.length - 1];
			ImageIO.write(buffImage, ext, srcImgFile);
			
		} catch (Exception e) {
			throw new RuntimeException("图片错误");
		}
		return srcImgFile;
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
