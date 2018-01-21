import {Country} from "../services/domain";

export class CountryConstants {
    private static available: Country[] = [new Country("Slovak republic", "SK"), new Country("German", "DE"), new Country("Czech republic", "CZ")];
    public static COUNTRY_LOCAL_STORAGE = "ks.country";

    public static country(code: string): Country {
        return this.available.find(it => it.code == code.toUpperCase())
    }

    static countries(): Country[] {
        return this.available;
    }
}