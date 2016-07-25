package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

/**
 * Created by Bogdan Kaftanatiy on 21.04.2016.
 */
public class Program extends JFrame {
    private DiagramPanel gp;
    public Program(){
        this.setTitle("MindMapRedactor");
        this.setSize(1100, 650);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLayout(null);
        this.setVisible(true);

        gp = new DiagramPanel(new Dimension(this.getWidth() - 20, this.getHeight() - 65));


        JMenuBar menuBar = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenuItem save = new JMenuItem("Save..");
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser(new File(".\\resources\\saves"));
                int result = fileChooser.showSaveDialog(null);
                if(result == JFileChooser.APPROVE_OPTION) {
                    String filename = fileChooser.getCurrentDirectory() + "\\" + fileChooser.getSelectedFile().getName();
                    gp.writeFile(filename);
                }
            }
        });
        JMenuItem open = new JMenuItem("Open..");
        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser(new File(".\\resources\\saves"));
                int result = fileChooser.showOpenDialog(null);
                if(result == JFileChooser.APPROVE_OPTION) {
                    String filename = fileChooser.getCurrentDirectory() + "\\" + fileChooser.getSelectedFile().getName();
                    gp.readFile(filename);
                }
            }
        });
        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processWindowEvent(new WindowEvent(Program.this, WindowEvent.WINDOW_CLOSING));
            }
        });
        file.add(save);
        file.add(open);
        file.addSeparator();
        file.add(exit);
        menuBar.add(file);
        JMenu edit = new JMenu("Edit");
        JMenuItem undo = new JMenuItem("Undo");
        undo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gp.undo();
            }
        });
        JMenuItem redo = new JMenuItem("Redo");
        redo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gp.redo();
            }
        });
        edit.add(undo);
        edit.add(redo);
        menuBar.add(edit);
        JMenu about = new JMenu("About");
        JFrame temp = this;
        about.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(temp, "Coursework\nAuthor: Kaftanatiy Bogdan\n2016");
            }
        });
        menuBar.add(about);
        this.setJMenuBar(menuBar);

        gp.setBounds(0, 0, (this.getWidth() - 20), (this.getHeight() - 65));
        this.add(gp);

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                gp.setBounds(0, 0, getWidth() - 20, getHeight() - 65);
                gp.setPreferredSize(new Dimension(getWidth() - 20, getHeight() - 65));
                gp.revalidate();
                gp.repaint();
            }
        });
    }
}
