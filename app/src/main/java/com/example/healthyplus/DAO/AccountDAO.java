package com.example.healthyplus.DAO;

import com.example.healthyplus.Model.Account;

public class AccountDAO extends BaseDAO{
    public AccountDAO(){
        super("account");
    }

    public void addAccount(Account account){
        this.db.collection(collectionName).add(account);
    }
}
