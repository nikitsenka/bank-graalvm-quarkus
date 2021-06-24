package com.example.bank;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class Balance {

    private Integer balance;

    public Balance() {
    }

    public Balance(Integer balance) {
        this.balance = balance;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }
}
