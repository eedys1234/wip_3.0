package com.wip.bool.domain.cmmn.file;

import java.io.FileOutputStream;
import java.io.IOException;

public class FileIOManager extends FileManager {

    private FileOutputStream fileOutputStream;

    private FileIOManager(String filePath, String fileName) throws IOException {
        super(filePath, fileName, String.join("/", fileName, filePath));
    }

    @Override
    public void write(byte[] bytes) throws IOException {
        fileOutputStream.write(bytes);
    }

    @Override
    protected void close() throws IOException {
        fileOutputStream.close();
    }

    @Override
    protected void connect() throws IOException {
        fileOutputStream = new FileOutputStream(this.fileAbsolutePath);
    }

    public static boolean use(String filePath, String fileName, FileInterface<FileManager, IOException> block) throws IOException {

        FileIOManager fileManager = new FileIOManager(filePath, fileName);

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
}
