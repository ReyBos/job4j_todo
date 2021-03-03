package ru.reybos.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.reybos.model.Item;
import ru.reybos.store.Store;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ItemService {
    private static final Logger LOG = LoggerFactory.getLogger(ItemService.class.getName());
    private final Store store;
    private final Map<String, Function<Item, Boolean>> dispatch = new HashMap<>();

    public ItemService(Store store) {
        this.store = store;
    }

    public ItemService init() {
        this.load("save", save());
        this.load("update", update());
        this.load("delete", delete());
        return this;
    }

    public void load(String action, Function<Item, Boolean> handle) {
        this.dispatch.put(action, handle);
    }

    private Function<Item, Boolean> save() {
        return store::save;
    }

    private Function<Item, Boolean> update() {
        return item -> {
            Item itemDb = store.findItemById(item.getId());
            if (item.isDone() != null) {
                itemDb.setDone(item.isDone());
            }
            return store.update(itemDb);
        };
    }

    private Function<Item, Boolean> delete() {
        return store::delete;
    }

    public boolean execute(String action, Item item) {
        return dispatch.get(action).apply(item);
    }
}
