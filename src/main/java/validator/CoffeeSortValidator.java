package main.java.validator;

import main.java.domain.CoffeeSort;
import main.java.exceptions.InvalidCoffeeSortException;
import main.java.models.CoffeeSortTableModel;

import java.sql.Date;

public class CoffeeSortValidator {

    private static final int NAME_MIN_LENGTH = 3;
    private static final int NAME_MAX_LENGTH = 12;
    private static final int COUNTRY_LENGTH = 3;

    public static boolean valid(String manufacture, String acidity, String collectionDate, String fireDate,
                                String delivery, String name, String roast) throws InvalidCoffeeSortException {
        if (manufacture.length() != COUNTRY_LENGTH) {
            throw new InvalidCoffeeSortException("Manufacture country name must be 3 characters!");
        }
        if (delivery.length() != COUNTRY_LENGTH) {
            throw new InvalidCoffeeSortException("Delivery country name must be 3 characters!");
        }
        if (!countryNameIsValid(manufacture)) {
            throw new InvalidCoffeeSortException("Manufacture country should be 3 upper-cased chars!");
        }
        if (!countryNameIsValid(delivery)) {
            throw new InvalidCoffeeSortException("Delivery country should be 3 upper-cased chars!");
        }
        if (name.length() < NAME_MIN_LENGTH || name.length() > NAME_MAX_LENGTH) {
            throw new InvalidCoffeeSortException("Name must be 3 to 12 characters long!");
        }

        try {
            Float.parseFloat(acidity);
        } catch (NumberFormatException | NullPointerException e) {
            throw new InvalidCoffeeSortException("Not correct float for acidity provided!", e);
        }

        try {
            if (Integer.parseInt(roast) < 0)
                throw new InvalidCoffeeSortException("Not a positive number for a roast depth provided!");
        } catch (NumberFormatException e) {
            throw new InvalidCoffeeSortException("Not a correct integer for depth provided", e);
        }

        Date collection, roasting;
        try {
            collection = Date.valueOf(collectionDate);
        } catch (IllegalArgumentException e) {
            throw new InvalidCoffeeSortException("Collection date should be in format \"yyyy-[m]m-[d]d", e);
        }


        try {
            roasting = Date.valueOf(fireDate);
        } catch (IllegalArgumentException e) {
            throw new InvalidCoffeeSortException("Roast date should be in format \"yyyy-[m]m-[d]d", e);
        }

        if (roasting.before(collection)) {
            throw new InvalidCoffeeSortException("Roast date couldn't be before collection date!");
        }
        return true;
    }

    public void validate(CoffeeSort original, CoffeeSort other, CoffeeSortTableModel model,
                         boolean updateFlag)
            throws InvalidCoffeeSortException {


        String name = other.getName();
        int nameLength = name.length(),
                countryNameLength = other.getManufactureCountry().length(),
                deliveryNameLength = other.getDeliveryCountry().length();

        if (nameLength < NAME_MIN_LENGTH || nameLength > NAME_MAX_LENGTH) {
            throw new InvalidCoffeeSortException("Name must be 3 to 12 characters long!");
        }
        if (countryNameLength != COUNTRY_LENGTH) {
            throw new InvalidCoffeeSortException("Manufacture country name must be 3 characters!");
        }
        if (deliveryNameLength != COUNTRY_LENGTH) {
            throw new InvalidCoffeeSortException("Delivery country name must be 3 characters!");
        }

        if (!nameHasValidCharacters(name)) {
            throw new InvalidCoffeeSortException("Name can only have (a-z A-Z 0-9 space and _) characters!");
        }

        if (!countryNameIsValid(other.getManufactureCountry())) {
            throw new InvalidCoffeeSortException("Manufacture country should be 3 upper-cased chars!");
        }
        if (!countryNameIsValid(other.getDeliveryCountry())) {
            throw new InvalidCoffeeSortException("Delivery country should be 3 upper-cased chars!");
        }

        if ((updateFlag) && (original.getName().equals(name))) {
            //TODO
            return;
        }

        if (model.contains(other)) {

            throw new InvalidCoffeeSortException("This coffee sort already exists!");
        }
    }

    private static boolean countryNameIsValid(String input) {
        char[] cc = input.toCharArray();
        for (char c : cc) {
            if (!Character.isUpperCase(c)) {
                return false;
            }
        }
        return true;
    }

    private boolean nameHasValidCharacters(String input) {
        char[] cc = input.toCharArray();
        for (char c : cc) {
            if (!((Character.isLetterOrDigit(c)) || (Character.isSpaceChar(c)) || (c == '_'))) {
                return false;
            }
        }
        return true;
    }
}
