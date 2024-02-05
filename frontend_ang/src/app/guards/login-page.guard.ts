import {CanActivateFn, Router, ROUTES} from '@angular/router';
import {inject, Injectable} from "@angular/core";
import {LoginService} from "../login/login.service";

export const loginPageGuard: CanActivateFn = (route, state) => {
	const loginService = inject(LoginService);
	const router = inject(Router)
	if (loginService.getToken()) {
		return router.parseUrl('/chat')
	}
	return true;
};
