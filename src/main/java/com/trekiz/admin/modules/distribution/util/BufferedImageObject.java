package com.trekiz.admin.modules.distribution.util;

import java.awt.image.BufferedImage;
import jp.sourceforge.qrcode.data.QRCodeImage;

/**
 * 图片解析工具类
 * @author gaoyang
 * @Time 2017-01-05 20:14
 */
public class BufferedImageObject implements QRCodeImage {

	BufferedImage bufImg;

	public BufferedImageObject(BufferedImage bufImg) {
		this.bufImg = bufImg;
	}

	@Override
	public int getHeight() {
		return bufImg.getHeight();
	}

	@Override
	public int getPixel(int x, int y) {
		return bufImg.getRGB(x, y);
	}

	@Override
	public int getWidth() {
		return bufImg.getWidth();
	}

}