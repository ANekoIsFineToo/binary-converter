package es.ikerperez.binaryconverter.models;

/**
 * Creado por Iker Pérez Brunelli <DarkerTV> a fecha de 05/10/2016.
 */

public class OperateResult {

    private String _base;
    private String _firstValue;
    private String _secondValue;
    private String _operation;
    private String _result;

    public OperateResult(String base, String firstValue, String secondValue, String operation,
                         String result) {
        this._base = base;
        this._firstValue = firstValue;
        this._secondValue = secondValue;
        this._operation = operation;
        this._result = result;
    }

    public String getBase() {
        return _base;
    }

    public void setBase(String base) {
        this._base = base;
    }

    public String getFirstValue() {
        return _firstValue;
    }

    public void setFirstValue(String firstValue) {
        this._firstValue = firstValue;
    }

    public String getSecondValue() {
        return _secondValue;
    }

    public void setSecondValue(String secondValue) {
        this._secondValue = secondValue;
    }

    public String getOperation() {
        return _operation;
    }

    public void setOperation(String operation) {
        this._operation = operation;
    }

    public String getResult() {
        return _result;
    }

    public void setResult(String result) {
        this._result = result;
    }

    public boolean compareTo(OperateResult otherOperateResult) {
        return (getBase().equals(otherOperateResult.getBase())) &&
                (getFirstValue().equals(otherOperateResult.getFirstValue())) &&
                (getSecondValue().equals(otherOperateResult.getSecondValue())) &&
                (getOperation().equals(otherOperateResult.getOperation())) &&
                (getResult().equals(otherOperateResult.getResult()));
    }
}
