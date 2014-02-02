package hearthdecks;

import static hearthdecks.Handler.getClosestClass;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.awt.*;
import net.sourceforge.tess4j.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import org.imgscalr.Scalr;

public class HearthDecks {
    
//    public static void main(String[] args) throws IOException {
//        BufferedImage bI = ImageIO.read( new File("capt2.png") );
    
    public static Deck getDeck( BufferedImage bI ) throws IOException {
        String hero = getClosestClass( bI );
        
        List<Integer[]> lineCuts = cutLines(bI);
//        ImageIO.write(bI, "png", new File("a2.png"));
        Tesseract instance = Tesseract.getInstance();
        
        String title = null;
        List<Card> cards = new ArrayList<>();
        
        int i=0;
        for( Integer[] lineCut : lineCuts ){
            BufferedImage sub = bI.getSubimage( 0, lineCut[0]-4, bI.getWidth(), lineCut[1]+8 );
            BufferedImage countImg = getCountImage( sub );
            keepGreyIsh(sub);
            sub = resize( sub, 4 );

//            ImageIO.write(sub, "png", new File("test" + (i) + ".png"));
//            ImageIO.write(countImg, "png", new File("test" + (i) + "b.png"));
            try {
                if( i==0 )
                    instance.setTessVariable("tessedit_char_whitelist", "");
                else
                    instance.setTessVariable("tessedit_char_whitelist", "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ'-:7!.");
                instance.setPageSegMode(3);
                
//                String result = instance.doOCR( new File("test" + i + ".png") );
                String result = instance.doOCR( sub );
//                System.out.println("@ " + result);
//                String result = instance.doOCR( sub );
                
                String rs = result.trim();
                if( rs.isEmpty() )
                    continue;
                
//                String[] s = result.split("\n");
//                String rs = "";
//                for(String tmp : s){
////                    System.out.println("djzaiom    " + tmp);
//                    if( !tmp.trim().isEmpty() && tmp.length() > rs.length() )
//                        rs = tmp;
//                }
    
                if( i==0 )
                    title = rs;
                else {
                    String cardTitle = closestCard(rs);
//                    System.out.print( rs + " -> " + closestCard(rs) );

                    instance.setTessVariable("tessedit_char_whitelist", "");
                    instance.setPageSegMode(6);
//                    instance.setPageSegMode(10);

                    countImg = getMostRightChar( countImg );
                    if( countImg == null )
                        result = "";
                    else {
                        countImg = resize(countImg, 4);
//                        ImageIO.write(countImg, "png", new File("test" + (i) + "c.png"));
                        result = instance.doOCR( countImg );
                    }
//                    result = instance.doOCR( countImg );
//                    System.out.println(result);
                    if( result.contains( "2" ) )
                        cards.add(new Card(cardTitle, 2));
                    else
                        cards.add(new Card(cardTitle, 1));
                }
                
            } catch (TesseractException e) {
                System.err.println(e.getMessage());
            }
            
            i++;
        }
        
        return new Deck( hero, title, cards );
    }
    
    private static BufferedImage getMostRightChar( BufferedImage img ){
        boolean[] w = new boolean[ img.getWidth() ];
        for( int i=0 ; i<img.getWidth() ; i++ )
            for( int j=0 ; j<img.getHeight() ; j++ )
                if( img.getRGB(i, j) == 0xFFFFFFFF ){
                    w[i] = true;
                    break;
                }
        
        int end = w.length-1;
        while( end >= 0)
            if( w[end--] )
                break;
        int start = end;
        while( start >= 0 )
            if( !w[start--] )
                break;
        
        if( end - start < 3 )
            return null;
        
        start = Math.max(0, start - 1);
        end = Math.min(img.getWidth(), end + 3);
//        System.out.println(start + " -> " + end);
        return img.getSubimage(start, 0, end-start, img.getHeight());
    }

