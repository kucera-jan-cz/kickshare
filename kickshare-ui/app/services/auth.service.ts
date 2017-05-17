/**
 * Created by KuceraJan on 27.3.2017.
 */
import {Injectable} from "@angular/core";
import {Headers, Http} from "@angular/http";

//@TODO - rename this service
export const TOKEN_KEY = 'auth_token';

@Injectable()
export class UserService {

    private loggedIn = false;

    constructor(private http: Http) {
        this.loggedIn = !!localStorage.getItem('auth_token');
    }

    login(email: String, password: String) {
        let headers = new Headers();
        headers.append('Content-Type', 'application/json');

        return this.http
            .post(
                '/login',
                JSON.stringify({email, password}),
                {headers}
            )
            .map(res => res.json())
            .map(res => {
                if (res.success) {
                    localStorage.setItem(TOKEN_KEY, res.auth_token);
                    this.loggedIn = true;
                }

                return res.success;
            });
    }

    logout() {
        localStorage.removeItem(TOKEN_KEY);
        this.loggedIn = false;
    }

    isLoggedIn(): Boolean {
        return this.loggedIn;
    }

    getToken(): string {
        return localStorage.getItem(TOKEN_KEY);
    }
}
