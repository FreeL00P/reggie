package com.itheima.reggie.controller;

import com.itheima.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

/**
 * CommonController
 * 文件上传和下载
 * @author fj
 * @date 2022/10/8 15:34
 */
@RestController
@Slf4j
@RequestMapping("/common")
public class CommonController {
    //从配置文件中获取文件保存路径
    @Value("${reggie.path}")
    private String basePath;

    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){//参数名字必须与前端一致
        log.info(file.toString());
        String originalFilename = file.getOriginalFilename();
        log.info("文件上传={}",originalFilename);
        String type = originalFilename.substring(originalFilename.lastIndexOf("."));
        String filename= UUID.randomUUID().toString()+type;
        log.info("处理后的文件名 {}",filename);
        //创建一个目录
        File dir = new File(basePath);
        if (!dir.exists()){
            //目录不存在
            dir.mkdirs();
        }
        try{
            //将文件转存到指定位置
            file.transferTo(new File(basePath + filename));
        }catch (Exception e) {
            e.printStackTrace();
        }
        return R.success(filename);
    }

    /**
     * 文件下载
     * @param name
     * @param response
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) {
        //输入流 通过输入流读取文件内容
        ServletOutputStream outputStream = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));
            //输出流 通过输出流将文件写到浏览器 在浏览器展示图片
            //因为要写到浏览器 所以需要再去啊response中获取输出流
            outputStream = response.getOutputStream();
            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
                outputStream.flush();
            }
            //关闭流释放资源
            outputStream.close();
            fileInputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
