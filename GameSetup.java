import java.util.Scanner;

import javax.xml.transform.Source;

public class GameSetup {
	
	Scanner input=new Scanner(System.in);
	
	int Row_count=6;
	int Column_count=7;
	
	int [][] board= new int [Row_count][Column_count];
	
	void drop_piece(int [][] board,int col,int row, int piece)
	{
		board[row][col]=piece;
	}
	
	boolean is_valid_location(int [][] board,int col)
	{
		return board[Row_count-1][col]==0;
	}
	
	
	int get_next_open_row(int [][] board,int col)
	{
		for(int r=0;r<Row_count;r++)
		{
			if(board[r][col]==0)
				return r;
		}
		return 0;
	}
	
	void print_board(int [][] board)
	{
		for(int i=5;i>=0;i--)
		{
			for(int j=0;j<7;j++)
				System.out.print(board[i][j]+" ");
			
			System.out.println();
		}
	}
	
	boolean winning_move(int [][] board, int piece)
	{
		//check horizontal
		
		for(int c=0;c<Column_count-3;c++)
		{
			for(int r=0;r<Row_count;r++)
			{
				if(board[r][c]== piece && board[r][c+1]== piece && board[r][c+2]== piece && board[r][c+3]== piece)
				{
					return true;
				}
			}
		}
		
		//check vertically
		
		for(int c=0;c<Column_count;c++)
		{
			for(int r=0;r<Row_count-3;r++)
			{
				if(board[r][c]== piece && board[r+1][c]== piece && board[r+2][c]== piece && board[r+3][c]== piece)
				{
					return true;
				}
			}
		}
		
		//positively sloped diagonals
		for(int c=0;c<Column_count-3;c++)
		{
			for(int r=0;r<Row_count-3;r++)
			{
				if(board[r][c]== piece && board[r+1][c+1]== piece && board[r+2][c+2]== piece && board[r+3][c+3]== piece)
				{
					return true;
				}
			}
		}
		
		
		//negatively sloped diagonals
		for(int c=0;c<Column_count-3;c++)
		{
			for(int r=3;r<Row_count;r++)
			{
				if(board[r][c]== piece && board[r-1][c+1]== piece && board[r-2][c+2]== piece && board[r-3][c+3]== piece)
				{
					return true;
				}
			}
		}
		return false;
		
		
	}
	
	public void game_session()
	{
		boolean game_over=false;
		int turn=0;
		int col;
		while(!game_over)
		{
			if(turn==0)
			{
				System.out.println("Player 1");
				col=input.nextInt();
				
				if(is_valid_location(board, col))
				{
					int row=get_next_open_row(board, col);
					drop_piece(board, col, row, 1);
					
					if(winning_move(board, 1))
					{
						System.out.println("Player 1 wins");
						game_over=true;
					}
				}
				
			}
			else
			{
				System.out.println("Player 2");
				col=input.nextInt();
				
				if(is_valid_location(board, col))
				{
					int row=get_next_open_row(board, col);
					drop_piece(board, col, row, 2);
					
					if(winning_move(board, 2))
					{
						System.out.println("Player 2 wins");
						game_over=true;
					}
				}
			}
			
			turn+=1;
			turn%=2;
			
			print_board(board);
			
		}
	}

}
