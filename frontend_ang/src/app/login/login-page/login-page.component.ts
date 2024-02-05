import {Component, OnInit} from "@angular/core";
import {UserCredentials} from "../model/user-credentials";
import {LoginFormComponent} from "../login-form/login-form.component";
import {LoginService} from "../login.service";
import { Router } from '@angular/router';
import {HttpErrorResponse} from "@angular/common/http";
import {NgIf} from "@angular/common";
import {MatSnackBar, MatSnackBarModule} from '@angular/material/snack-bar';
@Component({
	selector: "app-login-page",
	templateUrl: "./login-page.component.html",
	styleUrls: ["./login-page.component.css"],
	imports: [
		LoginFormComponent,
		NgIf
	],
	standalone: true
})
export class LoginPageComponent implements OnInit {
	errorMessage: string = '';
	constructor(private router: Router, private loginService: LoginService, private snackBar: MatSnackBar) {
	}

	ngOnInit(): void {
	}

	onLogin(userCredentials: UserCredentials) {
		this.loginService.login(userCredentials).then(() => {
			this.router.navigate(['/chat'])
		}).catch((error) => {
			if (error instanceof HttpErrorResponse) {
				if (error.status === 403) {
					this.errorMessage = "Mot de passe invalide";
				} else {
					this.errorMessage = "Problème de Connexion";
				}
			}
		});
	}
}
