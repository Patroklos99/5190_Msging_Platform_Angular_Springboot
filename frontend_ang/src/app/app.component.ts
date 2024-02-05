import {Component, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {Router, RouterOutlet} from '@angular/router';
import {LoginFormComponent} from "./login/login-form/login-form.component";
import {LoginPageComponent} from "./login/login-page/login-page.component";
import {HttpClientModule} from "@angular/common/http";

@Component({
	selector: 'app-root',
	standalone: true,
	imports: [CommonModule, RouterOutlet, LoginFormComponent, LoginPageComponent, HttpClientModule],
	templateUrl: './app.component.html',
	styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
	constructor(private router: Router) {
	}

	ngOnInit() {
		window.addEventListener('storage', (event) => {
			if (event.key === 'isLoggedIn' && event.newValue === 'false') {
				this.router.navigate(['/']);
			}
		});
	}

	title = 'frontend-ang';
}
