package source;

import java.util.Scanner;

public class Menu {
    private String inputString;
    private Scanner scanner = new Scanner(System.in);
    public void getInput() {
        inputString = scanner.next();
        System.out.println("Input: [" + inputString + "]");
    }
    public void getInputLine() {
        inputString = scanner.nextLine();
        System.out.println("Input: [" + inputString + "]");
    }

    public String getInputString() {
        return inputString;
    }
    //tries to convert the input to two ints and returns them, or throws an error
    public int[] getCoords() throws UnknownCoordinatesException{
        int[] coords = new int[2];
        String[] parts = inputString.trim().split(" ");
        //read the coords from the inputString 1 5 etc -> {1, 5}
        try {
            coords[0] = Integer.parseInt(parts[0]);
            coords[1] = Integer.parseInt(parts[1]);
        }
        catch (Exception e){
            throw new UnknownCoordinatesException("Invalid coordinates! Try again.");
        }
        return coords;
    }
}
