package com.infoepoch.cmgs.utils;

import com.alibaba.fastjson.JSON;
import com.infoepoch.cmgs.model.EagleMqTopicModel;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;

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

    public static void main(String[] args) {
        String content = read("/Users/apple/Documents/study/eaglemq/broker/config/eaglemq-topic.json");
        List<EagleMqTopicModel> models = JSON.parseArray(content, EagleMqTopicModel.class);
        System.out.println(models);
    }
}
