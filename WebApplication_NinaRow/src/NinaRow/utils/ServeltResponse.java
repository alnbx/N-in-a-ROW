package NinaRow.utils;

public abstract class ServeltResponse {
    private Boolean success;
    private String msg;

    public ServeltResponse() {
        this.success = true;
        this.msg = "";
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Boolean getSuccess() {
        return success;
    }

    public String getMsg() {
        return msg;
    }
}
