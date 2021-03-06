package Model;

import Model.Exception.AccountStoreException;
import Model.Exception.NoSuchAccountException;
import Model.Exception.StoreException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Robin on 2015-09-28.
 *
 * Uses a database connection to Implement an AccountStore.
 */

class AccountDB implements AccountStore {

    @Override
    public boolean exists(String username) throws AccountStoreException {
        try (Connection connection = Database.getConnection()) {
            try (PreparedStatement statement =
                         connection.prepareStatement(AccountTable.Exists.QUERY)) {

                statement.setString(AccountTable.Exists.IN.NAME, username);
                return statement.executeQuery().getInt(0) != 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AccountStoreException();
        }
    }

    @Override
    public void add(Account account) throws AccountStoreException {
        try (Connection connection = Database.getConnection()) {
            try (PreparedStatement statement =
                         connection.prepareStatement(AccountTable.Add.QUERY)) {

                statement.setString(AccountTable.Add.IN.NAME, account.getUsername());
                statement.setString(AccountTable.Add.IN.PASSWORD, account.getPassword());
                statement.setString(AccountTable.Add.IN.SALT, account.getSalt());
                statement.setString(AccountTable.Add.IN.ZIP, account.getZip());
                statement.setString(AccountTable.Add.IN.STREET, account.getStreet());
                statement.setInt(AccountTable.Add.IN.ROLE, account.getRole().getId());
                statement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AccountStoreException();
        }
    }


    @Override
    public Account findByUsername(String username) throws AccountStoreException {
        try (Connection connection = Database.getConnection()) {
            try (PreparedStatement statement =
                         connection.prepareStatement(AccountTable.FindByName.QUERY)) {

                statement.setString(AccountTable.FindByName.IN.NAME, username);
                ResultSet result = statement.executeQuery();

                if (result.next()) {
                    return accountFromResult(result);
                } else
                    throw new NoSuchAccountException();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AccountStoreException();
        }
    }

    private Account accountFromResult(ResultSet result) throws SQLException {
        return new Account()
                .setId(result.getInt(AccountTable.FindByName.OUT.ID))
                .setUsername(result.getString(AccountTable.FindByName.OUT.NAME))
                .setPassword(result.getString(AccountTable.FindByName.OUT.PASSWORD))
                .setSalt(result.getString(AccountTable.FindByName.OUT.SALT))
                .setStreet(result.getString(AccountTable.FindByName.OUT.STREET))
                .setZip(result.getString(AccountTable.FindByName.OUT.ZIP))
                .setRole(new Role(result.getInt(AccountTable.FindByName.OUT.ROLE)));
    }

    @Override
    public void deRegister(Account account) throws AccountStoreException {
        try (Connection connection = Database.getConnection()) {
            try (PreparedStatement statement =
                    connection.prepareStatement(AccountTable.RemoveById.QUERY)) {

                statement.setInt(AccountTable.RemoveById.IN.ACCOUNT_ID, account.getId());
                statement.execute();

                if (statement.getUpdateCount() == 0)
                    throw new NoSuchAccountException();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AccountStoreException();
        }
    }

    @Override
    public ArrayList<Account> getManagers() throws AccountStoreException {
        ArrayList<Account> managers = new ArrayList<>();

        try (Connection connection = Database.getConnection()) {
            try (PreparedStatement statement =
                    connection.prepareStatement(AccountTable.GetManagers.QUERY)) {

                ResultSet result = statement.executeQuery();

                while (result.next()) {
                    managers.add(accountFromResult(result));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AccountStoreException();
        }
        return managers;
    }

    protected Account findById(int accountId) throws AccountStoreException {
        Account account = new Account();

        try (Connection connection = Database.getConnection()) {
            try (PreparedStatement statement =
                    connection.prepareStatement(AccountTable.FindById.QUERY)) {
                statement.setInt(AccountTable.FindById.IN.ACCOUNT_ID, accountId);
                ResultSet result = statement.executeQuery();

                if (result.next()) {
                    account = accountFromResult(result);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AccountStoreException();
        }
        return account;
    }
}
