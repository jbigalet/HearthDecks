package hearthdecks;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Deck {
    
    String hero;
    String title;
    List<Card> cards;

    public Deck( String hero, String title, List<Card> cards ) {
        this.hero = hero;
        this.title = title;
        this.cards = cards;
    }
    
    
    public Deck( File file ){
        this.cards = new ArrayList<>();
        
        try( BufferedReader br = new BufferedReader(new FileReader( file )) ){
            this.hero = br.readLine();
            this.title = br.readLine();
            
            String line;
            while( (line = br.readLine()) != null)
                cards.add( new Card(line, Integer.parseInt( br.readLine() )) );
            
        } catch ( IOException ex ) {
            System.out.println( ex.toString() );
        }
    }
    
    
    public void save( String fname ){
        try( PrintWriter pw = new PrintWriter( fname ) ){
            pw.println( hero );
            pw.println( title );
            for( Card card : cards ){
                pw.println( card.title );
                pw.println( card.count );
            }
            
            SysTray.reloadDeckList();

        } catch (FileNotFoundException ex) {
            System.out.println( ex.toString() );
        }
    }

    
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(" ### ").append(title).append(" [").append(hero).append("] ###\n");
        for( Card c : cards )
            s.append( c.toString() ).append("\n");
        return s.toString();
    }
}
