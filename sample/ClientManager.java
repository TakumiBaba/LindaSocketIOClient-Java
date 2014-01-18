import org.babascript.java.Client.LindaCallback;
import org.babascript.java.Client.LindaSocketIOClient;
import org.babascript.java.Client.TupleSpace;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by takumi on 2014/01/18.
 */
public class ClientManager {

//    LindaSocketIOClient.TupleSpace ts;
    TupleSpace ts;
    LindaSocketIOClient client;

    public ClientManager(){
        client = new LindaSocketIOClient();
        ts = new TupleSpace("masuilab", client);
        client.once("connect", new LindaCallback() {
            @Override
            public void callback(JSONArray object) {
                JSONObject test = new JSONObject();
                test.put("hoge", "Fuga");
//                ts.write(test);
//                ts.write(test);
                ts.read(test, new LindaCallback() {
                    @Override
                    public void callback(JSONArray object) {
                        System.out.println("read!!!");
                        System.out.println(object.toString());
                    }
                });
//                ts.watch(test, new LindaCallback() {
//                    @Override
//                    public void callback(JSONArray object) {
//                        System.out.println("watch!!!");
//                        System.out.println(object.toString());
//                    }
//                });
//                ts.take(test, new LindaCallback() {
//                    @Override
//                    public void callback(JSONArray object) {
//                        System.out.println("take!!!");
//                        System.out.println(object.toString());
//                    }
//                });
//                ts.take(test, new LindaCallback() {
//                    @Override
//                    public void callback(JSONArray object) {
//                        System.out.println("take222!!!");
//                        System.out.println(object.toString());
//                        ts.take(object.getJSONObject(1).getJSONObject("data"), new LindaCallback() {
//                            @Override
//                            public void callback(JSONArray object) {
//                                System.out.println("take333!!!");
//                                System.out.println(object.toString());
//                            }
//                        });
//                    }
//                });
            }
        });
    }
}
