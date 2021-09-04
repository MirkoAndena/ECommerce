package ecommerce_ria.utils;

import java.util.List;
import java.util.function.Predicate;

public class ListUtils {
	
	/**
	 * Cerca un elemento nella lista utilizzando il predicato passato, ritorna null se non trova nulla
	 * @param <T>
	 * @param list
	 * @param predicate
	 * @return
	 */
	public static <T> T find(List<T> list, Predicate<T> predicate) {
		for (T item : list)
			if (predicate.test(item))
				return item;
		return null;
	}
}
