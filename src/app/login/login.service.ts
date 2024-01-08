// debugger;
import {Injectable} from '@angular/core';
import {Router, RouterModule} from '@angular/router';
import {BehaviorSubject, Observable} from 'rxjs';

@Injectable({
	providedIn: 'root',
})
export class LoginService {
	static KEY = 'username';

	private username = new BehaviorSubject<string | null>(null);

	constructor(private router: Router) {
		this.username.next(localStorage.getItem(LoginService.KEY));
	}

	login(login: { username: string; password: string }) {
		localStorage.setItem(LoginService.KEY, login.username)
		this.username.next(localStorage.getItem(LoginService.KEY))
	}

	logout() {
		localStorage.removeItem(LoginService.KEY)
		this.username.next(null)
		this.router.navigate([''])
	}

	getUsername(): Observable<string | null> {
		return this.username.asObservable();
	}
}
