package main.trees.strategy;

import main.primitives.MyPoint;

/**
 * Created by Bogdan Kaftanatiy on 17.04.2016.
 */
public class LeftStrategy implements IStrategy {
    @Override
    public MyPoint getEndPoint(MyPoint begin, int countNext) {
        MyPoint end;
        if(countNext == 0){
            end = new MyPoint(begin.getX() - 250, begin.getY());
        } else if (countNext%2 == 1){
            end = new MyPoint(begin.getX() - 250 + ((countNext+1)/2)*10, begin.getY() + ((countNext+1)/2)*30);
        } else {
            end = new MyPoint(begin.getX() - 250 + (countNext/2)*10, begin.getY() - (countNext/2)*30);
        }
        return end;
    }
}
