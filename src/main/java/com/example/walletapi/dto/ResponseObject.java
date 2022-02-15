package com.example.walletapi.dto;

public class ResponseObject {
    private String status;
    private String error;
    private Object obj;

    public boolean equals(ResponseObject res) {
        return res.status.compareTo(status) == 0 && res.error.compareTo(error) == 0 && res.obj.equals(obj);
    }

    public ResponseObject(Object obj) {
        status = "Successful";
        error = "";
        this.obj = obj;
    }

    public ResponseObject(String status, String error, Object obj) {
        this.status = status;
        this.error = error;
        this.obj = obj;
    }

    public ResponseObject() {

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}
