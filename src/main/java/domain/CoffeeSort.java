package main.java.domain;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "COFFEE")
public class CoffeeSort implements Comparable<CoffeeSort> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String manufactureCountry;
    private float acidity;
    private Date collectionDate;
    private Date roastDate;
    private String deliveryCountry;
    private String name;
    private int roastingDepth;

    public CoffeeSort() {

    }

    public CoffeeSort(String manufactureCountry, String acidity, String collectionDate,
                      String roastDate, String deliveryCountry, String name, String roastingDepth) {
        this.manufactureCountry = manufactureCountry;
        this.deliveryCountry = deliveryCountry;
        this.name = name;
        this.collectionDate = java.sql.Date.valueOf(collectionDate);
        this.roastDate = java.sql.Date.valueOf(roastDate);
        this.roastingDepth = Integer.parseInt(roastingDepth);
        this.acidity = Float.parseFloat(acidity);
    }


    public float getAcidity() {
        return acidity;
    }

    public void setAcidity(float acidity) {
        this.acidity = acidity;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getManufactureCountry() {
        return manufactureCountry;
    }

    public void setManufactureCountry(String manufactureCountry) {
        this.manufactureCountry = manufactureCountry;
    }

    public String getDeliveryCountry() {
        return deliveryCountry;
    }

    public void setDeliveryCountry(String deliveryCountry) {
        this.deliveryCountry = deliveryCountry;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCollectionDate() {
        return collectionDate;
    }

    public void setCollectionDate(Date collectionDate) {
        this.collectionDate = collectionDate;
    }

    public Date getRoastDate() {
        return roastDate;
    }

    public void setRoastDate(Date roastDate) {
        this.roastDate = roastDate;
    }

    public int getRoastingDepth() {
        return roastingDepth;
    }

    public void setRoastingDepth(int roastingDepth) {
        this.roastingDepth = roastingDepth;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CoffeeSort that = (CoffeeSort) o;
        return getRoastingDepth() == that.getRoastingDepth() &&
                Double.compare(that.getAcidity(), getAcidity()) == 0 &&
                Objects.equals(getManufactureCountry(), that.getManufactureCountry()) &&
                Objects.equals(getDeliveryCountry(), that.getDeliveryCountry()) &&
                Objects.equals(getName(), that.getName()) &&
                Objects.equals(getCollectionDate(), that.getCollectionDate()) &&
                Objects.equals(getRoastDate(), that.getRoastDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getManufactureCountry(), getDeliveryCountry(), getName(), getCollectionDate(),
                getRoastDate(), getRoastingDepth(), getAcidity());
    }

    public String[] toStringArray() {
        return new String[]{getManufactureCountry(), String.valueOf(getAcidity()), String.valueOf(getCollectionDate()),
                String.valueOf(getRoastDate()), getDeliveryCountry(), getName(), String.valueOf(getRoastingDepth())};
    }

    @Override
    public String toString() {
        return "CoffeeSort{" +
                "id=" + id +
                ", manufactureCountry='" + manufactureCountry + '\'' +
                ", deliveryCountry='" + deliveryCountry + '\'' +
                ", name='" + name + '\'' +
                ", collectionDate=" + collectionDate +
                ", roastDate=" + roastDate +
                ", roastingDepth=" + roastingDepth +
                ", acidity=" + acidity +
                '}';
    }

    @Override
    public int compareTo(CoffeeSort o) {
        int compareManufactureCountries = getManufactureCountry().compareTo(o.manufactureCountry);
        if (compareManufactureCountries != 0) return compareManufactureCountries;

        int compareAcidity = Double.compare(getAcidity(), o.getAcidity());
        if (compareAcidity != 0) return compareAcidity;

        int compareCollectionDates = getCollectionDate().compareTo(o.collectionDate);
        if (compareCollectionDates != 0) return compareCollectionDates;

        int compareRoastDates = getRoastDate().compareTo(o.getRoastDate());
        if (compareRoastDates != 0) return compareRoastDates;

        int compareDeliveryCountries = getDeliveryCountry().compareTo(o.getDeliveryCountry());
        if (compareDeliveryCountries != 0) return compareDeliveryCountries;

        int compareNames = getName().compareTo(o.getName());
        if (compareNames != 0) return compareNames;

        return Integer.compare(getRoastingDepth(), o.getRoastingDepth());
    }


}
