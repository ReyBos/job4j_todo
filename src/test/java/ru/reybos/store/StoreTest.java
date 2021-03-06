package ru.reybos.store;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.reybos.model.Item;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
public class StoreTest {
    private static final Logger LOG = LoggerFactory.getLogger(StoreTest.class.getName());
    private Store store;

    public StoreTest(Store store) {
        this.store = store;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> instancesToTest() {
        return Arrays.asList(
                new Object[]{MemStore.instOf()},
                new Object[]{HbmStore.instOf()}
        );
    }

    @Test
    public void saveItem() {
        Item item = new Item("сделать что-то", true);
        store.save(item);
        Item rsl = store.findItemById(item.getId());
        assertThat(rsl, is(item));
        store.delete(item);
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
        store.delete(item);
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
        store.delete(item);
        store.delete(item2);
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
        store.delete(item);
        store.delete(item2);
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
        store.delete(item);
        store.delete(item2);
    }

    @Test
    public void whenFindByIdItemThenNull() {
        Item item = new Item(1);
        Item rsl = store.findItemById(item.getId());
        assertNull(rsl);
    }
}