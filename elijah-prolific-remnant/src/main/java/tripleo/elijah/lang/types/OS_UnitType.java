package tripleo.elijah.lang.types;

import tripleo.elijah.lang.*;


public class OS_UnitType extends __Abstract_OS_Type {

	@Override
	public OS_Element getElement() {
		return null;
	}

	@Override
	public Type getType() {
		return Type.UNIT_TYPE;
	}

	@Override
	public String asString() {
		return "<OS_UnitType>";
	}

	@Override
	public boolean isUnitType() {
		return true;
	}

	protected boolean _isEqual(final OS_Type aType) {
		return aType.getType() == Type.UNIT_TYPE;
	}

	@Override
	public String toString() {
		return "<UnitType>";
	}
}


