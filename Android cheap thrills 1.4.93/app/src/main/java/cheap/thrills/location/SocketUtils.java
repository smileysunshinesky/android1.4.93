package cheap.thrills.location;

import java.util.Timer;

/**
 * Created by ms3 ctfinal.
 */

public class SocketUtils {

   private static SocketUtils mInstance;
   private OSocket oSocket;

   public SocketUtils() {
   }

   public static SocketUtils getInstance() {
      if (mInstance == null){
         mInstance = new SocketUtils();
      }
      return mInstance;
   }

   public OSocket getSocketInterface() {
      return oSocket;
   }

   public void setSocketInterface(OSocket oSocket) {
      this.oSocket = oSocket;
   }

   public interface OSocket {
      Timer Timer();
   }

}
