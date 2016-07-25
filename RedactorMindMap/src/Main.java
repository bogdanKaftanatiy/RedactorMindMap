import main.Program;
import javax.swing.*;

/**
 * Created by Bogdan Kaftanatiy on 17.04.2016.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Program();
            }
        });
    }
}
