package com.wip.bool.domain.cmmn.file;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * File에 대한 CleanUP 클래스
 * File Channel을 blocking //
 */
@Getter
@NoArgsConstructor
@Slf4j
public class FileManager {

    private String fileAbsolutePath;
    private String filePath;
    private String fileName;

    private Path path;
    private FileChannel channel;
    private ByteBuffer byteBuffer;

    private FileManager(String filePath, String fileName) throws IOException {
        this.filePath = filePath;
        this.fileName = fileName;
        this.fileAbsolutePath = filePath + fileName;
        path = Paths.get(this.fileAbsolutePath);
        channel = FileChannel.open(path, StandardOpenOption.WRITE, StandardOpenOption.CREATE);
        byteBuffer = ByteBuffer.allocate(1024);
    }

    public void write(byte[] bytes) throws IOException {
        channel.write(byteBuffer.put(bytes));
    }

    private void close() throws IOException {
        byteBuffer.clear();
        channel.close();
    }

    public static void use(String filePath, String fileName, FileInterface<FileManager, IOException> block) throws IOException {

        FileManager fileManager = new FileManager(filePath, getsFileDirectory(fileName));

        try {
            block.accept(fileManager);
        }
        finally {
            fileManager.close();
        }
    }

    public static boolean delete(String filePath, String fileName) throws IOException {
        return Files.deleteIfExists(Paths.get(filePath + getsFileDirectory(fileName)));
    }

    private static String getsFileDirectory(String fileName) {
        return String.format("%s/%s/%s/%s", fileName.charAt(0), fileName.charAt(1), fileName.charAt(2), fileName);
    }

    @FunctionalInterface
    public interface FileInterface<T, V extends Throwable> {
        void accept(T t);
    }



}
