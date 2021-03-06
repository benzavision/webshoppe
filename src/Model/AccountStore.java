package Model;

import Model.Exception.AccountStoreException;

import java.util.ArrayList;

/**
 * Created by Robin on 2015-09-28.
 *
 * Interface for an AccountStore that handles
 * the persistent storage of Account objects.
 */

interface AccountStore {
    /**
     * Check if the account exists in the store.
     * @param username of the account to be checked, this must be unique.
     * @return the existence of the account.
     * @throws AccountStoreException when the store has failed to retrieve a result.
     */
    boolean exists(String username) throws AccountStoreException;

    /**
     * Adds a new account to the store.
     * @param account to be added to the store.
     * @throws Model.Exception.AccountExistsException when the account already exists.
     * @throws AccountStoreException when the store failed to store the account.
     */
    void add(Account account) throws AccountStoreException;

    /**
     * Finds an account by its username.
     * @param username of the account to find.
     * @return a new Account object from the data in the store.
     * @throws Model.Exception.NoSuchAccountException when no account is found.
     * @throws AccountStoreException when the store failed to respond.
     */
    Account findByUsername(String username) throws AccountStoreException;

    /**
     * Removes an account from the account store.
     * @param account to be removed.
     * @throws Model.Exception.NoSuchAccountException when the account was not found.
     * @throws AccountStoreException when the store failed to remove the account.
     */
    void deRegister(Account account) throws AccountStoreException;

    /**
     * Get all the managers in the store.
     * @return a list of all the managers in the store.
     */
    ArrayList<Account> getManagers() throws AccountStoreException;
}
