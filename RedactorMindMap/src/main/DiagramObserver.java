package main;

import javax.swing.*;

/**
 * Created by Bogdan Kaftanatiy on 26.04.2016.
 */
public interface DiagramObserver {
    void update();
    void update(JComponent c);
    void saveState();
}
