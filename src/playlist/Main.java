package playlist;

import com.thoughtworks.xstream.*;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import com.thoughtworks.xstream.io.xml.StaxWriter;
import com.thoughtworks.xstream.io.xml.XmlFriendlyReplacer;
import java.awt.Color;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JRadioButton;
import javax.swing.table.DefaultTableModel;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.apache.commons.io.FileUtils;
import playlist.mediainfo.MediaInfo;
import playlist.xml.*;
        
        

public class Main extends javax.swing.JFrame {
    private DefaultTableModel playlistTableModel;
    public TVFormat TVFormatInstance;
    private Pair<Integer, String> currentTVFormat;
    private List<Videos<String, String, Integer, BigInteger>> videos = new ArrayList<Videos<String, String, Integer, BigInteger>>();
    private Boolean hasDropTarget = false;
    private MediaInfo mediaInfoInstance;
    private Events events;
    
    private XStream xstream = new XStream(new StaxDriver(new XmlFriendlyReplacer("__", "_")){ // FIXME
        @Override
        public StaxWriter createStaxWriter(XMLStreamWriter out) throws XMLStreamException { 
            return createStaxWriter(out, false); 
        }
    });
    
    /** Creates new form Main */
    public Main() throws Exception {
        setJnaLibraryPath(); // Must be first!
        mediaInfoInstance = new MediaInfo();
        
        xstream.alias("Cinegy", Cinegy.class);
        xstream.alias("BatchIngestList", BatchIngestList.class);
        xstream.alias("CinegyItem", CinegyItem.class);
        
        playlistTableModel = new DefaultTableModel(
            new Object [][] { },
            new String [] {
                "Path", "Format", "BitRate(Mb/sec)", "Framerate", "Aspect ratio", "Duration", "TV Format"
            }
        );

        initComponents(); // Requires playlistTableModel to be defined

        creatPlaylistButton.setEnabled(false);
        TVFormatInstance = TVFormat.getInstance(this);
        events = new Events();
    }
    
    private void setJnaLibraryPath() throws Exception {
        String path = System.getProperty("jna.library.path");
        String sep = System.getProperty("file.separator");
        String libFolder = System.getProperty("os.name") + "_" + System.getProperty("os.arch"); // FIXME

        if(path == null){
            throw new Exception("jna.library.path is empty!");
        }
        
        System.setProperty("jna.library.path", path + sep + libFolder);
    }
    
