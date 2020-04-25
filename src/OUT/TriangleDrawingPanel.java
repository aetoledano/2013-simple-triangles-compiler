package OUT;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.border.LineBorder;

public class TriangleDrawingPanel extends javax.swing.JPanel {

    public TriangleDrawingPanel() {
        initComponents();
        setSize(480, 320);
        setBorder(new LineBorder(Color.WHITE, 3));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBackground(new java.awt.Color(102, 204, 255));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    ArrayList<dibu.Triangle> list;

    public void setList(ArrayList<dibu.Triangle> list) {
        this.list = list;
    }

    public void Pinta() {
        paint(getGraphics());
    }
    boolean movingOn;
    dibu.Triangle m;
    Dimension oldSize;
    BufferedImage buffer;

    @Override
    public void paint(Graphics g) {
        if (getWidth() > 0 && getHeight() > 0) {
            
            synchronized (this) {
                if (oldSize == null || oldSize != getSize()) {
                    oldSize = getSize();
                    buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
                }
               Graphics2D bg = (Graphics2D) buffer.getGraphics();
               bg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
               bg.setStroke(new BasicStroke(2.5f));
                super.paint(bg);
                if (movingOn) {
                    int x0 = m.getX();
                    int y0 = m.getY();
                    int dim = m.getDim();
                    Polygon inm = CalculateTriangle(x0, y0, dim);
                    bg.drawPolygon(inm);
                }
                if (list != null) {
                    for (dibu.Triangle t : list) {
                        if (t != null) {
                            if (t.isInit()) {
                                int x0 = t.getX();
                                int y0 = t.getY();
                                int dim = t.getDim();
                                Polygon tr = CalculateTriangle(x0, y0, dim);
                                bg.drawPolygon(tr);
                            }
                        }
                    }

                }
            }
            g.drawImage(buffer, 0, 0, this);
        }
    }

    public void moveTriangle(dibu.Triangle t, int newX, int newY) {
        movingOn = true;

        m = new dibu.Triangle(0);
        m.setDim(t.getDim());
        m.setX(t.getX());
        m.setY(t.getY());
        
        int x0 = t.getX();
        int y0 = t.getY();

        //para las x
        if (x0 > newX) {
            while (m.getX() != newX) {
                m.setX(x0--);
                paint(getGraphics());
                try {
                    Thread.sleep(2);
                } catch (InterruptedException ex) {
                    Logger.getLogger(TriangleDrawingPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            while (m.getX() != newX) {
                m.setX(x0++);
                paint(getGraphics());
                try {
                    Thread.sleep(2);
                } catch (InterruptedException ex) {
                    Logger.getLogger(TriangleDrawingPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        System.out.println("Sali de las x");
        //para las y
        if (y0 > newY) {
            while (m.getY() != newY) {
                m.setY(y0--);
                paint(getGraphics());
                try {
                    Thread.sleep(2);
                } catch (InterruptedException ex) {
                    Logger.getLogger(TriangleDrawingPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            while (m.getY() != newY) {
                m.setY(y0++);
                paint(getGraphics());
                try {
                    Thread.sleep(2);
                } catch (InterruptedException ex) {
                    Logger.getLogger(TriangleDrawingPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        System.out.println("Sali de las y");
        movingOn = false;
    }

    private Polygon CalculateTriangle(int x0, int y0, int dim) {
        int h = (int) Math.sqrt(Math.pow(dim, 2) - Math.pow((dim / 2), 2));
        int[] xs = new int[3];
        int[] ys = new int[3];
        //las xs
        xs[0] = x0;
        xs[1] = x0 - dim / 2;
        xs[2] = x0 + dim / 2;

        //las ys
        ys[0] = y0;
        ys[1] = y0 + h;
        ys[2] = ys[1];
        return new Polygon(xs, ys, 3);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

}
