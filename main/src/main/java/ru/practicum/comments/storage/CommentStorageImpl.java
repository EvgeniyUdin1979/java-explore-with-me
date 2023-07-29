package ru.practicum.comments.storage;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.practicum.comments.model.Comment;
import ru.practicum.comments.model.CommentsSearchParams;
import ru.practicum.comments.model.QComment;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
public class CommentStorageImpl implements CommentStorageDao {

    private final CommentRepository repository;

    private final EntityManager em;


    @Autowired
    public CommentStorageImpl(CommentRepository repository, EntityManager em) {
        this.repository = repository;
        this.em = em;
    }

    @Override
    public Optional<Comment> findById(long commentId) {
        return repository.findById(commentId);
    }

    @Override
    public Comment add(Comment comment) {
        return repository.save(comment);
    }

    @Override
    public Comment update(Comment comment) {
        return repository.save(comment);
    }

    @Override
    public List<Comment> getAllComment(CommentsSearchParams params) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QComment comment = QComment.comment;
        JPAQuery<Comment> eventJPAQuery = queryFactory.selectFrom(comment);
        if (params.getUserIds() != null) {
            eventJPAQuery.where(comment.creator.id.in(params.getUserIds()));
        }
        if (params.getCommentIds() != null) {
            eventJPAQuery.where(comment.id.in(params.getCommentIds()));
        }
        if (params.getStatus().isPresent()) {
            eventJPAQuery.where(comment.status.eq(params.getStatus().get()));
        }
        if (params.getRangeStart() != null) {
            eventJPAQuery.where(comment.created.after(params.getRangeStart()));
        }
        if (params.getRangeEnd() != null) {
            eventJPAQuery.where(comment.created.before(params.getRangeEnd()));
        }
        return eventJPAQuery
                .offset(params.getFrom())
                .limit(params.getSize())
                .fetch();
    }

    @Override
    public List<Comment> getAllCommentsById(List<Long> commentIds) {

        return repository.getAllCommentsByIdIn(commentIds);
    }

    @Override
    public List<Comment> updateAll(List<Comment> comments) {
        return repository.saveAll(comments);
    }
}
