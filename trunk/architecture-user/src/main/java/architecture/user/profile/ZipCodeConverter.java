package architecture.user.profile;

import architecture.ee.util.ApplicationHelper;


public class ZipCodeConverter extends StringConverter
{
    public ZipCodeConverter()
    {
    }

    public boolean stringIsConvertable(String string)
    {
        if(string == null)
            return false;
        string = string.trim();
        boolean match = string.trim().matches(zipCodeRegEx);
        if(match)
        {
            int digitCount = 0;
            for(int i = 0; i < string.length(); i++)
            {
                char phoneChar = string.charAt(i);
                if(phoneChar >= '0' && phoneChar <= '9')
                    digitCount++;
            }

            if(digitCount < minZipCodeLength)
                return false;
        }
        return match;
    }

    public String getValidationKey()
    {
        return "validators.zipcd_fmt_error.text";
    }

    private static String zipCodeRegEx = ApplicationHelper.getApplicationProperty("validators.zipCode.lengthRegExp", "[\\-|0-9|a-z|A-Z| ]{2,15}");
    private static int minZipCodeLength = ApplicationHelper.getApplicationIntProperty("validators.zipCode.minLen", 2);

}
