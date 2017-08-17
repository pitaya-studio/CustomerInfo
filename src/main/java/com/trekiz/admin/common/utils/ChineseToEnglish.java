package com.trekiz.admin.common.utils;
import com.trekiz.admin.modules.island.util.StringUtil;

import net.sourceforge.pinyin4j.PinyinHelper;  
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;  
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;  
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;  
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;  
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;  
  
public class ChineseToEnglish {  
	
    // 将汉字转换为全拼  大写
    public static String getPingYin(String src) {  
    	if(StringUtil.isBlank(src)){
    		return "";
    	}
  
		char[] t1 = null;
		t1 = src.toCharArray();
		String[] t2 = new String[t1.length];
		HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();

		t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		t3.setVCharType(HanyuPinyinVCharType.WITH_V);
		String t4 = "";
		int t0 = t1.length;
		try {
			for (int i = 0; i < t0; i++) {
				// 判断是否为汉字字符
				if (java.lang.Character.toString(t1[i]).matches(
						"[\\u4E00-\\u9FA5]+")) {
					t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3);
					t4 += t2[0];
				} else
					t4 += java.lang.Character.toString(t1[i]);
			}
			// System.out.println(t4);
			return t4;
		} catch (BadHanyuPinyinOutputFormatCombination e1) {
			e1.printStackTrace();
		}
		return t4;
    }  
    
    // 将汉字转换为全拼  小写
    public static String getPinyinLower(String src){
    	if(StringUtil.isBlank(src)){
    		return "";
    	}
    	StringBuilder sb = new StringBuilder();
		String tempPinyin = null;
		for (int i = 0; i < src.length(); ++i)
		{
			tempPinyin = getCharacterPinYin(src.charAt(i));
			if (tempPinyin == null)
			{
				// 如果str.charAt(i)非汉字，则保持原样
				sb.append(src.charAt(i));
			}
			else
			{
				sb.append(tempPinyin);
			}
		}
		return sb.toString();
    }
  
    // 返回中文的首字母  
    public static String getPinYinHeadChar(String str) {  
    	if(StringUtil.isBlank(str)){
			return "";
		}
        String convert = "";  
        for (int j = 0; j < str.length(); j++) {  
            char word = str.charAt(j);  
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);  
            if (pinyinArray != null) {  
                convert += pinyinArray[0].charAt(0);  
            } else {  
                convert += word;  
            }  
        }  
        return convert;  
    }  
  
    // 将字符串转移为ASCII码  
    public static String getCnASCII(String cnStr) {  
    	if(StringUtil.isBlank(cnStr)){
    		return "";
    	}
        StringBuffer strBuf = new StringBuffer();  
        byte[] bGBK = cnStr.getBytes();  
        for (int i = 0; i < bGBK.length; i++) {  
            strBuf.append(Integer.toHexString(bGBK[i] & 0xff));  
        }  
        return strBuf.toString();  
    }
    
    /**
	 * 获取字符串的首字母
	 * @param str
	 * @return
	 */
	public static String getFirstLetter(String str){
		if(StringUtil.isBlank(str)){
			return "";
		}
		String letter = "";
		char[] c = str.trim().toCharArray();
		
		if(c.length>0){
			try {
				if(Character.toString(c[0]).matches("[\u4E00-\u9FA5]+")){
					letter = (PinyinHelper.toHanyuPinyinStringArray(c[0]))[0].substring(0,1).toUpperCase();
				}else{
					letter = Character.toString(c[0]).toUpperCase();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return letter;
	}
	
	// 转换单个字符
	public static String getCharacterPinYin(char c) {
		HanyuPinyinOutputFormat outputFormat = new HanyuPinyinOutputFormat();
		String[] pinyin = null;
		try {
			pinyin = PinyinHelper.toHanyuPinyinStringArray(c, outputFormat);
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
		}
		// 如果c不是汉字，toHanyuPinyinStringArray会返回null
		if (pinyin == null)
			return null;
		// 只取一个发音，如果是多音字，仅取第一个发音
		return pinyin[0];
	}
	
	public static String testFunction(String str){
		return null;
	}
	
	
  
    public static void main(String[] args) {  
//        System.out.println(getPingYin("马建成test").toUpperCase());  
//	    System.out.println(getPinYinHeadChar("马建成test").toUpperCase());  
//	    System.out.println(getCnASCII("綦江县"));
//	    System.out.println(getFirstLetter("马建成test"));
    	System.out.println(getPinyinLower("马建成test"));
	    System.out.println(testFunction("马建成test"));
    }  
}  