/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Runtime;

import OUT.InOut;
import OUT.dibu;

/**
 *
 * @author raven
 */
public class RuntimeInitTriangle extends RuntimeOperator {

    public RuntimeInitTriangle() {
        super();
    }

    @Override
    public void execute(Context context) {
        Integer cant = ((IntValue) context.getCode().get(context.getCurrent() + 1)).getValue();
        TriangleValue value;
        value = new TriangleValue(
                new Triangle(
                        ((IntValue) context.getStack().pop()).getValue(),
                        ((IntValue) context.getStack().pop()).getValue(),
                        ((IntValue) context.getStack().pop()).getValue()
                ));
        Integer dir = (Integer)context.getStack().peek();
        dibu.InitTriangle(dir, value.getValue().getDim(),value.getValue().getX(),value.getValue().getY());
        context.getStack().push(value);
        context.setCurrent(context.getCurrent() + 2);
    }

}
