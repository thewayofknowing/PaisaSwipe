/*******************************************************************************
 * Copyright 2013 Marian Schedenig
 *
 * This file is part of n Tile Puzzle.
 *
 * n Tile Puzzle is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 * 
 * n Tile Puzzle is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * n Tile Puzzle. If not, see http://www.gnu.org/licenses/.
 *******************************************************************************/
package com.example.backup.game;

public class PuzzleStats
{
	private int plays;
	private int best;
	private float avg;
	private boolean newBest;

	public PuzzleStats(int plays, int best, float avg, boolean newBest)
	{
		this.plays = plays;
		this.best = best;
		this.avg = avg;
		this.newBest = newBest;
	}

	public int getPlays()
	{
		return plays;
	}

	public int getBest()
	{
		return best;
	}

	public float getAvg()
	{
		return avg;
	}

	public boolean isNewBest()
	{
		return newBest;
	}
}