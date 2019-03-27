package matrix;

public class Matrix {

		double[][] entries;
		int rows;
		int columns;
		String determination;
		
		public Matrix(double[][] m) {
			entries = m;
			rows = m.length;
			if(rows > 0)
				columns = m[0].length;
			if(rows > columns)
				determination = "overdetermined";
			else if(rows < columns)
				determination = "underdetermined";
			else
				determination = "square";
		}
		
		public double determinant() {
			int n = getRows();
			double[][] matrix = getEntries();
			if(n == 1)
				return matrix[0][0];
			if(n == 2)
				return matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];
			double determinant = 0;
			int sign = -1;
			for(int i = 0;i < n;i++) {
				sign *= -1;
				Matrix sub = findSub(n, i);
				determinant += matrix[0][i] * sign * sub.determinant();
			}
			return determinant;
		}
		
		public Matrix findSub(int size, int index) {
			Matrix sub = new Matrix(new double[size - 1][size - 1]);
			int k;
			for(int i = 1;i < size;i++) {
				for(int j = 0;j < size;j++) {
					k = j;
					if(j != index) {
						if(j > index)
							k = j - 1;
						sub.setEntry(getEntries()[i][j], i - 1, k);
					}
				}
			}
			return sub;
		}
		
		public Matrix transpose() {
			int n = getRows();
			int m = getColumns();
			Matrix transpose = new Matrix(new double[m][n]);
			for(int i = 0;i < n;i++) {
				for(int j = 0;j < m;j++) {
					transpose.setEntry(getEntries()[i][j], j, i);
				}
			}
			return transpose;
		}
		
		public Matrix inverse() {
			double[][] augment = augment();
			Matrix inverse = new Matrix(augment);
			double[] row = new double[getColumns()];
			double[] modifyRow = new double[row.length];
			int n = augment.length;
			int sign;
			double val;
			double scalar;
			for(int j = 0;j < n;j++) {
				for(int i = 0;i < n;i++)
					if(i == j) {
						val = augment[i][j];
						if(val == 0) {
							if(i + 1 != n)
								inverse.rowSwap(i, i + 1);
							else
								inverse.rowSwap(i, 0);
							augment = inverse.getEntries();
						}
					}
				for(int i = 0;i < n;i++)
					if(i == j) {
						val = augment[i][j];
						scalar = 1/val;
						augment[i] = multiplyRow(augment[i], scalar);
						row = augment[i];
						break;
					}
				for(int i = 0;i < n;i++)
					if(i != j) {
						scalar = augment[i][j];
						if(scalar < 0) {
							sign = 1;
							scalar *= -1;
						}
						else
							sign = -1;
						modifyRow = multiplyRow(row, scalar);
						augment[i] = addRows(augment[i], modifyRow, sign);
					}
			}
			inverse.setEntries(augment);
			inverse.setEntries(inverse.separate());
			return inverse;
		}
		
		public boolean invertible() {
			if(!getDetermination().equals("square"))
				return false;
			return determinant() != 0;
		}
		
		public Matrix multiply(Matrix m) {
			int aRows = getRows();
			int bCols = m.getColumns();
			if(!canMultiply(m))
				return null;
			double[][] product = new double[aRows][bCols];
			m = m.transpose();
			for(int i = 0;i < aRows;i++)
				for(int j = 0;j < bCols;j++)
					product[i][j] = processRows(getEntries()[i], m.getEntries()[j]);
			return new Matrix(product);
		}
		
		public boolean canMultiply(Matrix m) {
			int aCols = getColumns();
			int bRows = m.getRows();
			return aCols == bRows;
		}
		
		public double processRows(double[] a, double[] b) {
			double val = 0;
			for(int i = 0;i < a.length;i++)
				val += a[i] * b[i];
			return val;
		}
		
		public Matrix pseudoInverse() {
			Matrix pseudoInverse;
			if(determination.equals("overdetermined"))
				pseudoInverse = ((transpose().multiply(this)).inverse()).multiply(transpose());
			else
				pseudoInverse = transpose().multiply((multiply(transpose()).inverse()));
			return pseudoInverse;
				
		}
		
		public double[] multiplyRow(double[] a, double b) {
			double c[] = new double[a.length];
			for(int i = 0;i < a.length;i++)
				c[i] = a[i] * b;
			return c;
		}

		public double[] addRows(double[] a, double[] b, int sign) {
			double[] c = new double[a.length];
			for(int i = 0;i < a.length;i++)
				c[i] = a[i] + sign * b[i];
			return c;
		}
		
		public void rowSwap(int a, int b) {
			int m = getColumns();
			double[] temp = new double[m];
			temp = getEntries()[a];
			setRow(getEntries()[b], a);
			setRow(temp, b);
		}

		public double[][] augment() {
			int n = getRows();
			int m = n * 2;
			double[][] matrix = new double[n][m];
			for(int i = 0;i < n;i++)
				for(int j = 0;j < m;j++) {
					if(j < n)
						matrix[i][j] = getEntries()[i][j];
					else
						if(i + n == j)
							matrix[i][j] = 1;
						else
							matrix[i][j] = 0;
				}
			return matrix;
		}
		
		public double[][] separate() {
			int n = getRows();
			int m = getColumns();
			int half = m/2;
			int col;
			double[][] matrix = new double[n][n];
			for(int i = 0;i < n;i++)
				for(int j = half;j < m;j++) {
					col = j - half;
					matrix[i][col] = getEntries()[i][j];
				}
			return matrix;
		}

		public double[][] getEntries() {
			return entries;
		}
		
		public void setEntry(double num, int i, int j) {
			entries[i][j] = num;
		}
		
		public void setRow(double[] r, int i) {
			entries[i] = r;
		}
		
		public void setEntries(double[][] m) {
			entries = m;
			rows = m.length;
			columns = m[0].length;
		}
		
		public int getRows() {
			return rows;
		}
		
		public int getColumns() {
			return columns;
		}
		
		public String getDetermination() {
			return determination;
		}
		
		public String toString() {
			String out = "";
			for(int i = 0;i < getRows();i++) {
				out += "|";
				for(int j = 0;j < getColumns();j++)
					out += getEntries()[i][j] + "|";
				out += "\n";
			}
			return out;
		}
}
