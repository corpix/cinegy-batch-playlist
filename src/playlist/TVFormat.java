/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package playlist;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.swing.JRadioButton;

/**
 *
 * @author corpix
 */
public class TVFormat extends javax.swing.JFrame {
    private static TVFormat instance = null;
    private Main classParent;
    public Map<String, Pair<Integer, JRadioButton>> formats;
    private Set formatsSet;

    protected TVFormat(Main parent) {
        classParent = parent;
        initComponents();
        
        formats = new HashMap<String, Pair<Integer, JRadioButton>>();
        
        formats.put("PAL 4:3", new Pair(1, format1Button));
        formats.put("PAL 16:9", new Pair(2, format2Button));
        formats.put("1080i@25", new Pair(5, format5Button));
        formats.put("720p@50", new Pair(14, format14Button));
        formats.put("1080p@25", new Pair(15, format15Button));

        formatsSet = formats.entrySet();
        Iterator iter = formatsSet.iterator();
        while(iter.hasNext()) {
            Map.Entry entry = (Map.Entry)iter.next();
            String format = (String)entry.getKey();

            Pair value = (Pair)entry.getValue();

            JRadioButton el = (JRadioButton)value.getSecond();
            el.setText(format);
            formatsGroup.add(el);
        }
    }
    
    public static TVFormat getInstance(Main parent) {
        if(instance == null) {
            instance = new TVFormat(parent);
        }
      
        return instance;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        formatsGroup = new javax.swing.ButtonGroup();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        format1Button = new javax.swing.JRadioButton();
        format2Button = new javax.swing.JRadioButton();
        format5Button = new javax.swing.JRadioButton();
        format14Button = new javax.swing.JRadioButton();
        format15Button = new javax.swing.JRadioButton();

        setAlwaysOnTop(true);
        setBounds(new java.awt.Rectangle(0, 0, 0, 0));
        setResizable(false);
        setType(java.awt.Window.Type.POPUP);
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                shownHandler(evt);
            }
        });

        okButton.setBackground(new java.awt.Color(137, 206, 64));
        okButton.setText("Применить");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Отмена");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        format1Button.setText("jRadioButton1");

        format2Button.setText("jRadioButton2");

        format5Button.setText("jRadioButton5");

        format14Button.setText("jRadioButton14");

        format15Button.setText("jRadioButton15");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(format1Button)
                            .addComponent(format2Button))
                        .addGap(26, 26, 26)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(format5Button)
                            .addComponent(format15Button)
                            .addComponent(format14Button))
                        .addGap(0, 105, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(format1Button)
                    .addComponent(format5Button))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(format2Button)
                    .addComponent(format14Button))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(format15Button)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(okButton)
                    .addComponent(cancelButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        this.setVisible(false);
        
        Iterator iter = formatsSet.iterator();
        Pair<Integer, String> currentTVFormat = classParent.getTVFormat();
        
        while(iter.hasNext()) { // Fixing selection
            Map.Entry entry = (Map.Entry)iter.next();
            Pair value = (Pair)entry.getValue();

            Integer TVFormat = (Integer)value.getFirst();
            if(TVFormat == (Integer)currentTVFormat.getFirst()) {
                JRadioButton el = (JRadioButton)value.getSecond();
                el.setSelected(true);

                break;
            }
        }
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        Iterator iter = formatsSet.iterator();
        while(iter.hasNext()) {
            Map.Entry entry = (Map.Entry)iter.next();
            String format = (String)entry.getKey();

            Pair value = (Pair)entry.getValue();

            JRadioButton el = (JRadioButton)value.getSecond();
            if(el.isSelected()){
                classParent.setTVFormat(new Pair((Integer)value.getFirst(), format));
                setVisible(false);
                break;
            }
        }
    }//GEN-LAST:event_okButtonActionPerformed

    private void shownHandler(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_shownHandler
        Pair <Integer, String> TVFormat = classParent.getTVFormat();

        cancelButton.setEnabled(TVFormat != null);
    }//GEN-LAST:event_shownHandler

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TVFormat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TVFormat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TVFormat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TVFormat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JRadioButton format14Button;
    private javax.swing.JRadioButton format15Button;
    private javax.swing.JRadioButton format1Button;
    private javax.swing.JRadioButton format2Button;
    private javax.swing.JRadioButton format5Button;
    private javax.swing.ButtonGroup formatsGroup;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JButton okButton;
    // End of variables declaration//GEN-END:variables
}
