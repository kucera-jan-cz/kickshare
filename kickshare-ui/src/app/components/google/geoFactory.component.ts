import GeocoderStatus = google.maps.GeocoderStatus;
import GeocoderRequest = google.maps.GeocoderRequest;
import GeocoderComponentRestrictions = google.maps.GeocoderComponentRestrictions;
import GeocoderResult = google.maps.GeocoderResult;
import {Location} from "./location.component";
import {LoggerFactory} from "../logger/loggerFactory.component";

export class GeoFactory {
    private logger = LoggerFactory.getLogger("components:google:geo:factory");
    private geoCoder = new google.maps.Geocoder();

    private toLocation = (geo : GeocoderResult) => {
        const display = geo.formatted_address;
        const address = geo.address_components.filter(c => c.types.indexOf('route') > -1).map(c => c.long_name)[0];
        const postal = geo.address_components.filter(c => c.types.indexOf('postal_code') > -1).map(c => c.long_name)[0];
        const lat = geo.geometry.location.lat();
        const lon = geo.geometry.location.lng();
        const location :Location = new Location(geo.place_id, display, address, postal, lat, lon);
        return location;
    };

    public search(country: string, address: string): Promise<Location[]> {

        const restrictions: GeocoderComponentRestrictions = {
            'country' : country
        };
        const request: GeocoderRequest = {
            'address' : address,
            'componentRestrictions' : restrictions
        };
        return new Promise((resolve, reject) => {
            this.geoCoder.geocode(request, (results,status) => {
                if(status == GeocoderStatus.OK) {
                    this.logger.info("GeoCoder result: {0}", JSON.stringify(results));
                    const locations: Location[] = results.map(this.toLocation);
                    this.logger.warn("Promise GeoCoder: {0}", JSON.stringify(locations));
                    resolve(locations);
                } else {
                    reject("Failed to retrieve geocode");
                }
            });
        });
    }
}