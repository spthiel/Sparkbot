package me.main;

public class Triplet<K,V,V2> extends Entry<K,V> {

	private V2 value2;

	public Triplet() {
		super();
	}

	public Triplet(K key, V value, V2 value2) {
		super(key,value);
		this.value2 = value2;
	}

	public void setValue2(V2 value2) {
		this.value2 = value2;
	}

	public V2 getValue2() {
		return value2;
	}
}
