package com.wip.bool.board.service;

import com.wip.bool.board.domain.*;
import com.wip.bool.board.dto.ReplyDto;
import com.wip.bool.cmmn.status.DeleteStatus;
import com.wip.bool.exception.excp.AuthorizationException;
import com.wip.bool.exception.excp.EntityNotFoundException;
import com.wip.bool.exception.excp.ErrorCode;
import com.wip.bool.user.domain.Role;
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
    public Long saveReply(Long userId, Long boardId, ReplyDto.ReplySaveRequest requestDto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(userId, ErrorCode.NOT_FOUND_USER));

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException(boardId, ErrorCode.NOT_FOUND_BOARD));

        Reply reply = Reply.createReply(requestDto.getContent(), board, user);

        if(!Objects.isNull(requestDto.getParentId())) {
            Reply parent = replyRepository.findById(requestDto.getParentId())
                    .orElseThrow(() -> new EntityNotFoundException("부모 댓글이 존재하지 않습니다. id = " + requestDto.getParentId(), ErrorCode.NOT_FOUND_REPLY));

            reply.updateParentReply(parent);
        }

        reply = replyRepository.save(reply);

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
    public Long updateReply(Long userId, Long replyId, ReplyDto.ReplyUpdateRequest requestDto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(userId, ErrorCode.NOT_FOUND_USER));

        Role role = user.getRole();

        if(role != Role.ROLE_ADMIN && role != Role.ROLE_NORMAL) {
            throw new AuthorizationException();
        }

        Reply reply = replyRepository.findById(userId, replyId, role)
                .orElseThrow(() -> new EntityNotFoundException(userId, ErrorCode.NOT_FOUND_REPLY));

        reply.updateContent(requestDto.getContent());

        if(hasText(requestDto.getOrgFileNames())) {

            List<ImageFile> imageFiles = Arrays.stream(requestDto.getOrgFileNames().split(","))
                    .map(orgFileName -> ImageFile.createImageFile(imageFilePath, orgFileName))
                    .collect(Collectors.toList());

            deleteImageFile(imageFiles);
            updateBoard(reply, imageFiles);
            moveImageFile(imageFiles, requestDto.getTempFileNames());
        }

        return reply.getId();
    }

    @Transactional
    public Long deleteReply(Long userId, Long replyId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(userId, ErrorCode.NOT_FOUND_USER));

        Role role = user.getRole();

        if(role != Role.ROLE_ADMIN && role != Role.ROLE_NORMAL) {
            throw new AuthorizationException();
        }

        Reply reply = replyRepository.findById(userId, replyId, role)
                    .orElseThrow(() -> new EntityNotFoundException(userId, ErrorCode.NOT_FOUND_REPLY));

        if(reply.getChildReply().size() > 0) {
            reply.deleteStatus();
        }
        else {
            replyRepository.delete(getAncestorReply(reply));
        }

        return 1L;
    }

    private List<ReplyDto.ReplyResponse> nestedStructure(List<ReplyDto.ReplyResponse> replies) {

        List<ReplyDto.ReplyResponse> newReplies = new ArrayList<>();
        Map<Long, ReplyDto.ReplyResponse> replyResponseMap = new HashMap<>();

        for(ReplyDto.ReplyResponse reply : replies)
        {
            replyResponseMap.put(reply.getReplyId(), reply);

            if(!Objects.isNull(reply.getParentId())) {
                replyResponseMap.get(reply.getParentId()).getNodes().add(reply);
            }
            else {
                newReplies.add(reply);
            }
        }

        return newReplies;
    }

    private Reply getAncestorReply(Reply reply) {
        Reply parent = reply.getParentReply();

        if(!Objects.isNull(parent) && parent.getChildReply().size() == 1 && parent.getIsDeleted() == DeleteStatus.DELETE) {
            return getAncestorReply(parent);
        }
        return reply;
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

    private boolean deleteImageFile(List<ImageFile> imageFiles) {

        for(ImageFile imageFile : imageFiles) {
            if(!imageFile.deleteImageFile()) {
                return false;
            }
        }
        return true;
    }
}
