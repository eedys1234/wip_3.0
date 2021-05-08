package com.wip.bool.board.service;

import com.wip.bool.board.domain.*;
import com.wip.bool.board.dto.ReplyDto;
import com.wip.bool.cmmn.status.DeleteStatus;
import com.wip.bool.user.domain.User;
import com.wip.bool.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.hasText;

@Service
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;

    private final BoardRepository boardRepository;

    private final UserRepository userRepository;

    @Value("${spring.reply.path}")
    private String imageFilePath;

    @Transactional
    public Long save(Long userId, Long boardId, ReplyDto.ReplySaveRequest requestDto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다. id = " + userId));

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다. id =" + boardId));

        Reply reply = Reply.createReply(requestDto.getContent(), board, user);

        if(!Objects.isNull(requestDto.getParentId())) {
            Reply parent = replyRepository.findById(requestDto.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("부모 댓글이 존재하지 않습니다. id = " + requestDto.getParentId()));

            reply.updateParentReply(parent);
        }

        replyRepository.save(reply);

        if(hasText(requestDto.getOrgFileNames())) {

            List<ImageFile> imageFiles = Arrays.stream(requestDto.getOrgFileNames().split(","))
                    .map(orgFileName -> ImageFile.createImageFile(imageFilePath, orgFileName))
                    .collect(Collectors.toList());

            updateBoard(reply, imageFiles);

            moveImageFile(imageFiles, requestDto.getTempFileNames());
        }

        return reply.getId();
    }

    @Transactional(readOnly = true)
    public List<ReplyDto.ReplyResponse> getsByBoard(Long boardId, int size, int offset) {
        return nestedStructure(replyRepository.findAllByBoard(boardId, size, offset));
    }

    @Transactional(readOnly = true)
    public List<ReplyDto.ReplyResponse> getsByReply(Long replyId, int size, int offset) {
        return nestedStructure(replyRepository.findAllByReply(replyId, size, offset));
    }

    @Transactional
    public Long delete(Long userId, Long replyId) {

        Reply reply = replyRepository.findById(userId, replyId)
                .orElseThrow(() -> new IllegalArgumentException());

        if(reply.getChildReply().size() != 0) {
            reply.deleteStatus();
        }
        else {
            replyRepository.delete(getAncestorReply(reply));
        }

        return 1L;
    }

    private List<ReplyDto.ReplyResponse> nestedStructure(List<ReplyDto.ReplyResponse> replys) {

        List<ReplyDto.ReplyResponse> newReplys = new ArrayList<>();
        Map<Long, ReplyDto.ReplyResponse> replyResponseMap = new HashMap<>();

        for(ReplyDto.ReplyResponse reply : replys) {

            if(Objects.isNull(reply.getParentId())) {
                replyResponseMap.get(reply.getParentId()).getNodes().add(reply);
            }
            else {
                newReplys.add(reply);
            }
        }

        return newReplys;
    }

    private Reply getAncestorReply(Reply reply) {
        Reply parent = reply.getParentReply();

        if(!Objects.isNull(parent) && parent.getChildReply().size() == 1 && parent.getIsDeleted() == DeleteStatus.DELETE) {
            return getAncestorReply(parent);
        }
        return parent;
    }

    private void updateBoard(Reply reply, List<ImageFile> imageFiles) {

        for(ImageFile imageFile : imageFiles) {
            imageFile.updateReply(reply);
        }
    }

    private boolean moveImageFile(List<ImageFile> imageFiles, String tempFileNames) {

        //파일 생성
        int cnt = 0;
        boolean isSuccess = true;
        String[] tempFilePaths = tempFileNames.split(",");

        for(cnt=0;cnt<imageFiles.size();cnt++) {
            if(!imageFiles.get(cnt).createImageFile(tempFilePaths[cnt])) {
                isSuccess = false;
                break;
            }
        }

        if(!isSuccess) {
            for(int i=0;i<cnt;i++) {
                imageFiles.get(i).deleteImageFile();
            }
        }

        return isSuccess;
    }
}
