package cz.mares.zonkyprinter.service;

import cz.mares.zonkyprinter.model.Loan;
import cz.mares.zonkyprinter.service.filter.LoanRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class ZonkyLoansLoaderImpl implements ZonkyLoansLoader {

    private static final String LOANS_URL = "https://api.zonky.cz/loans/marketplace";

    private RestTemplate restTemplate;

    @Autowired
    public void setRestTemplate(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    @Override
    public List<Loan> getLoans() {
        ResponseEntity<Loan[]> response = restTemplate.getForEntity(LOANS_URL, Loan[].class);
        return response.getBody() != null ? Arrays.asList(response.getBody()) : Collections.emptyList();
    }

    @Override
    public List<Loan> getLoans(LoanRequestFilter filter) {
        UriComponents uriComponents = UriComponentsBuilder
                .fromUriString(LOANS_URL)
                .query("datePublished__gt={datePublished}")
                .buildAndExpand(filter.getFormattedPublishedDate());

        ResponseEntity<Loan[]> response = restTemplate.getForEntity(uriComponents.toUriString(), Loan[].class);
        return response.getBody() != null ? Arrays.asList(response.getBody()) : Collections.emptyList();
    }
}
