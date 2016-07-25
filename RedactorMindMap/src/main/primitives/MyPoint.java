package main.primitives;

import javax.swing.*;

/**
 * Created by Bogdan Kaftanatiy on 17.04.2016.
 */
public class MyPoint extends JComponent {
    private int x;
    private int y;

    public MyPoint(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getX() {
        return x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getY() {
        return y;
    }
}
