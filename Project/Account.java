import java.util.ArrayList;
import java.util.List;

public class Account{
    private int accountNumber;
    private String accountHolderName;
    private List<FoodItem> customFoodItem;
    private List<Meal> customMeal;
    
    public Account(int accountNumber, String accountHolderName) {
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.customFoodItem = new ArrayList<>();
        this.customMeal = new ArrayList<>();
    }

    public void setCustomFoodItem(FoodItem customFoodItem) {
        this.customFoodItem.add(customFoodItem);
    }

    public void setCustomMeal(Meal customMeal) {
        this.customMeal.add(customMeal);
    }

    public int getAccountNumber() {
        return accountNumber;
    }
    
    public String getAccountHolderName() {
        return accountHolderName;
    }
}
