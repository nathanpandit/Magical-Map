public class Tuple<Type1, Type2> {
    Type1 first;
    Type2 second;

    public Tuple(Type1 first, Type2 second) {
        this.first = first;
        this.second = second;
    }
}

//a simple tuple that can have two elements of distinct types.