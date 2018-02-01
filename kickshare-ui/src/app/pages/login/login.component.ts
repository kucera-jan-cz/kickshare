import {Component} from "@angular/core";

import {AbstractControl, FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {AuthHttp} from "../../services/auth-http.service";
import {LoggerFactory} from "../../components/logger/loggerFactory.component";
import {UrlService} from "../../services/url.service";

declare var $: any;

@Component({
    selector: 'login',
    templateUrl: './login.html',
    styleUrls: ['login.scss']
})
export class LoginComponent {
    private logger = LoggerFactory.getLogger("components:login");
    public form: FormGroup;
    public email: AbstractControl;
    public password: AbstractControl;
    public submitted: boolean = false;

    model: any = {};
    public username: string;
    // public password: string;
    loading = false;

    constructor(private route: ActivatedRoute,
                private router: Router,
                private authenticationService: AuthHttp,
                public url: UrlService,
                private fb: FormBuilder) {
        this.form = fb.group({
            'email': ['', Validators.compose([Validators.required, Validators.email])],
            'password': ['', Validators.compose([Validators.required, Validators.minLength(4)])]
        });

        this.email = this.form.controls['email'];
        this.password = this.form.controls['password'];
    }

    async onSubmit() {
        this.logger.info("Authenticating....");
        this.submitted = true;
        if (this.form.valid) {
            let id: Number = await this.authenticationService.authenticate(this.form.value.email, this.form.value.password);
            if (id > 0) {
                const returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
                this.logger.info('Authentication successful');
                this.logger.info('Redirecting to {0}', returnUrl);
                this.router.navigate([returnUrl]);
            } else {
                this.logger.error('Authentication failed');
            }
        }
    }

    // async login() {
    //     console.info("Login: "+this.username);
    //     this.loading = true;
    //     await this.authenticationService.authenticate(this.username, this.password);
    //     if(this.authenticationService.isAuthenticated()) {
    //         console.info('Authentication successful');
    //     } else {
    //         console.error('Authentication failed');
    //     }
    // }
}
