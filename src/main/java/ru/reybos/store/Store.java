package ru.reybos.store;

import ru.reybos.model.Item;

import java.util.List;

public interface Store {
    List<Item> findAllItem();

    List<Item> findDoneItems();

    List<Item> findUndoneItems();

    Item findItemById(int id);

    boolean save(Item item);

    boolean delete(Item item);

    boolean update(Item item);
}
