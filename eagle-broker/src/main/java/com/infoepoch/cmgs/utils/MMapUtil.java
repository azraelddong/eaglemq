package com.infoepoch.cmgs.utils;

import sun.nio.ch.FileChannelImpl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * mmap内存映射工具类
 */
public class MMapUtil {

    private File file;
    private MappedByteBuffer mappedByteBuffer;
    private FileChannel fileChannel;

    /**
     * 加载文件
     *
     * @param filePath    文件路径
     * @param startOffset 开始偏移量
     * @param mappedSize  映射大小
     * @throws IOException IO异常
     */
    public void load(String filePath, int startOffset, int mappedSize) throws IOException {
        this.file = new File(filePath);
        if (!this.file.exists()) {
            throw new FileNotFoundException("文件路径：" + filePath + "，没有找到对应文件");
        }
        this.fileChannel = new RandomAccessFile(this.file, "rw").getChannel();
        this.mappedByteBuffer = this.fileChannel.map(FileChannel.MapMode.READ_WRITE, startOffset, mappedSize);
    }

    /**
     * 读取
     *
     * @param offset 开始读取偏移量
     * @param size   读取大小
     * @return 待读取的内容
     */
    public byte[] read(int offset, int size) {
        this.mappedByteBuffer.position(offset); // 定位开始读取的位置
        byte[] content = new byte[size];

        int j = 0;
        for (int i = 0; i < size; i++) {
            content[j++] = this.mappedByteBuffer.get(offset + i);   // 直接读取内存数据，很快！
        }
        return content;
    }

    /**
     * 写数据
     *
     * @param content 待写入内容数据
     * @param isForce 是否强制刷盘
     */
    public void write(byte[] content, boolean isForce) {
        this.mappedByteBuffer.put(content);
        if (isForce) {
            this.mappedByteBuffer.force();  // 强制刷盘数据
        }
    }

    /**
     * 清理mapped内存
     */
    public void clean() throws Exception {
        // todo：这种方式没有找到对应类
//        if (this.mappedByteBuffer == null || !this.mappedByteBuffer.isDirect() || this.mappedByteBuffer.capacity() == 0) {
//            return;
//        }
//
//        invoke(invoke(view(this.mappedByteBuffer), "cleaner"), "clean");

        Method method = FileChannelImpl.class.getDeclaredMethod("unmap", MappedByteBuffer.class);
        method.setAccessible(true);
        method.invoke(FileChannelImpl.class, this.mappedByteBuffer);
    }

    private ByteBuffer view(ByteBuffer buffer) {
        String methodName = "viewedBuffer";
        Method[] methods = buffer.getClass().getMethods();
        for (Method method : methods) {
            if (Objects.equals(method.getName(), methodName)) {
                methodName = "attachment";
                break;
            }
        }

        ByteBuffer viewedBuffer = (ByteBuffer) invoke(buffer, methodName);
        if (viewedBuffer == null) {
            return buffer;
        } else {
            return view(viewedBuffer);
        }
    }

    private Object invoke(final Object target, final String methodName, final Class<?>... args) {
        return AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
            try {
                Method method = method(target, methodName, args);
                method.setAccessible(true);
                return method.invoke(target);
            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            }
        });
    }

    private Method method(Object target, String methodName, Class<?>[] args) throws NoSuchMethodException {
        try {
            return target.getClass().getMethod(methodName, args);
        } catch (NoSuchMethodException e) {
            return target.getClass().getDeclaredMethod(methodName, args);
        }
    }

    /**
     * 测试映射内存是否释放
     */
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        long size = scanner.nextLong();
        MMapUtil mm = new MMapUtil();
        mm.load("/Users/apple/Documents/study/eaglemq/broker/store/order_cancel_topic/00000000", 0, (int) (size * 1024 * 1024));
        System.out.println("映射了" + size + "m的空间");
        TimeUnit.SECONDS.sleep(5);
        System.out.println("释放内存");
        mm.clean();
        TimeUnit.SECONDS.sleep(100000);
    }
}
