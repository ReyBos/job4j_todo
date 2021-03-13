package ru.reybos.store;

import ru.reybos.model.Category;
import ru.reybos.model.Item;
import ru.reybos.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class MemStore implements Store {
    private static final Store INST = new MemStore();
    private final Map<Integer, Item> items = new ConcurrentHashMap<>();
    private final Map<Integer, User> users = new ConcurrentHashMap<>();
    private final Map<Integer, Category> categories = new ConcurrentHashMap<>();
    private final AtomicInteger itemId = new AtomicInteger(1);
    private final AtomicInteger userId = new AtomicInteger(1);

    private MemStore() { }

    public static Store instOf() {
        return INST;
    }

    @Override
    public List<Item> findAllItem() {
        return new ArrayList<>(items.values());
    }

    @Override
    public List<Item> findDoneItems() {
        return items.values().stream()
                .filter(Item::isDone)
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> findUndoneItems() {
        return items.values().stream()
                .filter(item -> !item.isDone())
                .collect(Collectors.toList());
    }

    @Override
    public Item findItemById(int id) {
        return items.get(id);
    }

    @Override
    public boolean save(Item item) {
        item.setId(itemId.getAndIncrement());
        items.put(item.getId(), item);
        return true;
    }

    @Override
    public boolean delete(Item item) {
        int id = item.getId();
        return items.remove(id, item);
    }

    @Override
    public boolean update(Item item) {
        int id = item.getId();
        if (!items.containsKey(id)) {
            return false;
        }
        items.put(id, item);
        return true;
    }

    @Override
    public User findUserByEmail(String email) {
        for (User user : users.values()) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public boolean save(User user) {
        user.setId(userId.getAndIncrement());
        users.put(user.getId(), user);
        return false;
    }

    @Override
    public boolean delete(User user) {
        return users.remove(user.getId(), user);
    }

    @Override
    public List<Category> getAllCategories() {
        return new ArrayList<>(categories.values());
    }
}