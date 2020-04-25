/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package OUT;

import Runtime.Context;
import Runtime.RuntimeEntity;
import java.io.PrintWriter;
import java.util.List;
import javax.swing.JTextArea;

/**
 *
 * @author frodo
 */
public class Interpreter {

    private Context context;
    public Interpreter(List<RuntimeEntity> code) {
        context = new Context(code);
    }

    public void Execute() throws Exception {
        InOut.Write("********** Triangles Interpreter (Java Version 1.0) **********", null);
        InOut.Write("Program is running...", null);
        while (!context.Halt()) {
            context.getCode().get(context.getCurrent()).execute(context);
        }
    }

    public void setOut(final JTextArea t) {
        InOut.SetConsole(new Console() {

            @Override
            public void Write(String format, Object[] args) {
                t.append(format+"\n");
            }

            @Override
            public String Read() {
                return "Not allowed here!";
            }
        });
    }
}
