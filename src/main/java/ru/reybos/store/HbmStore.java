package ru.reybos.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import ru.reybos.model.Item;

import java.util.List;
import java.util.function.Function;

public class HbmStore implements Store, AutoCloseable {
    private static final Store INST = new HbmStore();
    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private final SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();

    private HbmStore() { }

    public static Store instOf() {
        return INST;
    }

    @Override
    public List<Item> findAllItem() {
        return tx(session -> {
            String sql = "FROM ru.reybos.model.Item";
            final Query query = session.createQuery(sql);
            return query.list();
        });
    }

    @Override
    public List<Item> findDoneItems() {
        return tx(session -> {
            String sql = "FROM ru.reybos.model.Item WHERE done=true ORDER BY created";
            final Query query = session.createQuery(sql);
            return query.list();
        });
    }

    @Override
    public List<Item> findUndoneItems() {
        return tx(session -> {
            String sql = "FROM ru.reybos.model.Item WHERE done=false ORDER BY created";
            final Query query = session.createQuery(sql);
            return query.list();
        });
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public Item findItemById(int id) {
        return tx(session -> {
            final Query query = session.createQuery("FROM ru.reybos.model.Item WHERE id=:id");
            query.setParameter("id", id);
            List<Item> result = (List<Item>) query.list();
            return result.size() == 0 ? null : result.get(0);
        });
    }

    @Override
    public boolean save(Item item) {
        return tx(session -> {
            session.save(item);
            return true;
        });
    }

    @Override
    public boolean delete(Item item) {
        return tx(session -> {
            session.delete(item);
            return true;
        });
    }

    @Override
    public boolean update(Item item) {
        return tx(session -> {
            session.update(item);
            return true;
        });
    }

    private <T> T tx(final Function<Session, T> command) {
        final Session session = sf.openSession();
        final Transaction tx = session.beginTransaction();
        try {
            T rsl = command.apply(session);
            tx.commit();
            return rsl;
        } catch (final Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public void close() throws Exception {
        StandardServiceRegistryBuilder.destroy(registry);
    }
}
