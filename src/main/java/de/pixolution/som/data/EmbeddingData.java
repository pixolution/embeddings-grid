package de.pixolution.som.data;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Arrays;
import java.util.Base64;

import de.pixolution.embeddingsGrid.model.SortingRequestImagesInnerJson;

public class EmbeddingData {
	
	protected float[] embedding;
	protected String id;
	protected String uri;
	protected boolean isSeeded = false;
	protected int column = -1;
	protected int row = -1;
	protected int sizeColumns = 1;
	protected int sizeRows = 1;
	protected boolean clone = false;
	
	/**
	 * A combination of uid and an embedding that is used as sortable asset on the grid. 
	 * @param id
	 * @param embedding
	 */
	public EmbeddingData(String id, float[] embedding) {
		this.embedding = embedding;
		this.id = id;
	}
	
	/**
	 * A combination of uid and an embedding that is used as sortable asset on the grid from a 
	 * HTTP Request SortingImagesInner object. 
	 * @param id
	 * @param embedding
	 */
	public EmbeddingData(SortingRequestImagesInnerJson image) {
		this.embedding = byteToFloatArray(Base64.getDecoder().decode(image.getEmbedding()));
		this.id = image.getId();
		// fix this embedding data to a position in the grid
		if (image.getSeedColumn() >= 0 && image.getSeedRow() >= 0) {
			// mark element as seeded (correct placement in the images array must still be done!) 
			seedOnGrid(image.getSeedColumn(), image.getSeedRow());
		}
		// does it provide a size attribute as well?
		if (image.getSizeCols() > 1 || image.getSizeRows() > 1) {
			// make sure the element is marked as fixed
			if (! isSeeded()) throw new IllegalArgumentException("The element "+id+" has size attributes set ("+image.getSizeCols()+"x"+image.getSizeRows()+") but is not seeded on the grid. This is not allowed, only seeded elements can have a size > 1x1.");
			// set attributes in EmbeddingData object (correct unwrap and placement must still be done!)
			setSize(image.getSizeCols(), image.getSizeRows());
		}
	}
	
	/**
	 * Returns if column and row have a value greater-then 1, which means that the element 
	 * has a larger size than the 1x1 default. Returns true if the values are > 1, else false
	 * @return
	 */
	public boolean isLargerThanOneSlot() {
		return (sizeColumns > 1 || sizeRows > 1);
	}
	/**
	 * The number of slots on the grid this element should cover. Anchor is top-left corer. 
	 * @param columns
	 * @param rows
	 * @return
	 */
	public void setSize(int columns, int rows) {
		this.sizeColumns = columns;
		this.sizeRows = rows;
	}
	
	public int getSizeColumns() {
		return this.sizeColumns;
	}
	
	public int getSizeRows() {
		return this.sizeRows;
	}
	
	public int getColumn() {
		return column;
	}

	public int getRow() {
		return row;
	}

	/**
	 * Fix the asset on the given position on the grid
	 * @param column
	 * @param row
	 */
	public void seedOnGrid(int column, int row) {
		this.isSeeded = true;
		this.column = column;
		this.row = row;
	}
	
	/**
	 * Convert an array of floats to byte[] (with float using 4 bytes)
	 * @param floatArray
	 * @return
	 */
    public static byte[] floatToByteArray(float[] floatArray) {
        ByteBuffer buf = ByteBuffer.allocate(Float.SIZE / Byte.SIZE * floatArray.length);
        buf.asFloatBuffer().put(floatArray);
        return buf.array();
    }
    
    /**
     * Convert an array of bytes to float[] (with float using 4 bytes) 
     * @param bytes
     * @return
     */
    public static float[] byteToFloatArray(byte[] bytes) {
        FloatBuffer buf = ByteBuffer.wrap(bytes).asFloatBuffer();
        float[] floatArray = new float[buf.limit()];
        buf.get(floatArray);
        return floatArray;
    }
    
    /**
     * The uniq identifier
     * @return
     */
    public String getId() {
    	return this.id;
    }
    
    /**
     * Return the embedding that belongs to this object
     */
	public float[] getFeature() {
		return embedding;
	}

	public boolean isSeeded() {
		return isSeeded;
	}

	/**
	 * Create a clone of this instance. Set the fixed seed to the new position.
	 * @param newCol
	 * @param newRow
	 * @return
	 */
	public EmbeddingData unwrapClone(int newCol, int newRow) {
		EmbeddingData ed = new EmbeddingData(this.getId(), this.getFeature());
		ed.seedOnGrid(newCol, newRow);
		// set the size
		ed.setSize(this.sizeColumns, this.sizeRows);
		ed.clone = true;
		return ed;
	}
	/**
	 * Is this element a symbolic placeholder for an over-sized-element?
	 * @return
	 */
	public boolean isClone() {
		return this.clone;
	}
	
	@Override
	public String toString() {
		return "{ id: "+id+", row: "+row+", column: "+column+", size_cols: "+sizeColumns+", size_rows: "+sizeRows+", embedding: "+Arrays.toString(embedding)+" }";
	}
}
