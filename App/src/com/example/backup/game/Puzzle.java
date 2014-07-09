package com.example.backup.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Puzzle
{
	public static final int DIRECTION_LEFT = 0;
	public static final int DIRECTION_UP = 1;
	public static final int DIRECTION_RIGHT = 2;
	public static final int DIRECTION_DOWN = 3;

	public static final int[] DIRECTION_X = {-1, 0, +1, 0}; 
	public static final int[] DIRECTION_Y = {0, -1, 0, +1};
	
	private int[] tiles;
	private int handleLocation;
	private int moveCount;
	
	private Random random = new Random();
	private int width;
	private int height;
	
	public void init(int width, int height)
	{
		this.width = width;
		this.height = height;
		tiles = new int[width * height];
		
		for(int i = 0; i < tiles.length; i++)
		{
			tiles[i] = i;
		}
		
		handleLocation = tiles.length - 1;
		moveCount = 0;
	}
	
	public boolean isSolved()
	{
		for(int i = 0; i < tiles.length; i++)
		{
			if(tiles[i] != i)
			{
				return false;
			}
		}
		
		return true;
	}
	
	public void setTiles(int[] tiles)
	{
		this.tiles = tiles;
		
		for(int i = 0; i < tiles.length; i++)
		{
			if(tiles[i] == tiles.length - 1)
			{
				handleLocation = i;
				break;
			}
		}
	}
	
	public int[] getTiles()
	{
		return tiles; // should not be written
	}
	
	public int getColumnAt(int location)
	{
		return location % width;
	}
	
	public int getRowAt(int location)
	{
		return location / width;
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	public int hammingDistance()
	{
		int hamming = 0;
		
		for(int i = 0; i < tiles.length; i++)
		{
			hamming += Math.abs(i - tiles[i]);
		}
		
		return hamming;
	}
	
	public void scramble()
	{
		if(width < 2 || height < 2)
		{
			return;
		}
		
		int limit = width * height * Math.max(width,height);
		int move = 0;
		
		while(hammingDistance() < limit)
		{
			move = pickRandomMove(invertMove(move));
			move(move, 1);
		}
		
		moveCount = 0;
	}

	private int invertMove(int move)
	{
		if(move == 0)
		{
			return 0;
		}
		
		if(move == 1 << DIRECTION_LEFT)
		{
			return 1 << DIRECTION_RIGHT;
		}
		
		if(move == 1 << DIRECTION_UP)
		{
			return 1 << DIRECTION_DOWN;
		}
		
		if(move == 1 << DIRECTION_RIGHT)
		{
			return 1 << DIRECTION_LEFT;
		}
		
		if(move == 1 << DIRECTION_DOWN)
		{
			return 1 << DIRECTION_UP;
		}
		
		return 0;
	}

	private int pickRandomMove(int exclude)
	{
		List<Integer> moves = new ArrayList<Integer>(4);
		int possibleMoves = getPossibleMoves() & ~exclude;
		
		if((possibleMoves & (1 << DIRECTION_LEFT)) > 0)
		{
			moves.add(DIRECTION_LEFT);
		}
		
		if((possibleMoves & (1 << DIRECTION_UP)) > 0)
		{
			moves.add(DIRECTION_UP);
		}
		
		if((possibleMoves & (1 << DIRECTION_RIGHT)) > 0)
		{
			moves.add(DIRECTION_RIGHT);
		}
		
		if((possibleMoves & (1 << DIRECTION_DOWN)) > 0)
		{
			moves.add(DIRECTION_DOWN);
		}

		return moves.get(random.nextInt(moves.size()));
	}

	public boolean move(int direction, int count)
	{
		boolean match = false;
		
		for(int i = 0; i < count; i++)
		{
			int targetLocation = handleLocation + DIRECTION_X[direction] + DIRECTION_Y[direction] * width;
			tiles[handleLocation] = tiles[targetLocation];
			match |= tiles[handleLocation] == handleLocation;
			tiles[targetLocation] = tiles.length - 1; // handle tile
			handleLocation = targetLocation;
		}
		
		moveCount++;
		return match;
	}
	
	public int getPossibleMoves()
	{
		int x = getColumnAt(handleLocation);
		int y = getRowAt(handleLocation);
		
		boolean left = x > 0;
		boolean right = x < width - 1;
		boolean up = y > 0;
		boolean down = y < height - 1;
		
		return (left ? 1 << DIRECTION_LEFT : 0) |
				(right ? 1 << DIRECTION_RIGHT : 0) |
				(up ? 1 << DIRECTION_UP : 0) |
				(down ? 1 << DIRECTION_DOWN : 0);
	}

	public int getDirection(int location)
	{
		int delta = location - handleLocation;
		
		if(delta % width == 0)
		{
			return delta < 0 ? DIRECTION_UP : DIRECTION_DOWN;
		}
		else if(handleLocation / width == (handleLocation + delta) / width)
		{
			return delta < 0 ? DIRECTION_LEFT : DIRECTION_RIGHT;
		}
		else
		{
			return -1;
		}
	}
	
	public int getMoveCount()
	{
		return moveCount;
	}
	
	public void setMoveCount(int moveCount)
	{
		this.moveCount = moveCount;
	}
	
	public int getHandleLocation()
	{
		return handleLocation;
	}
}
