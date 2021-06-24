package com.example.bank;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;

public class Bank {
    public static final String GET_BALANCE_SQL = "SELECT debit - credit FROM (SELECT COALESCE(sum(amount), 0) AS debit FROM transaction WHERE to_client_id = $1 ) a, ( SELECT COALESCE(sum(amount), 0) AS credit FROM transaction WHERE from_client_id = $2 ) b";
    public static final String INSERT_CLIENT_SQL = "INSERT INTO client(name, email, phone) VALUES ($1, $2, $3) RETURNING id";
    public static final String INSERT_TRANSACTION_SQL = "INSERT INTO transaction(from_client_id, to_client_id, amount) VALUES ($1, $2, $3) RETURNING id";


    public static Uni<Balance> getBalance(PgPool pgClient, Integer clientId) {
        return pgClient.preparedQuery(GET_BALANCE_SQL).execute(Tuple.of(clientId, clientId))
                .onItem().transform(RowSet::iterator)
                .onItem().transform(iterator -> iterator.hasNext() ? new Balance(iterator.next().getInteger(0)) : null);
    }

    public static Uni<Client> createClient(PgPool pgClient, Client client) {
        return pgClient.preparedQuery(INSERT_CLIENT_SQL).execute(Tuple.of(client.getName(),client.getEmail(), client.getPhone()))
                .onItem().transform(RowSet::iterator)
                .onItem().transform(iterator -> iterator.hasNext() ? new Client(iterator.next().getInteger(0), client.getName(),client.getEmail(), client.getPhone()) : null);
    }

    public static Uni<Transaction> createTransaction(PgPool pgClient, Transaction transaction) {
        return pgClient.preparedQuery(INSERT_TRANSACTION_SQL).execute(Tuple.of(transaction.getFromClientId(), transaction.getToClientId(), transaction.getAmount()))
                .onItem().transform(RowSet::iterator)
                .onItem().transform(iterator -> iterator.hasNext() ? new Transaction(iterator.next().getInteger(0), transaction.getFromClientId(), transaction.getToClientId(), transaction.getAmount()) : null);
    }
}
