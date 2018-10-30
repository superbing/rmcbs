package com.bfd.utils;

import com.bfd.common.exception.RmcbsException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;

/**
 * @Author: chong.chen
 * @Description: ftp工具类
 * @Date: Created in 9:56 2018/10/17
 * @Modified by:
 */
@Component
@Slf4j
public class FtpUtils {

    private static String hostname;

    private static Integer port;

    private static String username;

    private static String password;

    @Value("${ftp.hostname}")
    public void setHostname(String hostname) {
        FtpUtils.hostname = hostname;
    }

    @Value("${ftp.port}")
    public void setPort(Integer port) {
        FtpUtils.port = port;
    }

    @Value("${ftp.username}")
    public void setUsername(String username) {
        FtpUtils.username = username;
    }

    @Value("${ftp.password}")
    public void setPassword(String password) {
        FtpUtils.password = password;
    }

    /**
     * 初始化ftp服务器
     */
    public static FTPClient initFtpClient() {
        FTPClient ftpClient = new FTPClient();
        try {
            log.info("connecting...ftp服务器:" + hostname + ":" + port);
            ftpClient.connect(hostname, port);
            ftpClient.login(username, password);
            int replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                log.info("connect failed...ftp服务器:" + hostname + ":" + port);
            }
            log.info("connect successful...ftp服务器:" + hostname + ":" + port);
        } catch (Exception e) {
            throw new RmcbsException("ftp连接失败");
        }
        return ftpClient;
    }

    /**
     * 判断ftp服务器文件是否存在
     * @param path 文件路径
     * @param filename 文件名称
     * @return
     * @throws IOException
     */
    public static String existFile(String path, String filename) throws IOException {
        FTPClient ftpClient = initFtpClient();
        String fileName = null;
        try{
            Boolean flag = ftpClient.changeWorkingDirectory(new String(path.getBytes("GBK"), FTP.DEFAULT_CONTROL_ENCODING));
            if (flag) {
                log.info("进入文件夹" + path + " 成功！");
                FTPFile[] ftpFiles = ftpClient.listFiles();
                if (ftpFiles.length > 0) {
                    for (FTPFile file : ftpFiles) {
                        if (file.getName().contains(filename)) {
                            fileName = path.concat(new String(
                                    file.getName().getBytes("ISO-8859-1"), "GBK"));
                            break;
                        }else if(file.getName().contains(".pdf")){
                            fileName = path.concat(new String(
                                    file.getName().getBytes("ISO-8859-1"), "GBK"));
                        }
                    }
                }
            }
        } catch (Exception e){
            throw new RmcbsException("获取文件失败");
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return fileName;
    }

    public static void downloadFile(File file, String ftpPath) {
        OutputStream os = null;
        FTPClient ftpClient = initFtpClient();
        try {
            log.info("开始下载文件");
            ftpClient.enterLocalPassiveMode();
            OutputStream outputStream = new FileOutputStream(file);
            //检索指定从服务器上的文件并将其写入给定的输出流
            ftpClient.retrieveFile(
                    new String(ftpPath.getBytes("GBK"), FTP.DEFAULT_CONTROL_ENCODING), outputStream);
            outputStream.close();
            log.info("下载文件成功");
        } catch (Exception e) {
            throw new RmcbsException("下载文件失败");
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != os) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
