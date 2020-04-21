package com.wangyang.syscall.utils;

import com.wangyang.common.BaseResponse;
import com.wangyang.common.CmsConst;
import com.wangyang.common.utils.CMSUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

@Slf4j
public class LatexUtil {

    private static final String DVI_SVG="dvisvgm --no-fonts --no-styles -c2,2 -o %s %s";
    private static final  String LATEX_WORK= CmsConst.WORK_DIR+"/latex";
    private  static  final  String LATEX_COMMAND="latex  --interaction=nonstopmode --output-directory  %s %s";
    private static final  String TEX_DOCUMENT="\\documentclass{minimal}\n" +
                                                "\\usepackage{amsmath}\n" +
                                                "\\begin{document}\n" +
                                                "$$%s$$\n" +
                                                "\\end{document}";
//    public static void main(String[] args) {
//        try {
//            String result = exeCommand("ls");
//            System.out.println(result);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
    static boolean remove(File file) {

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


    public static BaseResponse getSvgPath(String latex){
        FileOutputStream fileOutputStream = null;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        String randomTime = CMSUtils.randomTime();

        String fileName=randomTime+".svg";
        String baseName = LATEX_WORK + "/" + randomTime;
        Path path = Paths.get(baseName);

        try {
            Files.createDirectories(path);
            String texFile = baseName + "/temp.tex";
            String dviFile = baseName + "/temp.dvi";
            String svgFile;
            svgFile = baseName+".svg";
            fileOutputStream = new FileOutputStream(texFile);
            fileOutputStream.write(String.format(TEX_DOCUMENT, latex).getBytes());

            String cmd = String.format(LATEX_COMMAND, baseName, texFile);
            log.info("执行命令" + cmd);
            int exitCode1 = exeCommand(cmd, out);
            String dviToSvgCmd = String.format(DVI_SVG, svgFile, dviFile);
            log.info("执行命令" + dviToSvgCmd);
            int exitCode2 = exeCommand(dviToSvgCmd, out);
            if (exitCode1 == 0 && exitCode2 == 0) {

                return BaseResponse.ok(fileName);
            }else {
                return BaseResponse.error(out.toString());
            }


        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponse.error(e.getMessage()+" ["+out.toString()+"]");
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            remove(path.toFile());
        }
    }

    public static String getSvg(String math,boolean isSaveSvg) {
        FileOutputStream fileOutputStream = null;
        FileInputStream fileInputStream = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        String baseName = LATEX_WORK + "/" + CMSUtils.randomTime();
        Path path = Paths.get(baseName);

        try {
            Files.createDirectories(path);
            String texFile = baseName + "/temp.tex";
            String dviFile = baseName + "/temp.dvi";
            String svgFile;
            if(!isSaveSvg){
                svgFile = baseName + "/temp.svg";
            }else {
                svgFile = baseName+".svg";
            }
            fileOutputStream = new FileOutputStream(texFile);
            fileOutputStream.write(String.format(TEX_DOCUMENT, math).getBytes());

            String cmd = String.format(LATEX_COMMAND, baseName, texFile);
            log.info("执行命令" + cmd);
            int exitCode1 = exeCommand(cmd, out);
            String dviToSvgCmd = String.format(DVI_SVG, svgFile, dviFile);
            log.info("执行命令" + dviToSvgCmd);
            int exitCode2 = exeCommand(dviToSvgCmd, out);
            if (exitCode1 == 0 && exitCode2 == 0) {
                fileInputStream = new FileInputStream(svgFile);
                BufferedReader bufferedReader  = new BufferedReader(new InputStreamReader(fileInputStream));
                StringBuffer sb  =new StringBuffer();
                bufferedReader.lines().forEach(line->{
                    sb.append(line).append("\n");
                });
                return sb.toString();
            }else {
                return out.toString();
            }


        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage()+" ["+out.toString()+"]";
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            remove(path.toFile());
        }
    }

    private static final String DEFAULT_CHARSET = "UTF-8";
    /**
     * 执行指定命令
     *
     * @param command 命令
     * @return 命令执行完成返回结果
     * @throws IOException 失败时抛出异常，由调用者捕获处理
     */
    public static String exeCommand(String command) throws IOException {
        try (
                ByteArrayOutputStream out = new ByteArrayOutputStream();
        ) {
            int exitCode = exeCommand(command, out);
            if (exitCode == 0) {
                System.out.println("命令运行成功！");
            } else {
                System.out.println("命令运行失败！");
            }
            return out.toString(DEFAULT_CHARSET);
        }
    }

    /**
     * 执行指定命令，输出结果到指定输出流中
     *
     * @param command 命令
     * @param out 执行结果输出流
     * @return 执行结果状态码：执行成功返回0
     * @throws ExecuteException 失败时抛出异常，由调用者捕获处理
     * @throws IOException 失败时抛出异常，由调用者捕获处理
     */
    public static int exeCommand(String command, OutputStream out) throws IOException,ExecuteException  {
        CommandLine commandLine = CommandLine.parse(command);
        PumpStreamHandler pumpStreamHandler = null;
        if (null == out) {
            pumpStreamHandler = new PumpStreamHandler();
        } else {
            pumpStreamHandler = new PumpStreamHandler(out);
        }

        // 设置超时时间为10秒10000
        ExecuteWatchdog watchdog = new ExecuteWatchdog(3*1000);

        DefaultExecutor executor = new DefaultExecutor();
        executor.setStreamHandler(pumpStreamHandler);
        executor.setWatchdog(watchdog);

        int execute = executor.execute(commandLine);
//        try {
//
//        } catch (IOException e) {
//            System.out.println("ssssssssss");
//            e.printStackTrace();
//        }
        return execute;
    }



    public static String runCommand(String... params){
        if(params.length==0){
            return "请输入命令参数!";
        }
        String[] cmd = new String[params.length];
        for (int i=0;i<params.length;i++){
            cmd[i]=params[i];
        }
        return exec(cmd);

    }
    public static String exec(String[] command){
        try {
            Runtime rt = Runtime.getRuntime();
            Process process = rt.exec(command);
//            process.waitFor();
            InputStream is = process.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuffer sb = new StringBuffer();
            br.lines().forEach(line->{
                sb.append(line);
            });
            return sb.toString();
        } catch (IOException e) {
//            e.printStackTrace();
            return "[Error] : "+e.getMessage();
        }finally {

        }
//        return "error";
    }



}
