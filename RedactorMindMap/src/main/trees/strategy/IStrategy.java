package main.trees.strategy;

import main.primitives.MyPoint;

/**
 * Created by Bogdan Kaftanatiy on 17.04.2016.
 */
public interface IStrategy{
    MyPoint getEndPoint(MyPoint begin, int countNext);
}
