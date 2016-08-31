package Assignment01;

/**
 * @author Shane Birdsall
 * Student ID: 14870204
 */
public interface RandomObtainable<E> {
	E getRandom();
	boolean removeRandom();
	boolean insertRandom(E element);
}
