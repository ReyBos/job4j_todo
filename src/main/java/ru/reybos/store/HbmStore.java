package ru.reybos.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.reybos.model.Item;

import java.util.List;

public class HbmStore implements Store, AutoCloseable {
    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private final SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();

    @Override
    public List<Item> findAllItem() {
        String sql = "FROM ru.reybos.model.Item";
        return findItemsByQuery(sql);
    }

    @Override
    public List<Item> findDoneItems() {
        String sql = "FROM ru.reybos.model.Item WHERE done=true";
        return findItemsByQuery(sql);
    }

    @Override
    public List<Item> findUndoneItems() {
        String sql = "FROM ru.reybos.model.Item WHERE done=false";
        return findItemsByQuery(sql);
    }

    @SuppressWarnings({"unchecked"})
    private List<Item> findItemsByQuery(String sql) {
        Session session = sf.openSession();
        session.beginTransaction();
        List<Item> result = (List<Item>) session.createQuery(sql).list();
        session.getTransaction().commit();
        session.close();
        return result;
    }

    @Override
    public Item findItemById(int id) {
        Session session = sf.openSession();
        session.beginTransaction();
        Item result = session.get(Item.class, id);
        session.getTransaction().commit();
        session.close();
        return result;
    }

    @Override
    public Item save(Item item) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.save(item);
        session.getTransaction().commit();
        session.close();
        return item;
    }

    @Override
    public void delete(int id) {
        Item item = findItemById(id);
        Session session = sf.openSession();
        session.beginTransaction();
        session.delete(item);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void update(Item item) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.update(item);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void close() throws Exception {
        StandardServiceRegistryBuilder.destroy(registry);
    }
}
