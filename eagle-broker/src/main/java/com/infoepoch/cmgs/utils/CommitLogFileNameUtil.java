package com.infoepoch.cmgs.utils;

import io.netty.util.internal.StringUtil;

/**
 * commitLog 文件名称生成工具类
 */
public class CommitLogFileNameUtil {

    /**
     * 构建初始commitLog文件名称
     *
     * @return 00000000
     */
    public static String buildCommitLogFileName() {
        return "00000000";
    }

    /**
     * 递增commitLog文件名称
     *
     * @param oldCommitLogFileName 旧commitLog文件名称
     * @return 新commitLog文件名称
     */
    public static String incrCommitLogFileName(String oldCommitLogFileName) {
        if (StringUtil.isNullOrEmpty(oldCommitLogFileName)
                || !oldCommitLogFileName.matches("\\d{8}")) {
            throw new IllegalArgumentException("Invalid commit log file name: " + oldCommitLogFileName);
        }

        long num = Long.parseLong(oldCommitLogFileName);
        num++;
        if (num > 99999999) {
            throw new RuntimeException("Commit log file name too big: " + oldCommitLogFileName);
        }

        return String.format("%08d", num);
    }
}
