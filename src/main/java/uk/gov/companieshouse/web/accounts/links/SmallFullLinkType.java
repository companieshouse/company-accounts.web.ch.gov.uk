package uk.gov.companieshouse.web.accounts.links;

public enum SmallFullLinkType implements LinkType {

    SELF("self"),
    CURRENT_PERIOD("current_period"),
    PREVIOUS_PERIOD("previous_period"),
    APPROVAL("approval");

    private String link;

    SmallFullLinkType(String link) {
        this.link = link;
    }

    public String getLink() {
        return link;
    }
}
