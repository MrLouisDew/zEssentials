package fr.maxlego08.essentials.api.economy;

import fr.maxlego08.essentials.api.modules.Module;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Represents a provider for handling economy-related operations in the plugin.
 */
public interface EconomyManager extends Module {

    /**
     * Checks if the specified player has enough money in the specified economy.
     *
     * @param player  The player to check.
     * @param economy The economy to check.
     * @return true if the player has enough money, false otherwise.
     */
    boolean hasMoney(OfflinePlayer player, Economy economy);

    /**
     * Gets the balance of the specified player in the specified economy.
     *
     * @param player  The player whose balance to get.
     * @param economy The economy to get the balance for.
     * @return The balance of the player in the specified economy.
     */
    BigDecimal getBalance(OfflinePlayer player, Economy economy);

    /**
     * Deposits the specified amount of currency into the account with the specified UUID and economy.
     *
     * @param uniqueId The UUID of the account to deposit to.
     * @param economy  The economy to deposit currency into.
     * @param amount   The amount of currency to deposit.
     * @return true if the deposit was successful, false otherwise.
     */
    boolean deposit(UUID uniqueId, Economy economy, BigDecimal amount);

    /**
     * Withdraws the specified amount of currency from the account with the specified UUID and economy.
     *
     * @param uniqueId The UUID of the account to withdraw from.
     * @param economy  The economy to withdraw currency from.
     * @param amount   The amount of currency to withdraw.
     * @return true if the withdrawal was successful, false otherwise.
     */
    boolean withdraw(UUID uniqueId, Economy economy, BigDecimal amount);

    /**
     * Sets the balance of the account with the specified UUID in the specified economy to the specified amount.
     *
     * @param uniqueId The UUID of the account.
     * @param economy  The economy to set the balance for.
     * @param amount   The new balance.
     * @return true if the balance was set successfully, false otherwise.
     */
    boolean set(UUID uniqueId, Economy economy, BigDecimal amount);

    /**
     * Gets all available economies.
     *
     * @return A collection containing all available economies.
     */
    Collection<Economy> getEconomies();

    /**
     * Gets the economy with the specified name, if it exists.
     *
     * @param economyName The name of the economy to get.
     * @return An optional containing the economy, or empty if not found.
     */
    Optional<Economy> getEconomy(String economyName);

    /**
     * Gets the default economy.
     *
     * @return The default economy.
     */
    Economy getDefaultEconomy();

    /**
     * Gets the Vault economy.
     *
     * @return The Vault economy.
     */
    Economy getVaultEconomy();

    /**
     * Formats the specified number according to the default economy's format.
     *
     * @param number The number to format.
     * @return The formatted number string.
     */
    String format(Number number);

    /**
     * Formats the specified number according to the specified price format.
     *
     * @param priceFormat The price format to use.
     * @param number      The number to format.
     * @return The formatted number string.
     */
    String format(PriceFormat priceFormat, Number number);

    /**
     * Formats the specified number according to the format of the specified economy.
     *
     * @param economy The economy whose format to use.
     * @param number  The number to format.
     * @return The formatted number string.
     */
    String format(Economy economy, Number number);

    /**
     * Gets the list of number format multiplication configurations for selling items.
     *
     * @return The list of number format multiplication configurations.
     */
    List<NumberMultiplicationFormat> getNumberFormatSellMultiplication();

    /**
     * Gets the multiplication format with the specified format string, if it exists.
     *
     * @param format The format string.
     * @return An optional containing the multiplication format, or empty if not found.
     */
    Optional<NumberMultiplicationFormat> getMultiplication(String format);

    /**
     * Initiates a payment from one player to another with the specified economy and amount.
     *
     * @param fromUuid The UUID of the player making the payment.
     * @param fromName The name of the player making the payment.
     * @param toUuid   The UUID of the player receiving the payment.
     * @param toName   The name of the player receiving the payment.
     * @param economy  The economy to use for the payment.
     * @param amount   The amount of currency to pay.
     */
    void pay(UUID fromUuid, String fromName, UUID toUuid, String toName, Economy economy, BigDecimal amount);

    /**
     * Gets the price format used for formatting currency amounts.
     *
     * @return The price format used for formatting currency amounts.
     */
    PriceFormat getPriceFormat();

    /**
     * Gets the list of price reduction configurations.
     *
     * @return The list of price reduction configurations.
     */
    List<NumberFormatReduction> getPriceReductions();

    /**
     * Gets the decimal format string used for formatting prices.
     *
     * @return The decimal format string used for formatting prices.
     */
    String getPriceDecimalFormat();

    String getBaltopPlaceholderUserEmpty();

    Baltop getBaltop(Economy economy);

    Optional<UserBaltop> getPosition(String economyName, int position);

    long getUserPosition(String economyName, UUID uuid);

    void sendBaltop(Player player, int page);
}
