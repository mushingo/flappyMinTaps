package flappy;

public class AdaptableEntry<K,V> extends Entry<K,V> {
	private int index;
	public AdaptableEntry(K key, V value, int j) {
		super(key, value);
		this.index = j;
	}
	public int getIndex() { return index;}
	public void setIndex(int j) { index = j;}

}



