package co.com.mycompany.model.authentication;

public enum Role {
    
    ADMIN("ADMIN", "System administrator with full access to all functionalities."),
    ADVISOR("ADVISOR", "Advisor with permissions to manage client requests and register users."),
    CLIENT("CLIENT", "Client with permissions to create and manage their own loan requests.");

    private final String name;
    private final String description;

    Role(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
