package ru.reybos.servlet;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.reybos.model.Item;
import ru.reybos.service.ItemService;
import ru.reybos.store.HbmStore;
import ru.reybos.store.Store;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/item")
public class ItemServlet extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(ItemServlet.class.getName());
    private final Store store = HbmStore.instOf();
    private final ItemService service = new ItemService(store).init();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        Item item = new Gson().fromJson(req.getReader(), Item.class);
        resp.setCharacterEncoding("UTF-8");
        PrintWriter writer = new PrintWriter(resp.getOutputStream());
        if (!service.execute(action, item)) {
            writer.write("Ошибка изменения элемента");
            resp.setStatus(500);
        } else {
            writer.write("Элемент изменен успешно");
        }
        writer.flush();
    }
}
