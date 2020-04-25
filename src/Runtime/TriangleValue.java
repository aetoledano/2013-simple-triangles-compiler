package Runtime;

public class TriangleValue extends RuntimeValue<Triangle>{

    public TriangleValue(){
        super(new Triangle(0, 0, 0));
    }
    
    public TriangleValue(Triangle value) {
        super(value);
    }

}
