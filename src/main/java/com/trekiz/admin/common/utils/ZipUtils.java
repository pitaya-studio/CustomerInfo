package com.trekiz.admin.common.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;

/**
 * 
 * @author liangjingming
 *
 */
public class ZipUtils {

    private static final Logger log = Logger.getLogger(ZipUtils.class);

    /**
     * 将单个文件压缩到输出流
     * @param sourceFile    需要压缩的文件
     * @param zos           输出流
     * @author              shijun.liu
     * @date                2016.07.05
     */
    private static void _zipFile(File sourceFile, ZipOutputStream zos){
        if (null == sourceFile || !sourceFile.exists()) {
            log.error("源文件不能为空");
            return;
        }

        BufferedInputStream inputStream = null;
        byte[] buffer = new byte[4096];
        try {
            inputStream = new BufferedInputStream(new FileInputStream(sourceFile));
            ZipEntry entry = new ZipEntry(sourceFile.getName());
            zos.putNextEntry(entry);
//            zos.setEncoding(Context.ENCODING_GBK);
            // 复制字节到压缩文件
            int len = 0;
            while ((len = inputStream.read(buffer)) != -1) {
                zos.write(buffer, 0, len);
            }
            zos.closeEntry();
            zos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            log.error("源文件找不到");
        } catch (IOException e) {
            e.printStackTrace();
            log.error("压缩失败");
        } finally {
            FileUtils.closeStream(inputStream, null);
        }
    }

    /**
     * 将某个文件压缩
     * @param sourceFile   待压缩的文件
     * @param targetFile   目标文件，即，压缩后的zip文件
     * @author  shijun.liu
     * @date    2016.07.05
     */
    public static void zipFile(File sourceFile, File targetFile) {
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(targetFile)), StandardCharsets.UTF_8);
            _zipFile(sourceFile, zos);
        } catch (FileNotFoundException e) {
            log.error("压缩文件失败");
            e.printStackTrace();
        } finally {
            FileUtils.closeStream(null, zos);
        }
    }

    /**
     * 将文件列表压缩为zip文件
     * @param fileList          待压缩的文件列表
     * @param targetFile        目标文件，压缩之后的文件，即，zip文件
     * @author  shijun.liu
     * @date    2016.07.05
     */
    public static void zipFileList(List<File> fileList, File targetFile){
        if(Collections3.isEmpty(fileList)){
            log.error("需要压缩的文件列表不能为空");
            throw new RuntimeException("需要压缩的文件列表不能为空");
        }
        if(null == targetFile){
            log.error("压缩为的zip文件不能为空");
            throw new RuntimeException("压缩为的zip文件不能为空");
        }
        ZipOutputStream zos = null;
        try{
            zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(targetFile)), StandardCharsets.UTF_8);
            for (File file : fileList){
                _zipFile(file, zos);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            FileUtils.closeStream(null, zos);
        }
    }

    public static void main(String[] args) throws Exception {
        List<File> list = new ArrayList<File>();
        File file = new File("d:/java/vim-change.txt");
        File file1 = new File("d:/java/aa.yy");
        File file3 = new File("d:/java/vimdoc-move.txt");
        list.add(file);
        list.add(file1);
        list.add(file3);
        zipFileList(list, new File("d:/java/tt.zip"));
    }

    /**
     * 压缩一个文件或者目录
     * @param zipFileName 压缩后的文件名及路径
     * @param inputFile 需要被压缩的文件路径
     * @throws Exception
     */
    public void zip(String zipFileName,String inputFile)throws Exception{
        zip(zipFileName,new File(inputFile));
    }

    /**
     *
     * @param zipFileName 压缩后的文件名及路径
     * @param inputFile 要被压缩的文件的输入流
     * @throws Exception
     */
    public void zip(String zipFileName,File inputFile)throws Exception{
        ZipOutputStream out=new ZipOutputStream(new FileOutputStream(zipFileName), StandardCharsets.UTF_8);
        zip(out,inputFile,"",true);
        System.out.println("zip done");
        out.close();
    }

    /**
     * 用于压缩整个目录或者单个文件
     * @param out 源文件的输出流
     * @param f 目标压缩文件的输入流
     * @param base a
     * @throws Exception
     *
     */
    public void zip(ZipOutputStream out,File f,String base,boolean isreal)throws Exception{
        System.out.println("Zipping  "+f.getName());
        if (f.isDirectory())
        {
            File[] fl=f.listFiles();
            out.putNextEntry(new ZipEntry(base +f.getName() +"/"));
            for (int i=0;i<fl.length ;i++ )
            {
                zip(out, fl[i], base+f.getName()+ "/",isreal);
            }
        }
        else
        {
            if(f.exists()){
                @SuppressWarnings("unused")
                Charset ch = Charset.defaultCharset();
//            	System.out.println(ch);
                String realName = isreal ? base:f.getName();
//                out.setEncoding("GBK");
//                out.putNextEntry(new org.apache.tools.zip.ZipEntry(new String(realName.getBytes(Charset.forName("UTF-8")),"UTF-8")));
                out.putNextEntry(new ZipEntry(realName));
                FileInputStream in=new FileInputStream(f);
                int b;
                while ((b=in.read()) != -1)
                    out.write(b);
                in.close();
            }
        }
    }

    /**
     * 压缩一组文件
     * @param zipFileName 压缩包文件路径
     * @param fileList 要打包的多个文件路径
     * @param bases 多个文件的真实名称
     * @param isreal 是否启用文件真实名称
     * @throws Exception
     */
    public void zip(String zipFileName,List<String> fileList,List<String> bases,boolean isreal)throws Exception{
        //压缩文件流
        ZipOutputStream out=new ZipOutputStream(new FileOutputStream(zipFileName), StandardCharsets.UTF_8);
//        out.putNextEntry(new ZipEntry(base+"/"));
        for(int i=0;i<fileList.size();i++){
            String filename = (String)fileList.get(i);
            System.out.println("add to zip:"+filename);
            File file = new File(filename);
            zip(out,file,bases.get(i),isreal);
            //
        }
        //out.finish();
        out.close();
    }


    /**
     * 解压缩
     * @param zipFileName 压缩文件
     * @param outputDirectory 目标路径
     * @throws Exception
     */
    public void unzip(String zipFileName,String outputDirectory)throws Exception{
        ZipInputStream in=new ZipInputStream(new FileInputStream(zipFileName));
        java.util.zip.ZipEntry z;
        while ((z=in.getNextEntry() )!= null)
        {
            System.out.println("unziping "+z.getName());
            if (z.isDirectory())
            {
                String name=z.getName();
                name=name.substring(0,name.length()-1);
                File f=new File(outputDirectory+File.separator+name);
                f.mkdir();
                System.out.println("mkdir "+outputDirectory+File.separator+name);
            }
            else{
                File f=new File(outputDirectory+File.separator+z.getName());
                f.createNewFile();
                FileOutputStream out=new FileOutputStream(f);
                int b;
                while ((b=in.read()) != -1)
                    out.write(b);
                out.close();
            }
        }

        in.close();
    }
}
