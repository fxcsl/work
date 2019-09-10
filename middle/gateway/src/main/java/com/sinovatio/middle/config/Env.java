package com.sinovatio.middle.config;

public class Env {
    private String arg;
    public Env(String arg){
        this.arg = arg;
    }

    /**
     * 获取服务的前缀
     * @return
     */
    public String getServicePreStr(){
        if(arg.equals("dev")){
            return "http://";
        }else {
            return "lb://";
        }
    }
}
