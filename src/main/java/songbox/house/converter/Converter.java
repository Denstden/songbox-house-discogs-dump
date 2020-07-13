package songbox.house.converter;

public interface Converter<D, E> {
    E convert(D dto);
}
