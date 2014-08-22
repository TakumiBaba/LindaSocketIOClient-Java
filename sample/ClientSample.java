import com.takumibaba.lindasocketio.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by takumi on 2014/01/18.
 */
public class ClientManager {

    TupleSpace ts;
    LindaSocketIOClient client;

    public ClientManager(){
        client = new LindaSocketIOClient("http://linda.babascript.org/");
        ts = new TupleSpace("masuilab", client);
        client.once("connect", new LindaCallback() {
            @Override
            public void callback(JSONArray object) {
                final JSONObject test = new JSONObject();
                try {
                    test.put("hoge", "Fuga");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ts.write(test);
                ts.read(test, new LindaCallback() {
                    @Override
                    public void callback(JSONArray object) {
                        System.out.println("read!!!");
                        System.out.println(object);
                        ts.write(test);
                    }
                });
                ts.watch(test, new LindaCallback() {
                    @Override
                    public void callback(JSONArray object) {
                        System.out.println("watch!!!");
                        System.out.println(object.toString());
                        ts.take(test, new LindaCallback() {
                            @Override
                            public void callback(JSONArray object) {
                                System.out.println("take!!!");
                                System.out.println(object.toString());
                            }
                        });
                    }
                });

            }
        });
    }
}
