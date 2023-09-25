import java.util.ArrayList;
import java.util.Collections;

public class SortedArrayList<E extends Comparable<? super E>> extends ArrayList<E> {
	public SortedArrayList() {
		super();
	}

	public boolean add(E item) {
		int index = Collections.binarySearch(this, item);
		if (index < 0) {
			index = -index - 1;
		}
		super.add(index, item);
		return true;
	} 

	public E set(int index, E item) {
		E oldItem = this.remove(index);
		this.add(item);
		return oldItem;
	}

	public int indexOf(Object item) {
		try {
			int index = Collections.binarySearch(this, (E)item);
			if (index >= 0) {
				return index;
			}
			return -1;
		}
		catch (ClassCastException e) {
			return -1;
		}
	}
}
