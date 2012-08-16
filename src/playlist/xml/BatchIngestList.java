/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package playlist.xml;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author corpix
 */
public class BatchIngestList {
    public int FromTape = 0;
    public int Silent = 0;
    public int NoTapeDlg = 0;
    protected String CapturerID = "Video File Ingestor : ";
    public String ListName = "%D %N";
    public int TV_Format;
    private List CinegyItem = new ArrayList();
    
    public void addItem(CinegyItem item){
        CinegyItem.add(item);
    }
    
    public void clear(){
        CinegyItem.clear();
    }
}
