package com.wip.bool.domain.cmmn.file;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.FileWriter;
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

    private FileWriter fw;
    private Path path;
    private FileChannel channel;
    private ByteBuffer byteBuffer;

    private FileManager(String filePath, String fileName) throws IOException {
        this.filePath = filePath;
        this.fileName = fileName;
        this.fileAbsolutePath = String.join("/", filePath, fileName);
    }

    public void write(byte[] bytes) throws IOException {
        byteBuffer = ByteBuffer.wrap(bytes);
//        byteBuffer.put(bytes);
//        byteBuffer.flip();
        channel.write(byteBuffer);
    }

    private void close() throws IOException {
        channel.close();
    }

    private void createParentDirectory() throws IOException {
        int index = this.fileAbsolutePath.lastIndexOf("/");

        if(index > -1 && !Files.exists(Paths.get(this.fileAbsolutePath.substring(0, index)))) {
            Files.createDirectories(Paths.get(this.fileAbsolutePath.substring(0, index)));
        }
    }

    private void connect() throws IOException {
        path = Paths.get(this.fileAbsolutePath);
        channel = FileChannel.open(path, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
    }

    public static boolean use(String filePath, String fileName, FileInterface<FileManager, IOException> block) throws IOException {

        FileManager fileManager = new FileManager(filePath, fileName);

        try {
            fileManager.createParentDirectory();
            fileManager.connect();
            block.accept(fileManager);
        }
        finally {
            fileManager.close();
            return true;
        }
    }

    public static boolean delete(String filePath, String fileName) throws IOException {
        if(Files.exists(Paths.get(filePath, fileName))) {
            return Files.deleteIfExists(Paths.get(filePath, fileName));
        }
        return true;
    }

    @FunctionalInterface
    public interface FileInterface<T, V extends Throwable> {
        void accept(T t) throws IOException;
    }



}
