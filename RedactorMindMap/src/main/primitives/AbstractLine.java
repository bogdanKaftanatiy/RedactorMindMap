package main.primitives;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bogdan Kaftanatiy on 17.04.2016.
 */
public abstract class AbstractLine extends JComponent{
    protected MyPoint begin;
    protected MyPoint end;
    protected int thickness;
    private ArrayList<MyPoint> pointArray;

    public AbstractLine(MyPoint b, MyPoint e, int t){
        this.begin = b;
        this.end = e;
        thickness = t;
        pointArray = getPointArray();
    }

    public abstract ArrayList<MyPoint> getPointArray();

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        Map renderingHints = new HashMap<>();
        renderingHints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        renderingHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHints(renderingHints);
        BasicStroke p = new BasicStroke(4);
        g2.setStroke(p);

        for(int i = 1; i < pointArray.size(); i++){
            g2.drawLine(pointArray.get(i-1).getX(), pointArray.get(i-1).getY(), pointArray.get(i).getX(), pointArray.get(i).getY());
        }
        g2.drawLine(pointArray.get(pointArray.size() - 1).getX(), pointArray.get(pointArray.size() - 1).getY(), end.getX(), end.getY());
    }

    public MyPoint getEndPoint() {
        return end;
    }

    public void setBegin(MyPoint begin) {
        this.begin = begin;
        pointArray.clear();
        pointArray = getPointArray();
    }

    public void setEnd(MyPoint end) {
        this.end = end;
        pointArray.clear();
        pointArray = getPointArray();
    }
}
