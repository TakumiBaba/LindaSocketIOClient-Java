package com.takumibaba.lindasocketio;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by takumi on 2014/01/17.
 */
public class LindaSocketIOClient extends Emitter {

    public Socket io;
    private Map<String, TupleSpace> tuplespaces;
    private boolean _isConnected = false;


    public LindaSocketIOClient(){
        this("http://node-linda-base.herokuapp.com/");
    }


    public LindaSocketIOClient(String api){
        try {
            io = IO.socket(api);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
//            io = new SocketIO(api);
        io.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                _isConnected = true;
                emit("connect");
            }
        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                _isConnected = false;
                io.emit("disconnect");
            }
        });
        io.connect();
        tuplespaces = new HashMap<String, TupleSpace>();
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

    public boolean isConnected(){return _isConnected;}

    public void push(JSONObject data){
        io.send(data);
    }


}
