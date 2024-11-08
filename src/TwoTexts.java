//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;

public class TwoTexts implements WritableComparable<TwoTexts> {
    private Text firstText;
    private Text secondText;

    public TwoTexts() {
        this.firstText = new Text();
        this.secondText = new Text();
    }

    public TwoTexts(Text firstText, Text secondText) {
        this.set(firstText, secondText);
    }

    public void set(Text firstText, Text secondText) {
        this.firstText = firstText;
        this.secondText = secondText;
    }

    public Text getfirstText() {
        return this.firstText;
    }

    public Text getsecondText() {
        return this.secondText;
    }

    public void readFields(DataInput in) throws IOException {
        this.firstText.readFields(in);
        this.secondText.readFields(in);
    }

    public void write(DataOutput out) throws IOException {
        this.firstText.write(out);
        this.secondText.write(out);
    }

    public String toString() {
        String var10000 = String.valueOf(this.firstText);
        return var10000 + "\t" + String.valueOf(this.secondText);
    }

    public int compareTo(TwoTexts o) {
        int cmp = this.firstText.compareTo(o.firstText);
        return cmp != 0 ? cmp : this.secondText.compareTo(o.secondText);
    }
}