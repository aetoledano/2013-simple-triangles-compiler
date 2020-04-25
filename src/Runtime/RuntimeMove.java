
package Runtime;

import OUT.InOut;
import OUT.dibu;

public class RuntimeMove extends RuntimeOperator{

    public RuntimeMove() {
        super();
    }

    @Override
    public void execute(Context context) throws Exception {
        IntValue x = (IntValue) context.getStack().pop();
        IntValue y = (IntValue) context.getStack().pop();
        Integer mem_address = (Integer) context.getStack().pop();
        TriangleValue tv = (TriangleValue)context.getMemory().getVal(mem_address);
        Triangle modified = tv.getValue();
        modified.setX(x.getValue());
        modified.setY(y.getValue());
        dibu.MoveTriangle(mem_address, x.getValue(), y.getValue());
        TriangleValue fvalue = new TriangleValue(modified);
        context.getStack().push(fvalue);
        context.setCurrent(context.getCurrent() + 1);
    }

}
