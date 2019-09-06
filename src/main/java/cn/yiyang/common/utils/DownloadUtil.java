package cn.yiyang.common.utils;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 文件处理工具类
 *
 * @author xmq
 */
public class DownloadUtil {

    /**
     * 下载文件
     * @param response
     * @param filePath 包括文件名如：c:/a.txt
     * @param fileName 文件名如：a.txt
     */
    public static void downFile(HttpServletResponse response,String filePath,String fileName){
        try {
            response.setCharacterEncoding("GBK");
            response.setContentType("text/plain");
            response.setHeader("Location",fileName);
            response.setHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes("gb2312"),"ISO8859-1"));
            FileInputStream  fis=new FileInputStream(filePath);
            OutputStream  os=response.getOutputStream();
            byte[] buf=new byte[1024];
            int c=0;
            while((c=fis.read(buf))!=-1){
                os.write(buf, 0, c);
            }
            os.flush();
            os.close();
            if(fis!=null){
                fis.close();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    /**
     * @Author Administrator
     * @Description 在线的文件下载
     * @Date 8:48 2018/9/29
     * @Param 
     * @return 
     **/
    public static void downFileByUrl(HttpServletRequest request, HttpServletResponse response, String fileUrl, String fileName){
        OutputStream toClient = null;
        InputStream in = null;
        HttpURLConnection conn = null;
        try {
            URL url = new URL(fileUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Connection","Keep-Alive");
            // conn.connect();
            int responseCode = conn.getResponseCode();


            int length = conn.getContentLength();
            in = conn.getInputStream();

            String userAgent = request.getHeader("USER-AGENT");
            if (org.apache.commons.lang.StringUtils.contains(userAgent, "MSIE")) {//IE浏览器
                fileName = URLEncoder.encode(fileName, "UTF-8");
            } else if (org.apache.commons.lang.StringUtils.contains(userAgent, "Mozilla")) {//google,火狐浏览器
                fileName = new String(fileName.getBytes(), "ISO8859-1");
            } else {
                fileName = URLEncoder.encode(fileName, "UTF-8");//其他浏览器
            }
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.addHeader("Content-Length", "" + length);
            toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            byte[] buff = new byte[1024];
            while (true) {
                int read = in.read(buff);
                if (read == -1)
                    break;
                toClient.write(buff, 0, read);
            }
            toClient.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (toClient != null) {
                    IOUtils.closeQuietly(toClient);
                    toClient.close();
                }
                if (in != null) {
                    IOUtils.closeQuietly(in);
                    in.close();
                }
                // conn.disconnect();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 检查文件是否存在，存在返回true
     * @param destFileName
     * @return
     */
    public static boolean checkFileIsExists(String destFileName){
        File file = new File(destFileName);
        if (file.exists()) {
            return true;
        }else{
            return false;
        }
    }
    /**
     * 复制文件
     * @param source
     * @param dest
     * @throws IOException
     */
    public static void copyFile(File source, File dest){
        InputStream input = null;
        OutputStream output = null;
        try {
            input = new FileInputStream(source);
            output = new FileOutputStream(dest);
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buf))>-1) {
                output.write(buf, 0, bytesRead);
            }
            output.close();
            input.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 把输入流保存到指定文件
     * @param source
     * @param dest
     * @throws IOException
     */
    public static void saveFile(InputStream source, File dest){
        InputStream input = null;
        OutputStream output = null;
        try {
            input =source;
            output = new FileOutputStream(dest);
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buf))>-1) {
                output.write(buf, 0, bytesRead);
            }
            output.close();
            input.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 创建文件
     */
    public static boolean createFile(String destFileName) {
        File file = new File(destFileName);
        if (file.exists()) {
            return false;
        }
        if (destFileName.endsWith(File.separator)) {
            return false;
        }
        if (!file.getParentFile().exists()) {
            if (!file.getParentFile().mkdirs()) {
                return false;
            }
        }
        try {
            if (file.createNewFile()) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 创建目录
     */
    public static boolean createDir(String destDirName) {
        File dir = new File(destDirName);
        if (dir.exists()) {
            return false;
        }
        if (!destDirName.endsWith(File.separator))
            destDirName = destDirName + File.separator;
        if (dir.mkdirs()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 根据路径删除指定的目录或文件，无论存在与否
     */
    public static boolean DeleteFolder(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        if (!file.exists()) {
            return flag;
        } else {
            if (file.isFile()) {
                return deleteFile(sPath);
            } else {
                return deleteDirectory(sPath);
            }
        }
    }

    /**
     * 删除单个文件
     */
    public static boolean deleteFile(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }

    /**
     * 删除目录（文件夹）以及目录下的文件
     */
    public static boolean deleteDirectory(String sPath) {
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        File dirFile = new File(sPath);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        boolean flag = true;
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag)
                    break;
            } else {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag)
            return false;
        if (dirFile.delete()) {
            return true;
        } else {
            return false;
        }
    }


    /*public static void main(String[] args) {
        String dir = "D:\\sgtsc_files\\393\\02\\";
        createDir(dir);
        String filename = "test1.txt";
        String subdir = "subdir";
        createDir(dir + subdir);
        createFile(dir + filename);
        createFile(dir + subdir + filename);
        DeleteFolder(dir);
    }*/

}