    private static BufferedImage resize( BufferedImage oI, int mult ){
 
        return Scalr.resize(oI, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_EXACT, oI.getWidth()*mult, oI.getHeight()*mult);
    }	
    
//    private static BufferedImage resize( BufferedImage oI, float mult ){
// 
//	BufferedImage rI = new BufferedImage( (int)mult*oI.getWidth(), (int)mult*oI.getHeight(), oI.getType() );
//        Graphics2D g = rI.createGraphics();
//	g.drawImage( oI, 0, 0, rI.getWidth(), rI.getHeight(), null);
//	g.dispose();
//	g.setComposite( AlphaComposite.Src );
// 
//	g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
//	g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
//	g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
// 
//	return rI;
//    }	
    
    private static List<Integer[]> cutLines( BufferedImage image ){
        boolean[] w = new boolean[ image.getHeight() ];
        int offset = image.getWidth() / 8;
        for( int j=0 ; j<image.getHeight() ; j++ )
            for( int i=offset ; i<2*offset ; i++ )
                if( isGreyIsh(image.getRGB(i, j), 50, 20 ) ){
                    w[j] = true;
                    break;
                }
        
        List<Integer[]> lineCuts = new ArrayList<>();
        int blackInRow = 0;     // whiteInRow huehue
        for( int i=0 ; i<w.length ; i++ )
            if( w[i] )
                blackInRow++;
            else {
                if( blackInRow > 5 )
                    lineCuts.add( new Integer[] { i - blackInRow, blackInRow - 1 } );
                blackInRow = 0;
            }
        
        if( blackInRow > 5 )
            lineCuts.add( new Integer[] { w.length - blackInRow, blackInRow - 1 } );
        
//        for( Integer[] j : lineCuts )
//            for( int j2=j[0]-4 ; j2<=j[0]+j[1]+4 ; j2++ )
//                for( int i=0 ; i<image.getWidth() ; i++ )
//                    image.setRGB(i, j2, fromRGB( new int[] {255,0,0,255} ) );
//
//        for( int i=0 ; i<w.length ; i++ )
//            if( w[i] )
//                image.setRGB(0, i, fromRGB( new int[] {255,0,255,0} ));
        
        return lineCuts;
    }
    
    
    private static int CIP = 4;
    private static BufferedImage getCountImage( BufferedImage img ){
        BufferedImage nImg = copyImage( img.getSubimage((CIP-1)*img.getWidth()/CIP, 0, img.getWidth()/CIP, img.getHeight()) );
        for( int i=0 ; i<nImg.getWidth() ; i++ )
            for( int j=0 ; j<nImg.getHeight() ; j++ )
                if( !isYellowIsh( nImg.getRGB(i, j) ) )
                    nImg.setRGB(i, j, fromRGB( new int[] {255,0,0,0} ) );
                else
                    nImg.setRGB(i, j, fromRGB( new int[] {255,255,255,255} ) );
        return nImg;
    }
    
    private static boolean isYellowIsh( int rgb ){
        int[] p = getRGB( rgb );
        if( p[1] > 100 && p[2] > 100 && p[3] < 50 )
            return true;
        return false;
    }
    
    private static void keepGreyIsh( BufferedImage image ){
        for( int i=0 ; i<image.getWidth() ; i++ )
            for( int j=0 ; j<image.getHeight() ; j++ )
                if( !isGreyIsh(image.getRGB(i, j), 150, 5 ) )
                    image.setRGB(i, j, fromRGB( new int[] {255,0,0,0} ) );
    }
    
    public static int[] getRGB( int rgb ){
        int alpha = (rgb >> 24) & 0xFF;
        int red =   (rgb >> 16) & 0xFF;
        int green = (rgb >>  8) & 0xFF;
        int blue =  (rgb      ) & 0xFF;
        return new int[] { alpha, red, green, blue };
    }
    
    private static int fromRGB( int[] p ){
        int rgb = p[0];
        for( int i=1 ; i<4 ; i++ )
            rgb = (rgb << 8) + p[i];
        return rgb;
    }
    
    private static boolean isGreyIsh( int rgb, int threshold, int threshold2 ){
        int[] p = getRGB( rgb );
        
        float average = (p[1] + p[2] + p[3]) / 3;
        
        if( average < 255 - threshold )
            return false;
        
        for( int i=1 ; i<4 ; i++ )
            if( Math.abs( p[i] - average ) > threshold2 )
                return false;
        
        return true;
    }
    
