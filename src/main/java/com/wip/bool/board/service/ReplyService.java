package com.wip.bool.board.service;

import com.wip.bool.board.domain.ReplyRepository;
import com.wip.bool.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;

    private final UserRepository userRepository;

    @Value("${spring.reply.path}")
    private String imageFilePath;

//    @Transactional
//    public Long save(Long userId, ReplyDto.ReplySaveRequest requestDto) {
//
//        List<ImageFile> imageFiles = Arrays.stream(requestDto.getOrgFileNames().split(","))
//                .map(orgFileName -> ImageFile.createImageFile(imageFilePath, orgFileName))
//                .collect(Collectors.toList());
//
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다. id = " + userId));
//
//        Reply reply = Reply.create
//        return 1L;
//    }


}
