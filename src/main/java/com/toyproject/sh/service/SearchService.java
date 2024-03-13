package com.toyproject.sh.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.toyproject.sh.domain.Post;
import com.toyproject.sh.domain.QPost;
import com.toyproject.sh.domain.QTagManager;
import com.toyproject.sh.dto.PostResponse;
import com.toyproject.sh.exception.ExceptionHandler;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class SearchService extends QuerydslRepositorySupport {

    @PersistenceContext
    EntityManager em;

    public SearchService() {
        super(Post.class);
    }

    JPAQueryFactory queryFactory;

    public Page<PostResponse> searchPost(Pageable pageable, String search) {

        queryFactory = new JPAQueryFactory(em);
        QPost post = QPost.post;
        QTagManager tagManager = QTagManager.tagManager;

        if (search.startsWith("#")) {
            String tagSearch = "%" + search + "%";
            List<Post> posts = queryFactory
                    .select(tagManager.post)
                    .from(tagManager)
                    .where(tagManager.tag.name.like(tagSearch)) // 묵시적 조인
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .orderBy(post.count.desc())
                    .fetch();

            Page<Post> postPage = PageableExecutionUtils.getPage(posts, pageable, () -> queryFactory.select(tagManager.post.count()).from(tagManager).where(tagManager.tag.name.like(tagSearch)).fetch().get(0));

            return postPage.map(p -> new PostResponse(p));


        } else {

            String nameSearch = "%" + search + "%";
            List<Post> posts = queryFactory
                    .selectFrom(post)
                    .where(post.thumbnail.like(nameSearch))
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .orderBy(post.count.desc())
                    .fetch();

            Page<Post> postPage = PageableExecutionUtils.getPage(posts, pageable, () -> queryFactory.select(post.count()).from(post).where(post.thumbnail.like(nameSearch)).fetch().get(0));

            return postPage.map(p -> new PostResponse(p));

        }

    }

}
