package hearthdecks;

public class Card {
    
    String title;
    int count;

    public Card(String title, int count) {
        this.title = title;
        this.count = count;
    }

    @Override
    public String toString() {
        return title + " x" + count;
    }
}