    private static boolean isWhiteIsh( int rgb, int threshold ){
        int[] p = getRGB( rgb );
        float average = (p[1] + p[2] + p[3]) / 3;
        if( average > 255 - threshold )
            return true;
        return false;
    }
    
    public static BufferedImage copyImage(BufferedImage source){
        BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics g = b.getGraphics();
        g.drawImage(source, 0, 0, null);
        g.dispose();
        return b;
    }
    
    private static int levenshtein(String a, String b) {
        a = a.toLowerCase();
        b = b.toLowerCase();
        // i == 0
        int [] costs = new int [b.length() + 1];
        for (int j = 0; j < costs.length; j++)
            costs[j] = j;
        for (int i = 1; i <= a.length(); i++) {
            // j == 0; nw = lev(i - 1, j)
            costs[0] = i;
            int nw = i - 1;
            for (int j = 1; j <= b.length(); j++) {
                int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]), a.charAt(i - 1) == b.charAt(j - 1) ? nw : nw + 1);
                nw = costs[j];
                costs[j] = cj;
            }
        }
        return costs[b.length()];
    }
    
    private static String closestCard( String s ){
        int min = Integer.MAX_VALUE;
        String bCard = null;
        for( String card : cardList ){
            int lev = levenshtein(s, card);
            if( min > lev ){
                min = lev;
                bCard = card;
            }
        }
        
        return bCard;
    }
    
    public static String[] cardList = new String[] {
	"Unleash the Hounds",
	"Pyroblast",
	"Blood Imp",
	"Charge",
	"Warsong Commander",
	"Sylvanas Windrunner",
	"Defender of Argus",
	"Abusive Sergeant",
	"Dark Iron Dwarf",
	"Novice Engineer",
	"Blizzard",
	"Cone of Cold",
	"Frost Nova",
	"Starving Buzzard",
	"Mind Control",
	"Flame Imp",
	"Argent Commander",
	"Shattered Sun Cleric",
	"Battle Rage",
	"Twilight Drake",
	"Voidwalker",
	"Lord Jaraxxus",
	"Cenarius",
	"Ancient of Lore",
	"Ancient of War",
	"Force of Nature",
	"Bite",
	"Keeper of the Grove",
	"Nourish",
	"Savagery",
	"Starfall",
	"Druid of the Claw",
	"Mark of Nature",
	"Naturalize",
	"Power of the Wild",
	"Soul of the Forest",
	"Wrath",
	"Ironbark Protector",
	"Moonfire",
	"Savage Roar",
	"Starfire",
	"Swipe",
	"Claw",
	"Innervate",
	"Mark of the Wild",
	"Wild Growth",
	"Healing Touch",
	"King Krush",
	"Bestial Wrath",
	"Gladiator's Longbow",
	"Snake Trap",
	"Eaglehorn Bow",
	"Explosive Shot",
	"Flare",
	"Misdirection",
	"Savannah Highmane",
	"Deadly Shot",
	"Explosive Trap",
	"Freezing Trap",
	"Scavenging Hyena",
	"Snipe",
	"Animal Companion",
	"Hunter's Mark",
	"Kill Command",
	"Tundra Rhino",
	"Arcane Shot",
	"Houndmaster",
	"Multi-Shot",
	"Timber Wolf",
	"Tracking",
	"Archmage Antonidas",
	"Ice Block",
	"Spellbender",
	"Counterspell",
	"Ethereal Arcanist",
	"Kirin Tor Mage",
	"Vaporize",
	"Ice Barrier",
	"Ice Lance",
	"Mana Wyrm",
	"Mirror Entity",
	"Sorcerer's Apprentice",
	"Flamestrike",
	"Frostbolt",
	"Mirror Image",
	"Water Elemental",
	"Arcane Explosion",
	"Arcane Intellect",
	"Arcane Missiles",
	"Fireball",
	"Polymorph",
	"Tirion Fordring",
	"Avenging Wrath",
	"Lay on Hands",
	"Sword of Justice",
	"Aldor Peacekeeper",
	"Blessed Champion",
	"Divine Favor",
	"Equality",
	"Holy Wrath",
	"Argent Protector",
	"Blessing of Wisdon",
	"Eye for an Eye",
	"Noble Sacrifice",
	"Redemption",
	"Repentance",
	"Blessing of Kings",
	"Consecration",
	"Guardian of Kings",
	"Humility",
	"Truesilver Champion",
	"Blessing of Might",
	"Hammer of Wrath",
	"Hand of Protection",
	"Holy Light",
	"Light's Justice",
	"Prophet Velen",
	"Cabal Shadow Priest",
	"Mindgames",
	"Shadowform",
	"Auchenai Soulpriest",
	"Holy Fire",
	"Lightwell",
	"Mass Dispel",
	"Shadow Madness",
	"Circle of Healing",
	"Inner Fire",
	"Lightspawn",
	"Silence",
	"Temple Enforcer",
	"Thoughtsteal",
	"Divine Spirit",
	"Holy Nova",
	"Mind Vision",
	"Shadow Word: Death",
	"Holy Smite",
	"Mind Blast",
	"Northshire Cleric",
	"Power Word: Shield",
	"Shadow Word: Pain",
	"Edwin VanCleef",
	"Kidnapper",
	"Patient Assassin",
	"Preparation",
	"Blade Flurry",
	"Headcrack",
	"Master of Disguise",
	"Perdition's Blade",
	"SI:7 Agent",
	"Betrayal",
	"Cold Blood",
	"Conceal",
	"Defias Ringleader",
	"Eviscerate",
	"Shadowstep",
	"Assassin's Blade",
	"Fan of Knives",
	"Shiv",
	"Sprint",
	"Vanish",
	"Assassinate",
	"Backstab",
	"Deadly Poison",
	"Sap",
	"Sinister Strike",
	"Al'Akir the Windlord",
	"Doomhammer",
	"Earth Elemental",
	"Far Sight",
	"Ancestral Spirit",
	"Feral Spirit",
	"Lava Burst",
	"Lightning Storm",
	"Mana Tide Totem",
	"Dust Devil",
	"Earth Shock",
	"Forked Lightning",
	"Lightning Bolt",
	"Stormforged Axe",
	"Unbound Elemental",
	"Bloodlust",
	"Fire Elemental",
	"Flametongue Totem",
	"Totemic Might",
	"Windspeaker",
	"Ancestral Healing",
	"Frost Shock",
	"Hex",
	"Rockbiter Weapon",
	"Windfury",
	"Bane of Doom",
	"Pit Lord",
	"Twisting Nether",
	"Doomguard",
	"Felguard",
	"Shadowflame",
	"Siphon Soul",
	"Void Terror",
	"Demonfire",
	"Power Overwhelming",
	"Sense Demons",
	"Summoning Portal",
	"Corruption",
	"Dred Infernal",
	"Mortal Coil",
	"Sacrificial Pact",
	"Soulfire",
	"Drain Life",
	"Hellfire",
	"Shadow Bolt",
	"Succubus",
	"Grommash Hellscream",
	"Brawl",
	"Gorehowl",
	"Shield Slam",
	"Armorsmith",
	"Commanding Shout",
	"Frothing Berserker",
	"Mortal Strike",
	"Upgrade!",
	"Arathi Weaponsmith",
	"Cruel Taskmaster",
	"Inner Rage",
	"Rampage",
	"Slam",
	"Arcanite Reaper",
	"Cleave",
	"Kor'kron Elite",
	"Shield Block",
	"Whirlwind",
	"Execute",
	"Fiery War Axe",
	"Heroic Strike",
	"Alexstrasza",
	"Baron Geddon",
	"Bloodmage Thalnos",
	"Cairne Bloodhoof",
	"Captain Greenskin",
	"Deathwing",
	"Gruul",
	"Harrison Jones",
	"Hogger",
	"Illidan Stormrage",
	"King Mukla",
	"Leeroy Jenkins",
	"Lorewalker Cho",
	"Malygos",
	"Millhouse Manastorm",
	"Nat Pagle",
	"Nozdormu",
	"Old Murk-Eye",
	"Onyxia",
	"Ragnaros the Firelord",
	"The Beast",
	"The Black Knight",
	"Tinkmaster Overspark",
	"Ysera",
	"Big Game Hunter",
	"Blood Knight",
	"Captain's Parrot",
	"Doomsayer",
	"Faceless Manipulator",
	"Hungry Crab",
	"Molten Giant",
	"Mountain Giant",
	"Murloc Warleader",
	"Southsea Captain",
	"Sea Giant",
	"Abomination",
	"Alarm-o-Bot",
	"Ancient Mage",
	"Ancient Watcher",
	"Angry Chicken",
	"Arcane Golem",
	"Azure Drake",
	"Bloodsail Corsair",
	"Coldlight Oracle",
	"Coldlight Seer",
	"Crazed Alchemist",
	"Demolisher",
	"Emperor Cobra",
	"Gadgetzan Auctioneer",
	"Imp Master",
	"Injured Blademaster",
	"Knife Juggler",
	"Lightwarden",
	"Mana Addict",
	"Mana Wraith",
	"Master Swordsmith",
	"Mind Control Tech",
	"Murloc Tidecaller",
	"Pint-Sized Summoner",
	"Questing Adventurer",
	"Ravenholdt Assassin",
	"Secretkeeper",
	"Stampeding Kodo",
	"Sunfury Protector",
	"Sunwalker",
	"Violet Teacher",
	"Wild Pyromancer",
	"Young Priestess",
	"Acolyte of Pain",
	"Amani Berserker",
	"Ancient Brewmaster",
	"Argent Squire",
	"Bloodsail Raider",
	"Cult Master",
	"Dire Wolf Alpha",
	"Dread Corsair",
	"Earthen Ring Farseer",
	"Faerie Dragon",
	"Fen Creeper",
	"Flesheating Ghoul",
	"Frost Elemental",
	"Harvest Golem",
	"Ironbeak Owl",
	"Jungle Panther",
	"Leper Gnome",
	"Loot Hoarder",
	"Mad Bomber",
	"Mogu'shan Warden",
	"Priestess of Elune",
	"Raging Worgen",
	"Scarlet Crusader",
	"Shieldbearer",
	"Silver Hand Knight",
	"Silvermoon Guardian",
	"Southsea Deckhand",
	"Spellbreaker",
	"Spiteful Smith",
	"Stranglethorn Tiger",
	"Tauren Warrior",
	"Thrallmar Farseer",
	"Venture Co. Mercenary",
	"Windfury Harpy",
	"Wisp",
	"Worgen Infiltrator",
	"Young Dragonhawk",
	"Youthful Brewmaster",
	"Acidic Swamp Ooze",
	"Archmage",
	"Bluegill Warrior",
	"Booty Bay Bodyguard",
	"Chillwind Yeti",
	"Core Hound",
	"Dalaran Mage",
	"Darkscale Healer",
	"Dragonling Mechanic",
	"Elven Archer",
	"Frostwolf Grunt",
	"Frostwolf Warlord",
	"Gnomish Inventor",
	"Goldshire Footman",
	"Grimscale Oracle",
	"Gurubashi Berskerker",
	"Ironforge Rifleman",
	"Ironfur Grizzly",
	"Kobold Geomancer",
	"Lord of the Arena",
	"Murloc Tidehunter",
	"Ogre Magi",
	"Razorfen Hunter",
	"Silverback Patriarch",
	"Stormspike Commando",
	"Stormwind Champion",
	"Stormwind Knight",
	"War Golem",
	"Bloodfen Raptor",
	"Boulderfist Ogre",
	"Magma Rager",
	"Murloc Raider",
	"Nightblade",
	"Oasis Snapjaw",
	"Raid Leader",
	"Reckless Rocketeer",
	"River Crocolisk",
	"Sen'jin Shieldmasta",
	"Stonetusk Boar",
	"Voodoo Doctor",
	"Wolfrider",
	"Elite Tauren Chieftain",
	"Gelbin Mekkatorque"
    };
}
