package service;

import dao.BankClientDAO;
import exception.DBException;
import model.BankClient;

import java.sql.*;
import java.util.List;

public class BankClientService {

    public BankClientService() {
    }

    public BankClient getClientById(long id) throws DBException {
        try {
            return getBankClientDAO().getClientById(id);
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public BankClient getClientByName(String name) throws SQLException {
        return getBankClientDAO().getClientByName(name);
    }

    public List<BankClient> getAllClient() throws SQLException {
        return getBankClientDAO().getAllBankClient();
    }

    public boolean deleteClient(String name) {
        return getBankClientDAO().deleteClient(name);
    }

    public boolean validateClient(String name, String password) throws SQLException {
        return getBankClientDAO().validateClient(name, password);
    }

    public boolean addClient(BankClient client) {
        try {
            getBankClientDAO().addClient(client);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean sendMoneyToClient(BankClient sender, String nameTo, Long value) throws SQLException {
        BankClientDAO dao = getBankClientDAO();
        String nameFrom = sender.getName();
        if(dao.getClientByName(nameTo)==null) {return false;}
        if (dao.isClientHasSum(sender.getName(), value)) {
            dao.updateClientsMoney(sender.getName(), -value);
            dao.updateClientsMoney(nameTo, value);
            return true;
        } else {
            return false;
        }
    }

    public void cleanUp() throws DBException {
        BankClientDAO dao = getBankClientDAO();
        try {
            dao.dropTable();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public void createTable() throws DBException {
        BankClientDAO dao = getBankClientDAO();
        try {
            dao.createTable();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    private static Connection getMysqlConnection() {
        try {
            DriverManager.registerDriver((Driver) Class.forName("com.mysql.jdbc.Driver").newInstance());
            Connection connection = DriverManager.
                    getConnection("jdbc:mysql://localhost:3306/db_example?serverTimezone=UTC&useSSL=false", "root", "1234");
            return connection;
        } catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new IllegalStateException();
        }
    }

    private static BankClientDAO getBankClientDAO() {
        return new BankClientDAO(getMysqlConnection());
    }
}
