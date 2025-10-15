package com.infoepoch.cmgs.core;

import java.io.IOException;

/**
 * 消息追加处理器
 */
public class MessageAppendHandler {

    private MMapFileModelManager modelManager = new MMapFileModelManager();

    private static final String FILE_PATH = "E:\\study\\eaglemq\\broker\\store\\order_cancel_topic\\00000000";
    private static final String TOPIC = "test_topic";

    public MessageAppendHandler() throws IOException {
        MMapFileModel model = new MMapFileModel();
        model.load(FILE_PATH, 0, 1024 * 1024);
        modelManager.put(TOPIC, model);
    }

    public void append(String topic, String content) {
        prepare(topic, content);
    }

    private void prepare(String topic, String content) {
        MMapFileModel model = modelManager.get(topic);
        if (model == null) {
            throw new RuntimeException("无效的主题");
        }
        model.write(content.getBytes(), false);
    }

    public void read(String topic) {
        MMapFileModel model = modelManager.get(topic);
        if (model == null) {
            throw new RuntimeException("无效的主题");
        }
        byte[] msg = model.read(0, 10);
        System.out.println(new String(msg));
    }

    public static void main(String[] args) throws IOException {
        MessageAppendHandler handler = new MessageAppendHandler();
        handler.append(TOPIC, "this is content");
        handler.read(TOPIC);
    }
}
