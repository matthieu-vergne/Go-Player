package org.goplayer.game.strategy;

import static org.junit.Assert.*;
import org.goplayer.game.IPlayer;
import org.goplayer.game.StrategicalPlayer;
import org.goplayer.go.Goban;
import org.goplayer.move.StoneMove;
import org.junit.Test;

public class RandomStrategyTest {

	@Test
	public void testInitialRandomness() {
		RandomStrategy strategy = new RandomStrategy();
		IPlayer player = new StrategicalPlayer(strategy);
		int colCount = 3;
		int rowCount = 5;

		// stress
		int[][] instances = new int[rowCount][colCount];
		int testPerInstance = 1000;
		int testCount = colCount * rowCount * testPerInstance;
		for (int i = 0; i < testCount; i++) {
			Goban goban = new Goban(rowCount, colCount);
			StoneMove move = (StoneMove) strategy.chooseMove(goban, player);
			instances[move.getRow()][move.getCol()]++;
		}

		// evaluate
		double deviation = 0.0;
		for (int row = 0; row < rowCount; row++) {
			for (int col = 0; col < colCount; col++) {
				deviation += Math.pow(instances[row][col] - testPerInstance, 2);
			}
		}
		deviation /= colCount * rowCount;
		deviation = Math.sqrt(deviation);
		deviation /= testPerInstance / 100.0; // deviation in %
		assertTrue("The standard deviation is too big: " + ((int) deviation)
				+ "%", deviation < 10);
	}

}
