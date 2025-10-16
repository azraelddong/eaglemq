package com.infoepoch.cmgs.core;

import com.infoepoch.cmgs.constants.BrokerConstants;
import com.infoepoch.cmgs.model.CommitLogMessageModel;

import java.io.IOException;

/**
 * 消息追加处理器
 */
public class CommitLogAppendHandler {

    private MMapFileModelManager modelManager = new MMapFileModelManager();

    public void prepareLoading(String topic) throws IOException {
        MMapFileModel model = new MMapFileModel();
        model.load(topic, 0, BrokerConstants.MMAP_SIZE);
        modelManager.put(topic, model);
    }

    public void append(String topic, byte[] content) throws IOException {
        prepare(topic, content);
    }

    public void prepare(String topic, byte[] content) throws IOException {
        MMapFileModel model = modelManager.get(topic);
        if (model == null) {
            throw new RuntimeException("invalid topic " + topic);
        }
        model.write(new CommitLogMessageModel(content, content.length));
    }

    public void read(String topic) {
        MMapFileModel model = modelManager.get(topic);
        if (model == null) {
            throw new RuntimeException("invalid topic " + topic);
        }
        byte[] msg = model.read(0, 10);
        System.out.println(new String(msg));
    }
}
