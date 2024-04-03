package com.cse5382.assignment.Util;

public class AppConstants {

    public static final String NAME_REGEX = "^[A-Z][a-zA-Z]*[-']?[a-zA-Z]+,? ?[a-zA-Z]*[-']?[a-zA-Z]+ ?[a-zA-Z]*[-']?[a-zA-Z]*[.]?$";
    public static final String PHONE_NUMBER_REGEX = "^\\d{5}$|^\\d{5}[. ]\\d{5}$|^\\d{3}[-. ]\\d{4}$|^\\+?\\b([1-9]|[1-9][0-9]|[1-9][0-9][0-8])\\b[-.\\( ]{0,2}\\d{2,3}[ \\-.\\)]{0,2}\\d{3}[-. ]\\d{4}$|^[-.\\( ]?\\d{2,3}[ \\-.\\)]\\d{3}[-. ]\\d{4}$|^(00|011)[-.\\( ]?\\d{0,3}[ -.\\)][-.\\( ]?\\d{2,3}[ -.\\)]\\d{3}[-. ]\\d{4}$|^[+45. ]{0,4}\\d{4}[. ]\\d{4}$|^[+45. ]{0,4}\\d{2}[. ]\\d{2}[. ]\\d{2}[. ]\\d{2}$";
    
}
