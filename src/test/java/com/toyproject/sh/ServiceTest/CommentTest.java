package com.toyproject.sh.ServiceTest;

import com.toyproject.sh.domain.Comment;
import com.toyproject.sh.domain.Member;
import com.toyproject.sh.service.CommentService;
import com.toyproject.sh.service.MemberService;
import com.toyproject.sh.service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CommentTest {

    @Autowired
    CommentService commentService;

    @Test
    void 댓글과대댓글테스트() {
        Comment comment = new Comment();
        Comment reply = new Comment(null, null, "111", comment);
        commentService.saveComment(comment);
        commentService.saveComment(reply);

    }
}
