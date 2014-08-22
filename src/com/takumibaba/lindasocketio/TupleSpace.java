package com.takumibaba.lindasocketio;

import org.json.JSONObject;

import java.util.Date;

/**
 * Created by takumi on 2014/01/17.
 */
public class TupleSpace{
    String name;
    LindaSocketIOClient client;

    public TupleSpace(String name, LindaSocketIOClient client){
        this.name = name;
        this.client = client;
    }

    public String write(JSONObject tuple){
        JSONObject data = new JSONObject();
        String id = this.createCallbackId();
        data.put("tuplespace", this.name);
        data.put("tuple", tuple);
        data.put("options", new JSONObject());
        data.put("id", id);
        this.client.io.emit("__linda_write", data);
        return id;
    }

    public String read(JSONObject tuple, LindaCallback callback){
        JSONObject data = new JSONObject();
        String id = this.createCallbackId();
        data.put("tuplespace", this.name);
        data.put("tuple", tuple);
        data.put("options", new JSONObject());
        data.put("id", id);
        this.client.io.emit("__linda_read", data);
        String eventName = "__linda_read_" + id;
        this.client.once(eventName, callback);
        return id;
    }

    public String take(JSONObject tuple, LindaCallback callback){
        JSONObject data = new JSONObject();
        String id = this.createCallbackId();
        data.put("tuplespace", this.name);
        data.put("tuple", tuple);
        data.put("options", new JSONObject());
        data.put("id", id);
        this.client.io.emit("__linda_take", data);
        String eventName = "__linda_take_" + id;
        this.client.once(eventName, callback);
        return id;
    }
    public String watch(JSONObject tuple, LindaCallback callback){
        JSONObject data = new JSONObject();
        String id = this.createCallbackId();
        data.put("tuplespace", this.name);
        data.put("tuple", tuple);
        data.put("options", new JSONObject());
        data.put("id", id);
        this.client.io.emit("__linda_watch", data);
        String eventName = "__linda_watch_" + id;
        this.client.on(eventName, callback);
        return id;
    }

    void cancel(String id){
        if(this.client.io.isConnected() == true){
            JSONObject tuple = new JSONObject();
            tuple.put("tuplespace", this.name);
            tuple.put("id", id);
            this.client.io.emit("__linda_cancel", tuple);
            if(this.client.callbacks.containsKey(id)){
                this.client.off(id);
            }
        }

    }

    String createCallbackId(){
        return String.valueOf(new Date().getTime() + Math.random());
    }
}
