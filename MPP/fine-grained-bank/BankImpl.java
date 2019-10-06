package fgbank;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Bank implementation.
 *
 * <p>:TODO: This implementation has to be made thread-safe.
 */
public class BankImpl implements Bank {
    /**
     * An array of accounts by index.
     */
    private final Account[] accounts;

    /**
     * Creates new bank instance.
     * @param n the number of accounts (numbered from 0 to n-1).
     */
    public BankImpl(int n) {
        accounts = new Account[n];
        for (int i = 0; i < n; i++) {
            accounts[i] = new Account();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNumberOfAccounts() {
        return accounts.length;
    }

    /**
     * {@inheritDoc}
     * <p>:TODO: This method has to be made thread-safe. +
     */
    @Override
    public long getAmount(int index) {
        Account account = accounts[index];

        account.lock.lock();
        long amount = account.amount;
        account.lock.unlock();

        return amount;
    }

    /**
     * {@inheritDoc}
     * <p>:TODO: This method has to be made thread-safe. +
     */
    @Override
    public long getTotalAmount() {
        long sum = 0;

        for (Account account : accounts) {
            account.lock.lock();
            sum += account.amount;
        }

        for (Account account : accounts) {
            account.lock.unlock();
        }

        return sum;
    }

    /**
     * {@inheritDoc}
     * <p>:TODO: This method has to be made thread-safe. +
     */
    @Override
    public long deposit(int index, long amount) {
        if (amount <= 0)
            throw new IllegalArgumentException("Invalid amount: " + amount);

        Account account = accounts[index];

        account.lock.lock();
        long newAmount = account.amount + amount;

        try {
            if (amount > MAX_AMOUNT || newAmount > MAX_AMOUNT){
                throw new IllegalStateException("Overflow");
            }

            account.amount = newAmount;
        }finally {
            account.lock.unlock();
        }

        return newAmount;
    }

    /**
     * {@inheritDoc}
     * <p>:TODO: This method has to be made thread-safe. +
     */
    @Override
    public long withdraw(int index, long amount) {
        if (amount <= 0)
            throw new IllegalArgumentException("Invalid amount: " + amount);

        Account account = accounts[index];

        account.lock.lock();
        long newAmount = account.amount - amount;

        try {
            if (newAmount < 0) {
                throw new IllegalStateException("Underflow");
            }

            account.amount = newAmount;
        }finally {
            account.lock.unlock();
        }

        return newAmount;
    }

    /**
     * {@inheritDoc}
     * <p>:TODO: This method has to be made thread-safe. +
     */
    @Override
    public void transfer(int fromIndex, int toIndex, long amount) {
        if (amount <= 0)
            throw new IllegalArgumentException("Invalid amount: " + amount);

        if (fromIndex == toIndex)
            throw new IllegalArgumentException("fromIndex == toIndex");


        Account from = accounts[fromIndex];
        Account to = accounts[toIndex];

        accounts[Math.min(fromIndex, toIndex)].lock.lock();
        accounts[Math.max(fromIndex, toIndex)].lock.lock();

        try{
            if (amount > from.amount) {
                throw new IllegalStateException("Underflow");
            }else if (amount > MAX_AMOUNT || to.amount + amount > MAX_AMOUNT) {
                throw new IllegalStateException("Overflow");
            }

            from.amount -= amount;
            to.amount += amount;
        }finally {
            from.lock.unlock();
            to.lock.unlock();
        }
    }

    /**
     * Private account data structure.
     */
    private static class Account {
        /**
         * Amount of funds in this account.
         */
        long amount;

        Lock lock = new ReentrantLock();
    }
}
