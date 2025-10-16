package com.infoepoch.cmgs.utils;

import java.io.*;

/**
 * 文件相关工具类
 */
public class FileUtil {

    public static String read(String path) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            StringBuilder sb = new StringBuilder();
            while (reader.ready()) {
                sb.append(reader.readLine());
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    public static void write(String path, String content) {
        try (FileWriter fw = new FileWriter(path)) {
            fw.write(content);
            fw.flush();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
