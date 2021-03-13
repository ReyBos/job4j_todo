package ru.reybos.service;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.reybos.model.Item;
import ru.reybos.model.User;
import ru.reybos.store.HbmStore;
import ru.reybos.store.Store;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ItemService {
    private static final Logger LOG = LoggerFactory.getLogger(ItemService.class.getName());
    private final Store store = HbmStore.instOf();
    private final Map<String, Function<Item, Boolean>> dispatch = new HashMap<>();

    private ItemService() {
        this.load("save", save());
        this.load("update", update());
        this.load("delete", delete());
    }

    public static ItemService getInstance() {
        return Holder.INSTANCE;
    }

    private static final class Holder {
        private static final ItemService INSTANCE = new ItemService();
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
            itemDb.setDone(item.isDone());
            return store.update(itemDb);
        };
    }

    private Function<Item, Boolean> delete() {
        return store::delete;
    }

    public boolean execute(HttpServletRequest request) throws IOException {
        String action = request.getParameter("action");
        Item item = new Gson().fromJson(request.getReader(), Item.class);
        User user = (User) request.getSession().getAttribute("user");
        item.setUser(user);
        return dispatch.get(action).apply(item);
    }
}
