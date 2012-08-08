package org.goplayer.move;

import static org.junit.Assert.*;

import org.junit.Test;

public class StoneMoveTest {

	@Test
	public void testRowAndCol() {
		{
			int row = 0;
			int col = 0;
			StoneMove move = new StoneMove(row, col);
			assertEquals(row, move.getRow());
			assertEquals(col, move.getCol());
		}
		
		{
			int row = 5;
			int col = 3;
			StoneMove move = new StoneMove(row, col);
			assertEquals(row, move.getRow());
			assertEquals(col, move.getCol());
		}
		
		{
			int row = 19;
			int col = 0;
			StoneMove move = new StoneMove(row, col);
			assertEquals(row, move.getRow());
			assertEquals(col, move.getCol());
		}
	}

}
