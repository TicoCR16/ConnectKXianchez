import java.awt.*;
import java.util.Random;
import connectK.CKPlayer;
import java.util.ArrayList;
import connectK.BoardModel;

public class Xiachez extends CKPlayer {
	private final byte PLAYER_1 = 1;
	private final byte PLAYER_2 = 2;
	private final int DEPTH_MAX = 2;
	private final int P_INF = Integer.MAX_VALUE;
	private final int N_INF = Integer.MIN_VALUE;
	
	public Xiachez( byte player, BoardModel state ) {
		super( player, state );
        teamName = "Xiachez";
	}
	
	public Point randomMove( BoardModel state ) {
		System.out.println( "RANDOM RETURN" );
		ArrayList possibleMoves = possibleMoves( state );
		return (Point) possibleMoves.get( new Random().nextInt( possibleMoves.size() ) );
	}
	
	@Override
    public Point getMove( BoardModel state ) {
		if( state.spacesLeft == state.height * state.width )
			return new Point( state.height / 2, state.width / 2 );
		Point move = minimax( state );
		return ( move != null ? move : randomMove( state ) ); 
	}
	
	@Override
	public Point getMove( BoardModel state, int deadline ) { return getMove( state ); }
	
	//Simulates Minimax algorithm
	public Point minimax( BoardModel state ) {
		Point action = null;
		int value = Integer.MIN_VALUE;
		int temp = heuristicFunction( state, 0 );
		for( Point cell : possibleMoves( state ) ) {
			temp = depthScore( state.placePiece( cell, PLAYER_2 ), 0, value, N_INF, P_INF );
			if( value < temp ) {
				value = temp;
				action = cell;
			}
		}
		return action;
	}
	
	//Assigns a score to each possible state
	public int depthScore( BoardModel state, int depth, int score, int alpha, int beta ) {	
		byte terminateStatusCode = state.winner();
        if( terminateStatusCode != -1 )
            return ( !state.hasMovesLeft() ? 0 : ( (terminateStatusCode == 1 ) ? N_INF : P_INF ) );
        if( depth > DEPTH_MAX ) return heuristicFunction( state, score );
              
        int returnValue = 0;
        for( Point cell : possibleMoves( state ) ) {
        	if( depth % 2 == 0 ) {
        		returnValue = Math.min( beta, depthScore( state.placePiece( cell, PLAYER_1 ),
        			          depth + 1, heuristicFunction( state, score ), alpha, beta ) );
        		if( returnValue <= alpha ) 
        		{
        			beta = returnValue;
        			break;
        		}
        	}
        	else {
        		returnValue = Math.max( alpha, depthScore( state.placePiece( cell,  PLAYER_2 ),
        				      depth + 1, heuristicFunction( state, score ), alpha, beta ) ); 
        		if( beta <= returnValue )
        		{
        			alpha = returnValue;
        			break;	
        		}
        	}
        }
        return returnValue;  
	}
	
	//Compares scores
	public int heuristicFunction( BoardModel state, int eval ) {
        int nearestPlayer = 0;
        int pos = -1;
        int count = 0;
        Point lastMove = state.lastMove;
        int row = lastMove.x;
        int column = lastMove.y;
        int start = ((column - state.kLength + 1) > -1) ? column - state.kLength + 1 : 0;
        int end = ((column + state.kLength) < state.height) ? column + state.kLength : state.height;
        for(int i = start; i < end; i++) {
            if(state.getSpace(row, i) == 0) {
                count++;
                if(count == state.getkLength() && nearestPlayer == 0) count--;
                else if(count == state.getkLength())  {
                    if(nearestPlayer == 1) eval--;
                    else eval++;
                    count--;
                }
                if(pos != -1 && (i - pos) >= state.getkLength()-1) {
                    nearestPlayer = 0;
                    pos = -1;
                }
            }

            else if(state.getSpace(row, i) == 1) {
                if(nearestPlayer != 2) {
                    count++;
                    if(count == state.getkLength()) {
                        eval--;
                        count--;
                    }
                    pos = i;
                }
                else {
                    count = i - pos;
                    pos = i;

                }
                nearestPlayer = 1;
            }

            else {
                if(nearestPlayer != 1) {
                    count++;
                    if(count == state.getkLength()) {
                        eval++;
                        count--;
                    }
                    pos = i;
                }
                else {
                    count = i - pos;
                    pos = i;
                }
                nearestPlayer = 2;
            }
        }

        count = 0;
        nearestPlayer = 0;
        pos = -1;
        start = (row - state.kLength + 1) > -1 ? row - state.kLength + 1 : 0;
        end = (row + state.kLength) < state.height ? row + state.kLength: state.width;

        for(int i = start; i < end; i++) {
            if(state.getSpace(i, column) == 0) {
                count++;
                if(count == state.getkLength() && nearestPlayer == 0) count--;
                else if(count == state.getkLength())  {
                    if(nearestPlayer == 1) eval--;
                    else eval++;
                    count--;
                }
                if(pos != -1 && (i - pos) >= state.getkLength()-1) {
                    nearestPlayer = 0;
                    pos = -1;
                }
            }

            else if(state.getSpace(i, column) == 1) {
                if(nearestPlayer != 2) {
                    count++;
                    if(count == state.getkLength()) {
                        eval--;
                        count--;
                    }
                    pos = i;
                }
                else {
                    count = i - pos;
                    pos = i;

                }
                nearestPlayer = 1;
            }

            else {
                if(nearestPlayer != 1) {
                    count++;
                    if(count == state.getkLength()) {
                        eval++;
                        count--;
                    }
                    pos = i;
                }
                else {
                    count = i - pos;
                    pos = i;
                }
                nearestPlayer = 2;
            }
        }
        return eval;
     
	}

	//Returns an ArrayList object containing all available( 0 pieces ) moves
	public ArrayList< Point > possibleMoves( BoardModel state ) {
		ArrayList< Point > possibleMoves = new ArrayList< Point >();
		for( int x = 0; x < state.width; x++ )
            for( int y = 0; y < state.height; y++ ) {
                if( state.getSpace( x, y ) == 0 ) {
                    possibleMoves.add( new Point( x, y ) );
                    if( state.gravity ) break; 
                }
            }		
		return possibleMoves;
	}
}