package uk.gov.companieshouse.web.accounts.service.smallfull;

import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.links.SmallFullLinkType;

public abstract class SmallFullResourceService {

    @Autowired
    protected SmallFullService smallFullService;


    /**
     * Returns true / false depending on whether a small full resource exists to determine whether to
     * POST or PUT a child resource
     * @param transactionId The CHS transaction id
     * @param companyAccountsId The company accounts identifier
     * @param resource The small full resource for which to determine the existence
     * @return true or false
     * @throws ServiceException on small full retrieval failure
     */
    protected boolean smallFullResourceExists(String transactionId, String companyAccountsId,
                                                SmallFullLinkType resource) throws ServiceException {

        SmallFullApi smallFullApi = smallFullService.getSmallFullAccounts(transactionId, companyAccountsId);

        return smallFullApi.getLinks().containsKey(resource.getLink());
    }
}
