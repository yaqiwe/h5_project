package com.example.demo.util;

import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Enumeration;
import java.util.UUID;

/*
 *@author yaqiwe
 *@data 2019-05-15 15:58
 * 写入日记文件 && 读取日记文件
 **/
@Getter
@Slf4j
@Component
public class fileSrcUtil {
    @Value("${fileSrc}")//本地资源存储路径
    private String fileSrc;
    @Value("${server.port}")//获取端口
    String pott;
    @Value("${mappingUrl}")//获取资源文件的映射地址
    String mappingUrl;
    @Value("${InternetIp}")//获取资源文件的映射地址
    String InternetIp;
    public String witeDataToFile(String data, String userName) {
        Path filePath = null;
        String fileName=UUID.randomUUID().toString();
        try {
            //设置日记的存放位置
            filePath = Paths.get(fileSrc + userName + "/");
            if (Files.notExists(filePath))
                Files.createDirectories(filePath);
            filePath = filePath.resolve(fileName);
            byte[] fileByte = data.getBytes();
            //写入文件
            Files.write(filePath, fileByte);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return userName + "/" + fileName;
    }

    /**
     * 读取文件内容
     * @param url
     * @return
     */
    public String readDataToFile(String url) {
        Path filePath = null;
        byte[] date = null;
        filePath = Paths.get(fileSrc + url);
        if (Files.notExists(filePath))
            return null;
        try {
            date = Files.readAllBytes(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return new String(date);
    }

    /**
     * 删除文件
     * @param url 文件地址
     * @return
     */
    public boolean deleteFile(String url) {
        Path filePath = Paths.get(fileSrc + url);
        log.info(fileSrc + url);
        try {
            if (!Files.notExists(filePath))
                Files.delete(filePath);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     *保存图片并返回图片地址
     * @param file 头像文件
     * @param userName 用户名
     * @return
     */
    public String uploadFile(MultipartFile file, String userName) {
        String fileName;
        String suffixNam;
        if (file.isEmpty())
            return null;
        try {
            fileName = UUID.randomUUID().toString();
            String nam=file.getOriginalFilename();
            suffixNam = nam.substring(nam.indexOf("."));
            //文件路径+文件名
            String path = fileSrc +userName+"/"+ fileName+suffixNam;
            File dest = new File(path);
            //若不存在位置则创建该位置
            if (!dest.getParentFile().exists())
                dest.getParentFile().mkdirs();
            /*写入文件*/
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return userName+"/"+fileName+suffixNam;
    }

    /**
     * 获取当前主机的资源前缀
     * @return
     */
    public String getIp() {
        if (pott == null)
            pott = "8080";
        InetAddress address = null;
        return "http://" + InternetIp + ":" + pott + mappingUrl;
    }

}
