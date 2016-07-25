package main.primitives;

import java.util.ArrayList;

/**
 * Created by Bogdan Kaftanatiy on 17.04.2016.
 */
public class VerticalLine extends AbstractLine {
    public VerticalLine(MyPoint b, MyPoint e, int t){
        super(b, e, t);
    }

    @Override
    public ArrayList<MyPoint> getPointArray() {
        ArrayList<MyPoint> array = new ArrayList<>();
        int x2, y2, x3, y3, w, h;
        w = Math.abs(end.getX() - begin.getX());
        h = Math.abs(end.getY() - begin.getY());

        x2 = begin.getX();
        y2 = (begin.getY()+end.getY())/2;
        x3 = begin.getX() - (end.getX() - begin.getX())/2;
        y3 = end.getY();
        MyPoint second = new MyPoint(x2, y2);
        MyPoint third = new MyPoint(x3, y3);


        double step = (1. / (double)(w + h))*10;
        if(step < 0.001)
            step = 0.001;

        for(double t = 0; t <= 1; t += step){
            double x, y;
            x = Math.pow((1-t),3)*begin.getX() + 3*Math.pow((1-t),2)*t*second.getX() +
                    3*(1-t)*t*t*third.getX() + Math.pow(t,3)*end.getX();
            y = Math.pow((1-t),3)*begin.getY() + 3*Math.pow((1-t),2)*t*second.getY() +
                    3*(1-t)*t*t*third.getY() + Math.pow(t,3)*end.getY();
            y+=0.50;
            MyPoint temp = new MyPoint((int)x, (int)y);
            array.add(temp);
        }
        return array;
    }
}
