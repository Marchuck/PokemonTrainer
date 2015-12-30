package pl.lukmarr.pokemontrainer.utils;

/**
 * Created by Łukasz Marczak
 *
 * @since 29.12.15
 */
public class Triple<A,B,C> {
    public A first;
    public B second;
    public C third;

    public Triple(A first, B second, C third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }
}
