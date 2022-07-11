package franz;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Pair<K, V> {
    Pair(K key){
        this.key = key;
    }
    private K key;
    private V value;
}
