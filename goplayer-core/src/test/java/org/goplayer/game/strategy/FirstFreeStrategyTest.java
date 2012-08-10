package org.goplayer.game.strategy;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.goplayer.Coord;
import org.goplayer.go.Goban;
import org.goplayer.go.Stone;
import org.goplayer.go.StoneColor;
import org.goplayer.move.IMove;
import org.goplayer.move.PassMove;
import org.goplayer.move.StoneMove;
import org.goplayer.player.IPlayer;
import org.goplayer.player.StrategicalPlayer;
import org.junit.Test;

public class FirstFreeStrategyTest {

	@Test
	public void testInitialSelectionOnEmptyGoban() {
		FirstFreeStrategy strategy = new FirstFreeStrategy();
		int rowCount = 10;
		int colCount = 5;
		Goban goban = new Goban(rowCount, colCount);

		IPlayer player = new StrategicalPlayer(strategy);
		for (int i = 0; i < 100; i++) {
			IMove move = strategy.chooseMove(goban, player);
			assertTrue(move instanceof StoneMove);
			StoneMove m = (StoneMove) move;
			assertEquals(0, m.getRow());
			assertEquals(0, m.getCol());
		}
	}

	@Test
	public void testInitialSelectionOnHalfFilledGoban() {
		Random rand = new Random();
		int rowCount = 10;
		int colCount = 5;
		for (int i = 0; i < 100; i++) {
			int rowTarget = rand.nextInt(rowCount);
			int colTarget = rand.nextInt(colCount);
			Goban goban = new Goban(rowCount, colCount);
			{
				boolean stopFilling = false;
				for (int row = 0; row < rowCount && !stopFilling; row++) {
					for (int col = 0; col < colCount && !stopFilling; col++) {
						stopFilling = rowTarget == row && colTarget == col;
						if (stopFilling) {
							continue;
						} else {
							goban.setCoordContent(row, col, new Stone(
									StoneColor.BLACK));
						}
					}
				}
			}

			FirstFreeStrategy strategy = new FirstFreeStrategy();
			IPlayer player = new StrategicalPlayer(strategy);
			for (int j = 0; j < 10; j++) {
				IMove move = strategy.chooseMove(goban, player);
				assertTrue(move instanceof StoneMove);
				StoneMove m = (StoneMove) move;
				assertEquals(rowTarget, m.getRow());
				assertEquals(colTarget, m.getCol());
			}
		}
	}

	@Test
	public void testPassOnFilledGoban() {
		int rowCount = 10;
		int colCount = 5;
		Goban goban = new Goban(rowCount, colCount);
		for (int row = 0; row < goban.getRowCount(); row++) {
			for (int col = 0; col < goban.getColCount(); col++) {
				goban.setCoordContent(row, col, new Stone(StoneColor.BLACK));
			}
		}

		FirstFreeStrategy strategy = new FirstFreeStrategy();
		IPlayer player = new StrategicalPlayer(strategy);
		for (int j = 0; j < 10; j++) {
			IMove move = strategy.chooseMove(goban, player);
			assertTrue(move instanceof PassMove);
		}
	}

	@Test
	public void testFirstFreeSelected() {
		FirstFreeStrategy strategy = new FirstFreeStrategy();
		int rowCount = 10;
		int colCount = 5;
		int totalInstances = rowCount * colCount;
		Goban goban = new Goban(rowCount, colCount);

		// compute free places
		List<Coord> freePlaces = new ArrayList<Coord>();
		for (int row = 0; row < rowCount; row++) {
			for (int col = 0; col < colCount; col++) {
				freePlaces.add(new Coord(row, col));
			}
		}

		// fill half with random stones
		Random rand = new Random();
		for (int i = 0; i < totalInstances / 2; i++) {
			Coord coord = freePlaces.remove(rand.nextInt(freePlaces.size()));
			goban.setCoordContent(coord.getRow(), coord.getCol(), new Stone(
					StoneColor.BLACK));
		}

		IPlayer player = new StrategicalPlayer(strategy);
		IMove move = null;
		move = strategy.chooseMove(goban, player);
		while (move instanceof StoneMove) {
			StoneMove actual = (StoneMove) move;
			Coord expected = freePlaces.remove(0);
			assertEquals(expected.getRow(), actual.getRow());
			assertEquals(expected.getCol(), actual.getCol());
			goban.setCoordContent(actual.getRow(), actual.getCol(), new Stone(
					StoneColor.WHITE));
			move = strategy.chooseMove(goban, player);
		}
		assertEquals(0, freePlaces.size());
	}

	
}
