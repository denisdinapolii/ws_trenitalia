package com.example.template;

public class Result {
    private boolean result;
    private Object message;

    Result(){
        this.result=false;
        message=new Object();
    }

    public Result(boolean result, Object message) {
        this.result = result;
        this.message = message;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    
}
