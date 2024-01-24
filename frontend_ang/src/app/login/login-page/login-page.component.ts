import {Component, OnInit} from "@angular/core";
import {UserCredentials} from "../model/user-credentials";
import {LoginFormComponent} from "../login-form/login-form.component";
import {LoginService} from "../login.service";
import { Router } from '@angular/router';

@Component({
	selector: "app-login-page",
	templateUrl: "./login-page.component.html",
	styleUrls: ["./login-page.component.css"],
	imports: [
		LoginFormComponent
	],
	standalone: true
})
export class LoginPageComponent implements OnInit {
	constructor(private router: Router, private loginService: LoginService) {
	}

	ngOnInit(): void {
	}

	onLogin(userCredentials: UserCredentials) {
		this.loginService.login(userCredentials)
		this.router.navigate(['/chat'])
	}
}
