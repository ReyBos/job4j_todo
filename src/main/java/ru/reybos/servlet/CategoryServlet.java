package ru.reybos.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.reybos.service.CategoryService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/category")
public class CategoryServlet extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(CategoryServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        CategoryService service = CategoryService.getInstance();
        String categories = service.getAllCategories();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter writer = new PrintWriter(resp.getOutputStream());
        writer.write(categories);
        writer.flush();
    }
}