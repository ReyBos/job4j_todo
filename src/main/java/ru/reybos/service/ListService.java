package ru.reybos.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.reybos.model.Item;
import ru.reybos.store.HbmStore;
import ru.reybos.store.Store;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ListService {
    private static final Logger LOG = LoggerFactory.getLogger(ListService.class.getName());
    private final Store store = HbmStore.instOf();

    private ListService() { }

    public static ListService getInstance() {
        return ListService.Holder.INSTANCE;
    }

    private static final class Holder {
        private static final ListService INSTANCE = new ListService();
    }

    public String getAllItems() {
        Map<String, String> rsl = new LinkedHashMap<>();
        final Gson gson = new GsonBuilder().create();
        List<Item> doneItems = store.findDoneItems();
        List<Item> undoneItems = store.findUndoneItems();
        rsl.put("doneItems", gson.toJson(doneItems));
        rsl.put("undoneItems", gson.toJson(undoneItems));
        return gson.toJson(rsl);
    }
}
