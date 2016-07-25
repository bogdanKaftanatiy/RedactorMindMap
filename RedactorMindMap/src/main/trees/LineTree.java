package main.trees;

import main.DiagramObserver;
import main.primitives.*;
import main.trees.strategy.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * Created by Bogdan Kaftanatiy on 17.04.2016.
 */
public class LineTree extends BeginTree {
    private AbstractLine line;
    private JTextField message;
    private int index;
    private BeginTree parentTree;
    private Color lineColor;

    public LineTree(MyPoint b, AbstractLine l, TreeType t, DiagramObserver p, BeginTree pt, int i){
        super(b, t, p);
        parentTree = pt;
        index = i;
        line = l;
        lineColor = getColor();

        message = new JTextField("Edit text");
        message.setHorizontalAlignment(JLabel.CENTER);
        message.setOpaque(false);
        message.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        message.setEditable(false);
        message.setVisible(true);
        notifyObserver(message);
        message.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(!message.isEditable() && e.getClickCount() == 2){
                    parentPanel.saveState();
                    message.setOpaque(true);
                    message.setEditable(true);
                    message.getCaret().setVisible(true);
                }
            }
        });
        message.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(message.isEditable()){
                    message.setOpaque(false);
                    message.setEditable(false);
                    message.getCaret().setVisible(false);
                }
            }
        });


        LineTree current = this;
        add.removeMouseListener(add.getMouseListeners()[0]);
        add.addMouseListener(new MouseAdapter() {
            private boolean isDragged = false;
            @Override
            public void mousePressed(MouseEvent e) {
                parentPanel.saveState();
                isDragged = true;
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3){
                    parentTree.deleteLine(current);
                } else if(e.getButton() == MouseEvent.BUTTON1){
                    addLine();
                } else if(e.getButton() == MouseEvent.BUTTON2){
                    Color temp = JColorChooser.showDialog(null, "Choose color", lineColor);
                    if(temp != null) {
                        lineColor = temp;
                    }
                }
                notifyObserver();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                if(isDragged){
                    if((e.getX() > 0 && e.getX() < 16) && (e.getY() > 0 && e.getY() < 16))
                        return;

                    setEnd(new MyPoint(line.getEndPoint().getX() + e.getX(), line.getEndPoint().getY() + e.getY()));
                    notifyObserver();
                }
                isDragged = false;
            }
        });
    }

    @Override
    public void addLine() {
        int addIndex;
        if(freeIndex.size()>0) {
            addIndex = freeIndex.get(freeIndex.size()-1);
        } else addIndex = next.size();
        MyPoint e = begin;
        switch (type){
            case LEFT:
            case RIGHT:
                e = strategy.getEndPoint(this.line.getEndPoint(), addIndex);
                break;
            case TOP:
            case BOTTOM:
                IStrategy tempStrategy;
                if(index %2 == 0) {
                    tempStrategy = new RightStrategy();
                }
                else {
                    tempStrategy = new LeftStrategy();
                }
                e = tempStrategy.getEndPoint(this.line.getEndPoint(), addIndex);
                break;
        }
        addLine("Edit text", e, getColor());
    }

    @Override
    public void addLine(String m, MyPoint e, Color c){
        AbstractLine line;
        TreeType tempType = type;

        int addIndex;
        if(freeIndex.size()>0) {
            addIndex = freeIndex.get(freeIndex.size()-1);
            freeIndex.remove(freeIndex.size()-1);
        } else addIndex = next.size();

        if(type == TreeType.BOTTOM || type == TreeType.TOP){
            if(index %2 == 0) {
                tempType = TreeType.RIGHT;
            }
            else {
                tempType = TreeType.LEFT;
            }
        }

        line = new HorizontalLine(this.line.getEndPoint(), e, thickness);
        LineTree lt = new LineTree(this.line.getEndPoint(), line, tempType, parentPanel, this, addIndex);
        lt.setMessage(m);
        lt.setLineColor(c);
        next.add(addIndex, lt);
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(lineColor);
        line.paintComponent(g);
        add.setBounds(line.getEndPoint().getX() - 8, line.getEndPoint().getY() - 8, 16, 16);
        message.setBounds(line.getEndPoint().getX() - 75, line.getEndPoint().getY() - 20, 150, 25);

        for(int i = 0; i<next.size(); i++){
            next.get(i).paintComponent(g);
        }
    }

    public MyPoint getEnd(){
        return line.getEndPoint();
    }

    @Override
    public void setBegin(MyPoint begin) {
        int dX = begin.getX() - this.begin.getX();
        int dY = begin.getY() - this.begin.getY();
        this.begin = begin;
        line.setBegin(begin);
        line.setEnd(new MyPoint(line.getEndPoint().getX() + dX, line.getEndPoint().getY() + dY));
        for(int i = 0; i<next.size(); i++){
            next.get(i).setBegin(line.getEndPoint());
        }

    }

    public void deleteMessage(){
        notifyObserver(message);
        for (int i = 0; i < next.size(); i++){
            next.get(i).deleteMessage();
        }
    }

    public void deleteAdd(){
        notifyObserver(add);
        for (int i = 0; i < next.size(); i++){
            next.get(i).deleteAdd();
        }
    }

    public void deleteLine(LineTree obj){
        freeIndex.add(next.get(next.indexOf(obj)).getIndex());
        next.get(next.indexOf(obj)).deleteMessage();
        next.get(next.indexOf(obj)).deleteAdd();
        next.remove(obj);

        notifyObserver();
    }

    public void setEnd(MyPoint end){
        line.setEnd(new MyPoint(end.getX(), end.getY()));
    }

    public ArrayList<String> getString(){
        ArrayList<String> array = new ArrayList<>();

        String adds = "";
        adds+=index + "><message>" + message.getText() + "</message>";
        adds+="<color>" + lineColor.getRed() + "," + lineColor.getGreen() + "," + lineColor.getBlue() + "</color>";
        adds+="<end>" + getEnd().getX() + "," + getEnd().getY() + "</end>";
        adds+="<next>" + next.size() + "</next>";
        array.add(adds);
        for(int i = 0; i < next.size(); i++){
            ArrayList<String> nextArray = next.get(i).getString();
            for(int j = 0; j < nextArray.size(); j++){
                array.add(index + "," + nextArray.get(j));
            }
        }

        return array;
    }

    public void setLineColor(Color c){
        lineColor = c;
    }

    public void removeTextField(){
        if(message.isEditable()){
            message.setOpaque(false);
            message.setEditable(false);
            message.getCaret().setVisible(false);
        }
        for(int i = 0; i < next.size(); i++){
            next.get(i).removeTextField();
        }
        notifyObserver();
    }

    public int getIndex() {
        return index;
    }

    public String getMessage(){
        return message.getText();
    }

    public void setMessage(String text){
        message.setText(text);
    }
}
