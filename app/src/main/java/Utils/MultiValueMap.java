package Utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by MarkNguyen on 5/28/16.
 */
public class MultiValueMap<K,V> {
    private final Map<K,Set<V>> mappings = new TreeMap<>();

    public Set<V> getValues(K key)
    {
        return mappings.get(key);
    }

    public Boolean putValue(K key, V value)
    {
        Set<V> target = mappings.get(key);

        if(target == null)
        {
            target = new HashSet<V>();
            mappings.put(key,target);
        }
        return target.add(value);
    }

    /**
     * get data list
     * @return
     */
    public Map<K, List<V>> getData() {
        Map<K, List<V>> m2 = new TreeMap<>();
        Iterator<K> iter = mappings.keySet().iterator();
        while (iter.hasNext()) {
            K key = iter.next();
            Set<V> value = mappings.get(key);
            m2.put(key, new ArrayList<V>(value));
        }
        return m2;
    }

}