package cheap.thrills.models;

public class Data {
    private String id = "";
    private String first_name = "";
    private String last_name = "";
    private String email = "";
    private String password = "";
    private String update_subscription = "";
    private String term_of_service = "";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUpdate_subscription() {
        return update_subscription;
    }

    public void setUpdate_subscription(String update_subscription) {
        this.update_subscription = update_subscription;
    }

    public String getTerm_of_service() {
        return term_of_service;
    }

    public void setTerm_of_service(String term_of_service) {
        this.term_of_service = term_of_service;
    }
}
