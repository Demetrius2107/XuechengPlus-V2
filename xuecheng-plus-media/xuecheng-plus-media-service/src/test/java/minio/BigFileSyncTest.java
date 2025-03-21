package minio;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author: Elon
 * @title: BigFileSyncTest
 * @projectName: xuecheng-plus-project
 * @description: TODO
 * @date: 2025/3/20 11:33
 */
public class BigFileSyncTest {

    @Test
    public void testChunk() throws IOException, InterruptedException {
        File sourceFile = new File("G:\\Carls\\xuecheng-plus-project\\file\\mediavideo\\燃烧.2018.BD1080p.zwzm.mp4");
        String chunkPath = "G:\\Carls\\xuecheng-plus-project\\file\\bigfiletest\\chunk";
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
        int bufferSize = 65536; // 64KB
        byte[] buffer = new byte[bufferSize];

        // 创建线程池
        int threadCount = Runtime.getRuntime().availableProcessors();
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

        for (int i = 0; i < chunkNum; i++) {
            final int chunkIndex = i;
            executorService.submit(() -> {
                try (RandomAccessFile raf_read = new RandomAccessFile(sourceFile, "r")) {
                    File chunkFile = new File(chunkPath + chunkIndex);
                    if (chunkFile.exists()) {
                        chunkFile.delete();
                    }
                    if (chunkFile.createNewFile()) {
                        try (RandomAccessFile raf_write = new RandomAccessFile(chunkFile, "rw")) {
                            // 定位到分块起始位置
                            raf_read.seek(chunkIndex * chunkSize);
                            long bytesToRead = Math.min(chunkSize, sourceFile.length() - chunkIndex * chunkSize);
                            long bytesRead = 0;
                            int len;
                            while (bytesRead < bytesToRead && (len = raf_read.read(buffer, 0, (int) Math.min(bufferSize, bytesToRead - bytesRead))) != -1) {
                                raf_write.write(buffer, 0, len);
                                bytesRead += len;
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        // 关闭线程池
        executorService.shutdown();
        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
    }

    //测试文件合并方法
    @Test
    public void testMerge() throws IOException {
        //块文件目录
        File chunkFolder = new File("d:/develop/bigfile_test/chunk/");
        //原始文件
        File originalFile = new File("d:/develop/bigfile_test/nacos.mp4");
        //合并文件
        File mergeFile = new File("d:/develop/bigfile_test/nacos01.mp4");
        if (mergeFile.exists()) {
            mergeFile.delete();
        }
        //创建新的合并文件
        mergeFile.createNewFile();
        //用于写文件
        RandomAccessFile raf_write = new RandomAccessFile(mergeFile, "rw");
        //指针指向文件顶端
        raf_write.seek(0);
        //缓冲区
        byte[] b = new byte[1024];
        //分块列表
        File[] fileArray = chunkFolder.listFiles();
        // 转成集合，便于排序
        List<File> fileList = Arrays.asList(fileArray);
        // 从小到大排序
        Collections.sort(fileList, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                return Integer.parseInt(o1.getName()) - Integer.parseInt(o2.getName());
            }
        });
        //合并文件
        for (File chunkFile : fileList) {
            RandomAccessFile raf_read = new RandomAccessFile(chunkFile, "rw");
            int len = -1;
            while ((len = raf_read.read(b)) != -1) {
                raf_write.write(b, 0, len);

            }
            raf_read.close();
        }
        raf_write.close();

        //校验文件
        try (

                FileInputStream fileInputStream = new FileInputStream(originalFile);
                FileInputStream mergeFileStream = new FileInputStream(mergeFile);

        ) {
            //取出原始文件的md5
            String originalMd5 = DigestUtils.md5Hex(fileInputStream);
            //取出合并文件的md5进行比较
            String mergeFileMd5 = DigestUtils.md5Hex(mergeFileStream);
            if (originalMd5.equals(mergeFileMd5)) {
                System.out.println("合并文件成功");
            } else {
                System.out.println("合并文件失败");
            }

        }


    }
}