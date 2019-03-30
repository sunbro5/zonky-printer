package cz.mares.zonkyprinter.service.filter;

import lombok.Builder;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@Builder
public class LoanRequestFilter {

    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    private Date publishedDate;

    public String getFormattedPublishedDate(){
        return formatDate(publishedDate);
    }

    private String formatDate(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        return sdf.format(date);
    }
}
