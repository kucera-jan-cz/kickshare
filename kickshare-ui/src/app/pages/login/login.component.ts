import {Component} from "@angular/core";

import {AbstractControl, FormBuilder, FormGroup, NgForm, Validators} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {AuthHttp} from "../../services/auth-http.service";

declare var $: any;

@Component({
    selector: 'login',
    templateUrl: './login.html',
    styleUrls: ['login.scss']
})
export class LoginComponent {
    public form: FormGroup;
    public email: AbstractControl;
    public password: AbstractControl;
    public submitted: boolean = false;

    model: any = {}
    public username: string;
    // public password: string;
    loading = false;

    constructor(private route: ActivatedRoute,
                private router: Router,
                private authenticationService: AuthHttp,
                private fb: FormBuilder) {
        this.form = fb.group({
            'email': ['', Validators.compose([Validators.required, Validators.email])],
            'password': ['', Validators.compose([Validators.required, Validators.minLength(4)])]
        });

        this.email = this.form.controls['email'];
        this.password = this.form.controls['password'];
    }

    async onSubmit(values: NgForm) {
        console.info("Authenticating....");
        this.submitted = true;
        if (this.form.valid) {
            let id: Number = await this.authenticationService.authenticate(values.value.email, values.value.password);
            if (id > 0) {
                const returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
                console.info('Authentication successful');
                console.info('Redirecting to :' + returnUrl);
                this.router.navigate([returnUrl]);
            } else {
                console.error('Authentication failed');
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
