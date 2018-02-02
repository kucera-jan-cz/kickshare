import {Observable} from "rxjs/Observable";
import "rxjs/add/operator/toPromise";
import {BehaviorSubject} from "rxjs/BehaviorSubject";
import {LoggerFactory} from "../components/logger/loggerFactory.component";
import {HttpClient, HttpHeaders, HttpParams, HttpRequest, HttpResponse} from "@angular/common/http";

export abstract class AuthHttp {
    private _logger = LoggerFactory.getLogger("services:auth:http");
    protected authenticatedSubject: BehaviorSubject<boolean> = new BehaviorSubject(null);
    protected userIdSubject: BehaviorSubject<number> = new BehaviorSubject(null);
    private authenticateEmitter: Observable<boolean>;
    private userIdEmitter: Observable<number>;

    constructor(protected host: String, protected http: HttpClient) {
        this.authenticateEmitter = this.authenticatedSubject.asObservable();
        this.userIdEmitter = this.userIdSubject.asObservable();
    }

    public getAuthEmitter(): Observable<boolean> {
        return this.authenticateEmitter;
    }

    public getUserIdEmitter(): Observable<number> {
        return this.userIdEmitter;
    }

    abstract authenticate(username: string, password: string): Promise<Number>;

    abstract isAuthenticated(): boolean;

    abstract logout(): void ;

    public getJsonp(path, handler: (...args: any[]) => void): void {
        const url: string = `${this.host}/${path}`;
        this._logger.info("Calling JSONP: " + url);
        this.http.jsonp(url, "JSONP_CALLBACK").subscribe(response => handler(response));
    }

    public jsonp(path, options: HttpParams, callback = "JSONP_CALLBACK"): Observable<Object> {
        const params = options.toString();
        const url: string = `${this.host}/${path}?${params}`;
        return this.http.jsonp(url, callback)
    }

    get<T>(path, params?: HttpParams, contentType?: "text" | "json"): Promise<T> {
        return this.exchange<T>("GET", path, params, null, contentType);
    }

    post<T>(path, data, contentType?: "text" | "json"): Promise<T> {
        return this.exchange<T>("POST", path, null, data, contentType);
    }

    patch<T>(path, data, contentType?: "text" | "json"): Promise<T> {
        return this.exchange<T>("PATH", path, null, data, contentType);
    }

    delete<T>(path, data, contentType?: "text" | "json"): Promise<T> {
        return this.exchange<T>("DELETE", path, null, data, contentType);
    }

    private exchange<T>(method: string, path: string, params: HttpParams | undefined, body: any, responseType: "json" | "text" = "json"): Promise<T> {
        this._logger.debug("{0}: {1}", method, path);
        const url: string = `${this.host}/${path}`;
        const request: HttpRequest<any> = this.createRequest(method, url, params, body, responseType);
        const observable = this.http.request<T>(request) as Observable<HttpResponse<T>>;
        return observable.map(it => it.body).toPromise();
    }

    protected abstract authHeaders(): HttpHeaders;

    private createRequest<T>(method: string, url: string, params: HttpParams | undefined, body: T, responseType?: 'json' | 'text'): HttpRequest<T> {
        var headers: HttpHeaders = this.authHeaders();
        headers = headers.append('Content-Type', 'application/json');

        return new HttpRequest<T>(method, url, body, {
            'headers': headers,
            'params': params,
            'responseType': responseType,
            'withCredentials': true
        });
    }
}