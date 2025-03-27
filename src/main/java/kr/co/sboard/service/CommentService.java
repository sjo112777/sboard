package kr.co.sboard.service;

import kr.co.sboard.dto.CommentDTO;
import kr.co.sboard.entity.Comment;
import kr.co.sboard.entity.User;
import kr.co.sboard.repository.CommentRepository;
import kr.co.sboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public List<CommentDTO> findByParent(int parent){

        List<Comment> commentList = commentRepository.findByParent(parent);

        List<CommentDTO> commentDTOList = commentList.stream().map(entity -> {
            CommentDTO commentDTO = modelMapper.map(entity, CommentDTO.class);
            return commentDTO;
        }).toList();

        return commentDTOList;
    }

    public CommentDTO save(CommentDTO commentDTO){

        User user = userRepository.findById(commentDTO.getWriter()).get();

        Comment comment = modelMapper.map(commentDTO, Comment.class);
        comment.setUser(user);

        Comment savedComment = commentRepository.save(comment);
        log.info("savedComment : {}", savedComment);

        return modelMapper.map(savedComment, CommentDTO.class);
    }

}