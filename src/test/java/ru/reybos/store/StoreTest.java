package ru.reybos.store;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.reybos.model.Item;

import java.util.Comparator;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class StoreTest {
    private static final Logger LOG = LoggerFactory.getLogger(StoreTest.class.getName());
    private Store store;

    @Before
    public void init() {
        store = new HbmStore();
    }

    @Test
    public void saveItem() {
        Item item = new Item("сделать что-то", true);
        store.save(item);
        Item rsl = store.findItemById(item.getId());
        assertThat(rsl, is(item));
        store.delete(item.getId());
    }

    @Test
    public void updateItem() {
        Item item = new Item("сделать что-то", true);
        store.save(item);
        item.setDescription("не сделали");
        item.setDone(false);
        store.update(item);
        Item rsl = store.findItemById(item.getId());
        assertThat(rsl, is(item));
        store.delete(item.getId());
    }

    @Test
    public void findAllItem() {
        Item item = new Item("сделать что-то", true);
        Item item2 = new Item("сделать что-то 2", false);
        store.save(item);
        store.save(item2);
        List<Item> expected = List.of(item, item2);
        List<Item> out = store.findAllItem();
        out.sort(Comparator.comparing(Item::getCreated));
        assertThat(out, is(expected));
        store.delete(item.getId());
        store.delete(item2.getId());
    }

    @Test
    public void findDoneItems() {
        Item item = new Item("сделать что-то", true);
        Item item2 = new Item("сделать что-то 2", false);
        store.save(item);
        store.save(item2);
        List<Item> expected = List.of(item);
        List<Item> out = store.findDoneItems();
        assertThat(out, is(expected));
        store.delete(item.getId());
        store.delete(item2.getId());
    }

    @Test
    public void findUndoneItems() {
        Item item = new Item("сделать что-то", true);
        Item item2 = new Item("сделать что-то 2", false);
        store.save(item);
        store.save(item2);
        List<Item> expected = List.of(item2);
        List<Item> out = store.findUndoneItems();
        assertThat(out, is(expected));
        store.delete(item.getId());
        store.delete(item2.getId());
    }
}