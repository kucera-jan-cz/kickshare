import {Injectable} from "@angular/core";
import {Category} from "./domain";
import {AuthHttp} from "./auth-http.service";

/**
 * Created by KuceraJan on 6.4.2017.
 */

@Injectable()
export class CategoryService {
    constructor(private http: AuthHttp) {
    }

    getCategories(): Promise<Category[]> {
        const promise: Promise<Category[]> = this.http.get('/categories');
        return promise;
    }
}

