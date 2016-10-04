package es.ikerperez.binaryconverter.models;

/**
 * Creado por Iker Pérez Brunelli <DarkerTV> a fecha de 01/10/2016.
 */

public class BinaryResult {

    private String _prev;
    private String _value;
    private String _origin;
    private String _target;

    public BinaryResult(String prev, String value, String origin, String target) {
        this._prev = prev;
        this._value = value;
        this._origin = origin;
        this._target = target;
    }

    public String getPrev() {
        return this._prev;
    }

    public void setPrev(String prev) {
        this._prev = prev;
    }

    public String getValue() {
        return _value;
    }

    public void setValue(String value) {
        this._value = value;
    }

    public String getOrigin() {
        return _origin;
    }

    public void setOrigin(String origin) {
        this._origin = origin;
    }

    public String getTarget() {
        return _target;
    }

    public void setTarget(String target) {
        this._target = target;
    }

    public boolean compareTo(BinaryResult otherBinaryResult) {
        return (getPrev().equals(otherBinaryResult.getPrev())) &&
                (getValue().equals(otherBinaryResult.getValue())) &&
                (getOrigin().equals(otherBinaryResult.getOrigin())) &&
                (getTarget().equals(otherBinaryResult.getTarget()));
    }
}
