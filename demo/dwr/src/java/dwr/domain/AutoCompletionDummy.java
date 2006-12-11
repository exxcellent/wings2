package dwr.domain;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * TODO: document me!!!
 * <p/>
 * <code>AutoCompletionDummy</code>.
 * <p/>
 * User: rro
 * Date: May 2, 2006
 * Time: 6:32:02 PM
 *
 * @author Roman R&auml;dle
 */
public class AutoCompletionDummy {

    List completion;

    public AutoCompletionDummy() {
        completion = new ArrayList();

        initCompletion();
    }

    private void initCompletion() {
        completion.add("Afghanistan");
        completion.add("Albania");
        completion.add("Algeria");
        completion.add("American Samoa");
        completion.add("Andorra");
        completion.add("Angola");
        completion.add("Anguilla");
        completion.add("Antarctica");
        completion.add("Antigua & Barbuda");
        completion.add("Antilles, Netherlands");
        completion.add("Arabia, Saudi");
        completion.add("Argentina");
        completion.add("Armenia");
        completion.add("Aruba");
        completion.add("Asia Pacific");
        completion.add("Australia");
        completion.add("Austria");
        completion.add("Azerbaijan");
        completion.add("Bahamas, The");
        completion.add("Bahrain");
        completion.add("Bangladesh");
        completion.add("Barbados");
        completion.add("Belarus");
        completion.add("Belgium");
        completion.add("Belize");
        completion.add("Benin");
        completion.add("Bermuda");
        completion.add("Bhutan");
        completion.add("Bolivia");
        completion.add("Bosnia and Herzegovina");
        completion.add("Botswana");
        completion.add("Bouvet Island");
        completion.add("Brazil");
        completion.add("British Indian Ocean Territory");
        completion.add("British Virgin Islands");
        completion.add("Brunei Darussalam");
        completion.add("Bulgaria");
        completion.add("Burkina Faso");
        completion.add("Burundi");
        completion.add("Cambodia");
        completion.add("Cameroon");
        completion.add("Canada");
        completion.add("Cape Verde");
        completion.add("Cayman Islands");
        completion.add("Central African Republic");
        completion.add("Chad");
        completion.add("Chile");
        completion.add("China");
        completion.add("Christmas Island");
        completion.add("Cocos (Keeling) Islands");
        completion.add("Colombia");
        completion.add("Comoros");
        completion.add("Congo");
        completion.add("Congo, Democratic Rep. of the");
        completion.add("Cook Islands");
        completion.add("Costa Rica");
        completion.add("Cote D'Ivoire");
        completion.add("Croatia");
        completion.add("Cuba");
        completion.add("Cyprus");
        completion.add("Czech Republic");
        completion.add("Czechoslovakia (Former)");
        completion.add("Denmark");
        completion.add("Djibouti");
        completion.add("Dominica");
        completion.add("Dominican Republic");
        completion.add("East Timor (Timor-Leste)");
        completion.add("Ecuador");
        completion.add("Egypt");
        completion.add("El Salvador");
        completion.add("Equatorial Guinea");
        completion.add("Eritrea");
        completion.add("Estonia");
        completion.add("Ethiopia");
        completion.add("European Union");
        completion.add("Falkland Islands (Malvinas)");
        completion.add("Faroe Islands");
        completion.add("Fiji");
        completion.add("Finland");
        completion.add("France");
        completion.add("French Guiana");
        completion.add("French Polynesia");
        completion.add("French Southern Territories - TF");
        completion.add("Gabon");
        completion.add("Gambia, the");
        completion.add("Georgia");
        completion.add("Germany");
        completion.add("Ghana");
        completion.add("Gibraltar");
        completion.add("Greece");
        completion.add("Greenland");
        completion.add("Grenada");
        completion.add("Guadeloupe");
        completion.add("Guam");
        completion.add("Guatemala");
        completion.add("Guerney");
        completion.add("Guinea");
        completion.add("Guinea-Bissau");
        completion.add("Guinea, Equatorial");
        completion.add("Guiana, French");
        completion.add("Guyana");
        completion.add("Haiti");
        completion.add("Heard and McDonald Islands");
        completion.add("Holy See (Vatican City State)");
        completion.add("Holland (see Netherlands)");
        completion.add("Honduras");
        completion.add("Hong Kong, (China)");
        completion.add("Hungary");
        completion.add("Iceland");
        completion.add("India");
        completion.add("Indonesia");
        completion.add("Iran, Islamic Republic of");
        completion.add("Iraq");
        completion.add("Ireland");
        completion.add("Israel");
        completion.add("Ivory Coast (see Cote d'Ivoire)");
        completion.add("Italy");
        completion.add("Jamaica");
        completion.add("Japan");
        completion.add("Jersey");
        completion.add("Jordan");
        completion.add("Kazakhstan");
        completion.add("Kenya");
        completion.add("Kiribati");
        completion.add("Korea, Demo. People's Rep. of");
        completion.add("Korea, (South) Republic of");
        completion.add("Kuwait");
        completion.add("Kyrgyzstan");
        completion.add("Lao People's Democratic Republic");
        completion.add("Latvia");
        completion.add("Lebanon");
        completion.add("Lesotho");
        completion.add("Liberia");
        completion.add("Libyan Arab Jamahiriya");
        completion.add("Liechtenstein");
        completion.add("Lithuania");
        completion.add("Luxembourg");
        completion.add("Macao, (China)");
        completion.add("Macedonia, TFYR");
        completion.add("Madagascar");
        completion.add("Malawi");
        completion.add("Malaysia");
        completion.add("Maldives");
        completion.add("Mali");
        completion.add("Malta");
        completion.add("Marshall Islands");
        completion.add("Martinique");
        completion.add("Mauritania");
        completion.add("Mauritius");
        completion.add("Mayotte");
        completion.add("Mexico");
        completion.add("Micronesia, Federated States of");
        completion.add("Moldova, Republic of");
        completion.add("Monaco");
        completion.add("Mongolia");
        completion.add("Montserrat");
        completion.add("Morocco");
        completion.add("Mozambique");
        completion.add("Myanmar (ex-Burma)");
        completion.add("Namibia");
        completion.add("Nauru");
        completion.add("Nepal");
        completion.add("Netherlands");
        completion.add("Netherlands Antilles");
        completion.add("New Caledonia");
        completion.add("New Zealand");
        completion.add("Nicaragua");
        completion.add("Niger");
        completion.add("Nigeria");
        completion.add("Niue");
        completion.add("Norfolk Island");
        completion.add("Northern Mariana Islands");
        completion.add("Norway");
        completion.add("Oman");
        completion.add("Pakistan");
        completion.add("Palau");
        completion.add("Palestinian Territory");
        completion.add("Panama");
        completion.add("Papua New Guinea");
        completion.add("Paraguay");
        completion.add("Peru");
        completion.add("Philippines");
        completion.add("Pitcairn Island");
        completion.add("Poland");
        completion.add("Portugal");
        completion.add("Puerto Rico");
        completion.add("Qatar");
        completion.add("Reunion");
        completion.add("Romania");
        completion.add("Russia (Russian Federation)");
        completion.add("Rwanda");
        completion.add("Sahara");
        completion.add("Saint Helena");
        completion.add("Saint Kitts and Nevis");
        completion.add("Saint Lucia");
        completion.add("Saint Pierre and Miquelon");
        completion.add("Saint Vincent and the Grenadines");
        completion.add("Samoa");
        completion.add("San Marino");
        completion.add("Sao Tome and Principe");
        completion.add("Saudi Arabia");
        completion.add("Senegal");
        completion.add("Serbia & Montenegro");
        completion.add("Seychelles");
        completion.add("Sierra Leone");
        completion.add("Singapore");
        completion.add("Slovakia");
        completion.add("Slovenia");
        completion.add("Solomon Islands");
        completion.add("Somalia");
        completion.add("South Africa");
        completion.add("S. Georgia and S. Sandwich Is.");
        completion.add("Spain");
        completion.add("Sri Lanka (ex-Ceilan)");
        completion.add("Sudan");
        completion.add("Suriname");
        completion.add("Svalbard and Jan Mayen Islands");
        completion.add("Swaziland");
        completion.add("Sweden");
        completion.add("Switzerland");
        completion.add("Syrian Arab Republic");
        completion.add("Taiwan");
        completion.add("Tajikistan");
        completion.add("Tanzania, United Republic of");
        completion.add("Thailand");
        completion.add("Timor-Leste (East Timor)");
        completion.add("Togo");
        completion.add("Tokelau");
        completion.add("Tonga");
        completion.add("Trinidad & Tobago");
        completion.add("Tunisia");
        completion.add("Turkey");
        completion.add("Turkmenistan");
        completion.add("Turks and Caicos Islands");
        completion.add("Tuvalu");
        completion.add("Uganda");
        completion.add("Ukraine");
        completion.add("United Arab Emirates");
        completion.add("United Kingdom");
        completion.add("United States");
        completion.add("US Minor Outlying Islands");
        completion.add("Uruguay");
        completion.add("Uzbekistan");
        completion.add("Vanuatu");
        completion.add("Vatican City State (Holy See)");
        completion.add("Venezuela");
        completion.add("Viet Nam");
        completion.add("Virgin Islands, British");
        completion.add("Virgin Islands, U.S.");
        completion.add("Wallis and Futuna");
        completion.add("Western Sahara");
        completion.add("Yemen");
        completion.add("Zambia");
        completion.add("Zimbabwe");
    }

    public List getData(String part) {

        List returning = new ArrayList();

        for (Iterator iter = completion.iterator(); iter.hasNext();) {

            Object o = iter.next();

            if (o instanceof String) {

                if (((String) o).toLowerCase().startsWith(part.toLowerCase())) {
                    returning.add(o);
                }
            }
        }

        return returning;
    }
}
