package OnboardingPlatform.complianceForms.model;

import OnboardingPlatform.complianceForms.repository.WirtschaftlichBerechtigter;

public class IdentifiedPersonModel implements WirtschaftlichBerechtigter.IdentifiedPerson {
    private boolean identicalToContractPartner;
    private boolean identicalToWealthContributor;
    private boolean memberOfLeadershipBody;
    private boolean protectorOrSimilar;
    private boolean beneficiaryOrPotential;
    private boolean holdsMoreThan25PercentShares;
    private boolean exercisesControlOverManagement;

    @Override
    public boolean isIdenticalToContractPartner() {
        return identicalToContractPartner;
    }

    @Override
    public boolean isIdenticalToWealthContributor() {
        return identicalToWealthContributor;
    }

    @Override
    public boolean isMemberOfLeadershipBody() {
        return memberOfLeadershipBody;
    }

    @Override
    public boolean isProtectorOrSimilar() {
        return protectorOrSimilar;
    }

    @Override
    public boolean isBeneficiaryOrPotential() {
        return beneficiaryOrPotential;
    }

    @Override
    public boolean holdsMoreThan25PercentShares() {
        return holdsMoreThan25PercentShares;
    }

    @Override
    public boolean exercisesControlOverManagement() {
        return exercisesControlOverManagement;
    }


    // Add setters to set the values of these properties
    public void setIdenticalToContractPartner(boolean identicalToContractPartner) {
        this.identicalToContractPartner = identicalToContractPartner;
    }

    public void setIdenticalToWealthContributor(boolean identicalToWealthContributor) {
        this.identicalToWealthContributor = identicalToWealthContributor;
    }

    public void setMemberOfLeadershipBody(boolean memberOfLeadershipBody) {
        this.memberOfLeadershipBody = memberOfLeadershipBody;
    }

    public void setProtectorOrSimilar(boolean protectorOrSimilar) {
        this.protectorOrSimilar = protectorOrSimilar;
    }

    public void setBeneficiaryOrPotential(boolean beneficiaryOrPotential) {
        this.beneficiaryOrPotential = beneficiaryOrPotential;
    }

    public void setHoldsMoreThan25PercentShares(boolean holdsMoreThan25PercentShares) {
        this.holdsMoreThan25PercentShares = holdsMoreThan25PercentShares;
    }

    public void setExercisesControlOverManagement(boolean exercisesControlOverManagement) {
        this.exercisesControlOverManagement = exercisesControlOverManagement;
    }


}
