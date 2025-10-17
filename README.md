## 存储

- json配置文件读写和复写?
    - mmap内存映射技术
- commitLog是否写满问题?
    - 创建新的commitLog文件
- 定时刷盘?
    - 守护线程
- 多线程更改offset问题?
    - lock锁

## 分发

当消息写入到commitLog文件之后，需要将消息对应的commitLog文件的位置封装成对象，然后写入到consumerQueue中。

- 对象封装(commitLog文件名称、offset等信息)
- 写入操作(单线程)
- queue文件的mmap映射