package test.connection;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.Rserve.RConnection;

import javax.swing.*;
import java.awt.*;

public class TestRServeFrame {// extends JFrame {
//    private static final long serialVersionUID = 1L;
//    static Image img;
//
//    public static void main(String[] args) throws Exception {
//        TestRServeFrame wc = new TestRServeFrame();
//        REXP xp = wc.getRobj();// 获得R对象
//        wc.PlotDemo(xp, wc);// 错误
//
//    }
//
//    private REXP getRobj() throws Exception {
//        RConnection c = new RConnection();
//        c.setStringEncoding("utf8");// 设置字符编码
//        REXP Rservesion = c.eval("R.version.string");
//        System.out.println(Rservesion.asString());
//        System.out.println("n----------绘图演示--------");
//        System.out.println("");
//        REXP xp = c.parseAndEval("jpeg('test.jpg',quality=90)");
//        c.eval("plot(c(1:5));dev.off()");
////        c.eval("library(wordcloud)");
////        c.voidEval("colors=c('red','blue','green','yellow','purple')");
////        c.parseAndEval("data(SOTU);wordcloud(SOTU,min.freq=10,colors=colors);dev.off()");
//        xp = c.parseAndEval("r=readBin('test.jpg','raw',3000*3000);unlink('test.jpg');r");
//        return xp;
//    }
//
//    public void PlotDemo(REXP xp, JFrame f) throws Exception {
//        img = Toolkit.getDefaultToolkit().createImage(xp.asBytes());
//        MediaTracker mediaTracker = new MediaTracker(this);
//        mediaTracker.addImage(img, 0);
//        mediaTracker.waitForID(0);
//        f.setTitle("Test Image");
//        f.setSize(img.getWidth(null), img.getHeight(null));
//        f.setDefaultCloseOperation(EXIT_ON_CLOSE);
//        f.setVisible(true);
//    }
//
//    public void paint(Graphics g) {
//        g.drawImage(img, 0, 0, null);
//    }
}
