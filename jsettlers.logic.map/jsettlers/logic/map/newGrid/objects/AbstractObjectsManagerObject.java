package jsettlers.logic.map.newGrid.objects;

import java.io.Serializable;

import jsettlers.common.position.ISPosition2D;
import jsettlers.logic.map.newGrid.interfaces.AbstractHexMapObject;

/**
 * 
 * @author Andreas Eberle
 * 
 */
public abstract class AbstractObjectsManagerObject extends AbstractHexMapObject implements Serializable {
	private static final long serialVersionUID = 6013184372588966504L;

	private final ISPosition2D pos;

	protected AbstractObjectsManagerObject(ISPosition2D pos) {
		this.pos = pos;
	}

	protected ISPosition2D getPos() {
		return pos;
	}

	protected abstract void changeState();

}
