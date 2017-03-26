/**
 * Created by KuceraJan on 26.3.2017.
 */
import {Component, OnInit, Sanitizer, SecurityContext} from "@angular/core";
import {Router} from "@angular/router";
@Component({
    selector: 'gmap',
    templateUrl: 'app/gmap.component.html',
    styles: [`
        /* Always set the map height explicitly to define the size of the div
         * element that contains the map. */
        #map {
            height: 800px;
            width: 1200px;
        }

        /* Optional: Makes the sample page fill the window. */
        html, body {
            height: 100%;
            margin: 0;
            padding: 0;
        }
`]
})
export class GMap implements OnInit {

    constructor(
        private router: Router,
        private sanitizer: Sanitizer
    ) {}

    loadAPI: Promise<any>;
    //@TODO externalize the key
    url = 'https://maps.googleapis.com/maps/api/js?key=AIzaSyA-NAJu2diDDRzMKz9jKTIj6HVXODMjXpk&callback=initMap';
    index = 1;
    searchTerm: String;
    ngOnInit() {
        this.loadScript();
    }

    increment() {
        this.index = this.index + 1;
    }

    search(input: string) {
        const searchTerm = this.sanitizer.sanitize(SecurityContext.NONE, input);
        // this.index = this.index + 1;
        // this.loadScript();
        // console.log('resolving promise...');
        // const t = term.value;
        window['readData'](new google.maps.LatLng(49.19, 16.60));
        console.log(`Searching for: ${searchTerm}`);
    }

    public loadScript() {
        console.log('preparing to load...')
        let node = document.createElement('script');
        node.src = this.url;
        node.type = 'text/javascript';
        node.async = true;
        node.charset = 'utf-8';
        document.getElementsByTagName('head')[0].appendChild(node);
    }

}