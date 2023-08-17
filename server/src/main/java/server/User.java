package server;

import client.Client;

class User {
    private final String name;
    private final Client client;

    User(String name, Client client) {
        this.name = name;
        this.client = client;
    }
    public String token;

    public String getName() {
        return name;
    }

    public Client getClient() {
        return client;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                '}';
    }
}
