package com.wip.bool.domain.cmmn.file;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * File에 대한 CleanUP 클래스
 * File Channel을 blocking //
 */
@Getter
@Slf4j
public class FileNIOManager extends FileManager {

    private Path path;
    private FileChannel channel;
    private ByteBuffer byteBuffer;

    private FileNIOManager(String filePath, String fileName) throws IOException {
        super(filePath, fileName, String.join("/", filePath, fileName));
    }

    public void write(byte[] bytes) throws IOException {
        byteBuffer = ByteBuffer.wrap(bytes);
        channel.write(byteBuffer);
    }

    protected void close() throws IOException {
        channel.close();
    }

    protected void connect() throws IOException {
        path = Paths.get(this.fileAbsolutePath);
        channel = FileChannel.open(path, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
    }

    public static boolean use(String filePath, String fileName, FileInterface<FileNIOManager, IOException> block) throws IOException {

        FileNIOManager fileManager = new FileNIOManager(filePath, fileName);

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
