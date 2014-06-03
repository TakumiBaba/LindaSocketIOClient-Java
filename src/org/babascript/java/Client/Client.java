package org.babascript.java.Client;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by takumi on 2014/01/16.
 */
public class Client {

    private String api;
    private LindaSocketIOClient linda;
    private JSONArray tasks = new JSONArray();
    public JSONObject task = null;
    private TupleSpace ts = null;
    private String name = "";

    public Client(String name, JSONObject options){
        this.name = name;
        String _api = options.getString("linda");
        if(!api.equals("")){
            this.api = _api;
        }else{
            this.api = "http://linda.babascript.org";
        }
        linda = new LindaSocketIOClient(api);
        linda.once("connect", new LindaCallback() {
            @Override
            public void callback(JSONArray object) {

            }
        });
    }

    private void connect(){
        this.ts = this.linda.tuplespace(this.name);
        nextTask();
        broadcast();
        unicast();
    }

    private void nextTask(){
        if(this.tasks.length() > 0){
            this.task = this.tasks.getJSONObject(0);
        }else{

        }
    }

    private void broadcast(){
        JSONObject tuple = new JSONObject();
        tuple.put("baba", "script");
        tuple.put("type", "broadcast");
        Client c = this;
        this.ts.take(tuple, new LindaCallback() {
            @Override
            public void callback(JSONArray object) {
                c.getTask(object);
            }
        });
    }

    private void unicast(){
        JSONObject tuple = new JSONObject();
        tuple.put("baba", "script");
        tuple.put("type", "unicast");
        Client c = this;
        this.ts.take(tuple, new LindaCallback() {
            @Override
            public void callback(JSONArray object) {
                c.getTask(object);
            }
        });
    }

    private void watchCancel(){
        JSONObject tuple = new JSONObject();
        tuple.put("baba", "script");
        tuple.put("type", "cancel");
        this.ts.watch(tuple, new LindaCallback() {
            @Override
            public void callback(JSONArray object) {

            }
        });
    }

    private void doCancel(){
        String cid = this.task.getString("cid");
        JSONObject tuple = new JSONObject();
        tuple.put("baba", "script");
        tuple.put("type", "cancel");
        tuple.put("cid", cid);
        this.tasks.remove(0);
        this.ts.write(tuple);
    }

    private void returnValue(){

    }

    private String getId(){
        return String.valueOf(new Date().getTime() + Math.random());
    }

}
