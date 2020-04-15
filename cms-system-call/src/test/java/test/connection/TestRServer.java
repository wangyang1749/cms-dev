package test.connection;

import org.junit.jupiter.api.Test;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;


public class TestRServer {

    @Test
    public void test(){
        try {
            RConnection c = new RConnection();
            REXP x = c.eval("R.version.string");
            System.out.println(x.asString());
        } catch (RserveException e) {
            e.printStackTrace();
        } catch (REXPMismatchException e) {
            e.printStackTrace();
        }

    }
}
