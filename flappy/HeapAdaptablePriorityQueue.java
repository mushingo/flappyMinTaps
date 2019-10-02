package flappy;
import java.util.*;


public class HeapAdaptablePriorityQueue<K,V> extends HeapPriorityQueue<K,V> {
	
	public HeapAdaptablePriorityQueue(Comparator<K> comp) {
		super(comp);
	}
	
	public HeapAdaptablePriorityQueue() {
		super();
	}

	protected AdaptableEntry<K,V> validate(Entry<K,V> entry) throws Exception{
		AdaptableEntry<K,V> locator = (AdaptableEntry<K,V>) entry;
		int j = locator.getIndex();
		if (j >= heap.size() || heap.get(j) != locator)
			throw new Exception();
		return locator;
	}
	
	protected void swap(int i, int j) {
		super.swap(i, j);
		((AdaptableEntry<K,V>) heap.get(i)).setIndex(i);
		((AdaptableEntry<K,V>) heap.get(j)).setIndex(j);
	}
	
	protected void bubble(int j) {
		if (j > 0 && compare(heap.get(j), heap.get(parent(j))) < 0)
			upheap(j);
		else
			downheap(j);				
	}
	
	public Entry<K,V> insert(K key, V value) {
		Entry<K,V> newest = new AdaptableEntry<>(key, value, heap.size());
		heap.add(newest);
		upheap(heap.size() - 1);
		return newest;
	}
	
	public void remove(Entry<K,V> entry) throws Exception {
		AdaptableEntry<K,V> locator = validate(entry);
		int j = locator.getIndex();
		if (j == heap.size() - 1) 
			heap.remove(heap.size() - 1);
		else {
			swap(j, heap.size() - 1);
			heap.remove(heap.size() - 1);
			bubble(j);
		}
	}
	
	public void replaceKey(Entry<K,V> entry, K key) throws Exception {
		AdaptableEntry<K,V> locator = validate(entry);
		locator.setKey(key);
		bubble(locator.getIndex());
	}
	
	public void replaceValue(Entry<K,V> entry, V value) throws Exception {
		AdaptableEntry<K,V> locator = validate(entry);
		locator.setValue(value);
	}
	

}
