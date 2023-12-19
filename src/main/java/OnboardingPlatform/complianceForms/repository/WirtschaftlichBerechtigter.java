package OnboardingPlatform.complianceForms.repository;

public interface WirtschaftlichBerechtigter {

    interface IdentifiedPerson {
        boolean isIdenticalToContractPartner();

        boolean isIdenticalToWealthContributor();

        boolean isMemberOfLeadershipBody();

        boolean isProtectorOrSimilar();

        boolean isBeneficiaryOrPotential();

        boolean holdsMoreThan25PercentShares();

        boolean exercisesControlOverManagement();
    }


    interface USPersonQualification {
        boolean isUSCitizen();

        boolean isBornInUSTerritories();

        boolean hasCertificateOfLossOfNationality();

        boolean hasGreenCard();

        boolean hasUSImmigrationServiceCard();

        boolean hasUSResidenceForTax();

        boolean isUSResidentForOtherReasons();
    }

    interface LegalStatements {
        String getDeclaration();

        String getSignature();

        String getSignatoryName();

        String getLocationAndDate();
    }

    IdentifiedPerson getIdentifiedPerson();

    USPersonQualification getUSPersonQualification();

    LegalStatements getLegalStatements();
}

