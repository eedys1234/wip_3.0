package com.wip.bool.domain.cmmn.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public abstract class FileManager {

    protected String fileAbsolutePath;
    protected String filePath;
    protected String fileName;

    public FileManager(String filePath, String fileName, String fileAbsolutePath) {
        this.filePath = filePath;
        this.fileName = fileName;
        this.fileAbsolutePath = fileAbsolutePath;
    }

    public abstract void write(byte[] bytes) throws IOException;

    protected abstract void close() throws IOException;

    protected abstract void connect() throws IOException;

    protected void createParentDirectory() throws IOException {
        int index = this.fileAbsolutePath.lastIndexOf("/");

        if(index > -1 && !Files.exists(Paths.get(this.fileAbsolutePath.substring(0, index)))) {
            Files.createDirectories(Paths.get(this.fileAbsolutePath.substring(0, index)));
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
