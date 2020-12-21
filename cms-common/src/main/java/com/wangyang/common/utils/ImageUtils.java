package com.wangyang.common.utils;

import lombok.extern.slf4j.Slf4j;
import net.sf.image4j.codec.ico.ICODecoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class ImageUtils {

    public static final String EXTENSION_ICO = "ico";
//    public static final String pattern = "<img(\\s*?) src=\"(.*?)\"(.*?)>";
    public static final String pattern = "!\\[(.*?)\\]\\((.*?)\\)";
    public static final Pattern r = Pattern.compile(pattern);

    public static BufferedImage getImageFromFile(InputStream is, String extension) throws IOException {
        log.debug("Current File type is : [{}]", extension);

        if (EXTENSION_ICO.equals(extension)) {
            return ICODecoder.read(is).get(0);
        } else {
            return ImageIO.read(is);
        }
    }

    public static  String getImgSrc(String html){
        Matcher m = r.matcher(html);
        if(m.find()){
            String str = m.group(2);
            String[] split = str.split(" ");
            return   split[0];
        }
        return null;
    }
}
