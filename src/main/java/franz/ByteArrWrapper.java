package franz;

import java.util.Arrays;

public class ByteArrWrapper {
    private final byte[] arr;

    ByteArrWrapper(byte[] arr) {
        this.arr = arr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ByteArrWrapper that = (ByteArrWrapper) o;
        return Arrays.equals(arr, that.arr);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(arr);
    }

    @Override
    public String toString() {
        return "ByteArrWrapper{" +
                "arr=" + Arrays.toString(arr) +
                '}';
    }
}
