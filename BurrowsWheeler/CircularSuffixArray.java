import java.util.Arrays;

public class CircularSuffixArray {
    
    private final SharedString[] sharedStrings;
    
    // circular suffix array of s
    public CircularSuffixArray(String s) {
        char[] initialValue = s.toCharArray();
        char[] sharedValue = new char[s.length() * 2 - 1];
        for (int i = 0; i < sharedValue.length; i++) {
            sharedValue[i] = initialValue[i % initialValue.length];
        }
        
        sharedStrings = new SharedString[s.length()];
        for (int i = 0; i < sharedStrings.length; i++) {
            sharedStrings[i] = new SharedString(sharedValue, i, i + s.length());
        }
        
        Arrays.sort(sharedStrings);
        
    }
    
    // length of s
    public int length() {
        return sharedStrings.length;
    }
    
    // returns index of ith sorted suffix
    public int index(int i) {
        return sharedStrings[i].start;
    }
    
    private class SharedString implements Comparable<SharedString> {
        
        private char[] value;
        private int start;
        private int stop;
        
        public SharedString(char[] value, int start, int stop) {
            this.value = value;
            this.start = start;
            this.stop = stop;
        }
        
        @Override
        public int compareTo(SharedString that) {
            for (int i = this.start, j = that.start; i < this.stop && j < that.stop; i++, j++) {
                if (this.value[i] > that.value[j]) {
                    return 1;
                } else if (this.value[i] < that.value[j]) {
                    return -1;
                }
            }
            return 0;
        }
        
    }
}
