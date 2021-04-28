package com.wip.bool.web.controller.file;

import com.wip.bool.cmmn.file.FileManager;
import com.wip.bool.cmmn.file.FileNIOManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.file.FileSystems;
import java.nio.file.Files;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@ActiveProfiles(value = "local")
@SpringBootTest
public class FileManagerTest {

    @Value("${spring.images.path}")
    private String imagesFilePath;

    @Test
    public void FileManager_테스트() throws Exception {

        String orgFileName = "test_01.png";

        boolean isSave = FileManager.use(imagesFilePath, "/A/" + orgFileName, FileNIOManager.class,
                fileManager -> fileManager.write(Files.readAllBytes(FileSystems.getDefault().getPath(imagesFilePath, orgFileName)))
        );

        assertThat(isSave).isEqualTo(true);
    }
}
