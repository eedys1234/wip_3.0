package com.wip.bool.domain.board;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class ImageFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_file_id")
    private Long id;

    @Column(name = "org_name")
    private String orgName;

    @Column(name = "new_name")
    private String newName;

}
