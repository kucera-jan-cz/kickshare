import {Response, URLSearchParams} from "@angular/http";
import {Observable} from "rxjs/Observable";
import "rxjs/add/operator/toPromise";

export abstract class AuthHttp {
    abstract authenticate(username: string, password: string): Promise<Number>;

    abstract isAuthenticated(): boolean;

    abstract getAuthEmitter() : Observable<boolean>;

    abstract logout(): void ;

    abstract getJsonp(path, handler: (...args: any[]) => void): void;

    abstract get(path, params?: URLSearchParams): Promise<Response>;

    abstract post(path, data): Promise<Response>;

    abstract patch(path, data): Promise<Response>;
}