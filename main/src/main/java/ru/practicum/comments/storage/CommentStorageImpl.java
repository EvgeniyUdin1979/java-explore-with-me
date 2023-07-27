package ru.practicum.comments.storage;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.practicum.comments.model.Comment;
import ru.practicum.comments.model.CommentsPrivateSearchParams;
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
    public List<Comment> getAllCommentForPrivate(CommentsPrivateSearchParams params) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QComment comment = QComment.comment;
        JPAQuery<Comment> eventJPAQuery = queryFactory.selectFrom(comment);
        eventJPAQuery.where(comment.creator.id.eq(params.getUserId()));
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

}
