package ru.reybos.store;

import ru.reybos.model.Item;

import java.util.List;

public interface Store {
    List<Item> findAllItem();

    List<Item> findDoneItems();

    List<Item> findUndoneItems();

    Item findItemById(int id);

    Item save(Item item);

    void delete(int id);

    void update(Item item);
}
