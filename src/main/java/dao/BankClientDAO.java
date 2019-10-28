package dao;

import model.BankClient;
import dao.executor.Executor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BankClientDAO {

    private Connection connection;
    private Executor executor;

    public BankClientDAO(Connection connection) {
        this.connection = connection;
        executor = new Executor(connection);
    }

    public List<BankClient> getAllBankClient() throws SQLException {
        return executor.execQuery("select * from bank_client", result -> {
            List<BankClient> clientsList = new ArrayList<>();
            while (result.next()) {
                clientsList.add(new BankClient(result.getLong(1), result.getString(2),
                        result.getString(3), result.getLong(4)));
            }
            return clientsList;
        });
    }

    public void addClient(BankClient client) throws SQLException {
        executor.execUpdate("insert into bank_client (name, password, money) values ('" + client.getName() + "', '" +
                client.getPassword() + "', '" + client.getMoney() + "')");
    }

    public boolean deleteClient(String name)  {
        try {
            executor.execUpdate("delete from bank_client where name = '" + name + "';");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } return false;
    }

    public boolean validateClient(String name, String password) throws SQLException {
        return getClientByName(name).getPassword().equals(password);
    }

    public void updateClientsMoney(String name, Long transactValue) throws SQLException {
        long newMoney = getClientByName(name).getMoney()+transactValue;
    String sql = "update bank_client set money=? where name=?";
//    executor.execUpdate("update bank_client set money=" + newMoney + " where name='" +
//            name+"';");
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setLong(1, newMoney);
        stmt.setString(2, name);
        stmt.executeUpdate();
        stmt.close();
    }

    public BankClient getClientById(long id) throws SQLException {
        if(id==-1) return null;
        return executor.execQuery("select * from bank_client where id=" + id, result -> {
            result.next();
            return new BankClient(id, result.getString("name"),
                    result.getString("password"), result.getLong("money"));
        });
    }

    public boolean isClientHasSum(String name, Long expectedSum) throws SQLException {
        return getClientByName(name).getMoney() >= expectedSum;
    }

    public long getClientIdByName(String name) throws SQLException {
        return executor.execQuery("select * from bank_client where name='" + name + "'", result -> {
            if (result.next()) {
            return result.getLong("id");
            }
        return -1l;}  );
    }

    public BankClient getClientByName(String name) throws SQLException {
        return getClientById(getClientIdByName(name));
    }


    public void createTable() throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute("create table if not exists bank_client (id bigint auto_increment, name varchar(256), password varchar(256), money bigint, primary key (id))");
        stmt.close();
    }

    public void dropTable() throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("DROP TABLE IF EXISTS bank_client");
        stmt.close();
    }
}
