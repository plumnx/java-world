package com.plumnix.cloud.multithreadwrite;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.stream.IntStream;

@Slf4j
public class MultiThreadWriteFile {

    @Test
    public void test_write_file_with_mapped_byte_buffer() {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(new File("C:\\Temp\\write_file.txt"), "rw")) {
            try (FileChannel fc = randomAccessFile.getChannel()) {
                try {
                    int position = 0;
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1_024_000);
                    for (int i = 0; i < 1000; i++) {
                        byteBuffer.clear();
                        IntStream.range(0, 1_000).forEach(no -> {
                            byteBuffer.put("This is a test line.This is a test line.This is a test line.This is a test line.This is a test line.This is a test line.This is a test line.This is a test line.This is a test line.\r\n".getBytes());
                        });
                        byteBuffer.flip();

                        MappedByteBuffer mappedByteBuffer = fc.map(FileChannel.MapMode.READ_WRITE, position, byteBuffer.limit());
                        mappedByteBuffer.put(byteBuffer);
                        position = position + byteBuffer.limit();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_merge_file_with_transfer() throws IOException {
        File[] files = {new File("C:\\Temp\\write_file.txt"), new File("C:\\Temp\\write_file1.txt")};
        File mergedFile = new File("C:\\Temp\\merged_file.txt");
        try (FileChannel resultFileChannel = new FileOutputStream(mergedFile, true).getChannel();) {
            for (File file : files) {
                try (FileChannel fileChannel = new FileInputStream(file).getChannel()) {
                    resultFileChannel.transferFrom(fileChannel, resultFileChannel.size(), fileChannel.size());
                }
            }
        }
    }
}
