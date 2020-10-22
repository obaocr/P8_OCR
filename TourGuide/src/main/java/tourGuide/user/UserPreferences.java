package tourGuide.user;

import org.javamoney.moneta.Money;

import javax.money.CurrencyUnit;
import javax.money.Monetary;

/**
 * UserPreferences Model
 */
public class UserPreferences {

	// TODO OBA, passage en Integer

	private Integer attractionProximity = Integer.MAX_VALUE;
	private CurrencyUnit currency = Monetary.getCurrency("USD");
	private Money lowerPricePoint = Money.of(0, currency);
	private Money highPricePoint = Money.of(Integer.MAX_VALUE, currency);
	private Integer tripDuration = 1;
	private Integer ticketQuantity = 1;
	private Integer numberOfAdults = 1;
	private Integer numberOfChildren = 0;
	
	public UserPreferences() {
	}
	
	public void setAttractionProximity(Integer attractionProximity) {
		this.attractionProximity = attractionProximity;
	}
	
	public Integer getAttractionProximity() {
		return attractionProximity;
	}

	public CurrencyUnit getCurrency() { return currency; }

	public void setCurrency(CurrencyUnit currency) { this.currency = currency; }

	public Money getLowerPricePoint() { return lowerPricePoint;	}

	public void setLowerPricePoint(Money lowerPricePoint) { this.lowerPricePoint = lowerPricePoint;	}

	public Money getHighPricePoint() { return highPricePoint; }

	public void setHighPricePoint(Money highPricePoint) {
		this.highPricePoint = highPricePoint;
	}
	
	public Integer getTripDuration() {
		return tripDuration;
	}

	public void setTripDuration(Integer tripDuration) {
		this.tripDuration = tripDuration;
	}

	public Integer getTicketQuantity() {
		return ticketQuantity;
	}

	public void setTicketQuantity(Integer ticketQuantity) {
		this.ticketQuantity = ticketQuantity;
	}
	
	public Integer getNumberOfAdults() {
		return numberOfAdults;
	}

	public void setNumberOfAdults(Integer numberOfAdults) {
		this.numberOfAdults = numberOfAdults;
	}

	public Integer getNumberOfChildren() {
		return numberOfChildren;
	}

	public void setNumberOfChildren(Integer numberOfChildren) {
		this.numberOfChildren = numberOfChildren;
	}

}
