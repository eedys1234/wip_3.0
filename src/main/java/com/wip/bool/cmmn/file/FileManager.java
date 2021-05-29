package com.wip.bool.cmmn.file;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 외부 Resource(File)에 대한 클린업 클래스
 * File IO, NIO 등 입출력에 관하여 유연하게 대처하기 위한 추상클래스
 */
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
        int index = this.fileAbsolutePath.lastIndexOf('/');

        Path path = Paths.get(this.fileAbsolutePath.substring(0, index));
        if(index > -1 && !path.toFile().exists()) {
            Files.createDirectories(path);
        }
    }

    public static boolean delete(String filePath, String fileName) throws IOException {
        Path path = Paths.get(filePath, fileName);
        if(path.toFile().exists()) {
            return Files.deleteIfExists(path);
        }
        return true;
    }

    public static boolean copy(String srcFullFilePath, String destFullFilePath) throws IOException {
        Files.copy(FileSystems.getDefault().getPath(srcFullFilePath), FileSystems.getDefault().getPath(destFullFilePath));
        return true;
    }

    public static boolean move(String srcFullFilePath, String destFullFilePath) throws IOException {
        Files.move(FileSystems.getDefault().getPath(srcFullFilePath), FileSystems.getDefault().getPath(destFullFilePath));
        return true;
    }

    public static <T> boolean use(String filePath, String fileName, Class<T> classType, FileInterface<FileManager, IOException> block)
            throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        return (boolean) classType.getDeclaredMethod("use", String.class, String.class, FileInterface.class)
        .invoke(null, filePath, fileName, block);
    }

    @FunctionalInterface
    public interface FileInterface <T extends FileManager, V extends Throwable> {
        void accept(T t) throws V;
    }
}
