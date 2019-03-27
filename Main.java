package matrix;

public class Main {

	public static void main(String[] args) {
		Matrix m = new Matrix(new double[][] {
			{1,2,3},
			{4,5,6},
			{7,8,9}
		});
		
		Matrix a = new Matrix(new double[][] {
			{1,1},
			{1,1},
			{1,1},
			{1,1},
			{1,1}
		});
		
		System.out.println("Rows: " + m.getRows());
		System.out.println("Columns: " + m.getColumns());
		System.out.println("Determination: " + m.getDetermination() + "\n");
		
		if(m.determination == "square")
			System.out.println("Determinant: " + m.determinant());
		
		System.out.println("Transpose:\n" + m.transpose().toString());
	
		if(m.invertible())
			System.out.println("Inverse:\n" + m.inverse().toString());
		
		if(m.canMultiply(a))
			System.out.println("Multiply with:\n" + a.toString() + "\nProduct:\n" + m.multiply(m.inverse()).toString());
		
		if(!m.getDetermination().equals("square"))
			System.out.println("Pseudo Inverse:\n" + m.pseudoInverse().toString());
	}
}
