
public class Piece {
	  private PieceType type;
	  private Color color;
	  
	  
	  public Piece(PieceType type, Color color) {
		  this.type = type;
		  this.color = color;
	  }
	  
	  public Piece(char type) {
		  String pstr = "";
		  if(Character.isUpperCase(type))  {
			  color = Color.White;
			  pstr += "PBNRQK";
		  }
		  else  {
			  color = Color.Black;
			  pstr += "pbnrqk";
		  }
		  
		  this.type = PieceType.values()[pstr.indexOf(type)];
	  }
	  
	  public String toString() {
		  String letters = "pbnrqk";
		  String out = "" + letters.charAt(type.ordinal());
		  if(color == Color.White) out = out.toUpperCase();
		  return out;
	  }
}

