package com.trekiz.admin.modules.distribution.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import jp.sourceforge.qrcode.QRCodeDecoder;
import jp.sourceforge.qrcode.exception.DecodingFailedException;

import com.swetake.util.Qrcode;

/**
 * @author gaoyang
 * @Time 2017-01-05 20:14
 */
public class QRCodeUtils {

    /**
     * 生成二维码图片
     * @param content 存储内容
     * @param imgPath 二维码生成路径
     * @param imgType 生成的二维码图片类型 ("png"、"jpg")
     * @param size    二维码尺寸(取值范围1-40)
     * 				      注：该值只是设置二维码存储的信息量大小，而不是生成二维码图片的大小。图片大小（宽、高）与size的关系公式为      67 + 12 * (size - 1)
     * 				 	  如果 二维码满足不了规定的图片大小尺寸，只能通过前端去设置<img>大小让二维码图片自适应即可
     * 					  例如：<img src="二维码图片地址/获取二维码图片流接口" width="430px" height="430px">
     */
    public static void encoderQRCode(String content, String imgPath, String imgType, int size) {
        try {
            BufferedImage bufImg = qRCodeCommon(content, imgType, size);
            File imgFile = new File(imgPath);
            // 生成二维码图片
            ImageIO.write(bufImg, imgType, imgFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 
     * 生成二维码图流
     * @param content 存储内容
     * @param output  输出流
     * @param imgType 生成的二维码图片类型 ("png"、"jpg")
     * @param size    二维码尺寸(取值范围1-40)
     * 				      注：该值只是设置二维码存储的信息量大小，而不是生成二维码图片的大小。图片大小（宽、高）与size的关系公式为      67 + 12 * (size - 1)
     * 				 	  如果 二维码满足不了规定的图片大小尺寸，只能通过前端去设置<img>大小让二维码图片自适应即可
     * 					  例如：<img src="二维码图片地址/获取二维码图片流接口" width="430px" height="430px">
     */  
    public static void encoderQRCode(String content, OutputStream output, String imgType, int size) {
        try {
            BufferedImage bufImg = qRCodeCommon(content, imgType, size);
            // 生成二维码图片流  
            ImageIO.write(bufImg, imgType, output);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成二维码图片(带logo)
     * @param content 存储内容
     * @param imgPath LoGo图片路径
     * @param imgPath 二维码生成路径
     * @param logoHeith logo图片高
     * @param logoWidth logo图片宽
     * @param imgType 生成的二维码图片类型 ("png"、"jpg")
     * @param size    二维码尺寸(取值范围1-40)
     * 				      注：该值只是设置二维码存储的信息量大小，而不是生成二维码图片的大小。图片大小（宽、高）与size的关系公式为      67 + 12 * (size - 1)
     * 				 	  如果 二维码满足不了规定的图片大小尺寸，只能通过前端去设置<img>大小让二维码图片自适应即可
     * 					  例如：<img src="二维码图片地址/获取二维码图片流接口" width="430px" height="430px">
     */
    public static void encoderQRCodeWithLoGo(String content, String logoPath
    		,int logoHeith, int logoWidth, String imgPath, String imgType, int size) {
        try {
            BufferedImage bufImg = qRCodeCommonWithLoGo(content, imgType, logoPath, logoHeith, logoWidth, size);
            File imgFile = new File(imgPath);
            // 生成二维码图片
            ImageIO.write(bufImg, imgType, imgFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成二维码图片流(带logo)
     * @param content 存储内容
     * @param imgPath LoGo图片路径
     * @param logoHeith logo图片高
     * @param logoWidth logo图片宽
     * @param output  输出流
     * @param imgType 生成的二维码图片类型 ("png"、"jpg")
     * @param size    二维码尺寸(取值范围1-40)
     * 				      注：该值只是设置二维码存储的信息量大小，而不是生成二维码图片的大小。图片大小（宽、高）与size的关系公式为      67 + 12 * (size - 1)
     * 				 	  如果 二维码满足不了规定的图片大小尺寸，只能通过前端去设置<img>大小让二维码图片自适应即可
     * 					  例如：<img src="二维码图片地址/获取二维码图片流接口" width="430px" height="430px">
     */
    public static void encoderQRCodeWithLoGo(String content, String logoPath
    		,int logoHeith, int logoWidth, OutputStream output, String imgType, int size) {
        try {
            BufferedImage bufImg = qRCodeCommonWithLoGo(content, imgType, logoPath, logoHeith, logoWidth, size);
            // 生成二维码图片流  
            ImageIO.write(bufImg, imgType, output);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析二维码通过图片
     * 注：只能解析部分带logo的二维码图片，否则会乱码（跟二维码与logo尺寸比例有关）
     * @param imgPath 二维码图片路径
     * @return
     */
    public static String decoderQRCode(String imgPath) {
        // 二维码图片的文件
        File imageFile = new File(imgPath);
        BufferedImage bufImg = null;
        String content = null;
        try {
            bufImg = ImageIO.read(imageFile);
            QRCodeDecoder decoder = new QRCodeDecoder();
            content = new String(decoder.decode(new BufferedImageObject(bufImg)), "utf-8"); 
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DecodingFailedException dfe) {
            dfe.printStackTrace();
        }

        return content;
    }

    /**
     * 解析二维码通过图片流
     * 注：只能解析部分带logo的二维码图片，否则会乱码（跟二维码与logo尺寸比例有关）
     * @param input 二维码图片输入流
     * @return
     */
    public static String decoderQRCode(InputStream input) {
        BufferedImage bufImg = null;
        String content = null;
        try {
            bufImg = ImageIO.read(input);
            QRCodeDecoder decoder = new QRCodeDecoder();
            content = new String(decoder.decode(new BufferedImageObject(bufImg)), "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DecodingFailedException dfe) {
            dfe.printStackTrace();
        }

        return content;
    }

    /**
     * 生成二维码图片(不带logo图片)
     * @param content 存储内容
     * @param imgType 生成的二维码图片类型 ("png"、"jpg")
     * @param size    二维码尺寸(取值范围1-40)
     * 				      注：该值只是设置二维码存储的信息量大小，而不是生成二维码图片的大小。图片大小（宽、高）与size的关系公式为      67 + 12 * (size - 1)
     * 				 	  如果 二维码满足不了规定的图片大小尺寸，只能通过前端去设置<img>大小让二维码图片自适应即可
     * 					  例如：<img src="二维码图片地址/获取二维码图片流接口" width="430px" height="430px">
     * @return BufferedImage对象
     */  
    private static BufferedImage qRCodeCommon(String content, String imgType, int size) {
        BufferedImage bi = null;
        try {
            Qrcode qd = new Qrcode();
            // 设置二维码排错率，可选L(7%)、M(15%)、Q(25%)、H(30%)，排错率越高可存储的信息越少，但对二维码清晰度的要求越小
            qd.setQrcodeErrorCorrect('Q');
            // 编码模式：Numeric 数字, Alphanumeric 英文字母,Binary 二进制, Kanji 汉字(第一个大写字母表示)
            qd.setQrcodeEncodeMode('B');
            /* 
	       		二维码的版本号：也象征着二维码的信息容量；二维码可以看成一个黑白方格矩阵，版本不同，矩阵长宽方向方格的总数量分别不同。 
	        	1-40总共40个版本，版本1为21*21矩阵，版本每增1，二维码的两个边长都增4； 
	       		版本2 为25x25模块，最高版本为是40，是177*177的矩阵； 
	        */  
            qd.setQrcodeVersion(size);
            // 获得内容的字节数组，设置编码格式
            byte[] contentBytes = content.getBytes("utf-8");
            // 二维码图片尺寸(宽、高)
            int imgSize = 67 + 12 * (size - 1);
            bi = new BufferedImage(imgSize, imgSize, BufferedImage.TYPE_INT_RGB);
            // 创建图层
            Graphics2D gs = bi.createGraphics();
            // 设置背景颜色
            gs.setBackground(new Color(225,238,254)); // #E1EEFE
            // 矩形 X、Y、width、height
            gs.clearRect(0, 0, imgSize, imgSize);
            // 设定图像颜色
            gs.setColor(new Color(54,126,215)); // #367ED7
            // 设置偏移量，不设置可能导致解析出错
            int pixoff = 2;
            // 输出内容(二维码)
            if (contentBytes.length > 0 && contentBytes.length < 800) {
                boolean[][] codeOut = qd.calQrcode(contentBytes);
                for (int i = 0; i < codeOut.length; i++) {
                    for (int j = 0; j < codeOut.length; j++) {
                        if (codeOut[j][i]) {
                            gs.fillRect(j * 3 + pixoff, i * 3 + pixoff, 3, 3);
                        }
                    }
                }
            } else {
                throw new Exception("QRCode content bytes length = " + contentBytes.length + " not in [0, 800].");
            }
            // 释放此图形的上下文以及它使用的所有系统资源。调用 dispose之后，就不能再使用 Graphics对象
            gs.dispose();
            // 刷新此bi对象正在使用的所有可重构的资源
            bi.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bi;
    }

    /**
     * 生成二维码图片(带logo图片)
     * @param content  存储内容
     * @param imgType  生成的二维码图片类型 ("png"、"jpg")
     * @param logoPath logo图片路径
     * @param size     二维码尺寸(取值范围1-40)
     * 				         注：该值只是设置二维码存储的信息量大小，而不是生成二维码图片的大小。图片大小（宽、高）与size的关系公式为      67 + 12 * (size - 1)
     * 				 	     如果 二维码满足不了规定的图片大小尺寸，只能通过前端去设置<img>大小让二维码图片自适应即可
     * 					     例如：<img src="二维码图片地址/获取二维码图片流接口" width="430px" height="430px">
     * @return BufferedImage对象
     */  
    private static BufferedImage qRCodeCommonWithLoGo(String content
    		,String imgType, String logoPath, int logoHeith, int logoWidth, int size) {
        BufferedImage bi = null;
        try {
            Qrcode qd = new Qrcode();
            // 设置二维码排错率，可选L(7%)、M(15%)、Q(25%)、H(30%)，排错率越高可存储的信息越少，但对二维码清晰度的要求越小
            qd.setQrcodeErrorCorrect('Q');
            // 编码模式：Numeric 数字, Alphanumeric 英文字母,Binary 二进制, Kanji 汉字(第一个大写字母表示)
            qd.setQrcodeEncodeMode('B');
            /* 
           		二维码的版本号：也象征着二维码的信息容量；二维码可以看成一个黑白方格矩阵，版本不同，矩阵长宽方向方格的总数量分别不同。 
            	1-40总共40个版本，版本1为21*21矩阵，版本每增1，二维码的两个边长都增4； 
           		版本2 为25x25模块，最高版本为是40，是177*177的矩阵； 
            */  
            qd.setQrcodeVersion(size);
            // 获得内容的字节数组，设置编码格式
            byte[] contentBytes = content.getBytes("utf-8");
            // 二维码图片尺寸(宽、高)
            int imgSize = 67 + 12 * (size - 1);
            bi = new BufferedImage(imgSize, imgSize, BufferedImage.TYPE_INT_RGB);
            // 创建图层
            Graphics2D gs = bi.createGraphics();
            // 设置背景颜色
            gs.setBackground(Color.WHITE);
            // 矩形 X、Y、width、height
            gs.clearRect(0, 0, imgSize, imgSize);
            // 设定图像颜色
            gs.setColor(Color.BLACK);
            // 设置偏移量，不设置可能导致解析出错
            int pixoff = 2;
            // 输出内容(二维码)
            if (contentBytes.length > 0 && contentBytes.length < 800) {
                boolean[][] codeOut = qd.calQrcode(contentBytes);
                for (int i = 0; i < codeOut.length; i++) {
                    for (int j = 0; j < codeOut.length; j++) {
                        if (codeOut[j][i]) {
                            gs.fillRect(j * 3 + pixoff, i * 3 + pixoff, 3, 3);
                        }
                    }
                }
            } else {
                throw new Exception("QRCode content bytes length = " + contentBytes.length + " not in [0, 800].");
            }
            // logo图片
		    Image img = ImageIO.read(new File(logoPath));
		    // logo图片位置
		    int xPoint = 0;
		    int yPoint = 0;
		    if (imgSize >= logoWidth) {
		    	xPoint = (imgSize - logoWidth)/2;
		    }
		    if (imgSize >= logoHeith) {
		    	yPoint = (imgSize - logoHeith)/2;
		    }
		    gs.drawImage(img, xPoint, yPoint, logoWidth, logoHeith, null);
            // 释放此图形的上下文以及它使用的所有系统资源。调用 dispose之后，就不能再使用 Graphics对象
            gs.dispose();
            // 刷新此bi对象正在使用的所有可重构的资源
            bi.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bi;
    }
}