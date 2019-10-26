package servlet;

import dao.BankClientDAO;
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

public class MoneyTransactionServlet extends HttpServlet {

    BankClientService bService = new BankClientService();
    private Map<String, Object> resultMap = new HashMap<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().println(PageGenerator.getInstance()
                .getPage("moneyTransactionPage.html", new HashMap<>()));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String senderName = req.getParameter("senderName");
        String password = req.getParameter("senderPass");
        String nameTo = req.getParameter("nameTo");
        Long count = Long.parseLong(req.getParameter("count"));

        try {
            BankClient bClient = bService.getClientByName(senderName);
            if (bClient == null) {
                resultMap.put("message", "transaction rejected");
            } else {
                if (bService.validateClient(senderName, password)) {
                    if (bService.sendMoneyToClient(bClient, nameTo, count)) {
                        resultMap.put("message", "The transaction was successful");
                    } else {
                        resultMap.put("message", "transaction rejected");
                    }
                } else {
                    resultMap.put("message", "transaction rejected");
                }
            }
            resp.setContentType("text/html;charset=utf-8");
            resp.getWriter().println(PageGenerator.getInstance().getPage("resultPage.html", resultMap));
            resp.setStatus(HttpServletResponse.SC_OK);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}