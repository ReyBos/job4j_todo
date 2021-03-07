package ru.reybos.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.reybos.model.User;
import ru.reybos.store.HbmStore;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/reg")
public class RegServlet extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(ItemServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("reg.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        User user = User.of(name, email, password);
        HbmStore.instOf().save(user);
        if (user.getId() == 0) {
            req.setAttribute("error", "Ошибка сохранения пользователя");
            req.getRequestDispatcher("reg.jsp").forward(req, resp);
        } else {
            HttpSession sc = req.getSession();
            sc.setAttribute("user", user);
            resp.sendRedirect(req.getContextPath());
        }
    }
}
