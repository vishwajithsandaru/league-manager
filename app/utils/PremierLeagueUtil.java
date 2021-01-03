package utils;

import com.fasterxml.jackson.databind.JsonNode;
import exceptions.UserInputException;
import play.libs.Json;
import responses.ErrorResponse;
import responses.GeneralResponse;

import java.util.Date;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class PremierLeagueUtil {

    private static Scanner scanner = new Scanner(System.in);

    public PremierLeagueUtil() {

    }

    public static String getStringInput(String promptMessage, String invalidMessage, int length) {

        String result = null;
        System.out.print(promptMessage + ": ");
        try {
            result = getValidatedString(scanner, invalidMessage, length);
        } catch (UserInputException e) {
            System.out.println("*"+e.getMessage()+"\n");
            result = getStringInput(promptMessage, invalidMessage, length);
        }

        return result;

    }

    public static int getIntegerInput(String promptMessage, String invalidMessage, int l, int u) {

        int result = 0;
        System.out.print(promptMessage + ": ");
        try {
            result = getValidatedInteger(scanner, invalidMessage, l, u);
        } catch (UserInputException e) {
            System.out.println("*"+e.getMessage()+"\n");
            result = getIntegerInput(promptMessage, invalidMessage, l, u);
        }

        return result;

    }

    public static String getSeasonText(int year){
        String season = year + "-" + Integer.toString(year+1).substring(2);
        return season;
    }

    public static void printMenuHeader(String str){

        System.out.println("-------------------------------------------------------");
		System.out.println("~ "+str+" ~");
		System.out.println("-------------------------------------------------------");
		System.out.println();

    }

    public static String returnStringIfNull(String str, String nullMessage){
        try {
            if(str.isBlank() || str.isEmpty() || str == null || str.equalsIgnoreCase("")){
                return nullMessage;
            }
            else{
                return str;
            }
        }catch (Exception e){
            return nullMessage;
        }

    }

    public static String formatNDigitNumber(int n, String num){

        String returnstr = "0";
        String pattern = "%0"+n+"d";

        try{
            int x = Integer.parseInt(num);
            returnstr = String.format(pattern, x);
            return returnstr;
        }catch (Exception e){
            for(int i=0; i<n-1; i++){
                returnstr += "0";
            }
            return returnstr;
        }
    }

    public static JsonNode returnJsonError(String errorCode, String errorMessage){
        ErrorResponse response = new ErrorResponse();
        response.setErrorCode(errorCode);
        response.setErrorMessage(errorMessage);
        return Json.toJson(response);
    }

    public static JsonNode returnJsonGeneralResponse(String status, String message){
        GeneralResponse response = new GeneralResponse();
        response.setStatus(status);
        response.setMessage(message);
        return Json.toJson(response);
    }

    private static String getValidatedString(Scanner scanner, String invalidMessage, int length)
            throws UserInputException {

        if (length < 1)
            throw new IllegalArgumentException("length cannot be lesser than 1.");

        String inputString = scanner.nextLine();

        if (inputString.length() != length) {
            if (length > 1) {
                throw new UserInputException("Input must contain " + length + " characters.");
            } else {
                throw new UserInputException("Input must contain " + length + " character.");
            }
        }

        return inputString;

    }

    private static int getValidatedInteger(Scanner scanner, String invalidMessage, int lowerRange, int upperRange) throws UserInputException {

        int returnInt = 0;
        String inputString = scanner.nextLine();

        if(lowerRange != 0 && upperRange != 0){
            try {
                returnInt = Integer.parseInt(inputString);
            } catch (Exception e) {
                throw new UserInputException("Input must be an integer");
            }
            if(returnInt < lowerRange || returnInt > upperRange){
                throw new UserInputException("Input is not between the given range");
            }
        }else{
            try {
                returnInt = Integer.parseInt(inputString);
            } catch (Exception e) {
                throw new UserInputException("Input must be an integer");
            }
        }

        return returnInt;

    }

    public static String randomDate(int season) {

        int year = getRandomNumber(season, season+1);
        int month = getRandomNumber(1, 12);
        int day = getRandomNumber(1, 30);

        return (year+"-"+String.format("%02d", month)+"-"+String.format("%02d", day));

    }

    public static String randomTime(){

        int hour = getRandomNumber(1, 24);
        int min = getRandomNumber(1, 59);

        String time = String.format("%02d", hour) + ":" + String.format("%02d", min);
        return time;

    }

    public static int getRandomNumber(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }

}
