package hearthdecks;

import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.Menu;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class SysTray {

    public static Menu loadMenu = new Menu("Load");
    
    public static void main(String[] args) throws IOException {
        final PopupMenu popup = new PopupMenu();
        final TrayIcon trayIcon =
                new TrayIcon( ImageIO.read( new File("icon16.png") ), "Hearthstone decks" );
        final SystemTray tray = SystemTray.getSystemTray();
       
        MenuItem saveItem = new MenuItem("Save");
        saveItem.addActionListener( new ActionListener() {
            @Override public void actionPerformed(ActionEvent ae) {
                saveDeck();
            }
        });
        
        MenuItem saveFromFileItem = new MenuItem("Save from file");
        saveFromFileItem.addActionListener( new ActionListener() {
            @Override public void actionPerformed(ActionEvent ae) {
                Handler.saveDeckFromFile( new ChooseFile().getFile() );
            }
        });
        
        MenuItem saveFromURLItem = new MenuItem("Save from URL");
        saveFromURLItem.addActionListener( new ActionListener() {
            @Override public void actionPerformed(ActionEvent ae) {
                Handler.saveDeckFromURL( JOptionPane.showInputDialog("URL: ") );
            }
        });
        
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.addActionListener( new ActionListener() {
            @Override public void actionPerformed(ActionEvent ae) {
//                tray.remove( trayIcon );
                System.exit(0);
            }
        });
        
        reloadDeckList();
       
        popup.add( saveItem );
        popup.add( saveFromFileItem );
        popup.add( saveFromURLItem );
        popup.add( loadMenu );
        popup.addSeparator();
        popup.add( exitItem );
       
        trayIcon.setPopupMenu(popup);
        
//        trayIcon.addMouseListener( new MouseListener() {
//            @Override public void mouseClicked(MouseEvent me) {
//                System.out.println("plop");
//                reloadDeckList();
//            }
//
//            @Override public void mousePressed(MouseEvent me) {}
//            @Override public void mouseReleased(MouseEvent me) {}
//            @Override public void mouseEntered(MouseEvent me) {}
//            @Override public void mouseExited(MouseEvent me) {}
//        } );
        
        try {
            tray.add( trayIcon );
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
        }
    }
        
    public static void reloadDeckList(){
        loadMenu.removeAll();
        File deckFolder = new File( "Decks/" );
        for( File deckFile : deckFolder.listFiles() )
            addDeckToMenu( new Deck( deckFile ) );
    }
    
    public static void addDeckToMenu( final Deck deck ){
        MenuItem deckItem = new MenuItem( deck.title + " [" + deck.hero + "]");
        deckItem.addActionListener( new ActionListener() {
            @Override public void actionPerformed(ActionEvent ae) {
//                System.out.println("action blip");
                loadDeck( deck );
            }
        });
        loadMenu.add( deckItem );
    }
    
    public static void loadDeck( Deck deck ){
        try {
            Handler.loadDeck( deck );
        } catch( AWTException | InterruptedException | IOException e ){
            System.out.println( e.toString() );
        }
    }
    
    public static void saveDeck(){
        try {
            Handler.saveDeck();
        } catch( InterruptedException | IOException e ){
            System.out.println( e.toString() );
        }
    }
}
