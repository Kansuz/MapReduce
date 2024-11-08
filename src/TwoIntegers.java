//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.hadoop.io.Writable;

public class TwoIntegers implements Writable {
    private long firstValue;
    private long secondValue;

    public TwoIntegers() {
    }

    public TwoIntegers(long firstValue, long secondValue) {
        this.set(firstValue, secondValue);
    }

    public void set(long firstValue, long secondValue) {
        this.firstValue = firstValue;
        this.secondValue = secondValue;
    }

    public long getFirstValue() {
        return this.firstValue;
    }

    public long getSecondValue() {
        return this.secondValue;
    }

    public void readFields(DataInput in) throws IOException {
        this.firstValue = in.readLong();
        this.secondValue = in.readLong();
    }

    public void write(DataOutput out) throws IOException {
        out.writeLong(this.firstValue);
        out.writeLong(this.secondValue);
    }

    public String toString() {
        return this.firstValue + "\t" + this.secondValue;
    }
}