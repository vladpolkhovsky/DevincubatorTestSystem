package repository.managers.lazy;

import repository.dao.DaoRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Предназначен для упрощения контроля сессий и транзакций при взаимодействии с БД.
 * LazyManager базовый интерфейс для менеджеров.
 * @param <T> Entity
 */

@Repository
public interface LazyManager<T> {

    SessionFactory getSessionFactory();

    DaoRepository<T> getRepository();

    default void create(T entity) {
        try(Session session = getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            getRepository().create(entity, session);
            transaction.commit();
        }
    }

    default T read(int id) {
        try(Session session = getSessionFactory().openSession()){
            return read(id, session);
        }
    }

    default void update(T entity) {
        try(Session session = getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            getRepository().update(entity, session);
            transaction.commit();
        }
    }

    default void delete(T entity) {
        try(Session session = getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            getRepository().delete(entity, session);
            transaction.commit();
        }
    }

    default List<T> executeSql(String sql) {
        try(Session session = getSessionFactory().openSession()) {
            return getRepository().executeSql(sql, session);
        }
    }

    default List<T> executeSql(String sql, Session session) {
        return getRepository().executeSql(sql, session);
    }

    /**
     * Метод используется для получения прокси объекта с "живой" сессией. Используйте этот метод, когда нужно получить доступ к полям объекта,
     * которые помечены "{@code fetch = FetchType.LAZY}". Ответственность за состояние сессии ложится на пользователя.
     * @param id идентификатор объекта
     * @param session сессия соединения с БД
     * @return объект с указанным идентификатором.
     */
    default T read(int id, Session session) {
        return getRepository().findById(getRepository().getTemplatedClass(), id, session);
    }

    default List<T> getAll() {
        try(Session session = getSessionFactory().openSession()){
            return getRepository().findAll(session);
        }
    }

    default List<T> getAll(Session session) {
        return getRepository().findAll(session);
    }

}
