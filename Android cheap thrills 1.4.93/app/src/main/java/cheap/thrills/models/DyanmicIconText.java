package cheap.thrills.models;

import java.util.List;

public class DyanmicIconText {
    private String status="";
    List<IconData> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<IconData> getData() {
        return data;
    }

    public void setData(List<IconData> data) {
        this.data = data;
    }
}
