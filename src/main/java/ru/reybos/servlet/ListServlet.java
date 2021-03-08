package ru.reybos.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.reybos.service.ListService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/list")
public class ListServlet extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(ItemServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        ListService service = ListService.getInstance();
        String items = service.getAllItems();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter writer = new PrintWriter(resp.getOutputStream());
        writer.write(items);
        writer.flush();
    }
}