
package Runtime;

import OUT.InOut;
import OUT.dibu;

/**
 *
 * @author raven
 */
public class RuntimeNewTriangle extends RuntimeEntity {

    @Override
    public void execute(Context context) throws Exception {
        TriangleValue value = new TriangleValue();
        context.getMemory().addVal(value);
        int a = context.getMemory().getSize()-1;
        dibu.NewTriangle( a );
        context.setCurrent( context.getCurrent() + 1);
    }

}
