import {Response, URLSearchParams} from "@angular/http";
import {Observable} from "rxjs/Observable";
import "rxjs/add/operator/toPromise";
import {BehaviorSubject} from "rxjs/BehaviorSubject";
import {LoggerFactory} from "../components/logger/loggerFactory.component";

export abstract class AuthHttp {
    private _logger = LoggerFactory.getLogger("services:auth:http");
    protected authenticatedSubject: BehaviorSubject<boolean> = new BehaviorSubject(null);
    protected userIdSubject: BehaviorSubject<number> = new BehaviorSubject(null);
    private authenticateEmitter: Observable<boolean>;
    private userIdEmitter: Observable<number>;

    constructor() {
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

    abstract getJsonp(path, handler: (...args: any[]) => void): void;

    abstract getResponse(path, params?: URLSearchParams): Promise<Response>;

    get<T>(path, params?: URLSearchParams): Promise<T> {
        this._logger.debug("GET: {0}", path);
        return this.getResponse(path, params).then(res => res.json() as T);
    }

    abstract postResponse(path, data): Promise<Response>;


    post<T>(path, data): Promise<T> {
        this._logger.debug("POST: {0}", path);
        return this.postResponse(path, data).then(res => res.json() as T);
    }

    abstract patchResponse(path, data): Promise<Response>;

    patch<T>(path, data): Promise<T> {
        this._logger.debug("PATCH: {0}", path);
        return this.patchResponse(path, data).then(res => res.json() as T);
    }

    abstract deleteResponse(path, data): Promise<Response>;

    delete<T>(path,data) : Promise<T> {
        this._logger.debug("DELETE: {0}", path);
        return this.deleteResponse(path, data).then(res => res.json() as T);
    }
}