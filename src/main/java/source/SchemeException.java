package source;

/*COMP20050 Software Engineering Project II Team Scheme
Developed by Quinn Berryman, Lones Xheladini, Cassidy Aytan*/
public class SchemeException extends Exception{
    public SchemeException(String message) {
        super(message);
    }
}
class PieceNotFoundException extends SchemeException {
    public PieceNotFoundException(String message) {
        super(message);
    }
}

class IllegalMoveException extends SchemeException {
    public IllegalMoveException(String message) {
        super(message);
    }
}

class UnknownCoordinatesException extends SchemeException {
    public UnknownCoordinatesException(String message) {
        super(message);
    }
}