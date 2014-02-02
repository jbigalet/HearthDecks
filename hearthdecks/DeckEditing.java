package hearthdecks;

import static hearthdecks.Handler.classPix;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class DeckEditing extends javax.swing.JFrame {

    public static void main(String[] args){
        new DeckEditing( new Deck( new File( "Decks/deck1.txt" ) ) ).setVisible( true );
    }
    
    public Deck deck;
    public static String[] cardList;
    public static String[] classList;
    
    public DeckEditing( Deck deck ) {
        cardList = Arrays.copyOf( HearthDecks.cardList, HearthDecks.cardList.length );
        Arrays.sort( cardList );

        classList = new String[ Handler.classPix.size() ];
        Handler.classPix.keySet().toArray( classList );
        Arrays.sort( classList );
        
        initComponents();
        jtfTitle.setText( deck.title );
        setClassListComboBox( deck.hero );
        
        jspCards.setLayout( new BoxLayout(jspCards, BoxLayout.PAGE_AXIS) );
        for( Card card : deck.cards )
            jspCards.add( createCardPanel( card ) );
        
        this.pack();
    }
    
    private JPanel createCardPanel( Card card ){
        final JPanel jpanel = new JPanel();
        jpanel.setLayout( new BoxLayout(jpanel, BoxLayout.LINE_AXIS) );
        
        jpanel.add( createCardListComboBox( card.title) );
        jpanel.add( new JCheckBox( "x2", card.count == 2 ) );
        
        JButton jButton = new JButton("X");
        jButton.addActionListener( new ActionListener() {
            @Override public void actionPerformed(ActionEvent ae) {
                jspCards.remove( jpanel );
                jspCards.revalidate();
            }
        });
        jpanel.add( jButton );
        
        return jpanel;
    }
    
    private JComboBox<String> createCardListComboBox( String card ){
        JComboBox<String> comboBox = new JComboBox<>( cardList );
        comboBox.setSelectedItem( card );
        return comboBox;
    }
    
    private void setClassListComboBox( String c ){
        jcbClass.setModel( new DefaultComboBoxModel( classList ) );
        jcbClass.setSelectedItem( c );
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jcbClass = new javax.swing.JComboBox();
        jtfTitle = new javax.swing.JTextField();
        jspCards = new javax.swing.JPanel();
        jButtonAdd = new javax.swing.JButton();
        jButtonSave = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Title:");

        jLabel2.setText("Class:");

        jtfTitle.setText("Title");
        jtfTitle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfTitleActionPerformed(evt);
            }
        });

        jspCards.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout jspCardsLayout = new javax.swing.GroupLayout(jspCards);
        jspCards.setLayout(jspCardsLayout);
        jspCardsLayout.setHorizontalGroup(
            jspCardsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jspCardsLayout.setVerticalGroup(
            jspCardsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 189, Short.MAX_VALUE)
        );

        jButtonAdd.setText("Add");
        jButtonAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddActionPerformed(evt);
            }
        });

        jButtonSave.setText("Save");
        jButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jspCards, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jcbClass, 0, 214, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jtfTitle)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonSave))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButtonAdd)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtfTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonSave))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcbClass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jspCards, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonAdd)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jtfTitleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfTitleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtfTitleActionPerformed

    private void jButtonAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddActionPerformed
        jspCards.add( createCardPanel( new Card(cardList[0], 1) ) );
        this.pack();
    }//GEN-LAST:event_jButtonAddActionPerformed

    private static final char[] ILLEGAL_CHARACTERS = {'/', '`', '?', '*', '\\', '<', '>', '|', '\"', ':'};
    
    private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveActionPerformed
        Component[] comps = jspCards.getComponents();
        List<Card> cards = new ArrayList<>();
        for( Component comp : comps )
            if( comp instanceof JPanel ){
                String cardName = null;
                int cardCount = -1;
                JPanel jpanel = (JPanel) comp;
                Component[] pcomps = jpanel.getComponents();
                for( Component pcomp : pcomps )
                    if( pcomp instanceof JComboBox )
                        cardName = (String) ((JComboBox) pcomp).getSelectedItem();
                    else if( pcomp instanceof JCheckBox )
                        cardCount = ( (JCheckBox) pcomp ).isSelected() ? 2 : 1;
                
                if( cardCount != -1 && cardName != null )
                    cards.add( new Card( cardName, cardCount ) );
            }
        Deck d = new Deck((String) jcbClass.getSelectedItem(), jtfTitle.getText(), cards);
        
//        String deckName;
//        do {
//            deckName = "Decks/deck" + (int)(Math.random()*10000) + ".txt";
//        } while( new File( deckName ).exists() );
        
        String deckFullName = d.hero + "_" + d.title;
        for( char c : ILLEGAL_CHARACTERS )
            deckFullName = deckFullName.replace( c + "", "" );  // Meh
        
        String deckName = "Decks/" + deckFullName + ".txt";
        while( new File( deckName ).exists() )
            deckName = "Decks/" + deckFullName + (int)(Math.random()*10000) + ".txt";
        
        d.save( deckName );
        
        this.dispose();
    }//GEN-LAST:event_jButtonSaveActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAdd;
    private javax.swing.JButton jButtonSave;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JComboBox jcbClass;
    private javax.swing.JPanel jspCards;
    private javax.swing.JTextField jtfTitle;
    // End of variables declaration//GEN-END:variables
}
