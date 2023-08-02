package de.pixolution.som.data;

public class Grid {

	protected final int rows;
	protected final int columns;
	protected final EmbeddingData[] elements;
	
	public Grid(int columns, int rows, EmbeddingData[] elements) {
		this.rows = rows;
		this.columns = columns;
		this.elements = elements;
	}

	public EmbeddingData[] getElements() {
		return elements;
	}

	public int getRows() {
		return rows;
	}

	public int getColumns() {
		return columns;
	}

	public EmbeddingData getElement(int column, int row) {
		return elements[row*columns+column];
	}
	
	public void setElement(int column, int row, EmbeddingData entry) {
		elements[row*columns+column] = entry;
	}

	public EmbeddingData getElement(int index) {
		return elements[index];
	}
	
	public void setElement(int index, EmbeddingData entry) {
		elements[index] = entry;
	}

	public int getSize() {
		return rows*columns;
	}

	/**
	 * Count how many map cells are not NULL
	 */
	public int getElementCount() {
		int count = 0;
		for (int i = 0; i < elements.length; i++)
			if(elements[i] != null)
				count++;
		return count;
	}

}