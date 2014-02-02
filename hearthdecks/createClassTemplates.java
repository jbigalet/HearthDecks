package hearthdecks;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class createClassTemplates {
    
    public static void main( String[] args ) throws IOException {
        
        System.out.println("public static final Map<String, int[]> classPix = new HashMap<String, int[]>();");
        System.out.println("static {");
        File folder = new File(".");
        for( File file : folder.listFiles() )
            if( file.getName().matches( ".*class.*.png" ) ){
                String name = file.getName().substring( 6, file.getName().length()-4 );
                
                BufferedImage image = ImageIO.read( file );
                int[] av = Handler.getClassAverage( image );
                
                System.out.println("\tclassPix.put(\"" + name 
                        + "\", new int[] { " + av[0] + ", " + av[1] + ", " + av[2] + " } );");                
//                System.out.println(  );
            }
        
        
        System.out.println("}");
        
    }
    
}
