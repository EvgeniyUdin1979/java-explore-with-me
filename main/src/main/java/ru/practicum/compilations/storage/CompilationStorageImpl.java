package ru.practicum.compilations.storage;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.practicum.compilations.model.Compilation;
import ru.practicum.compilations.model.QCompilation;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
public class CompilationStorageImpl implements CompilationStorageDao {

    private final CompilationRepository repository;
    private final EntityManager em;

    @Autowired
    public CompilationStorageImpl(CompilationRepository repository, EntityManager em) {
        this.repository = repository;
        this.em = em;
    }

    @Override
    public Compilation add(Compilation compilation) {
        return repository.save(compilation);
    }

    @Override
    public Compilation update(Compilation compilation) {
        return repository.save(compilation);
    }

    @Override
    public Long deleteById(long id) {
        return repository.deleteById(id);
    }

    @Override
    public Optional<Compilation> findById(long id) {
        return repository.findById(id);
    }

    @Override
    public List<Compilation> findAll(Optional<Boolean> pinned, int from, int size) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QCompilation compilation = QCompilation.compilation;
        JPAQuery<Compilation> compilationJPAQuery = queryFactory.selectFrom(compilation);
        pinned.ifPresent(aBoolean -> compilationJPAQuery.where(compilation.pinned.eq(aBoolean)));
        return compilationJPAQuery
                .offset(from)
                .limit(size)
                .fetch();
    }
}
