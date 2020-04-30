package com.wangyang.common.utils;

import com.google.common.base.Joiner;
import com.wangyang.common.CmsConst;
import com.wangyang.model.pojo.entity.Category;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.util.ResourceUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class FileUtils {

    private static  String pattern = "<!--#include file=\"(.*?)\"-->";
    private static String varPattern = "<!--\\{\\{(.*?)}}-->";
    // 创建 Pattern 对象
    private static Pattern r = Pattern.compile(pattern);
    private static Pattern rv = Pattern.compile(varPattern);

    /**
     * Copies folder.
     *
     * @param source source path must not be null
     * @param target target path must not be null
     */
    public static void copyFolder(@NonNull Path source, @NonNull Path target) throws IOException {
        Assert.notNull(source, "Source path must not be null");
        Assert.notNull(target, "Target path must not be null");

        Files.walkFileTree(source, new SimpleFileVisitor<Path>() {

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                Path current = target.resolve(source.relativize(dir).toString());
                Files.createDirectories(current);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.copy(file, target.resolve(source.relativize(file).toString()), StandardCopyOption.REPLACE_EXISTING);
                return FileVisitResult.CONTINUE;
            }
        });
    }


    public static Path getJarResources(String resourceName) {
        try {
            Path source ;
            URI templateUri = ResourceUtils.getURL("classpath:"+resourceName).toURI();

            if ("jar".equalsIgnoreCase(templateUri.getScheme())) {
                // Create new file system for jar
                FileSystem fileSystem = getFileSystem(templateUri);
                source = fileSystem.getPath("/BOOT-INF/classes/" + resourceName);
            } else {
                source = Paths.get(templateUri);
            }
            return source;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @NonNull
    private static FileSystem getFileSystem(@NonNull URI uri) throws IOException {
        Assert.notNull(uri, "Uri must not be null");

        FileSystem fileSystem;

        try {
            fileSystem = FileSystems.getFileSystem(uri);
        } catch (FileSystemNotFoundException e) {
            fileSystem = FileSystems.newFileSystem(uri, Collections.emptyMap());
        }

        return fileSystem;
    }

    private static String openFile(File file){
        FileInputStream fileInputStream=null;
        try {
            if(file.exists()){
                fileInputStream = new FileInputStream(file);
                BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));
                List<String> list = reader.lines().collect(Collectors.toList());
                String content = Joiner.on("\n").join(list);
                return content;
            }else {
                return "Page is not found!!";
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "Page is not found!!";
        }finally {
            if(fileInputStream!=null){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private static String openFile(String path){
        File file = new File(path);
        return openFile(file);
    }
    public static String convertByString(String content, HttpServletRequest request){
        // 现在创建 matcher 对象
        Matcher m = r.matcher(content);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
//            System.out.println("Found value: " + m.group(0) );
//            System.out.println("Found value: " + m.group(1) );
            String components = m.group(1);
            String componentsContent = openFile( CmsConst.WORK_DIR+"/html/"+components);
            componentsContent = java.util.regex.Matcher.quoteReplacement(componentsContent);
            m.appendReplacement(sb,componentsContent);
        }

        m.appendTail(sb);
        String result = sb.toString();
        Matcher matcher = rv.matcher(result);
        while (matcher.find()){
            String attr = matcher.group(1);
//            System.out.println(attr);
            String attrS = (String) request.getAttribute(attr);
//            System.out.println(attrS);
            if(attrS!=null){
                result = matcher.replaceAll(attrS);
            }else {
                result = matcher.replaceAll("");
            }
        }
        return  result;
    }

    public static String convert(File file, HttpServletRequest request){
        String content = openFile(file);
        return  convertByString(content,request);
    }

    public static String convert(String viewPath, HttpServletRequest request){
        File file = new File(CmsConst.WORK_DIR+"/html/"+viewPath);
        return  convert(file,request);
    }
    public  static boolean removeCategoryPageTemp(Category category) {
        File file = new File(CmsConst.WORK_DIR+"/html/"+category.getPath()+"/"+category.getViewName());
        return remove(file);
    }
    public  static boolean remove(File file) {

        if (!file.exists()) {
            return false;
        }
        if (file.isFile()) {
            return file.delete();
        }

        Arrays.asList(file.listFiles()).forEach(f->{
            f.delete();
        });
        return file.delete();
    }
}
