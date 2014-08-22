package com.takumibaba.lindasocketio;

import com.github.nkzawa.emitter.Emitter;
import org.json.JSONArray;
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

    public String read(JSONObject tuple, final LindaCallback callback){
        JSONObject data = new JSONObject();
        String id = this.createCallbackId();
        data.put("tuplespace", this.name);
        data.put("tuple", tuple);
        data.put("options", new JSONObject());
        data.put("id", id);
        this.client.io.emit("__linda_read", data);
        String eventName = "__linda_read_" + id;
        this.client.io.once(eventName, new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                callback.call((JSONObject)objects[1]);
            }
        });
        return id;
    }

    public String take(JSONObject tuple, final LindaCallback callback){
        JSONObject data = new JSONObject();
        String id = this.createCallbackId();
        data.put("tuplespace", this.name);
        data.put("tuple", tuple);
        data.put("options", new JSONObject());
        data.put("id", id);
        this.client.io.emit("__linda_take", data);
        String eventName = "__linda_take_" + id;
        this.client.io.once(eventName, new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                callback.call((JSONObject)objects[1]);
            }
        });
        return id;
    }
    public String watch(JSONObject tuple, final LindaCallback callback){
        JSONObject data = new JSONObject();
        String id = this.createCallbackId();
        data.put("tuplespace", this.name);
        data.put("tuple", tuple);
        data.put("options", new JSONObject());
        data.put("id", id);
        this.client.io.emit("__linda_watch", data);
        String eventName = "__linda_watch_" + id;
        this.client.io.on(eventName, new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                callback.call((JSONObject)objects[1]);
            }
        });
        return id;
    }

    void cancel(String id){
        if(this.client.isConnected() == true){
            JSONObject tuple = new JSONObject();
            tuple.put("tuplespace", this.name);
            tuple.put("id", id);
            this.client.io.emit("__linda_cancel", tuple);
            if(this.client.hasListeners("__linda_read_"+id)) this.client.off("__linda_read_"+id);
            if(this.client.hasListeners("__linda_watch_"+id)) this.client.off("__linda_watch_"+id);
            if(this.client.hasListeners("__linda_take_"+id)) this.client.off("__linda_take_"+id);
        }

    }

    String createCallbackId(){
        return String.valueOf(new Date().getTime() + Math.random());
    }
}
