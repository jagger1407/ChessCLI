package jagger.Chess;

public enum PieceType {
	Pawn,
	Bishop,
	Knight,
	Rook,
	Queen,
	King;
	
	public static int material[] = {
		1,
		3,
		3,
		5,
		9,
		1
	};
}
