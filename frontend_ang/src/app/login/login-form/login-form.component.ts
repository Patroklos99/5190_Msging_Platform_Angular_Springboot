import {Component, EventEmitter, OnInit, Output} from "@angular/core";
import {FormBuilder, ReactiveFormsModule, Validators} from "@angular/forms";
import {UserCredentials} from "../model/user-credentials";
import {NgClass, NgIf} from "@angular/common";

@Component({
	selector: "app-login-form",
	templateUrl: "./login-form.component.html",
	styleUrls: ["./login-form.component.css"],
	imports: [
		ReactiveFormsModule,
		NgClass,
		NgIf
	],
	standalone: true
})
export class LoginFormComponent implements OnInit {
	loginForm = this.fb.group({
			username: ["", [Validators.required]],
			password: ["", [Validators.email, Validators.required]],
		}, {updateOn: "blur", validators: []}
	);

	@Output()
	login = new EventEmitter<UserCredentials>();

	constructor(private fb: FormBuilder) {
	}

	ngOnInit(): void {
	}

	onLogin() {
		if (this.loginForm.controls.username.value && this.loginForm.controls.password) {
			const usercredentials: UserCredentials = {
				username: this.loginForm.value.username!,
				password: this.loginForm.value.password!
			}
			this.login.emit(usercredentials)
		} else {
			this.loginForm.markAllAsTouched();
		}
	}

	showUsernameRequiredError(): boolean {
		return this.showError('username', "required");
	}

	showPasswordRequiredError(): boolean {
		return this.showError('password', "required");
	}

	private showError(field: 'username' | 'password', error: string): boolean {
		return (
			this.loginForm.controls[field].hasError(error) &&
			(this.loginForm.controls[field].dirty || this.loginForm.controls[field].touched)
		);
	}
}
