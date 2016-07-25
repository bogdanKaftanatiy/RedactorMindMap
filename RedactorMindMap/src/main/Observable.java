package main;

import javax.swing.*;

/**
 * Created by Bogdan Kaftanatiy on 11.05.2016.
 */
public interface Observable {
    void attach(DiagramObserver obs);
    void notifyObserver();
    void notifyObserver(JComponent c);
}
