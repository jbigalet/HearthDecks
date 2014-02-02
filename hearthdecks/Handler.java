package hearthdecks;

import jna.extra.GDI32Extra;
import jna.extra.User32Extra;
import jna.extra.WinGDIExtra;

import java.awt.image.BufferedImage;
import com.sun.jna.Memory;
import com.sun.jna.platform.win32.GDI32;
import com.sun.jna.platform.win32.WinDef.HBITMAP;
import com.sun.jna.platform.win32.WinDef.HDC;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.RECT;
import com.sun.jna.platform.win32.WinGDI;
import com.sun.jna.platform.win32.WinGDI.BITMAPINFO;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import static java.awt.event.KeyEvent.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Handler {
    
    public static Robot robot;
    static {
        try {
            robot = new Robot();
        } catch( AWTException e ){
            System.out.println( e.toString() );
        }
    }
    
    public static WinDef.RECT rect = new WinDef.RECT();
    public static int height = 0;
    public static int widthDif = 0;
    
    public static void main(String[] args) throws IOException, InterruptedException, AWTException {

        WinDef.HWND hWnd = null;
        while( ( hWnd = User32.INSTANCE.FindWindow(null, "Hearthstone") ) == null )
            Thread.sleep( 1000 );
        
        System.out.println("Hearthstone window found");
        
        User32.INSTANCE.SetForegroundWindow( hWnd );
        
//        WinDef.RECT wRect = new WinDef.RECT();
//        WinDef.RECT cRect = new WinDef.RECT();
        User32.INSTANCE.GetWindowRect( hWnd, rect );
//        User32Extra.INSTANCE.GetClientRect( hWnd, cRect );
        
//        System.out.println( cRect );
//        rect.top = wRect.top + cRect.top;
//        rect.left = wRect.left + cRect.left;
        
        BufferedImage capt = capture( hWnd );
        ImageIO.write( capt , "png",  new File( "capt.png" ) );
        
        int width = capt.getWidth();
        height = capt.getHeight();
        int realWidth = Math.min( width, (int) height * 4 / 3 );
        widthDif = (width - realWidth) / 2;
        int start = (int)( realWidth * .7656 );
        int newWidth = (int)( realWidth * .1875 );
        int newHeight = (int) ( height * .9 );
        
        BufferedImage subCapt = capt.getSubimage( widthDif + start, 0, newWidth, newHeight );
//        ImageIO.write( subCapt , "png",  new File( "capt" + width + "x" + height + ".png" ) );
        ImageIO.write( subCapt , "png",  new File( "aaa.png" ) );
        
        Deck deck = HearthDecks.getDeck( subCapt );
        System.out.println( deck );
        
//        deck.save( "Decks/deck1.txt" );
        
        Deck dTest = new Deck( new File( "Decks/deck1.txt" ));
        System.out.println( dTest );
        
        int[] cOffset = classOffsets.get( dTest.hero );
        int selectClassX = 202 + cOffset[0]*212;
        int selectClassY = 254 + cOffset[1]*200;
        
        mouseClick(selectClassX, selectClassY);
        Thread.sleep( 100 );
        mouseClick(1035, 793); // Choose button
        Thread.sleep( 1000 );
        for( Card card : dTest.cards ){
            mouseClick(719, 879); // Select input area while clicking the red cross if any
            Thread.sleep( 200 );
            mouseClick(719, 879); // Select input area while clicking the red cross if any
            Thread.sleep( 200 );

            type( card.title );
            Thread.sleep( 100 );
            keyClick( KeyEvent.VK_ENTER );
            Thread.sleep( 400 );
                    
            for( int i=1 ; i<=card.count ; i++ ){
                mouseClick(146, 311); // Click on the card
                Thread.sleep( 500 );
            }
        }        
    }
    
    public static void loadDeck( Deck deck ) throws AWTException, InterruptedException, IOException{
//        System.out.println("plop");
        
        WinDef.HWND hWnd = null;
        while( ( hWnd = User32.INSTANCE.FindWindow(null, "Hearthstone") ) == null )
            Thread.sleep( 1000 );
        
//        System.out.println("Hearthstone window found");
        
        User32.INSTANCE.SetForegroundWindow( hWnd );
        
//        WinDef.RECT wRect = new WinDef.RECT();
//        WinDef.RECT cRect = new WinDef.RECT();
        User32.INSTANCE.GetWindowRect( hWnd, rect );
//        User32Extra.INSTANCE.GetClientRect( hWnd, cRect );
        
//        System.out.println( cRect );
//        rect.top = wRect.top + cRect.top;
//        rect.left = wRect.left + cRect.left;
        
        BufferedImage capt = capture( hWnd );
//        ImageIO.write( capt , "png",  new File( "capt.png" ) );
        
        int width = capt.getWidth();
        height = capt.getHeight();
        int realWidth = Math.min( width, (int) height * 4 / 3 );
        widthDif = (width - realWidth) / 2;
        
        int[] cOffset = classOffsets.get( deck.hero );
        int selectClassX = 202 + cOffset[0]*212;
        int selectClassY = 254 + cOffset[1]*200;
        
        mouseClick(selectClassX, selectClassY);
        mediumPause();
        mouseClick(1035, 793); // Choose button
        longPause();
        longPause();
        mouseClick(1056, 64); // Title
        softPause();
        type( deck.title );
        softPause();
        keyClick( KeyEvent.VK_ENTER );
        mediumPause();
        
        for( Card card : deck.cards ){
            softPause();
            mouseClick(719, 879); // Select input area while clicking the red cross if any
            softPause();
            mouseClick(700, 879); // Select input area while clicking the red cross if any
            softPause();

            type( card.title );
            softPause();
            keyClick( KeyEvent.VK_ENTER );
            softPause();
            
            if( card.count == 2 ){ // Click on golden spot (only if need 2: If need 1, maybe 1 golden + 1 normal)
                mouseClick(361, 311); // Click on the card
                softPause();
                mouseClick(361, 311); // Click on the card
                softPause();
            }

            for( int i=1 ; i<=card.count ; i++ ){
                mouseClick(146, 311); // Click on the card
                softPause();
            }
        }
        
//        System.out.println("plouf");
    }
    
    private static void softPause(){
        try {
            Thread.sleep( 50 );
        } catch (InterruptedException ex) {
            System.out.println( ex.toString() );
        }
    }

    private static void mediumPause(){
        try {
            Thread.sleep( 200 );
        } catch (InterruptedException ex) {
            System.out.println( ex.toString() );
        }
    }
    
    private static void longPause(){
        try {
            Thread.sleep( 1000 );
        } catch (InterruptedException ex) {
            System.out.println( ex.toString() );
        }
    }

    
    public static void saveDeck() throws InterruptedException, IOException {
        WinDef.HWND hWnd = null;
        while( ( hWnd = User32.INSTANCE.FindWindow(null, "Hearthstone") ) == null )
            Thread.sleep( 1000 );
        
//        User32.INSTANCE.SetForegroundWindow( hWnd );

//        System.out.println("Hearthstone window found");
        
        BufferedImage capt = capture( hWnd );
//        ImageIO.write( capt , "png",  new File( "capt.png" ) );
        
        int width = capt.getWidth();
        height = capt.getHeight();
        int realWidth = Math.min( width, (int) height * 4 / 3 );
        widthDif = (width - realWidth) / 2;
        int start = (int)( realWidth * .7656 );
        int newWidth = (int)( realWidth * .1875 );
        int newHeight = (int) ( height * .9 );
        
        BufferedImage subCapt = capt.getSubimage( widthDif + start, 0, newWidth, newHeight );
//        ImageIO.write( subCapt , "png",  new File( "capt" + width + "x" + height + ".png" ) );
//        ImageIO.write( subCapt , "png",  new File( "aaa.png" ) );
        
        Deck deck = HearthDecks.getDeck( subCapt );
//        System.out.println( deck );
        
        new DeckEditing( deck ).setVisible( true );
//        deck.save( "Decks/deck1.txt" );
    }
    
    public static void saveDeckFromFile( File file ) {
        try {
            BufferedImage subCapt = ImageIO.read( file );
            Deck deck = HearthDecks.getDeck( subCapt );
            new DeckEditing( deck ).setVisible( true );
            
        } catch (IOException ex) {
            System.out.println( ex.toString() );
        }
    }
    
    public static void saveDeckFromURL( String url ){
        if( url.contains( "hearthhead" )){
            try {
                Document doc = Jsoup.connect( url ).get();
                
                Elements titles = doc.select( "title" );
                String title =  titles.get(0).text().split(" - ")[0];
                
                Elements classes = doc.select( "meta[name*=description]" );
                String hero = classes.get(0).attr( "content" ).split(" ")[0];
                
                List<Card> cards = new ArrayList<>();
                Elements ele = doc.select( "a[data-id]" );
                for( Element e : ele )
                    if( !e.parent().text().contains(" - ") )    // avoid duplicate with notes
                        cards.add( new Card( e.text(), e.parent().text().contains(" x2") ? 2 : 1 ) );
                
                
                Deck deck = new Deck( hero, title, cards );
                new DeckEditing( deck ).setVisible( true );
                
            } catch (IOException ex) {
                System.out.println( ex.toString() );
            }
            
        } else if( url.contains( "hearthpwn" ) ){
            try {
                Document doc = Jsoup.connect( url ).get();
                
                Elements titles = doc.select( "h2.t-deck-title" );
                String title =  titles.get(0).text();
                
                Elements classes = doc.select( "a.special-link" );
                String hero = classes.get(0).attr( "href" );
                hero = hero.substring(13, hero.indexOf("#")+1 );
                hero = (hero.charAt(0) + "").toUpperCase() + hero.substring(1, hero.length() - 1 );
                
                List<Card> cards = new ArrayList<>();
                Elements ele = doc.select("table#cards").select("td.col-name");
                for( Element e : ele )
                        cards.add( new Card( e.text().substring(0, e.text().length()-4),
                                e.parent().text().contains(" × 2 ") ? 2 : 1 ) );
                
                Deck deck = new Deck( hero, title, cards );
                new DeckEditing( deck ).setVisible( true );
                
            } catch (IOException ex) {
                System.out.println( ex.toString() );
            }
            
        } else {
            System.out.println( "Website unknown" );
        }
    }

    public static void keyClick( int key ){
        robot.keyPress( key );
        robot.keyRelease( key );
    }
    
    public static void mouseClick( int x, int y ){
        robot.mouseMove(    9 + widthDif + rect.left + height * x / 960,
                            29 +            rect.top  + height * y / 960 );
        robot.mousePress( InputEvent.BUTTON1_MASK );
        robot.mouseRelease( InputEvent.BUTTON1_MASK );
    }
    
    public static String getClosestClass( BufferedImage image ){
        int[] av = getClassAverage( image );
        
        String minClass = null;
        int min = Integer.MAX_VALUE;
        for( Map.Entry<String, int[]> e : classPix.entrySet() ){
            int d = 0;
            for( int i=0 ; i<3 ; i++ ){
                int tmp = av[i] - e.getValue()[i];
                d += tmp * tmp;
            }
            if( min > d ){
                min = d;
                minClass = e.getKey();
            }
        }
        
        return minClass;
    }
    
    public static int[] getClassAverage( BufferedImage image ){
        int xStart = image.getWidth() * 76 / 240;
        int yStart = image.getWidth() * 29 / 240;
        int xSize = image.getWidth() * 117 / 240;
        int ySize = image.getWidth() * 17 / 240;

//        try {
//            BufferedImage towrite = image.getSubimage( xStart, yStart, xSize, ySize );
//            ImageIO.write( towrite , "png",  new File( "bbb.png" ) );
//        } catch (IOException ex) {
//            System.out.println( ex.toString() );
//        }
        
        int[] average = new int[3];
        int n = 0;
        for( int x=xStart ; x<=xStart+xSize ; x++ )
            for( int y=yStart ; y<=yStart+ySize ; y++ ){
                n++;
                int[] pix = HearthDecks.getRGB( image.getRGB(x, y) );
//                System.out.println( x + "x" + y + " : " + Arrays.toString( pix ) );
                for( int i=0 ; i<3 ; i++ )
                    average[i] += pix[i+1];
            }
        
        for( int i=0 ; i<3 ; i++ )
            average[i] /= n;
        
        return average;
    }
    
    public static final Map<String, int[]> classPix = new HashMap<>();
    static {
        classPix.put("Druid", new int[] { 132, 163, 159 } );
        classPix.put("Hunter", new int[] { 55, 53, 55 } );
        classPix.put("Mage", new int[] { 94, 98, 118 } );
        classPix.put("Paladin", new int[] { 110, 87, 87 } );
        classPix.put("Priest", new int[] { 167, 141, 136 } );
        classPix.put("Rogue", new int[] { 123, 65, 66 } );
        classPix.put("Shaman", new int[] { 49, 80, 73 } );
        classPix.put("Warlock", new int[] { 82, 42, 48 } );
        classPix.put("Warrior", new int[] { 135, 66, 26 } );
    }
    
    public static final Map<String, int[]> classOffsets = new HashMap<>();
    static {
        String[] order = new String[] {
            "Warrior", "Shaman", "Rogue", "Paladin", "Hunter", "Druid", "Warlock", "Mage", "Priest"
        };
        
        int a = 0;
        for( int i=0 ; i<3 ; i++ )
            for( int j=0 ; j<3 ; j++ )
                classOffsets.put(order[a++], new int[] {j, i});
    }
            
    // http://stackoverflow.com/questions/4433994/java-window-image
    public static BufferedImage capture(HWND hWnd) {

        HDC hdcWindow = User32.INSTANCE.GetDC(hWnd);
        HDC hdcMemDC = GDI32.INSTANCE.CreateCompatibleDC(hdcWindow);

        RECT bounds = new RECT();
        User32Extra.INSTANCE.GetClientRect(hWnd, bounds);

        int width = bounds.right - bounds.left;
        int height = bounds.bottom - bounds.top;

        HBITMAP hBitmap = GDI32.INSTANCE.CreateCompatibleBitmap(hdcWindow, width, height);

        HANDLE hOld = GDI32.INSTANCE.SelectObject(hdcMemDC, hBitmap);
        GDI32Extra.INSTANCE.BitBlt(hdcMemDC, 0, 0, width, height, hdcWindow, 0, 0, WinGDIExtra.SRCCOPY);

        GDI32.INSTANCE.SelectObject(hdcMemDC, hOld);
        GDI32.INSTANCE.DeleteDC(hdcMemDC);

        BITMAPINFO bmi = new BITMAPINFO();
        bmi.bmiHeader.biWidth = width;
        bmi.bmiHeader.biHeight = -height;
        bmi.bmiHeader.biPlanes = 1;
        bmi.bmiHeader.biBitCount = 32;
        bmi.bmiHeader.biCompression = WinGDI.BI_RGB;

        Memory buffer = new Memory(width * height * 4);
        GDI32.INSTANCE.GetDIBits(hdcWindow, hBitmap, 0, height, buffer, bmi, WinGDI.DIB_RGB_COLORS);

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        image.setRGB(0, 0, width, height, buffer.getIntArray(0, width * height), 0, width);

        GDI32.INSTANCE.DeleteObject(hBitmap);
        User32.INSTANCE.ReleaseDC(hWnd, hdcWindow);

        return image;

    }
    
    // http://stackoverflow.com/questions/1248510/convert-string-to-keyevents
    
    public static void type(CharSequence characters) {
        int length = characters.length();
        for (int i = 0; i < length; i++) {
            char character = characters.charAt(i);
            type(character);
        }
    }

    public static void type(char character) {
        switch (character) {
        case 'a': doType(VK_A); break;
        case 'b': doType(VK_B); break;
        case 'c': doType(VK_C); break;
        case 'd': doType(VK_D); break;
        case 'e': doType(VK_E); break;
        case 'f': doType(VK_F); break;
        case 'g': doType(VK_G); break;
        case 'h': doType(VK_H); break;
        case 'i': doType(VK_I); break;
        case 'j': doType(VK_J); break;
        case 'k': doType(VK_K); break;
        case 'l': doType(VK_L); break;
        case 'm': doType(VK_M); break;
        case 'n': doType(VK_N); break;
        case 'o': doType(VK_O); break;
        case 'p': doType(VK_P); break;
        case 'q': doType(VK_Q); break;
        case 'r': doType(VK_R); break;
        case 's': doType(VK_S); break;
        case 't': doType(VK_T); break;
        case 'u': doType(VK_U); break;
        case 'v': doType(VK_V); break;
        case 'w': doType(VK_W); break;
        case 'x': doType(VK_X); break;
        case 'y': doType(VK_Y); break;
        case 'z': doType(VK_Z); break;
        case 'A': doType(VK_SHIFT, VK_A); break;
        case 'B': doType(VK_SHIFT, VK_B); break;
        case 'C': doType(VK_SHIFT, VK_C); break;
        case 'D': doType(VK_SHIFT, VK_D); break;
        case 'E': doType(VK_SHIFT, VK_E); break;
        case 'F': doType(VK_SHIFT, VK_F); break;
        case 'G': doType(VK_SHIFT, VK_G); break;
        case 'H': doType(VK_SHIFT, VK_H); break;
        case 'I': doType(VK_SHIFT, VK_I); break;
        case 'J': doType(VK_SHIFT, VK_J); break;
        case 'K': doType(VK_SHIFT, VK_K); break;
        case 'L': doType(VK_SHIFT, VK_L); break;
        case 'M': doType(VK_SHIFT, VK_M); break;
        case 'N': doType(VK_SHIFT, VK_N); break;
        case 'O': doType(VK_SHIFT, VK_O); break;
        case 'P': doType(VK_SHIFT, VK_P); break;
        case 'Q': doType(VK_SHIFT, VK_Q); break;
        case 'R': doType(VK_SHIFT, VK_R); break;
        case 'S': doType(VK_SHIFT, VK_S); break;
        case 'T': doType(VK_SHIFT, VK_T); break;
        case 'U': doType(VK_SHIFT, VK_U); break;
        case 'V': doType(VK_SHIFT, VK_V); break;
        case 'W': doType(VK_SHIFT, VK_W); break;
        case 'X': doType(VK_SHIFT, VK_X); break;
        case 'Y': doType(VK_SHIFT, VK_Y); break;
        case 'Z': doType(VK_SHIFT, VK_Z); break;
        case '`': doType(VK_BACK_QUOTE); break;
        case '0': doType(VK_SHIFT, VK_0); break;
        case '1': doType(VK_SHIFT, VK_1); break;
        case '2': doType(VK_SHIFT, VK_2); break;
        case '3': doType(VK_SHIFT, VK_3); break;
        case '4': doType(VK_SHIFT, VK_4); break;
        case '5': doType(VK_SHIFT, VK_5); break;
        case '6': doType(VK_SHIFT, VK_6); break;
        case '7': doType(VK_SHIFT, VK_7); break;
        case '8': doType(VK_SHIFT, VK_8); break;
        case '9': doType(VK_SHIFT, VK_9); break;
        case '-': doType(VK_MINUS); break;
        case '=': doType(VK_EQUALS); break;
        case '~': doType(VK_SHIFT, VK_BACK_QUOTE); break;
        case '!': doType(VK_EXCLAMATION_MARK); break;
        case '@': doType(VK_AT); break;
        case '#': doType(VK_NUMBER_SIGN); break;
        case '$': doType(VK_DOLLAR); break;
        case '%': doType(VK_SHIFT, VK_5); break;
        case '^': doType(VK_CIRCUMFLEX); break;
        case '&': doType(VK_AMPERSAND); break;
        case '*': doType(VK_ASTERISK); break;
        case '(': doType(VK_5); break;
        case ')': doType(VK_RIGHT_PARENTHESIS); break;
        case '_': doType(VK_UNDERSCORE); break;
        case '+': doType(VK_PLUS); break;
        case '\t': doType(VK_TAB); break;
        case '\n': doType(VK_ENTER); break;
        case '[': doType(VK_ALT_GRAPH, VK_5); break;
        case ']': doType(VK_CLOSE_BRACKET); break;
        case '\\': doType(VK_BACK_SLASH); break;
        case '{': doType(VK_SHIFT, VK_OPEN_BRACKET); break;
        case '}': doType(VK_SHIFT, VK_CLOSE_BRACKET); break;
        case '|': doType(VK_SHIFT, VK_BACK_SLASH); break;
        case ';': doType(VK_SEMICOLON); break;
        case ':': doType(VK_COLON); break;
        case '\'': doType(VK_4); break;
        case '"': doType(VK_QUOTEDBL); break;
        case ',': doType(VK_COMMA); break;
        case '<': doType(VK_LESS); break;
        case '.': doType(VK_PERIOD); break;
        case '>': doType(VK_GREATER); break;
        case '/': doType(VK_SLASH); break;
        case '?': doType(VK_SHIFT, VK_SLASH); break;
        case ' ': doType(VK_SPACE); break;
        default:
            throw new IllegalArgumentException("Cannot type character " + character);
        }
    }

    private static void doType(int... keyCodes) {
        doType(keyCodes, 0, keyCodes.length);
    }

    private static void doType(int[] keyCodes, int offset, int length) {
        if (length == 0)
            return;

        robot.keyPress(keyCodes[offset]);
        doType(keyCodes, offset + 1, length - 1);
        robot.keyRelease(keyCodes[offset]);

//        try {
//            Thread.sleep( 100 );
//        } catch (InterruptedException ex) {
//            System.out.println( ex.toString() );
//        }
    }

}
