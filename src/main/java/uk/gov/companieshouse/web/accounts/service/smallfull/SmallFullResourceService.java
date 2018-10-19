package uk.gov.companieshouse.web.accounts.service.smallfull;

import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.links.SmallFullLinkType;

public abstract class SmallFullResourceService {

    @Autowired
    protected SmallFullService smallFullService;


    protected boolean smallFullResourceExists(String transactionId, String companyAccountsId,
                                                SmallFullLinkType resource) throws ServiceException {

        SmallFullApi smallFullApi = smallFullService.getSmallFullAccounts(transactionId, companyAccountsId);

        return smallFullApi.getLinks().containsKey(resource.getLink());
    }
}
