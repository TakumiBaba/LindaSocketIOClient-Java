package com.takumibaba.lindasocketio;

import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by takumi on 2014/01/17.
 */
public class LindaSocketIOClient {

    SocketIO io;
    Map<String, TupleSpace> tuplespaces;
    Map<String, JSONObject> callbacks;


    public LindaSocketIOClient(String api){
        try {
            io = new SocketIO(api);
            io.connect(new IOCallback() {
                @Override
                public void onDisconnect() {
                    System.out.println("disConnect!");
                }

                @Override
                public void onConnect() {
                    System.out.println("onConnect!");
                    emit("connect", new JSONArray());
                }

                @Override
                public void onMessage(String s, IOAcknowledge ioAcknowledge) {
                     System.out.println("onMessage" + s);
                }

                @Override
                public void onMessage(JSONObject jsonObject, IOAcknowledge ioAcknowledge) {
                    System.out.println("onMessage!!!");
                }

                @Override
                public void on(String s, IOAcknowledge ioAcknowledge, Object... objects) {
                    System.out.println("event is"+s);
                    JSONArray data = new JSONArray();
                    for(Object obj:objects){
                        data.put(obj);
                    }
                    System.out.println("event is"+ data.toString());
                    emit(s, data);
                }

                @Override
                public void onError(SocketIOException e) {

                }
            });
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        tuplespaces = new HashMap<String, TupleSpace>();
        callbacks = new HashMap<String, JSONObject>();
    }

    public TupleSpace tuplespace(String name){
        if(tuplespaces.containsKey(name.toString()) == true){
            return tuplespaces.get(name.toString());
        }else{
            TupleSpace ts = new TupleSpace(name, this);
            tuplespaces.put(name, ts);
            return ts;
        }
    }

    public void push(JSONObject data){
        io.send(data);
    }

    public void on(String name, LindaCallback callback){
        JSONObject data = new JSONObject();
        JSONObject params = new JSONObject();
        try {
            params.put("type", "on");
            data.put("params", params);
            data.put("callback", callback);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        callbacks.put(name, data);
    }

    public void once(String name, LindaCallback callback){
        JSONObject data = new JSONObject();
        JSONObject params = new JSONObject();
        try {
            params.put("type", "once");
            data.put("params", params);
            data.put("callback", callback);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        callbacks.put(name, data);
    }

    public void emit(String event, JSONArray objects){
        if(callbacks.containsKey(event.toString()) == true){
            JSONObject callbackObject = callbacks.get(event.toString());
            LindaCallback callbackFunc = null;
            try {
                callbackFunc = (LindaCallback) callbackObject.get("callback");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            callbackFunc.callback(objects);
            try {
                if(callbackObject.getJSONObject("params").getString("type").equals("once")){
                    callbacks.remove(callbackObject);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public void off(String event){
        if(callbacks.containsKey(event.toString()) == true){
            JSONObject object = callbacks.get(event.toString());
            callbacks.remove(object);
        }
    }

}
