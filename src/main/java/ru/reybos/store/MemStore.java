package ru.reybos.store;

import ru.reybos.model.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class MemStore implements Store {
    private static final Store INST = new MemStore();
    private final Map<Integer, Item> data = new ConcurrentHashMap<>();
    private AtomicInteger id = new AtomicInteger(1);

    private MemStore() { }

    public static Store instOf() {
        return INST;
    }

    @Override
    public List<Item> findAllItem() {
        return new ArrayList<>(data.values());
    }

    @Override
    public List<Item> findDoneItems() {
        return data.values().stream()
                .filter(Item::isDone)
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> findUndoneItems() {
        return data.values().stream()
                .filter(item -> !item.isDone())
                .collect(Collectors.toList());
    }

    @Override
    public Item findItemById(int id) {
        return data.get(id);
    }

    @Override
    public boolean save(Item item) {
        item.setId(id.getAndIncrement());
        data.put(item.getId(), item);
        return true;
    }

    @Override
    public boolean delete(Item item) {
        int id = item.getId();
        return data.remove(id, item);
    }

    @Override
    public boolean update(Item item) {
        int id = item.getId();
        if (!data.containsKey(id)) {
            return false;
        }
        data.put(id, item);
        return true;
    }
}
