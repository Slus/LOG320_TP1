/**
 * Created by slus on 15-05-18.
 */
public class SecretCode {
    char symbol;
    String secretCode;

    public SecretCode(char symbol, String secretCode) {
        this.symbol = symbol;
        this.secretCode = secretCode;
    }

    public SecretCode() {
        this.symbol = '\0';
        this.secretCode = "";
    }

    public char getSymbol() {
        return symbol;
    }

    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }

    public String getSecretCode() {
        return secretCode;
    }

    public void setSecretCode(String secretCode) {
        this.secretCode = secretCode;
    }

    @Override
    public String toString() {
        return "SecretCode{" +
                "symbol=" + symbol +
                ", secretCode='" + secretCode + '\'' +
                '}';
    }
}
