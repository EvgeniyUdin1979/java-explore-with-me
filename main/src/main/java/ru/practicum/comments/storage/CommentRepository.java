package ru.practicum.comments.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.comments.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
