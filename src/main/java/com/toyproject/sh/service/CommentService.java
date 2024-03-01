package com.toyproject.sh.service;

import com.toyproject.sh.domain.Comment;
import com.toyproject.sh.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public void saveComment(Comment comment) {
        commentRepository.save(comment);
    }

    public Comment findParentComment(Long id) {
        return commentRepository.findOneComment(id);
    }
}
