package cn.yiyang.common.utils.JUtils.word;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;


import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类名: ExportWord
 * 功能: freemark导出word
 * 作者: vivira
 * 日期: 2016/12/6
 */
public class ExportWord {
    private static final Logger logger = LoggerFactory.getLogger(ExportWord.class);

    private Configuration configuration = null;


    public ExportWord() {
        configuration = new Configuration();
        configuration.setDefaultEncoding("utf-8");
    }

    /**
     * 功能: 导出word
     * 参数: word内容 map
     * 参数: flt路径 xmlPath
     * 参数: word名称 docName
     * 参数: flt名称 fltName
     * 参数: response
     * 返回值类型: void
     * 时间:  2016/12/6 13:35
     */
    public void exported(Map<String,Object> map ,String docName,String fltName,HttpServletResponse response ) {
        /**
         * 导出word
         */
        try {
//            configuration.setDirectoryForTemplateLoading(new File(xmlPath + "/template"));
            configuration.setClassForTemplateLoading(this.getClass(), "/template");
            Template t = configuration.getTemplate(fltName+".ftl");
            Writer out = null;
            try {
                //设置自选路径
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/msword");
                response.setHeader(
                        "content-disposition",
                        "attachment; filename="+URLEncoder.encode(docName, "UTF-8")+".doc");
                out = new BufferedWriter(new OutputStreamWriter(
                        response.getOutputStream(), "UTF-8"), 10240);
                logger.info(docName+"-自选路径设置成功！");
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("-自选路径设置失败");
            }
            try {
                t.process(map, out);
                out.flush();
                out.close();
                logger.info("freemark导出word-"+docName+"成功！");
            } catch (TemplateException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
            logger.error("freemark导出word-"+docName+"失败！",e);
        }
    }
}

