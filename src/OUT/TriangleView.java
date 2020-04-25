
package OUT;

import java.awt.Insets;
import javax.swing.JPanel;

public class TriangleView extends javax.swing.JFrame {

    TriangleDrawingPanel panel;

    public TriangleDrawingPanel getPanel() {
        return panel;
    }
    
    Insets in;
    
    public TriangleView() {
        initComponents();
        panel = new TriangleDrawingPanel();
        add(panel);
        setSize(panel.getWidth(), panel.getHeight());
        pack();
        setTitle("Triangle View Frame");
        setVisible(true);
        in = getInsets();
        setSize(panel.getWidth()+in.left-3, panel.getHeight()+in.top-3);
        setResizable(false);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                formMouseMoved(evt);
            }
        });
        getContentPane().setLayout(null);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseMoved
        setTitle("Triangle View Frame -> Coordinate: [x="+evt.getPoint().getX()+",y="+(evt.getPoint().getY()-in.top)+"]" );
    }//GEN-LAST:event_formMouseMoved


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
