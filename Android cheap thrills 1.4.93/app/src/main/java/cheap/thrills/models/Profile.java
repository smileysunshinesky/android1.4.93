package cheap.thrills.models;

import java.util.List;

public class Profile {
    Data data;
    List<Order> orderDetails;
    private String status = "";
    private String message = "";
    private String adultCount = "";
    private String kidCount = "";
    private String ThemeParkParenId = "";
    private String ThemeParkName = "";

    public String getAdultCount() {
        return adultCount;
    }

    public void setAdultCount(String adultCount) {
        this.adultCount = adultCount;
    }

    public String getKidCount() {
        return kidCount;
    }

    public void setKidCount(String kidCount) {
        this.kidCount = kidCount;
    }

    public List<Order> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<Order> orderDetails) {
        this.orderDetails = orderDetails;
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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getThemeParkParenId() {
        if(ThemeParkParenId != null)
        return ThemeParkParenId;
        return "";
    }

    public void setThemeParkParenId(String themeParkParenId) {
        ThemeParkParenId = themeParkParenId;
    }

    public String getThemeParkName() {
        return ThemeParkName;
    }

    public void setThemeParkName(String themeParkName) {
        ThemeParkName = themeParkName;
    }
}
