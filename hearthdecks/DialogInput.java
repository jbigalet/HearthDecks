package hearthdecks;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class DialogInput {
    
    private JFrame frame;
    
    public DialogInput() {
        frame = new JFrame();

        frame.setVisible(true);
        BringToFront();
    }
    
    public String getInput() {
        return JOptionPane.showInputDialog("URL: ");
    }

    private void BringToFront() {
        frame.setExtendedState(JFrame.ICONIFIED);
        frame.setExtendedState(JFrame.NORMAL);
    }
}
