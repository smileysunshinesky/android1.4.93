package cheap.thrills.models;

public class Disable {

    private String status = "";
    private String message = "";
    private Boolean isdisabled;

    public Boolean getIsdisabled() {
        return isdisabled;
    }

    public void setIsdisabled(Boolean isdisabled) {
        this.isdisabled = isdisabled;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
