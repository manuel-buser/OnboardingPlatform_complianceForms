package OnboardingPlatform.complianceForms.model;

import OnboardingPlatform.complianceForms.repository.WirtschaftlichBerechtigter;

public class USPersonQualificationModel implements WirtschaftlichBerechtigter.USPersonQualification {
    private boolean usCitizen;
    private boolean bornInUSTerritories;
    private boolean hasCertificateOfLossOfNationality;
    private boolean hasGreenCard;
    private boolean hasUSImmigrationServiceCard;
    private boolean hasUSResidenceForTax;
    private boolean usResidentForOtherReasons;

    @Override
    public boolean isUSCitizen() {
        return usCitizen;
    }

    @Override
    public boolean isBornInUSTerritories() {
        return bornInUSTerritories;
    }

    @Override
    public boolean hasCertificateOfLossOfNationality() {
        return hasCertificateOfLossOfNationality;
    }

    @Override
    public boolean hasGreenCard() {
        return hasGreenCard;
    }

    @Override
    public boolean hasUSImmigrationServiceCard() {
        return hasUSImmigrationServiceCard;
    }

    @Override
    public boolean hasUSResidenceForTax() {
        return hasUSResidenceForTax;
    }

    @Override
    public boolean isUSResidentForOtherReasons() {
        return usResidentForOtherReasons;
    }

    // Add setters to set the values of these properties
    // Example:
    public void setUSCitizen(boolean usCitizen) {
        this.usCitizen = usCitizen;
    }

    public void setBornInUSTerritories(boolean bornInUSTerritories) {
        this.bornInUSTerritories = bornInUSTerritories;
    }

    public void setHasCertificateOfLossOfNationality(boolean hasCertificateOfLossOfNationality) {
        this.hasCertificateOfLossOfNationality = hasCertificateOfLossOfNationality;
    }

    public void setHasGreenCard(boolean hasGreenCard) {
        this.hasGreenCard = hasGreenCard;
    }

    public void setHasUSImmigrationServiceCard(boolean hasUSImmigrationServiceCard) {
        this.hasUSImmigrationServiceCard = hasUSImmigrationServiceCard;
    }

    public void setHasUSResidenceForTax(boolean hasUSResidenceForTax) {
        this.hasUSResidenceForTax = hasUSResidenceForTax;
    }

    public void setUSResidentForOtherReasons(boolean usResidentForOtherReasons) {
        this.usResidentForOtherReasons = usResidentForOtherReasons;
    }
}
