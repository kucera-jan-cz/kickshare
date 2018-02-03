import {Bounds, Country, Location} from "../services/domain";

export class CountryConstants {
    //https://github.com/mihai-craita/countries_center_box/blob/master/countries.json
    private static available: Country[] = [
        new Country("Czech republic", "CZ", new Location(49.817492, 15.472962), 7,
            new Bounds(new Location(48.551808, 12.090589), new Location(51.055719, 18.859236))),
        // new Country("German", "DE"),
        // new Country("Slovak republic", "SK")
    ];
    public static COUNTRY_LOCAL_STORAGE = "ks.country";

    public static country(code: string): Country {
        return this.available.find(it => it.code == code.toUpperCase())
    }

    static countries(): Country[] {
        return this.available;
    }
}