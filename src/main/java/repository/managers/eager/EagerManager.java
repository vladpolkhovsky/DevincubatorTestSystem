package repository.managers.eager;

import repository.managers.lazy.LazyManager;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * Позволяет избежать ошибки LazyInitializeException
 * @param <T> Entity
 */

@Repository
public interface EagerManager<T> extends LazyManager<T> {

    T load(T t, Session session);

    default T load(T t) {
        try(Session session = getSessionFactory().openSession()) {
            return load(t, session);
        }
    }

    default Set<T> loadAll() {
        try(Session session = getSessionFactory().openSession()) {
            return loadAll(LazyManager.super.getAll(session), session);
        }
    }

    default Set<T> loadAll(Collection<T> entities) {
        try(Session session = getSessionFactory().openSession()) {
            return loadAll(entities, session);
        }
    }

    default Set<T> loadAll(Session session) {
        return loadAll(LazyManager.super.getAll(session), session);
    }

    default Set<T> loadAll(Collection<T> entities, Session session) {
        Set<T> list = new HashSet<>(entities.size());
        for(T t : entities)
            list.add(load(t, session));
        return list;
    }

    @Override
    default List<T> executeSql(String sql) {
        return new ArrayList<>(loadAll(LazyManager.super.executeSql(sql)));
    }

    @Override
    default T read(int id, Session session) {
        return load(getRepository().findById(getRepository().getTemplatedClass(), id, session), session);
    }

    @Override
    default T read(int id) {
        try (Session session = getSessionFactory().openSession()) {
            return load(getRepository().findById(getRepository().getTemplatedClass(), id, session), session);
        }
    }

    @Override
    default List<T> getAll() {
        return new ArrayList<>(loadAll());
    }

    @Override
    default List<T> getAll(Session session) {
        return new ArrayList<>(loadAll(session));
    }
}
