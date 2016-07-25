package main.primitives;

import java.util.ArrayList;

/**
 * Created by Bogdan Kaftanatiy on 17.04.2016.
 */
public class HorizontalLine extends AbstractLine {
    public HorizontalLine(MyPoint b, MyPoint e, int t){
        super(b, e, t);
    }

    @Override
    public ArrayList<MyPoint> getPointArray() {
        ArrayList<MyPoint> array = new ArrayList<>();
        int x2, y2, x3, y3, w, h;
        w = Math.abs(end.getX() - begin.getX());
        h = Math.abs(end.getY() - begin.getY());
        x2 = (end.getX() + begin.getX())/2;
        y2 = begin.getY();
        x3 = x2;
        y3 = end.getY();
        MyPoint second = new MyPoint(x2, y2);
        MyPoint third = new MyPoint(x3, y3);


        double step = (1. / (double)(w + h))*10;
        if(step < 0.001)
            step = 0.001;

        for(double i = 0; i <= 1; i += step){
            double x, y;
            x = Math.pow((1-i),3)*begin.getX() + 3*Math.pow((1-i),2)*i*second.getX() +
                    3*(1-i)*i*i*third.getX() + Math.pow(i,3)*end.getX();
            y = Math.pow((1-i),3)*begin.getY() + 3*Math.pow((1-i),2)*i*second.getY() +
                    3*(1-i)*i*i*third.getY() + Math.pow(i,3)*end.getY();
            y+=0.5;
            MyPoint temp = new MyPoint((int)x, (int)y);
            array.add(temp);
        }

        return array;
    }
}
