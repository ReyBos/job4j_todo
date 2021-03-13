package ru.reybos.store;

import ru.reybos.model.Category;
import ru.reybos.model.Item;
import ru.reybos.model.User;

import java.util.List;

public interface Store {
    List<Item> findAllItem();

    List<Item> findDoneItems();

    List<Item> findUndoneItems();

    Item findItemById(int id);

    boolean save(Item item);

    boolean delete(Item item);

    boolean update(Item item);

    User findUserByEmail(String email);

    boolean save(User user);

    boolean delete(User user);

    List<Category> getAllCategories();
}
