package ru.reybos.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.reybos.model.Category;
import ru.reybos.store.HbmStore;
import ru.reybos.store.Store;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CategoryService {
    private static final Logger LOG = LoggerFactory.getLogger(CategoryService.class.getName());
    private final Store store = HbmStore.instOf();

    private CategoryService() { }

    public static CategoryService getInstance() {
        return CategoryService.Holder.INSTANCE;
    }

    private static final class Holder {
        private static final CategoryService INSTANCE = new CategoryService();
    }

    public String getAllCategories() {
        Map<String, String> rsl = new LinkedHashMap<>();
        final Gson gson = new GsonBuilder().create();
        List<Category> categories = store.getAllCategories();
        rsl.put("category", gson.toJson(categories));
        return gson.toJson(rsl);
    }
}
