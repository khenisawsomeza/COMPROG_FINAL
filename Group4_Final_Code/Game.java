
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

public class Game {
    private String name; // e.g., "game0"
    private String company; // e.g., "gameInc"
    private ArrayList<String> genres; // e.g., ["sci-fi"]
    
    private double price_base; // e.g., 50.321
    private double price_taxed; // with tax
    private double price_final; // with discounts
    private double discount_percent;

    private boolean availability; // e.g., "available", "unavailable"

    // Constructor
    public Game(String name, String company, double price, String genres, String discount_data, boolean availability) {
        this.name = name;
        this.company = company;
        this.price_base = price; // Base price without tax or discount
        this.price_taxed = price_base * 1.12; // Add 12% tax
        this.discount_percent = -1; // Default to no discount
        this.availability = availability; // Set availability

        // Calculate final price with discount if applicable
        if (!discount_data.equals("[]")) {
            String[] discountParts = discount_data.replace("[", "").replace("]", "").split(",");

            if (discountParts.length == 3) {
                String beginDateStr = discountParts[0];
                String endDateStr = discountParts[1];
                double discount = Double.parseDouble(discountParts[2]); // Extract discount percentage (0 to 1)

                // Parse dates
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate beginDate = LocalDate.parse(beginDateStr, formatter);
                LocalDate endDate = LocalDate.parse(endDateStr, formatter);
                LocalDate today = LocalDate.now();

                // Apply discount only if today is within the discount period
                if ((today.isEqual(beginDate) || today.isAfter(beginDate)) && (today.isEqual(endDate) || today.isBefore(endDate))) {
                    this.price_final = price_taxed * (1 - discount); // Apply discount to taxed price
                    this.discount_percent = discount * 100; // Update discount percentage (0 to 100)
                } else {
                    this.price_final = price_taxed; // No discount, final price is the taxed price
                }
            } else {
                this.price_final = price_taxed; // Invalid discount data, default to taxed price
            }
        } else {
            this.price_final = price_taxed; // No discount, final price is the taxed price
        }

        // Parse genres into an ArrayList
        this.genres = new ArrayList<>(Arrays.asList(genres.replace("[", "").replace("]", "").split(",")));
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getCompany() {
        return company;
    }

    public double getPriceBase() {
        return price_base;
    }
    
    public double getPriceTaxxed() {
        return price_taxed;
    }
    
    public double getPriceFinal() {
        return price_final;
    }
    
    public double getDiscountPercent() {
        return discount_percent;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public boolean getAvailability() {
        return availability;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        Game game = (Game) obj;

        // Compare all fields: name, company, price, genres, and reviews
        return name.equals(game.name) &&
            company.equals(game.company) &&
            Double.compare(price_base, game.price_base) == 0 && // Use Double.compare for floating-point comparison
            Double.compare(price_taxed, game.price_taxed) == 0 && // Use Double.compare for floating-point comparison
            Double.compare(price_final, game.price_final) == 0 && // Use Double.compare for floating-point comparison
            Double.compare(discount_percent, game.discount_percent) == 0 &&
            genres.equals(game.genres);
    }
}