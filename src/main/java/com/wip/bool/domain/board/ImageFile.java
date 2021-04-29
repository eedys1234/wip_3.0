package com.wip.bool.domain.board;

import com.wip.bool.cmmn.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "image_file")
public class ImageFile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_file_id")
    private Long id;

    @Column(name = "image_file_path")
    private String filePath;

    @Column(name = "org_file_name")
    private String orgFileName;

    @Column(name = "new_file_name", unique = true, length = 32, nullable = false)
    private String newFileName;

    private int size;

    @Column(name = "image_file_ext", length = 5, nullable = false)
    private String fileExt;


}
