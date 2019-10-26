package servlet;

import exception.DBException;
import model.BankClient;
import service.BankClientService;
import util.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class RegistrationServlet extends HttpServlet {

    private BankClientService bs = new BankClientService();
    private Map<String, Object> resultMap = new HashMap<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().println(PageGenerator.getInstance()
                .getPage("registrationPage.html", new HashMap<>()));
        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String password = req.getParameter("password");
        Long money = Long.parseLong(req.getParameter("money"));
        try {
            BankClient tempClient = new BankClient(name, password, money);
            if (bs.addClient(tempClient)) {
                resultMap.put("message", "Add client successful");
            } else {
                resultMap.put("message", "Client not add");
            }
            resp.setContentType("text/html;charset=utf-8");
            resp.getWriter().println(PageGenerator.getInstance().getPage("resultPage.html", resultMap));
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
