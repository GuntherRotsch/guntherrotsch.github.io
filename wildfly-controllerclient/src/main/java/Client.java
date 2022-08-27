import java.io.IOException;
import java.net.InetAddress;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.sasl.RealmCallback;

import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.as.controller.client.OperationBuilder;
import org.jboss.dmr.ModelNode;

public class Client {

  public static void main(String... args) throws IOException {

    CallbackHandler callbackHandler = new CallbackHandler() {

      public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        for (Callback current : callbacks) {
          if (current instanceof NameCallback) {
            NameCallback ncb = (NameCallback) current;
            ncb.setName("admin");
          } else if (current instanceof PasswordCallback) {
            PasswordCallback pcb = (PasswordCallback) current;
            pcb.setPassword("jboss".toCharArray());
          } else if (current instanceof RealmCallback) {
            RealmCallback rcb = (RealmCallback) current;
            rcb.setText(rcb.getDefaultText());
          } else {
            throw new UnsupportedCallbackException(current);
          }
        }
      }
    };

    ModelControllerClient client = ModelControllerClient.Factory
        .create(InetAddress.getByName("127.0.0.1"), 9990, callbackHandler);

//    readResource(client);
    
    writeAttribute(client);

    client.close();
  }

  private static void readResource(ModelControllerClient client) throws IOException {
		ModelNode op = new ModelNode();
	    op.get("operation").set("read-resource-description");

	    ModelNode address = op.get("address");
	    address.add("subsystem", "deployment-scanner");
	    

	    op.get("recursive").set(true);
	    op.get("operations").set(true);

	    ModelNode returnVal = client.execute(op);
	    System.out.println(returnVal.get("result").toString());
	}
  private static void writeAttribute(ModelControllerClient client) throws IOException {
		ModelNode op = new ModelNode();
	    op.get("operation").set("write-attribute");

	    ModelNode address = op.get("address");
	    address.add("subsystem", "deployment-scanner");
	    
	    op.set("scan-enabled", false);
	    
	    
	    client.execute(new OperationBuilder(op).build());
//	    ModelNode returnVal = client.execute(op);
//	    System.out.println(returnVal.get("result").toString());
	}
}
