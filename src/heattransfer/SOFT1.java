/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package heattransfer;

import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import org.jvnet.substance.SubstanceLookAndFeel;
import org.jvnet.substance.painter.text.DefaultTextPainter;
import org.jvnet.substance.skin.ModerateSkin;
import org.jvnet.substance.theme.SubstanceAquaTheme;
import org.jvnet.substance.watermark.SubstanceBubblesWatermark;
import org.jvnet.substance.watermark.SubstanceWatermark;

/**
 *
 * @author yk
 */
public class SOFT1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
//        String lookAndFeel0 = "javax.swing.plaf.metal.MetalLookAndFeel";
//        String lookAndFeel1 = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
//        String lookAndFeel2 = "com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel";
//        String lookAndFeel3 = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
//        String lookAndFeel4 = "com.sun.java.swing.plaf.mac.MacLookAndFeel";
//        String lookAndFeel5 = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
//        String lookAndFeel6 = UIManager.getCrossPlatformLookAndFeelClassName();
//        String lookAndFeel7 = UIManager.getSystemLookAndFeelClassName();
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(new SubstanceLookAndFeel());
                    JFrame.setDefaultLookAndFeelDecorated(true);
                    SubstanceLookAndFeel.setSkin(new ModerateSkin());
                    SubstanceLookAndFeel.setCurrentTheme(
                            new SubstanceAquaTheme()
                    );
                    SubstanceLookAndFeel.setCurrentWatermark(
                            new SubstanceBubblesWatermark()
                    );
                    SubstanceLookAndFeel.setCurrentTextPainter(
                            new DefaultTextPainter()
                    );
                    FontUIResource fontRes = new FontUIResource(
                            new Font("宋体", Font.PLAIN, 25)
                    );
                } catch (Exception e) {

                }
//                new MainFrame(null, 0, 0, 0, 0);
                new Optimization(0, 0, 1300, 800);
            }
        });

    }

}
