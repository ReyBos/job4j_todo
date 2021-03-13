package ru.reybos.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.reybos.service.ItemService;

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

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        ItemService service = ItemService.getInstance();
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter writer = new PrintWriter(resp.getOutputStream());
        if (service.execute(req)) {
            resp.sendRedirect(req.getContextPath() + "/list");
        }
        writer.write("Ошибка изменения элемента");
        resp.setStatus(500);
        writer.flush();
    }
}
