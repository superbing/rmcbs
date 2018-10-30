package com.bfd.utils;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;

/**
 * @author: bing.shen
 * @Date: 2018/7/27
 * @Description:图片验证码
 */
public class ImageCode extends ValidateCode {

    private static final long serialVersionUID = -500010999504413020L;

    private BufferedImage image;

    public ImageCode(BufferedImage image, String code, int expireIn){
        super(code, expireIn);
        this.image = image;
    }

    public ImageCode(BufferedImage image, String code, LocalDateTime expireTime){
        super(code, expireTime);
        this.image = image;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }
}
