import java.util.Random;
/**@assignment lab 4
 * @author Sebastian Sandberg & Erik Risfelt
 * @groupnumber Labbgrupp 55
 */
public class Verktyg{
	private static boolean isEmpty = false;
	private static int adress;
	/**
	 * A method that randomizes the fields in an array.
	 * @param array to be randomized
	 */
	public static void slumpOrdning(Object[] parameter){
		Object[] placeholder = new Object[parameter.length];
		Random randomizer = new Random();
		for ( int i = 0; i < placeholder.length; i++){
			isEmpty = false;
			while ( !isEmpty){
				adress = randomizer.nextInt(placeholder.length);
				if ( placeholder[adress] == null) {
					placeholder[adress] = parameter[i];
					isEmpty = true;
				}
			}
		}
		for ( int j = 0; j < parameter.length; j++){
			parameter[j] = placeholder[j];
		}
	}
}