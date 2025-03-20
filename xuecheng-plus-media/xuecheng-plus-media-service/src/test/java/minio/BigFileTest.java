package minio;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * @author: Elon
 * @title: BigFileTest
 * @projectName: xuecheng-plus-project
 * @description: TODO
 * @date: 2025/3/20 11:09
 */
public class BigFileTest {

    @Test
    public void testChunk() throws IOException {
        File sourceFile = new File("G:\\Carls\\xuecheng-plus-project\\file\\mediavideo\\燃烧.2018.BD1080p.zwzm.mp4");
        String chunkPath = "G:\\Carls\\xuecheng-plus-project\\file\\bigfiletest";
        File chunkFolder = new File(chunkPath);
        if (!chunkFolder.exists()) {
            chunkFolder.mkdirs();
        }

        // 分块大小
        long chunkSize = 1024 * 1024 * 10;
        // 分块数量
        long chunkNum = (long) Math.ceil(sourceFile.length() * 1.0 / chunkSize);
        System.out.println("分块总数: " + chunkNum);

        // 增大缓冲区大小
        int bufferSize = 8192; // 8KB
        byte[] b = new byte[bufferSize];

        // 使用 try-with-resources 语句自动关闭资源
        try (RandomAccessFile raf_read = new RandomAccessFile(sourceFile, "r")) {
            // 分块
            for (int i = 0; i < chunkNum; i++) {
                // 创建分块文件
                File file = new File(chunkPath + i);
                if (file.exists()) {
                    file.delete();
                }

                if (file.createNewFile()) {
                    // 向分块文件中写入数据
                    try (RandomAccessFile raf_write = new RandomAccessFile(file, "rw")) {
                        int len;
                        while ((len = raf_read.read(b)) != -1) {
                            raf_write.write(b, 0, len);
                            if (file.length() >= chunkSize) {
                                break;
                            }
                        }
                    }
                }
            }
        }
    }
}