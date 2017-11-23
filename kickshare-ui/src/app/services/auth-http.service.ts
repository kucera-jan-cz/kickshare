import {Response, URLSearchParams} from "@angular/http";
import {Observable} from "rxjs/Observable";
import "rxjs/add/operator/toPromise";

export abstract class AuthHttp {
    abstract authenticate(username: string, password: string): Promise<Number>;

    abstract isAuthenticated(): boolean;

    abstract getAuthEmitter() : Observable<boolean>;

    abstract getUserIdEmitter(): Observable<number>;

    abstract logout(): void ;

    abstract getJsonp(path, handler: (...args: any[]) => void): void;

    //@TODO - rewrite all services to getJson and rename to get and this name getResponse/httpGet
    abstract get(path, params?: URLSearchParams): Promise<Response>;

    abstract getJson<T>(path, params?: URLSearchParams): Promise<T>;

    abstract post(path, data): Promise<Response>;

    abstract patch(path, data): Promise<Response>;
}