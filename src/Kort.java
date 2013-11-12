import java.awt.*;
import javax.swing.*;
/**@assignment lab 4
 * @author Sebastian Sandberg & Erik Risfelt
 * @groupnumber Labbgrupp 55
 */
public class Kort extends JButton {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Icon icon;
	private Status visibility = Status.DOLT;

	public enum Status {
		DOLT, SYNLIGT, SAKNAS
	}

	// ========Constructors=============
	public Kort(Icon icon) {
		this(icon, Status.SAKNAS);
	}

	public Kort(Icon icon, Status status) {
		this.icon = icon;
		setOpaque(true);
		setStatus(status);
	}

	// =====Methods======

	/**
	 * Set the status of the object Kort
	 * 
	 * @param newStatus
	 *            the status to be set.
	 */
	public void setStatus(Status newStatus) {
		visibility = newStatus;
		if (newStatus == Status.DOLT) {
			setIcon(null);
			setBackground(Color.BLUE);
		} else if (newStatus == Status.SAKNAS) {
			setIcon(null);
			setBackground(Color.WHITE);
		} else if (newStatus == Status.SYNLIGT) {
			setIcon(icon);

		}
	}

	/**
	 * Get the status of the object Kort
	 * 
	 */
	public Status getStatus() {
		return visibility;
	}

	/**
	 * Copys an object Kort's icon and status.
	 * 
	 * @param kort
	 */
	public Kort copy() {
		Kort copy = new Kort(this.icon, this.getStatus());
		return copy;
	}

	/**
	 * Checks if two "kort" have the same icon and returns a boolean depending
	 * on result
	 * 
	 * @param kort Kort to be tested
	 * @return boolean true if both kort have the same icon.
	 */
	public boolean sammaBild(Kort kort) {
		if (kort == null) {
			return false;
		}
		if (this.icon == kort.icon) {
			return true;
		} else {
			return false;
		}
	}
}
