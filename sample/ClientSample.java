import com.github.nkzawa.emitter.Emitter;
import com.takumibaba.lindasocketio.*;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by takumi on 2014/01/18.
 */
public class ClientSample {

    TupleSpace ts;
    LindaSocketIOClient client;

    public ClientSample(){
        client = new LindaSocketIOClient();
        final JSONObject test = new JSONObject();
        try {
            test.put("hoge", "Fuga");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        client.on("connect", new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                ts = new TupleSpace("masuilab", client);
                ts.watch(test, new LindaCallback() {
                    @Override
                    public void call(JSONObject object) {
                        System.out.println("watch!!!");
                        System.out.println(object);
                    }
                });
                ts.read(test, new LindaCallback() {
                    @Override
                    public void call(JSONObject object) {
                        System.out.println("read!!!");
                        System.out.println(object);
                        ts.write(test);
                        ts.take(test, new LindaCallback() {
                            @Override
                            public void call(JSONObject object) {
                                System.out.println("take!!!");
                                System.out.println(object.toString());
                            }
                        });
                    }
                });
                ts.write(test);
            }
        });
    }
}
