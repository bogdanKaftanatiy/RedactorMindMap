package main.trees.strategy;

import main.primitives.MyPoint;

/**
 * Created by Bogdan Kaftanatiy on 18.04.2016.
 */
public class TopStrategy implements IStrategy {
    @Override
    public MyPoint getEndPoint(MyPoint begin, int countNext) {
        MyPoint end;
        if (countNext%2 == 1){
            end = new MyPoint(begin.getX() - 50 - ((countNext+1)/2)*33, begin.getY() - 250 + ((countNext+1)/2)*25);
        } else {
            end = new MyPoint(begin.getX() + 50 + (countNext/2+1)*33, begin.getY() - 250 + (countNext/2 +1)*25);
        }
        return end;
    }
}
