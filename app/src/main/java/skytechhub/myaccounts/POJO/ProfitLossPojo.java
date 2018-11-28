package skytechhub.myaccounts.POJO;

/**
 * Created by ABC on 23-11-2017.
 */

public class ProfitLossPojo {
    String name;

    public int getIncome() {
        return income;
    }

    public void setIncome(int income) {
        this.income = income;
    }

    public int getExpense() {
        return expense;
    }

    public void setExpense(int expense) {
        this.expense = expense;
    }

    public int getPl() {
        return pl;
    }

    public void setPl(int pl) {
        this.pl = pl;
    }

    int income,expense,pl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
