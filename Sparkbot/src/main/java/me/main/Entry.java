package me.main;

@SuppressWarnings("unused")
public class Entry<K,V> {

	private K key;
	private V value;

	public Entry() {

	}

	public Entry(K key,V value) {
		this.key = key;
		this.value = value;
	}

	public V getValue() {
		return value;
	}

	public K getKey() {
		return key;
	}

	public void setValue(V value) {
		this.value = value;
	}

	public void setKey(K key) {
		this.key = key;
	}
}
