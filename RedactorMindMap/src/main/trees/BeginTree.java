package main.trees;

import main.*;
import main.primitives.*;
import main.trees.strategy.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Bogdan Kaftanatiy on 17.04.2016.
 */
public class BeginTree extends JComponent implements Observable{
    protected MyPoint begin;
    protected ArrayList<LineTree> next;
    protected JLabel add;
    protected int thickness;
    protected TreeType type;
    protected IStrategy strategy;
    protected DiagramObserver parentPanel;
    protected ArrayList<Integer> freeIndex;

    public BeginTree(MyPoint b, TreeType t, DiagramObserver p){
        attach(p);
        this.begin = b;
        next = new ArrayList<>();
        thickness = 4;
        type = t;
        freeIndex = new ArrayList<>();

        add = new JLabel();
        notifyObserver(add);
        add.addMouseListener (new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getButton() == MouseEvent.BUTTON3){
                    parentPanel.saveState();
                    for(int i = next.size() - 1; i >= 0; i--)
                        deleteLine(next.get(i));
                }else if(e.getButton() == MouseEvent.BUTTON1) {
                    parentPanel.saveState();
                    addLine();
                }
                parentPanel.update();
            }
        });
        add.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                add.setIcon(new ImageIcon("resources\\icon\\add.png"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                add.setIcon(new ImageIcon());
            }
        });

        switch (type){
            case LEFT:
                strategy = new LeftStrategy();
                break;
            case TOP:
                strategy = new TopStrategy();
                break;
            case RIGHT:
                strategy = new RightStrategy();
                break;
            case BOTTOM:
                strategy = new BottomStrategy();
                break;
            default:
                break;
        }
    }

    public void addLine() {
        int addIndex;
        if(freeIndex.size()>0) {
            addIndex = freeIndex.get(freeIndex.size()-1);
        } else addIndex = next.size();
        addLine("Edit text", strategy.getEndPoint(begin, addIndex), getColor());
    }

    public void addLine(String m, MyPoint e, Color c){
        AbstractLine line;
        int addIndex;
        if(freeIndex.size()>0) {
            addIndex = freeIndex.get(freeIndex.size()-1);
            freeIndex.remove(freeIndex.size()-1);
        } else addIndex = next.size();
        switch (type){
            case LEFT:
            case RIGHT:
                line = new HorizontalLine(begin, e, thickness);
                break;
            case TOP:
            case BOTTOM:
                line = new VerticalLine(begin, e, thickness);
                break;
            default:
                line = new HorizontalLine(begin, e, thickness);
                break;
        }
        LineTree lt = new LineTree(begin, line, type, parentPanel, this, addIndex);
        lt.setMessage(m);
        lt.setEnd(e);
        lt.setLineColor(c);
        next.add(addIndex, lt);
    }

    public TreeType getType(){
        return type;
    }

    public Color getColor(){
        Color result;

        switch ((new Random()).nextInt(8)){
            case 0:
                result = Color.blue;
                break;
            case 1:
                result = Color.cyan;
                break;
            case 2:
                result = Color.green;
                break;
            case 3:
                result = Color.magenta;
                break;
            case 4:
                result = Color.orange;
                break;
            case 5:
                result = Color.red;
                break;
            case 6:
                result = Color.yellow;
                break;
            case 7:
                result = Color.pink;
                break;
            default:
                result = Color.black;
                break;
        }

        return result;
    }

    public void deleteLine(LineTree obj){
        freeIndex.add(next.get(next.indexOf(obj)).getIndex());
        next.get(next.indexOf(obj)).deleteMessage();
        next.get(next.indexOf(obj)).deleteAdd();
        next.remove(next.indexOf(obj));

        notifyObserver();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        add.setBounds(begin.getX() - 8, begin.getY() - 8, 16, 16);
        for(int i = 0; i<next.size(); i++){
            next.get(i).paintComponent(g);
        }
    }

    public JLabel getAdd() {
        return add;
    }

    public void setBegin(MyPoint begin) {
        this.begin = begin;
        for(int i = 0; i<next.size(); i++){
            next.get(i).setBegin(begin);
        }
    }

    public void removeTextField(){
        for(int i = 0; i < next.size(); i++){
            next.get(i).removeTextField();
        }
    }

    public ArrayList<LineTree> getNext(){
        return next;
    }

    public void attach(DiagramObserver obs){
        parentPanel = obs;
    }

    public void notifyObserver(){
        parentPanel.update();
    }

    public void notifyObserver(JComponent c){
        parentPanel.update(c);
    }
}
