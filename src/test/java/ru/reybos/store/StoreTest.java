package ru.reybos.store;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.reybos.model.Item;
import ru.reybos.model.User;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

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
    public void saveAndFindUser() {
        User user = User.of("user", "email", "password");
        store.save(user);
        User userDb = store.findUserByEmail(user.getEmail());
        assertThat(userDb, is(user));
        store.delete(user);
    }

    @Test
    public void saveItem() {
        User user = User.of("user", "email", "password");
        store.save(user);
        Item item = Item.of("описание", true, user);
        store.save(item);
        Item rsl = store.findItemById(item.getId());
        assertThat(rsl, is(item));
        assertThat(rsl.getUser(), is(user));
        store.delete(item);
        store.delete(user);
    }

    @Test
    public void updateItem() {
        User user = User.of("user", "email", "password");
        User user2 = User.of("user2", "email2", "password2");
        store.save(user);
        store.save(user2);
        Item item = Item.of("описание", true, user);
        store.save(item);
        item.setDescription("новое описание");
        item.setDone(true);
        item.setUser(user2);
        store.update(item);
        Item rsl = store.findItemById(item.getId());
        assertThat(rsl.getDescription(), is("новое описание"));
        assertTrue(rsl.isDone());
        assertThat(rsl.getUser(), is(user2));
        store.delete(item);
        store.delete(user);
        store.delete(user2);
    }

    @Test
    public void findAllItem() {
        User user = User.of("user", "email", "password");
        Item item = Item.of("описание", true, user);
        Item item2 = Item.of("описание2", false, user);
        store.save(user);
        store.save(item);
        store.save(item2);
        List<Item> expected = List.of(item, item2);
        List<Item> out = store.findAllItem();
        out.sort(Comparator.comparing(Item::getCreated));
        assertThat(out, is(expected));
        store.delete(item);
        store.delete(item2);
        store.delete(user);
    }

    @Test
    public void findDoneItems() {
        User user = User.of("user", "email", "password");
        Item item = Item.of("описание", true, user);
        Item item2 = Item.of("описание2", false, user);
        store.save(user);
        store.save(item);
        store.save(item2);
        List<Item> expected = List.of(item);
        List<Item> out = store.findDoneItems();
        assertThat(out, is(expected));
        store.delete(item);
        store.delete(item2);
        store.delete(user);
    }

    @Test
    public void findUndoneItems() {
        User user = User.of("user", "email", "password");
        Item item = Item.of("описание", true, user);
        Item item2 = Item.of("описание2", false, user);
        store.save(user);
        store.save(item);
        store.save(item2);
        List<Item> expected = List.of(item2);
        List<Item> out = store.findUndoneItems();
        assertThat(out, is(expected));
        store.delete(item);
        store.delete(item2);
        store.delete(user);
    }

    @Test
    public void whenFindByIdItemThenNull() {
        Item item = new Item();
        item.setId(1);
        Item rsl = store.findItemById(item.getId());
        assertNull(rsl);
    }
}