package servlet.registcontra;

public class delarea {
    private String delareaCode;
    private String delareaName;

    public delarea(String delareaCode, String delareaName) {
        this.delareaCode = delareaCode;
        this.delareaName = delareaName;
    }

    public String getAreaCode() {
        return delareaCode;
    }

    public String getAreaName() {
        return delareaName;
    }
}
