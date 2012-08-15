/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package playlist;

/**
 *
 * @author corpix
 */
public class Pair<A, B> {
    A first = null;
    B second = null;

    Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    public A getFirst() {
        return first;
    }

    public Pair setFirst(A first) {
        this.first = first;
        
        return this;
    }

    public B getSecond() {
        return second;
    }

    public Pair setSecond(B second) {
        this.second = second;
        
        return this;
    }

}