    private int setDropTarget() { // FIXME: beauty
        if (hasDropTarget) {
            return 0;
        }
        
        hasDropTarget = true;
        playlistTableScrollPane.setDropTarget(new DropTarget(){
            @Override
            public synchronized void drop(DropTargetDropEvent dtde) {
                dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                Transferable t = dtde.getTransferable();
                List fileList = null;
                try {
                    fileList = (List)t.getTransferData(DataFlavor.javaFileListFlavor);
                } catch (UnsupportedFlavorException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                File f;
                String absolutePath;
                Object[] videoData;
                
                try {
                    int listSize = fileList.size();
                    
                    for (int i = 0; i < listSize; i++){
                        f = (File)fileList.get(i);
                        absolutePath = f.getAbsolutePath();
                        videoData = getVideoData(absolutePath);
                        if(videoData != null) {
                            playlistTableModel.insertRow(playlistTableModel.getRowCount(), videoData);
                        } else {
                            events.addEvent("Error", "Cant get video data for " + absolutePath);
                        }
                    }

                    creatPlaylistButton.setEnabled(true);
                } catch (Exception ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        return 0;
    }
    
    private Object[] getVideoData(String path){
        File file;
        String format, frameRate;
        Float bitRate, aspectRatio;
        BigInteger duration;
        String microduration;
        
        try {
            file = new File(path);

            mediaInfoInstance.open(file);
            format = mediaInfoInstance.get("Format");
            bitRate = Float.parseFloat(mediaInfoInstance.get("BitRate")) / 1000000;
            frameRate = mediaInfoInstance.get("FrameRate");

            aspectRatio = Float.parseFloat(mediaInfoInstance.get("AspectRatio"));
            duration = new BigInteger(mediaInfoInstance.get("Duration"));
            microduration = String.valueOf(duration.multiply(new BigInteger("10000")));
            mediaInfoInstance.close();
            
            // FIXME: new storage
            
            videos.add(new Videos(path, "%D %N", currentTVFormat.getSecond(), microduration));

            return new Object[]{ path, format, bitRate, frameRate, aspectRatio, microduration, currentTVFormat.getSecond() };
        } catch(Throwable ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
    }
    
    
    /*
     * TVFormat related
     */
    
    public Pair<Integer, String> getTVFormat(){
        return currentTVFormat;
    }
    
    public void setTVFormat(Pair<Integer, String> TVFormat){
        currentTVFormat = TVFormat;
        TVFormatLabel.setText(TVFormat.getSecond());
        setDropTarget();
    }
    
    public void showTVFormatFrame(){
        TVFormatInstance.setLocationRelativeTo(this);
        TVFormatInstance.setVisible(true);
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        creatPlaylistButton = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        dropVideoNotify = new javax.swing.JLabel();
        playlistTableScrollPane = new javax.swing.JScrollPane();
        playlistTable = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false; //Disallow the editing of any cell
            }
        };
        cleanupButton = new javax.swing.JButton();
        TVFormatButton = new javax.swing.JButton();
        TVFormatLabel = new javax.swing.JLabel();
        removeFromPlaylistButton = new javax.swing.JButton();
        eventsButton = new javax.swing.JButton();
        joinInRoll = new javax.swing.JCheckBox();
        programCodeTextField = new javax.swing.JTextField();
        programCodeTextField.setEnabled(joinInRoll.isSelected());

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Batch ingest playlist creator");

        creatPlaylistButton.setText("Создать плейлист");
        creatPlaylistButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                creatPlaylistButtonActionPerformed(evt);
            }
        });

        dropVideoNotify.setForeground(new java.awt.Color(148, 148, 148));
        dropVideoNotify.setText("Перетащите видео-файлы в таблицу");

        playlistTable.setModel(playlistTableModel);
        playlistTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                playlistTableMouseClicked(evt);
            }
        });
        playlistTableScrollPane.setViewportView(playlistTable);

        cleanupButton.setText("Очистить");
        cleanupButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cleanupButtonActionPerformed(evt);
            }
        });

        TVFormatButton.setText("Формат...");
        TVFormatButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TVFormatButtonActionPerformed(evt);
            }
        });

        removeFromPlaylistButton.setText("Удалить из плейлиста");
        removeFromPlaylistButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeFromPlaylistButtonActionPerformed(evt);
            }
        });

        eventsButton.setText("События");
        eventsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eventsButtonActionPerformed(evt);
            }
        });

        joinInRoll.setText("Соединить в roll");
        joinInRoll.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                joinInRollStateChanged(evt);
            }
        });
        joinInRoll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                joinInRollActionPerformed(evt);
            }
        });

        programCodeTextField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                programCodeTextFieldMouseClicked(evt);
            }
        });
        programCodeTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                programCodeTextFieldKeyPressed(evt);
            }
        });

        removeFromPlaylistButton.setEnabled(false);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jSeparator1)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(cleanupButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 220, Short.MAX_VALUE)
                        .add(dropVideoNotify)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(creatPlaylistButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(eventsButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 116, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(playlistTableScrollPane)
                    .add(layout.createSequentialGroup()
                        .add(TVFormatButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 150, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(TVFormatLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 112, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(joinInRoll)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(programCodeTextField)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(removeFromPlaylistButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 224, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(TVFormatButton)
                    .add(TVFormatLabel)
                    .add(removeFromPlaylistButton)
                    .add(joinInRoll)
                    .add(programCodeTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(playlistTableScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 436, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jSeparator1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(creatPlaylistButton)
                    .add(dropVideoNotify)
                    .add(cleanupButton)
                    .add(eventsButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void creatPlaylistButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_creatPlaylistButtonActionPerformed
        if(joinInRoll.isSelected()) {
            if(programCodeTextField.getText().length() == 0) {
                programCodeTextField.setBackground(Color.red);
                return;
            }
        }
        
        JFileChooser fc;
        CinegyItem cinegyItem;
        File file;
        String xml;
        int rowCount = playlistTableModel.getRowCount();
        int columnCount = playlistTableModel.getColumnCount();
        int row, saver;
        
        // Save dialog ->
        fc = new JFileChooser(System.getProperty("user.home"));
        fc.setSelectedFile(new File("batch.cbi"));
        fc.setDialogType(1);
        
        saver = fc.showSaveDialog(this);
        
        if(saver == JFileChooser.CANCEL_OPTION) {
            return;
        }
        
        creatPlaylistButton.setEnabled(false);
        file = fc.getSelectedFile();
        // <- Save dialog
        
        Cinegy cinegy = new Cinegy();
        cinegy.BatchIngestList = new BatchIngestList();
        cinegy.BatchIngestList.TV_Format = currentTVFormat.getFirst();
        
        
        for (row = 0; row < rowCount; row++){
            cinegyItem = new CinegyItem();
            for (int column = 0; column < columnCount; column++){
                switch(column){
                    case 0: {
                        cinegyItem.Source = (String)playlistTableModel.getValueAt(row, column);
                        break;
                    }
                    case 5: {
                        cinegyItem.TimeDuration = (String)playlistTableModel.getValueAt(row, column);
                        break;
                    }
                    case 6: {
                        String value = (String)playlistTableModel.getValueAt(row, column);
                        Pair<Integer, JRadioButton> pair = (Pair<Integer, JRadioButton>)TVFormatInstance.formats.get(value);
                        cinegyItem.TV_Format = pair.getFirst();
                        if (joinInRoll.isSelected()) {
                            cinegyItem.ProgramCode = programCodeTextField.getText();
                            cinegyItem.IgnoreMediaIdDiff = 1;
                        }
                        
                        cinegyItem.Channel = "1|0|3|" + cinegyItem.TV_Format + "|" + cinegyItem.Source + "|0||";
                        cinegyItem.MediaID = cinegyItem.Channel;
                        break;
                    }
                }
            }
            
            cinegy.BatchIngestList.addItem(cinegyItem);
        }

        xml = xstream.toXML(cinegy);
        
        try {
            // Fix xml
            CharSequence oldOpenStr = "<CinegyItem><CinegyItem>", newOpenStr = "<CinegyItem>";
            CharSequence oldCloseStr = "</CinegyItem></CinegyItem>", newCloseStr = "</CinegyItem>";
            
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
            xml = xml.replace(oldOpenStr, newOpenStr).replace(oldCloseStr, newCloseStr);
            try {
                out.write(xml);
            } finally {
                out.close();
            }
            
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            xml = null;
        }
        
        creatPlaylistButton.setEnabled(true);
    }//GEN-LAST:event_creatPlaylistButtonActionPerformed

    private void cleanupButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cleanupButtonActionPerformed
        creatPlaylistButton.setEnabled(false);
        videos.clear();
        int rows = playlistTableModel.getRowCount() - 1;
        while(rows >= 0) {
            playlistTableModel.removeRow(rows--);
        }
    }//GEN-LAST:event_cleanupButtonActionPerformed

    private void TVFormatButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TVFormatButtonActionPerformed
        showTVFormatFrame();
    }//GEN-LAST:event_TVFormatButtonActionPerformed

    private void playlistTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_playlistTableMouseClicked
        int selected = playlistTable.getSelectedRowCount();
        removeFromPlaylistButton.setEnabled(selected > 0);
    }//GEN-LAST:event_playlistTableMouseClicked

    private void removeFromPlaylistButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeFromPlaylistButtonActionPerformed
        int[] selected = playlistTable.getSelectedRows();
        int row = selected.length - 1;
        while (row >= 0){
            playlistTableModel.removeRow(selected[row]);
            row--;
        }
        
        removeFromPlaylistButton.setEnabled(false);
    }//GEN-LAST:event_removeFromPlaylistButtonActionPerformed

    private void eventsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eventsButtonActionPerformed
        events.setVisible(true);
    }//GEN-LAST:event_eventsButtonActionPerformed

    private void joinInRollActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_joinInRollActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_joinInRollActionPerformed

    private void joinInRollStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_joinInRollStateChanged
        programCodeTextField.setEnabled(joinInRoll.isSelected());
    }//GEN-LAST:event_joinInRollStateChanged

    private void programCodeTextFieldMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_programCodeTextFieldMouseClicked
        programCodeTextField.setBackground(Color.white);
    }//GEN-LAST:event_programCodeTextFieldMouseClicked

    private void programCodeTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_programCodeTextFieldKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_programCodeTextFieldKeyPressed
    
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
            javax.swing.UIManager.LookAndFeelInfo[] installedLookAndFeels=javax.swing.UIManager.getInstalledLookAndFeels();
            for (int idx=0; idx<installedLookAndFeels.length; idx++)
                if ("Nimbus".equals(installedLookAndFeels[idx].getName())) {
                    javax.swing.UIManager.setLookAndFeel(installedLookAndFeels[idx].getClassName());
                    break;
                }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Main main;
                try {
                    main = new Main();
                    main.setLocationRelativeTo(null);
                    main.setVisible(true);
                    main.showTVFormatFrame();
                } catch (Exception ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.WARNING, null, ex);
                }
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton TVFormatButton;
    private javax.swing.JLabel TVFormatLabel;
    private javax.swing.JButton cleanupButton;
    private javax.swing.JButton creatPlaylistButton;
    private javax.swing.JLabel dropVideoNotify;
    private javax.swing.JButton eventsButton;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JCheckBox joinInRoll;
    private javax.swing.JTable playlistTable;
    private javax.swing.JScrollPane playlistTableScrollPane;
    private javax.swing.JTextField programCodeTextField;
    private javax.swing.JButton removeFromPlaylistButton;
    // End of variables declaration//GEN-END:variables
    
}
