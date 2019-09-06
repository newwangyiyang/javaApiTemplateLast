package cn.yiyang.common.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;

/**
 * DateTime: 2016/9/19 14:07
 * 功能：生成64位的图片
 * 思路：
 */
public class ImgAnd64Binary {
    //测试
    public static void main(String[] args) {
        String imgSrcPath="G:/Demo/2.jpg"; //生成64位编码的图片路径
        String imgCreatePath="G:\\Demo/2bak.jpg";
        imgCreatePath=imgSrcPath.replaceAll("\\\\","/");
        System.out.println(imgCreatePath);
        String strImg=getImageStr(imgSrcPath);
        System.out.println(strImg);
        generateImage(imgSrcPath,imgCreatePath);
    }

    /**
     * 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
     * @param imgSrcPath    生成64编码的图片的路径
     * @return
     */
    public static String getImageStr(String imgSrcPath){
        InputStream in=null;
        byte[] data=null;

        //读取图片字节数组
        try {
            in=new FileInputStream(imgSrcPath);
            data=new byte[in.available()];
            in.read(data);
            in.close();
        } catch ( FileNotFoundException e ) {
            e.printStackTrace();
        } catch ( IOException e ) {
            e.printStackTrace();
        }

        //对字节数组Base64编码
        BASE64Encoder encoder=new BASE64Encoder();
        //返回Base64编码过的字节数组字符串
        return encoder.encode(data);

    }

    /**
     * 对字节数组字符串进行Base64解码并生成图片
     * @param imgStr    转换为图片的字符串
     * @param imgCreatePath 将64编码生成图片的路径
     * @return
     */
    public static boolean generateImage(String imgStr,String imgCreatePath){
        if(null==imgStr){   //图像数据为空
            return false;
        }
        BASE64Decoder decoder=new BASE64Decoder();
        try {
            //Base64解码
            byte[] b=decoder.decodeBuffer(imgStr);
            for ( int i = 0; i < b.length; i++ ) {
                if(b[i]<0){ //调整异常数据
                    b[i]+=256;
                }
            }

            OutputStream out=new FileOutputStream(imgCreatePath);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch ( IOException e ) {
            e.printStackTrace();
            return false;
        }
    }

}