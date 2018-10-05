package NinaRow.utils;

import webEngine.gamesList.GameStatus;

public abstract class ServeltResponse {
    private Boolean result;
    private String msg;

    public ServeltResponse() {
        this.result = true;
        this.msg = "";
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Boolean getResult() {
        return result;
    }

    public String getMsg() {
        return msg;
    }
}
